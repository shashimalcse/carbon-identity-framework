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

package org.wso2.carbon.identity.role.v2.mgt.core.model;

/**
 * Represents the role dto.
 */
public class RoleDTO {

    private String name;
    private String id;
    private int audienceRefId;
    private int tenantId;
    private RoleAudience roleAudience;

    public RoleDTO(String name, int audienceRefId) {

        this.name = name;
        this.audienceRefId = audienceRefId;
    }

    public RoleDTO(String name, String id, int audienceRefId, int tenantId) {

        this.name = name;
        this.id = id;
        this.audienceRefId = audienceRefId;
        this.tenantId = tenantId;
    }

    public RoleDTO(String name, int audienceRefId, int tenantId) {

        this.name = name;
        this.audienceRefId = audienceRefId;
        this.tenantId = tenantId;
    }

    /**
     * Get the role name.
     *
     * @return Role name.
     */
    public String getName() {

        return name;
    }

    /**
     * Set the role name.
     *
     * @param name Role name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Get the audience reference id.
     *
     * @return Audience reference id.
     */
    public int getAudienceRefId() {

        return audienceRefId;
    }

    /**
     * Set the audience reference id.
     *
     * @param audienceRefId Audience reference id.
     */
    public void setAudienceRefId(int audienceRefId) {

        this.audienceRefId = audienceRefId;
    }

    /**
     * Get the role audience.
     *
     * @return Role audience.
     */
    public RoleAudience getRoleAudience() {

        return roleAudience;
    }

    /**
     * Set the role audience.
     *
     * @param roleAudience Role audience.
     */
    public void setRoleAudience(RoleAudience roleAudience) {

        this.roleAudience = roleAudience;
    }

    /**
     * Get the ID.
     *
     * @return ID.
     */
    public String getId() {

        return id;
    }

    /**
     * Set the ID.
     *
     * @param id ID.
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Get the tenant id.
     *
     * @return Tenant id.
     */
    public int getTenantId() {

        return tenantId;
    }

    /**
     * Set the tenant id.
     *
     * @param tenantId Tenant id.
     */
    public void setTenantId(int tenantId) {

        this.tenantId = tenantId;
    }
}
