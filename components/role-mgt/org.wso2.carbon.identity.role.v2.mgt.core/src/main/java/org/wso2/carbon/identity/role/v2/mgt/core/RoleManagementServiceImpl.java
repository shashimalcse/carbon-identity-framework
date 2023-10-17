/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.role.v2.mgt.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.application.common.model.Scope;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.model.OperationNode;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.role.v2.mgt.core.dao.RoleDAO;
import org.wso2.carbon.identity.role.v2.mgt.core.dao.RoleMgtDAOFactory;
import org.wso2.carbon.identity.role.v2.mgt.core.internal.RoleManagementServiceComponentHolder;
import org.wso2.carbon.identity.role.v2.mgt.core.listener.RoleManagementListener;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.APPLICATION;
import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.Error.INVALID_AUDIENCE;
import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.Error.INVALID_PERMISSION;
import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.Error.UNEXPECTED_SERVER_ERROR;
import static org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants.ORGANIZATION;

/**
 * Implementation of the {@link RoleManagementService} interface.
 */
public class RoleManagementServiceImpl implements RoleManagementService {

    private static final Log log = LogFactory.getLog(RoleManagementServiceImpl.class);
    private final RoleDAO roleDAO = RoleMgtDAOFactory.getInstance().getRoleDAO();

    @Override
    public RoleBasicInfo addRole(String roleName, List<String> userList, List<String> groupList,
                                 List<Permission> permissions, String audience, String audienceId, String tenantDomain)
            throws IdentityRoleManagementException {

        if (StringUtils.startsWithIgnoreCase(roleName, UserCoreConstants.INTERNAL_SYSTEM_ROLE_PREFIX)) {
            String errorMessage = String.format("Invalid role name: %s. Role names with the prefix: %s, is not allowed"
                            + " to be created from externally in the system.", roleName,
                    UserCoreConstants.INTERNAL_SYSTEM_ROLE_PREFIX);
            throw new IdentityRoleManagementClientException(INVALID_REQUEST.getCode(), errorMessage);
        }
        if (isDomainSeparatorPresent(roleName)) {
            // SCIM2 API only adds roles to the internal domain.
            throw new IdentityRoleManagementClientException(INVALID_REQUEST.getCode(), "Invalid character: "
                    + UserCoreConstants.DOMAIN_SEPARATOR + " contains in the role name: " + roleName + ".");
        }
        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.preAddRole(roleName, userList, groupList,
                        permissions, audience, audienceId, tenantDomain);
            }
        }

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreAddRoleWithException(roleName, userList, groupList, permissions,
                audience, audienceId, tenantDomain);

        // Validate audience.
        if (StringUtils.isNotEmpty(audience)) {
            if (!(ORGANIZATION.equalsIgnoreCase(audience) || APPLICATION.equalsIgnoreCase(audience))) {
                throw new IdentityRoleManagementClientException(INVALID_AUDIENCE.getCode(), "Invalid role audience");
            }
            if (ORGANIZATION.equalsIgnoreCase(audience)) {
                validateOrganizationRoleAudience(audienceId);
                audience = ORGANIZATION;
            }
            if (APPLICATION.equalsIgnoreCase(audience)) {
                // audience validation done using listener.
                audience = APPLICATION;
            }
        } else {
            audience = ORGANIZATION;
            audienceId = getOrganizationIdByTenantDomain(tenantDomain);
        }
        validatePermissions(permissions, audience, audienceId, tenantDomain);
        RoleBasicInfo roleBasicInfo = roleDAO.addRole(roleName, userList, groupList, permissions, audience, audienceId,
                tenantDomain);
        roleManagementEventPublisherProxy.publishPostAddRole(roleBasicInfo.getId(), roleName, userList, groupList,
                permissions, audience, audienceId, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s add role of name : %s successfully.", getUser(tenantDomain), roleName));
        }
        RoleBasicInfo role = roleDAO.getRoleBasicInfoById(roleBasicInfo.getId(), tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postAddRole(role, roleName, userList,
                        groupList, permissions, audience, audienceId, tenantDomain);
            }
        }
        return role;
    }

    @Override
    public List<RoleBasicInfo> getRoles(Integer limit, Integer offset, String sortBy, String sortOrder,
                                        String tenantDomain) throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetRolesWithException(limit, offset, sortBy, sortOrder,
                tenantDomain);
        List<RoleBasicInfo> roleBasicInfoList = roleDAO.getRoles(limit, offset, sortBy, sortOrder, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetRoles(limit, offset, sortBy, sortOrder, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoles(roleBasicInfoList, limit, offset, sortBy, sortOrder,
                        tenantDomain);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get roles successfully.", getUser(tenantDomain)));
        }
        return roleBasicInfoList;
    }

    @Override
    public List<RoleBasicInfo> getRoles(String filter, Integer limit, Integer offset, String sortBy, String sortOrder,
                                                                               String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetRolesWithException(filter, limit, offset, sortBy, sortOrder,
                tenantDomain);
        List<ExpressionNode> expressionNodes = getExpressionNodes(filter);
        List<RoleBasicInfo> roleBasicInfoList = roleDAO.getRoles(expressionNodes, limit, offset, sortBy,
                sortOrder, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetRoles(filter, limit, offset, sortBy, sortOrder, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoles(roleBasicInfoList, filter, limit, offset, sortBy, sortOrder,
                        tenantDomain);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get filtered roles successfully.", getUser(tenantDomain)));
        }
        return roleBasicInfoList;
    }

    @Override
    public Role getRole(String roleID, String tenantDomain) throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetRoleWithException(roleID, tenantDomain);
        Role role = roleDAO.getRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetRole(roleID, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRole(role, roleID, tenantDomain);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get role of id : %s successfully.", getUser(tenantDomain), roleID));
        }
        return role;
    }

    @Override
    public RoleBasicInfo getRoleBasicInfoById(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        RoleBasicInfo role = roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoleBasicInfo(role, roleID, tenantDomain);
            }
        }
        return role;
    }

    @Override
    public RoleBasicInfo updateRoleName(String roleID, String newRoleName, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreUpdateRoleNameWithException(roleID, newRoleName, tenantDomain);
        if (isDomainSeparatorPresent(newRoleName)) {
            // SCIM2 API only adds roles to the internal domain.
            throw new IdentityRoleManagementClientException(INVALID_REQUEST.getCode(), "Invalid character: "
                    + UserCoreConstants.DOMAIN_SEPARATOR + " contains in the role name: " + newRoleName + ".");
        }
        roleDAO.updateRoleName(roleID, newRoleName, tenantDomain);
        roleManagementEventPublisherProxy.publishPostUpdateRoleName(roleID, newRoleName, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s updated role name of role id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
    }

    @Override
    public void deleteRole(String roleID, String tenantDomain) throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreDeleteRoleWithException(roleID, tenantDomain);
        roleDAO.deleteRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostDeleteRole(roleID, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s deleted role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
    }

    @Override
    public List<UserBasicInfo> getUserListOfRole(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetUserListOfRoleWithException(roleID, tenantDomain);
        List<UserBasicInfo> userBasicInfoList = roleDAO.getUserListOfRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetUserListOfRole(roleID, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get list of users of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return userBasicInfoList;
    }

    @Override
    public RoleBasicInfo updateUserListOfRole(String roleID, List<String> newUserIDList, List<String> deletedUserIDList,
                                              String tenantDomain) throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreUpdateUserListOfRoleWithException(roleID, newUserIDList,
                deletedUserIDList,
                tenantDomain);
        roleDAO.updateUserListOfRole(roleID, newUserIDList, deletedUserIDList, tenantDomain);
        roleManagementEventPublisherProxy.publishPostUpdateUserListOfRole(roleID, newUserIDList, deletedUserIDList,
                tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s updated list of users of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
    }

    @Override
    public List<GroupBasicInfo> getGroupListOfRole(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetGroupListOfRoleWithException(roleID, tenantDomain);
        List<GroupBasicInfo> groupBasicInfoList = roleDAO.getGroupListOfRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetGroupListOfRole(roleID, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get list of groups of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return groupBasicInfoList;
    }

    @Override
    public RoleBasicInfo updateGroupListOfRole(String roleID, List<String> newGroupIDList,
                                               List<String> deletedGroupIDList, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreUpdateGroupListOfRoleWithException(roleID, newGroupIDList,
                deletedGroupIDList, tenantDomain);
        roleDAO.updateGroupListOfRole(roleID, newGroupIDList, deletedGroupIDList, tenantDomain);
        roleManagementEventPublisherProxy.publishPostUpdateGroupListOfRole(roleID, newGroupIDList, deletedGroupIDList,
                tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s updated list of groups of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
    }

    @Override
    public List<IdpGroup> getIdpGroupListOfRole(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetIdpGroupListOfRoleWithException(roleID, tenantDomain);
        List<IdpGroup> idpGroups = roleDAO.getIdpGroupListOfRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostIdpGetGroupListOfRole(roleID, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get list of idp groups of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return idpGroups;
    }

    @Override
    public RoleBasicInfo updateIdpGroupListOfRole(String roleID, List<IdpGroup> newGroupList,
                                                  List<IdpGroup> deletedGroupList, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreUpdateIdpGroupListOfRoleWithException(roleID, newGroupList,
                deletedGroupList, tenantDomain);
        removeSimilarIdpGroups(newGroupList, deletedGroupList);
        roleDAO.updateIdpGroupListOfRole(roleID, newGroupList, deletedGroupList, tenantDomain);
        roleManagementEventPublisherProxy.publishPostUpdateIdpGroupListOfRole(roleID, newGroupList, deletedGroupList,
                tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s updated list of idp groups of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
    }

    @Override
    public List<Permission> getPermissionListOfRole(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetPermissionListOfRoleWithException(roleID, tenantDomain);
        List<Permission> permissionListOfRole = roleDAO.getPermissionListOfRole(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetPermissionListOfRole(roleID, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get list of permissions of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return permissionListOfRole;
    }

    @Override
    public RoleBasicInfo updatePermissionListOfRole(String roleID, List<Permission> addedPermissions,
                                                       List<Permission> deletedPermissions, String tenantDomain)
            throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreUpdatePermissionsForRoleWithException(roleID, addedPermissions,
                deletedPermissions, tenantDomain);
        removeSimilarPermissions(addedPermissions, deletedPermissions);
        RoleBasicInfo roleBasicInfo =  roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
        validatePermissions(addedPermissions, roleBasicInfo.getAudience(), roleBasicInfo.getAudienceId(), tenantDomain);
        roleDAO.updatePermissionListOfRole(roleID, addedPermissions,
                deletedPermissions, tenantDomain);
        roleManagementEventPublisherProxy.publishPostUpdatePermissionsForRole(roleID, addedPermissions,
                deletedPermissions, tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s set list of permissions of role of id : %s successfully.",
                    getUser(tenantDomain), roleID));
        }
        return roleDAO.getRoleBasicInfoById(roleID, tenantDomain);
    }

    @Override
    public boolean isExistingRole(String roleID, String tenantDomain) throws IdentityRoleManagementException {

        return roleDAO.isExistingRoleID(roleID, tenantDomain);
    }

    @Override
    public boolean isExistingRoleName(String roleName, String audience, String audienceId, String tenantDomain)
            throws IdentityRoleManagementException {

        return roleDAO.isExistingRoleName(roleName, audience, audienceId, tenantDomain);
    }

    @Override
    public Set<String> getSystemRoles() {

        return roleDAO.getSystemRoles();
    }

    @Override
    public int getRolesCount(String tenantDomain) throws IdentityRoleManagementException {

        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy =
                RoleManagementEventPublisherProxy.getInstance();
        roleManagementEventPublisherProxy.publishPreGetRolesCountWithException(tenantDomain);
        int count = roleDAO.getRolesCount(tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetRolesCount(tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s get roles count successfully.", getUser(tenantDomain)));
        }
        return count;
    }

    @Override
    public Role getRoleWithoutUsers(String roleID, String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        RoleManagementEventPublisherProxy roleManagementEventPublisherProxy = RoleManagementEventPublisherProxy
                .getInstance();
        roleManagementEventPublisherProxy.publishPreGetRoleWithException(roleID, tenantDomain);
        Role role = roleDAO.getRoleWithoutUsers(roleID, tenantDomain);
        roleManagementEventPublisherProxy.publishPostGetRole(roleID, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRole(role, roleID, tenantDomain);
            }
        }
        return role;
    }

    @Override
    public String getRoleNameByRoleId(String roleID, String tenantDomain) throws IdentityRoleManagementException {

        return roleDAO.getRoleNameByID(roleID, tenantDomain);
    }

    @Override
    public String getRoleIdByName(String roleName, String audience, String audienceId, String tenantDomain)
            throws IdentityRoleManagementException {

        return roleDAO.getRoleIdByName(roleName, audience, audienceId, tenantDomain);
    }

    @Override
    public void addMainRoleToSharedRoleRelationship(String mainRoleUUID, String sharedRoleUUID,
                                                    String mainRoleTenantDomain, String sharedRoleTenantDomain)
            throws IdentityRoleManagementException {

        roleDAO.addMainRoleToSharedRoleRelationship(mainRoleUUID, sharedRoleUUID, mainRoleTenantDomain,
                sharedRoleTenantDomain);
    }

    @Override
    public List<RoleBasicInfo> getRoleListOfUser(String userId, String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        List<RoleBasicInfo> roles = roleDAO.getRoleListOfUser(userId, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoleListOfUser(roles, userId, tenantDomain);
            }
        }
        return roles;
    }

    @Override
    public List<RoleBasicInfo> getRoleListOfGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        List<RoleBasicInfo> roles = roleDAO.getRoleListOfGroups(groupIds, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoleListOfGroups(roles, groupIds, tenantDomain);
            }
        }
        return roles;
    }

    @Override
    public List<RoleBasicInfo> getRoleListOfIdpGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException {

        List<RoleManagementListener> roleManagementListenerList = RoleManagementServiceComponentHolder.getInstance()
                .getRoleManagementListenerList();
        List<RoleBasicInfo> roles = roleDAO.getRoleListOfIdpGroups(groupIds, tenantDomain);
        for (RoleManagementListener roleManagementListener : roleManagementListenerList) {
            if (roleManagementListener.isEnable()) {
                roleManagementListener.postGetRoleListOfIdpGroups(roles, groupIds, tenantDomain);
            }
        }
        return roles;
    }

    @Override
    public List<String> getRoleIdListOfUser(String userId, String tenantDomain) throws IdentityRoleManagementException {

        return roleDAO.getRoleIdListOfUser(userId, tenantDomain);
    }

    @Override
    public List<String> getRoleIdListOfGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException {

        return roleDAO.getRoleIdListOfGroups(groupIds, tenantDomain);
    }

    @Override
    public List<String> getRoleIdListOfIdpGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException {

        return roleDAO.getRoleIdListOfIdpGroups(groupIds, tenantDomain);
    }

    @Override
    public void deleteRolesByApplication(String applicationId, String tenantDomain)
            throws IdentityRoleManagementException {

        roleDAO.deleteRolesByApplication(applicationId, tenantDomain);
    }

    @Override
    public Map<String, String> getMainRoleToSharedRoleMappingsBySubOrg(List<String> roleIds,
                                                                             String subOrgTenantDomain)
            throws IdentityRoleManagementException {

        return roleDAO.getMainRoleToSharedRoleMappingsBySubOrg(roleIds, subOrgTenantDomain);
    }

    private String getUser(String tenantDomain) {

        String user = CarbonContext.getThreadLocalCarbonContext().getUsername();
        if (StringUtils.isNotBlank(user)) {
            user = UserCoreUtil.addTenantDomainToEntry(user, tenantDomain);
        } else {
            user = CarbonConstants.REGISTRY_SYSTEM_USERNAME;
        }
        return user;
    }

    /**
     * Get organization ID by tenantDomain.
     *
     * @param tenantDomain tenantDomain.
     * @throws IdentityRoleManagementException Error occurred while retrieving organization id.
     */
    private String getOrganizationIdByTenantDomain(String tenantDomain) throws IdentityRoleManagementException {

        try {
            return RoleManagementServiceComponentHolder.getInstance().getOrganizationManager()
                    .resolveOrganizationId(tenantDomain);

        } catch (OrganizationManagementException e) {
            String errorMessage = "Error while retrieving the organization id for the given tenantDomain: "
                    + tenantDomain;
            throw new IdentityRoleManagementServerException(UNEXPECTED_SERVER_ERROR.getCode(), errorMessage, e);
        }
    }

    /**
     * Validate organization role audience.
     *
     * @param organizationId Organization ID.
     * @throws IdentityRoleManagementException Error occurred while validating organization role audience.
     */
    private void validateOrganizationRoleAudience(String organizationId)
            throws IdentityRoleManagementException {

        try {
            boolean isExists  = RoleManagementServiceComponentHolder.getInstance().getOrganizationManager()
                    .isOrganizationExistById(organizationId);
            if (!isExists) {
                throw new IdentityRoleManagementClientException(INVALID_AUDIENCE.getCode(),
                        "Invalid audience. No organization found with organization id: " + organizationId);
            }

        } catch (OrganizationManagementException e) {
            String errorMessage = "Error while checking the organization exist by id : " + organizationId;
            throw new IdentityRoleManagementServerException(UNEXPECTED_SERVER_ERROR.getCode(), errorMessage, e);
        }
    }

    /**
     * Validate permissions.
     *
     * @param permissions Permissions.
     * @param audience  Audience.
     * @param audienceId  Audience ID.
     * @param tenantDomain Tenant domain.
     * @throws IdentityRoleManagementException Error occurred while validating permissions.
     */
    private void validatePermissions(List<Permission> permissions, String audience, String audienceId,
                                     String tenantDomain)
            throws IdentityRoleManagementException {

        switch (audience) {
            case ORGANIZATION:
                validatePermissionsForOrganization(permissions, tenantDomain);
                break;
            default:
                break;
        }
    }

    /**
     * Validate permissions for organization audience.
     *
     * @param permissions Permissions.
     * @throws IdentityRoleManagementException Error occurred while validating permissions.
     */
    private void validatePermissionsForOrganization(List<Permission> permissions, String tenantDomain)
            throws IdentityRoleManagementException {

        try {
            List<Scope> scopes = RoleManagementServiceComponentHolder.getInstance()
                    .getApiResourceManager().getScopesByTenantDomain(tenantDomain, "");
            List<String> scopeNameList = new ArrayList<>();
            for (Scope scope : scopes) {
                scopeNameList.add(scope.getName());
            }
            for (Permission permission : permissions) {

                if (!scopeNameList.contains(permission.getName())) {
                    throw new IdentityRoleManagementClientException(INVALID_PERMISSION.getCode(),
                            "Permission: " + permission.getName() + " not found");
                }
            }
        } catch (APIResourceMgtException e) {
            throw new IdentityRoleManagementException("Error while retrieving scopes", "Error while retrieving scopes "
                    + "for tenantDomain: " + tenantDomain, e);
        }
    }

    /**
     * Check if the role name has a domain separator character.
     * @param roleName Role name.
     * @return True if the role name has a domain separator character.
     */
    private boolean isDomainSeparatorPresent(String roleName) {

        return roleName.contains(UserCoreConstants.DOMAIN_SEPARATOR);
    }

    private void removeSimilarPermissions(List<Permission> arr1, List<Permission> arr2) {
        List<Permission> toRemove = new ArrayList<>();

        for (Permission p1 : arr1) {
            for (Permission p2 : arr2) {
                if (p1.getName().equals(p2.getName())) {
                    toRemove.add(p1);
                    break;
                }
            }
        }
        arr1.removeAll(toRemove);
        arr2.removeAll(toRemove);
    }

    private void removeSimilarIdpGroups(List<IdpGroup> arr1, List<IdpGroup> arr2) {
        List<IdpGroup> toRemove = new ArrayList<>();

        for (IdpGroup p1 : arr1) {
            for (IdpGroup p2 : arr2) {
                if (p1.getGroupId().equals(p2.getGroupId())) {
                    toRemove.add(p1);
                    break;
                }
            }
        }
        arr1.removeAll(toRemove);
        arr2.removeAll(toRemove);
    }

    /**
     * Get the filter node as a list.
     *
     * @param filter Filter string.
     * @throws IdentityRoleManagementException Error when validate filters.
     */
    private List<ExpressionNode> getExpressionNodes(String filter) throws IdentityRoleManagementException {

        List<ExpressionNode> expressionNodes = new ArrayList<>();
        filter = StringUtils.isBlank(filter) ? StringUtils.EMPTY : filter;
        try {
            if (StringUtils.isNotBlank(filter)) {
                FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();
                setExpressionNodeList(rootNode, expressionNodes);
            }
            return expressionNodes;
        } catch (IOException | IdentityException e) {
            throw new IdentityRoleManagementClientException(INVALID_REQUEST.getCode(), "Invalid filter");
        }
    }

    /**
     * Set the node values as list of expression.
     *
     * @param node       filter node.
     * @param expression list of expression.
     */
    private void setExpressionNodeList(Node node, List<ExpressionNode> expression) {

        if (node instanceof ExpressionNode) {
            if (StringUtils.isNotBlank(((ExpressionNode) node).getAttributeValue())) {
                expression.add((ExpressionNode) node);
            }
        } else if (node instanceof OperationNode) {
            setExpressionNodeList(node.getLeftNode(), expression);
            setExpressionNodeList(node.getRightNode(), expression);
        }
    }
}