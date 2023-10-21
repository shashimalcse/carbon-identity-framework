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

package org.wso2.carbon.identity.role.v2.mgt.core.listener;

import org.wso2.carbon.identity.role.v2.mgt.core.exception.IdentityRoleManagementException;
import org.wso2.carbon.identity.role.v2.mgt.core.model.GroupBasicInfo;
import org.wso2.carbon.identity.role.v2.mgt.core.model.IdpGroup;
import org.wso2.carbon.identity.role.v2.mgt.core.model.Permission;
import org.wso2.carbon.identity.role.v2.mgt.core.model.Role;
import org.wso2.carbon.identity.role.v2.mgt.core.model.RoleBasicInfo;
import org.wso2.carbon.identity.role.v2.mgt.core.model.UserBasicInfo;

import java.util.List;

/**
 * Provides a set of methods to act as listeners before and after key operations related to role management.
 * These listeners can be utilized to enforce additional checks, validations, or side-effects for
 * role management functions.
 */
public interface RoleManagementListener {

    /**
     * Get the execution order identifier for this listener.
     *
     * @return The execution order identifier integer value.
     */
    int getExecutionOrderId();

    /**
     * Get the default order identifier for this listener.
     *
     * @return default order id
     */
    public int getDefaultOrderId();

    /**
     * Check whether the listener is enabled or not
     *
     * @return true if enabled
     */
    public boolean isEnable();

    /**
     * Invoked before a new role is added.
     *
     * @param roleName     The name of the role being added.
     * @param userList     A list of user IDs associated with this role.
     * @param groupList    A list of group IDs associated with this role.
     * @param permissions  A list of permissions associated with this role.
     * @param audience     The audience type for which the role is being created.
     * @param audienceId   The ID of the audience type.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the addition should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs.
     */
    boolean preAddRole(String roleName, List<String> userList, List<String> groupList,
                       List<Permission> permissions, String audience, String audienceId,
                       String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after a new role is added.
     *
     * @param roleBasicInfo The basic info of role being added.
     * @param roleName      The name of the role being added.
     * @param userList      A list of user IDs associated with this role.
     * @param groupList     A list of group IDs associated with this role.
     * @param permissions   A list of permissions associated with this role.
     * @param audience      The audience type for which the role is being created.
     * @param audienceId    The ID of the audience type.
     * @param tenantDomain  The domain in which the operation is being performed.
     * @return true if the addition should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs.
     */
    boolean postAddRole(RoleBasicInfo roleBasicInfo, String roleName, List<String> userList, List<String> groupList,
                        List<Permission> permissions, String audience, String audienceId,
                        String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving a list of roles based on specified criteria.
     *
     * @param limit        The maximum number of roles to retrieve.
     * @param offset       The starting index from which to retrieve roles.
     * @param sortBy       The attribute by which the roles should be sorted (e.g., "name", "creationDate").
     * @param sortOrder    The order in which to sort the roles.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoles(Integer limit, Integer offset, String sortBy, String sortOrder,
                        String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving a list of roles based on specified criteria.
     *
     * @param roleBasicInfoList The list of role basic info.
     * @param limit             The maximum number of roles to retrieve.
     * @param offset            The starting index from which to retrieve roles.
     * @param sortBy            The attribute by which the roles should be sorted (e.g., "name", "creationDate").
     * @param sortOrder         The order in which to sort the roles.
     * @param tenantDomain      The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetRoles(List<RoleBasicInfo> roleBasicInfoList, Integer limit, Integer offset, String sortBy,
                         String sortOrder, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving a list of roles based on specified criteria.
     *
     * @param filter       The filter value.
     * @param limit        The maximum number of roles to retrieve.
     * @param offset       The starting index from which to retrieve roles.
     * @param sortBy       The attribute by which the roles should be sorted (e.g., "name", "creationDate").
     * @param sortOrder    The order in which to sort the roles.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoles(String filter, Integer limit, Integer offset, String sortBy, String sortOrder,
                        String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving a list of roles based on specified criteria.
     *
     * @param roleBasicInfoList The list of role basic info.
     * @param filter            The filter value.
     * @param limit             The maximum number of roles to retrieve.
     * @param offset            The starting index from which to retrieve roles.
     * @param sortBy            The attribute by which the roles should be sorted (e.g., "name", "creationDate").
     * @param sortOrder         The order in which to sort the roles.
     * @param tenantDomain      The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetRoles(List<RoleBasicInfo> roleBasicInfoList, String filter, Integer limit, Integer offset,
                         String sortBy, String sortOrder, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving details of a specific role.
     *
     * @param roleId       The unique identifier of the role to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving details of a specific role.
     *
     * @param role         The role object.
     * @param roleId       The unique identifier of the role to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetRole(Role role, String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving basic details of a specific role.
     *
     * @param roleId       The unique identifier of the role to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleBasicInfo(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving basic details of a specific role.
     *
     * @param roleBasicInfo The role basic info object.
     * @param roleId        The unique identifier of the role to be retrieved.
     * @param tenantDomain  The domain in which the operation is being performed.
     * @return true if the retrieval should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetRoleBasicInfo(RoleBasicInfo roleBasicInfo, String roleId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before updating the name of a specific role.
     *
     * @param roleId       The unique identifier of the role whose name is to be updated.
     * @param newRoleName  The new name intended for the role.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the name update should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean preUpdateRoleName(String roleId, String newRoleName, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after updating the name of a specific role.
     *
     * @param roleId       The unique identifier of the role whose name is to be updated.
     * @param newRoleName  The new name intended for the role.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the name update should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean postUpdateRoleName(String roleId, String newRoleName, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before deleting a specific role.
     *
     * @param roleId       The unique identifier of the role to be deleted.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the role deletion should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-deletion phase.
     */
    boolean preDeleteRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after deleting a specific role.
     *
     * @param roleId       The unique identifier of the role to be deleted.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the role deletion should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-deletion phase.
     */
    boolean postDeleteRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of users associated with a specific role.
     *
     * @param roleId       The unique identifier of the role for which the user list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the user list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetUserListOfRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of users associated with a specific role.
     *
     * @param userBasicInfoList User basic info list.
     * @param roleId            The unique identifier of the role for which the user list is to be retrieved.
     * @param tenantDomain      The domain in which the operation is being performed.
     * @return true if the retrieval of the user list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetUserListOfRole(List<UserBasicInfo> userBasicInfoList, String roleId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before updating the list of users associated with a specific role.
     *
     * @param roleId            The unique identifier of the role for which the user list is to be updated.
     * @param newUserIDList     A list of user IDs to be newly associated with the role.
     * @param deletedUserIDList A list of user IDs to be disassociated from the role.
     * @param tenantDomain      The domain in which the operation is being performed.
     * @return true if the update of the user list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean preUpdateUserListOfRole(String roleId, List<String> newUserIDList, List<String> deletedUserIDList,
                                    String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after updating the list of users associated with a specific role.
     *
     * @param roleId            The unique identifier of the role for which the user list is to be updated.
     * @param newUserIDList     A list of user IDs to be newly associated with the role.
     * @param deletedUserIDList A list of user IDs to be disassociated from the role.
     * @param tenantDomain      The domain in which the operation is being performed.
     * @return true if the update of the user list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean postUpdateUserListOfRole(String roleId, List<String> newUserIDList, List<String> deletedUserIDList,
                                     String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of groups associated with a specific role.
     *
     * @param roleId       The unique identifier of the role for which the group list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetGroupListOfRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of groups associated with a specific role.
     *
     * @param groupBasicInfoList Group basic info list.
     * @param roleId             The unique identifier of the role for which the group list is to be retrieved.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the retrieval of the group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetGroupListOfRole(List<GroupBasicInfo> groupBasicInfoList, String roleId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before updating the list of groups associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the group list is to be updated.
     * @param newGroupIDList     A list of group IDs to be newly associated with the role.
     * @param deletedGroupIDList A list of group IDs to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean preUpdateGroupListOfRole(String roleId, List<String> newGroupIDList, List<String> deletedGroupIDList,
                                     String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after updating the list of groups associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the group list is to be updated.
     * @param newGroupIDList     A list of group IDs to be newly associated with the role.
     * @param deletedGroupIDList A list of group IDs to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean postUpdateGroupListOfRole(String roleId, List<String> newGroupIDList, List<String> deletedGroupIDList,
                                      String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of Identity Provider (IdP) groups associated with a specific role.
     *
     * @param roleId       The unique identifier of the role for which the IdP group list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the IdP group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetIdpGroupListOfRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of Identity Provider (IdP) groups associated with a specific role.
     *
     * @param idpGroups    Idp groups.
     * @param roleId       The unique identifier of the role for which the IdP group list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the IdP group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetIdpGroupListOfRole(List<IdpGroup> idpGroups, String roleId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before updating the list of Identity Provider (IdP) groups associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the IdP group list is to be updated.
     * @param newGroupIDList     A list of IdP groups to be newly associated with the role.
     * @param deletedGroupIDList A list of IdP groups to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the IdP group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean preUpdateIdpGroupListOfRole(String roleId, List<IdpGroup> newGroupIDList,
                                        List<IdpGroup> deletedGroupIDList, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after updating the list of Identity Provider (IdP) groups associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the IdP group list is to be updated.
     * @param newGroupIDList     A list of IdP groups to be newly associated with the role.
     * @param deletedGroupIDList A list of IdP groups to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the IdP group list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean postUpdateIdpGroupListOfRole(String roleId, List<IdpGroup> newGroupIDList,
                                         List<IdpGroup> deletedGroupIDList, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of permissions associated with a specific role.
     *
     * @param roleId       The unique identifier of the role for which the permissions list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the permissions list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetPermissionListOfRole(String roleId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of permissions associated with a specific role.
     *
     * @param permissionListOfRole Permission list of role.
     * @param roleId               The unique identifier of the role for which the permissions list is to be retrieved.
     * @param tenantDomain         The domain in which the operation is being performed.
     * @return true if the retrieval of the permissions list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean postGetPermissionListOfRole(List<Permission> permissionListOfRole, String roleId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before updating the list of permissions associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the permissions are to be updated.
     * @param addedPermissions   A list of permissions to be newly associated with the role.
     * @param deletedPermissions A list of permissions to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the permissions list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean preUpdatePermissionsForRole(String roleId, List<Permission> addedPermissions,
                                        List<Permission> deletedPermissions, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after updating the list of permissions associated with a specific role.
     *
     * @param roleId             The unique identifier of the role for which the permissions are to be updated.
     * @param addedPermissions   A list of permissions to be newly associated with the role.
     * @param deletedPermissions A list of permissions to be disassociated from the role.
     * @param tenantDomain       The domain in which the operation is being performed.
     * @return true if the update of the permissions list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-update phase.
     */
    boolean postUpdatePermissionsForRole(String roleId, List<Permission> addedPermissions,
                                         List<Permission> deletedPermissions, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the count of roles within a specified tenant domain.
     *
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the roles count should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRolesCount(String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the count of roles within a specified tenant domain.
     *
     * @param count        The number of roles retrieved from the specified tenant domain.
     * @param tenantDomain The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRolesCount(int count, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of roles associated with a specific user in the given tenant domain.
     *
     * @param userId       The unique identifier of the user for whom the roles list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of the user's roles list should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleListOfUser(String userId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of roles associated with a specific user in the given tenant domain.
     *
     * @param roleBasicInfoList A list of basic role information retrieved for the specified user.
     * @param userId            The unique identifier of the user for whom the roles list was retrieved.
     * @param tenantDomain      The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleListOfUser(List<RoleBasicInfo> roleBasicInfoList, String userId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of roles associated with specific groups in the given tenant domain.
     *
     * @param groupIds     A list of unique identifiers for the groups for which the roles list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of roles list for the specified groups should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleListOfGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of roles associated with specific groups in the given tenant domain.
     *
     * @param roleBasicInfoList A list of basic role information retrieved for the specified groups.
     * @param groupIds          A list of unique identifiers for the groups for which the roles list was retrieved.
     * @param tenantDomain      The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleListOfGroups(List<RoleBasicInfo> roleBasicInfoList, List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of roles associated with specific Identity Provider (IdP) groups in
     * the given tenant domain.
     *
     * @param groupIds     A list of unique identifiers for the IdP groups for which the roles list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of roles list for the specified IdP groups should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleListOfIdpGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of roles associated with specific Identity Provider (IdP) groups in
     * the given tenant domain.
     *
     * @param roleBasicInfoList A list of basic role information retrieved for the specified IdP groups.
     * @param groupIds          A list of unique identifiers for the IdP groups for which the roles list was retrieved.
     * @param tenantDomain      The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleListOfIdpGroups(List<RoleBasicInfo> roleBasicInfoList, List<String> groupIds,
                                       String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of role IDs associated with a specific user in the given tenant domain.
     *
     * @param userId       The unique identifier for the user for which the role IDs list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of role IDs for the specified user should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleIdListOfUser(String userId, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of role IDs associated with a specific user in the given tenant domain.
     *
     * @param roleIds      A list of role IDs retrieved for the specified user.
     * @param userId       The unique identifier for the user for which the role IDs list was retrieved.
     * @param tenantDomain The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleIdListOfUser(List<String> roleIds, String userId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of role IDs associated with specific groups in the given tenant domain.
     *
     * @param groupIds     A list of unique identifiers for the groups for which the role IDs list is to be retrieved.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of role IDs for the specified groups should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleIdListOfGroups(List<String> groupIds, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of role IDs associated with specific groups in the given tenant domain.
     *
     * @param roleIds      A list of role IDs retrieved for the specified groups.
     * @param tenantDomain The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleIdListOfGroups(List<String> roleIds, String tenantDomain) throws IdentityRoleManagementException;

    /**
     * Invoked before retrieving the list of role IDs associated with specific Identity Provider (IdP) groups
     * in the given tenant domain.
     *
     * @param groupIds     A list of unique identifiers for the IdP groups.
     * @param tenantDomain The domain in which the operation is being performed.
     * @return true if the retrieval of role IDs for the specified IdP groups should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-retrieval phase.
     */
    boolean preGetRoleIdListOfIdpGroups(List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after retrieving the list of role IDs associated with specific Identity Provider (IdP) groups
     * in the given tenant domain.
     *
     * @param roleIds      A list of role IDs retrieved for the specified IdP groups.
     * @param groupIds     A list of unique identifiers for the IdP groups for which the role IDs list was retrieved.
     * @param tenantDomain The domain in which the operation was performed.
     * @return true if the post-retrieval operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-retrieval phase.
     */
    boolean postGetRoleIdListOfIdpGroups(List<String> roleIds, List<String> groupIds, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked before deleting roles associated with a specific application in the given tenant domain.
     *
     * @param applicationId The unique identifier of the application for which roles are to be deleted.
     * @param tenantDomain  The domain in which the operation is being performed.
     * @return true if the deletion of roles for the specified application should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the pre-deletion phase.
     */
    boolean preDeleteRolesByApplication(String applicationId, String tenantDomain)
            throws IdentityRoleManagementException;

    /**
     * Invoked after deleting roles associated with a specific application in the given tenant domain.
     *
     * @param applicationId The unique identifier of the application for which roles were deleted.
     * @param tenantDomain  The domain in which the operation was performed.
     * @return true if the post-deletion operation should proceed, false otherwise.
     * @throws IdentityRoleManagementException If an error occurs during the post-deletion phase.
     */
    boolean postDeleteRolesByApplication(String applicationId, String tenantDomain)
            throws IdentityRoleManagementException;

}
