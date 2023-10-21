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

package org.wso2.carbon.identity.role.v2.mgt.core.dao;

/**
 * SQL Queries used in {@link RoleDAOImpl}.
 */
public class SQLQueries {

    public static final String GET_ROLE_UM_ID_BY_UUID = "SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID;";

    public static final String ADD_ROLE_SQL = "INSERT INTO UM_HYBRID_ROLE (UM_ROLE_NAME, " +
            "UM_AUDIENCE_REF_ID, UM_UUID, UM_TENANT_ID) VALUES (:UM_ROLE_NAME;, :UM_AUDIENCE_REF_ID;, :UM_UUID;, " +
            ":UM_TENANT_ID;)";

    public static final String ADD_ROLE_AUDIENCE_SQL = "INSERT INTO UM_HYBRID_ROLE_AUDIENCE (UM_AUDIENCE," +
            "UM_AUDIENCE_ID) VALUES (:UM_AUDIENCE;, :UM_AUDIENCE_ID;)";

    public static final String GET_ROLE_AUDIENCE_SQL = "SELECT UM_ID FROM UM_HYBRID_ROLE_AUDIENCE WHERE UM_AUDIENCE " +
            "=:UM_AUDIENCE; AND UM_AUDIENCE_ID=:UM_AUDIENCE_ID;";

    public static final String ADD_ROLE_SCOPE_SQL = "INSERT INTO ROLE_SCOPE (ROLE_ID, SCOPE_NAME, TENANT_ID) VALUES " +
            "(:ROLE_ID;, :SCOPE_NAME;, :TENANT_ID;)";

    public static final String DELETE_ROLE_SCOPE_BY_SCOPE_NAME_SQL = "DELETE ROLE_SCOPE WHERE ROLE_ID =:ROLE_ID; AND " +
            "SCOPE_NAME=:SCOPE_NAME;";

    public static final String DELETE_ROLE_SCOPE_BY_ROLE_ID_SQL = "DELETE ROLE_SCOPE WHERE ROLE_ID =:ROLE_ID;";

    public static final String GET_ROLE_SCOPE_SQL = "SELECT NAME, DISPLAY_NAME, API_ID FROM ROLE_SCOPE INNER JOIN " +
            "SCOPE ON ROLE_SCOPE.SCOPE_NAME = SCOPE.NAME AND ROLE_SCOPE.TENANT_ID = SCOPE.TENANT_ID WHERE " +
            "ROLE_ID =:ROLE_ID; AND ROLE_SCOPE.TENANT_ID =:TENANT_ID;";

    public static final String GET_ROLE_SCOPE_NAMES_SQL = "SELECT NAME FROM ROLE_SCOPE INNER JOIN " +
            "SCOPE ON ROLE_SCOPE.SCOPE_NAME = SCOPE.NAME AND ROLE_SCOPE.TENANT_ID = SCOPE.TENANT_ID WHERE " +
            "ROLE_ID =:ROLE_ID; AND ROLE_SCOPE.TENANT_ID =:TENANT_ID;";

    public static final String GET_SCOPE_BY_ROLES_SQL = "SELECT NAME FROM ROLE_SCOPE INNER JOIN SCOPE ON " +
            "ROLE_SCOPE.SCOPE_NAME = SCOPE.NAME AND ROLE_SCOPE.TENANT_ID = SCOPE.TENANT_ID WHERE " +
            "ROLE_SCOPE.TENANT_ID =:TENANT_ID; AND ROLE_ID IN (";

    public static final String GET_ASSOCIATED_APPS_BY_ROLE_ID_SQL = "SELECT APP_ID, APP_NAME FROM " +
            "APP_ROLE_ASSOCIATION INNER JOIN SP_APP ON APP_ROLE_ASSOCIATION.APP_ID = SP_APP.UUID " +
            "WHERE ROLE_ID =:ROLE_ID; AND SP_APP.TENANT_ID=:TENANT_ID;";

    public static final String GET_ASSOCIATED_APP_IDS_BY_ROLE_ID_SQL = "SELECT APP_ID FROM " +
            "APP_ROLE_ASSOCIATION INNER JOIN SP_APP ON APP_ROLE_ASSOCIATION.APP_ID = SP_APP.UUID " +
            "WHERE ROLE_ID =:ROLE_ID; AND SP_APP.TENANT_ID=:TENANT_ID;";

    public static final String ADD_APP_ROLE_ASSOCIATION_SQL = "INSERT INTO APP_ROLE_ASSOCIATION (APP_ID," +
            "ROLE_ID) VALUES (:APP_ID;, :ROLE_ID;)";

    public static final String DELETE_APP_ROLE_ASSOCIATION_BY_ROLE_ID_SQL = "DELETE APP_ROLE_ASSOCIATION WHERE " +
            "ROLE_ID=:ROLE_ID;";

    public static final String IS_SHARED_ROLE_SQL = "SELECT COUNT(UM_MAIN_ROLE_ID) FROM UM_SHARED_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE ON UM_SHARED_ROLE.UM_SHARED_ROLE_ID = UM_HYBRID_ROLE.UM_ID WHERE UM_UUID "
            + "=:UM_UUID; AND UM_SHARED_ROLE_TENANT_ID=:UM_SHARED_ROLE_TENANT_ID;";

    public static final String GET_SHARED_ROLE_MAIN_ROLE_ID_SQL = "SELECT hr.UM_UUID, hr.UM_TENANT_ID FROM " +
            "UM_HYBRID_ROLE hr JOIN UM_SHARED_ROLE sr ON hr.UM_ID = sr.UM_MAIN_ROLE_ID AND hr.UM_TENANT_ID = " +
            "sr.UM_MAIN_ROLE_TENANT_ID JOIN UM_HYBRID_ROLE hr2 ON sr.UM_SHARED_ROLE_ID = hr2.UM_ID AND " +
            "sr.UM_SHARED_ROLE_TENANT_ID = hr2.UM_TENANT_ID WHERE hr2.UM_UUID =:UM_UUID; AND " +
            "hr2.UM_TENANT_ID =:UM_TENANT_ID;";

    public static final String GET_SHARED_ROLES_MAIN_ROLE_IDS_SQL = "SELECT hr.UM_UUID, hr.UM_TENANT_ID FROM " +
            "UM_HYBRID_ROLE hr JOIN UM_SHARED_ROLE sr ON hr.UM_ID = sr.UM_MAIN_ROLE_ID AND hr.UM_TENANT_ID = " +
            "sr.UM_MAIN_ROLE_TENANT_ID JOIN UM_HYBRID_ROLE hr2 ON sr.UM_SHARED_ROLE_ID = hr2.UM_ID AND " +
            "sr.UM_SHARED_ROLE_TENANT_ID = hr2.UM_TENANT_ID WHERE AND hr2.UM_TENANT_ID =:UM_TENANT_ID; AND " +
            "hr2.UM_UUID IN (";

    public static final String GET_MAIN_ROLE_TO_SHARED_ROLE_MAPPINGS_BY_SUBORG_SQL = "SELECT m_main.UM_UUID " +
            ", m_shared.UM_UUID FROM UM_SHARED_ROLE AS s JOIN UM_HYBRID_ROLE " +
            "AS m_main ON s.UM_MAIN_ROLE_ID = m_main.UM_ID AND s.UM_MAIN_ROLE_TENANT_ID = m_main.UM_TENANT_ID " +
            "JOIN UM_HYBRID_ROLE AS m_shared ON s.UM_SHARED_ROLE_ID = m_shared.UM_ID AND s.UM_SHARED_ROLE_TENANT_ID " +
            "= m_shared.UM_TENANT_ID WHERE s.UM_SHARED_ROLE_TENANT_ID = ? AND m_main.UM_UUId IN (";

    public static final String DELETE_SHARED_HYBRID_ROLES_WITH_MAIN_ROLE_SQL = "DELETE FROM UM_HYBRID_ROLE WHERE " +
            "(UM_ID, UM_TENANT_ID) IN (SELECT UM_SHARED_ROLE_ID, UM_SHARED_ROLE_TENANT_ID FROM UM_SHARED_ROLE " +
            "WHERE (UM_MAIN_ROLE_ID, UM_MAIN_ROLE_TENANT_ID) = (SELECT UM_ID, UM_TENANT_ID FROM UM_HYBRID_ROLE " +
            "WHERE UM_UUID =:UM_UUID; AND UM_TENANT_ID =:UM_TENANT_ID;))";

    public static final String DELETE_SHARED_SCIM_ROLES_WITH_MAIN_ROLE_SQL = "DELETE FROM IDN_SCIM_GROUP " +
            "WHERE ROLE_NAME =:ROLE_NAME; AND TENANT_ID =:TENANT_ID; AND AUDIENCE_REF_ID =:AUDIENCE_REF_ID;";

    public static final String GET_SHARED_ROLES_SQL = "SELECT UM_HYBRID_ROLE.UM_ROLE_NAME, " +
            "UM_HYBRID_ROLE.UM_TENANT_ID, UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID FROM UM_SHARED_ROLE JOIN " +
            "UM_HYBRID_ROLE ON UM_SHARED_ROLE.UM_SHARED_ROLE_ID = UM_HYBRID_ROLE.UM_ID AND " +
            "UM_SHARED_ROLE.UM_SHARED_ROLE_TENANT_ID = UM_HYBRID_ROLE.UM_TENANT_ID WHERE " +
            "UM_SHARED_ROLE.UM_MAIN_ROLE_ID IN (SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID; AND " +
            "UM_TENANT_ID =:UM_TENANT_ID;);";

    public static final String GET_ROLES_BY_APP_ID_SQL = "SELECT UM_HYBRID_ROLE.UM_UUID, UM_HYBRID_ROLE.UM_ROLE_NAME," +
            " UM_HYBRID_ROLE.UM_TENANT_ID, UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID " +
            "WHERE UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE=:UM_AUDIENCE; AND " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID=:UM_AUDIENCE_ID; AND UM_HYBRID_ROLE.UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String DELETE_ROLES_BY_APP_ID_SQL = "DELETE FROM UM_HYBRID_ROLE WHERE " +
            "UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID IN (SELECT UM_HYBRID_ROLE_AUDIENCE.UM_ID FROM UM_HYBRID_ROLE_AUDIENCE " +
            "WHERE UM_AUDIENCE=:UM_AUDIENCE; AND UM_AUDIENCE_ID=:UM_AUDIENCE_ID;) AND " +
            "UM_HYBRID_ROLE.UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String IS_ROLE_EXIST_SQL = "SELECT COUNT(UM_ID) FROM UM_HYBRID_ROLE WHERE UM_ROLE_NAME "
            + "=:UM_ROLE_NAME; AND UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID;";

    public static final String GET_ROLE_ID_BY_NAME_AND_AUDIENCE_SQL = "SELECT ATTR_VALUE FROM IDN_SCIM_GROUP WHERE "
            + "TENANT_ID=:TENANT_ID; AND ROLE_NAME=:ROLE_NAME; AND ATTR_NAME=:ATTR_NAME; AND " +
            "AUDIENCE_REF_ID=:AUDIENCE_REF_ID;";

    public static final String ADD_USER_TO_ROLE_SQL = "INSERT INTO UM_HYBRID_USER_ROLE (UM_USER_NAME, UM_ROLE_ID, "
            + "UM_TENANT_ID, UM_DOMAIN_ID) VALUES (:UM_USER_NAME;,(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE "
            + "UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;), :UM_TENANT_ID;, (SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;))";

    public static final String ADD_USER_TO_ROLE_SQL_MSSQL = "INSERT INTO UM_HYBRID_USER_ROLE (UM_USER_NAME, " +
            "UM_ROLE_ID, UM_TENANT_ID,  UM_DOMAIN_ID) SELECT (:UM_USER_NAME;),(SELECT UM_ID FROM UM_HYBRID_ROLE " +
            "WHERE UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;), (:UM_TENANT_ID;), (SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;)";

    public static final String REMOVE_USER_FROM_ROLE_SQL =
            "DELETE FROM UM_HYBRID_USER_ROLE WHERE UM_USER_NAME=:UM_USER_NAME; AND "
                    + "UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_ROLE_NAME=:UM_ROLE_NAME; AND "
                    + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID=-:UM_AUDIENCE_REF_ID;) AND " +
                    "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_ID=(SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE " +
                    "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;)";

    public static final String GET_ROLE_LIST_OF_USER_SQL = "SELECT r.UM_ROLE_NAME, r.UM_UUID, ra.UM_AUDIENCE, " +
            "ra.UM_AUDIENCE_ID FROM UM_HYBRID_ROLE r INNER JOIN UM_HYBRID_ROLE_AUDIENCE ra ON r.UM_AUDIENCE_REF_ID = " +
            "ra.UM_ID INNER JOIN UM_HYBRID_USER_ROLE ur ON r.UM_ID = ur.UM_ROLE_ID INNER JOIN UM_DOMAIN d ON " +
            "ur.UM_DOMAIN_ID = d.UM_DOMAIN_ID WHERE ur.UM_USER_NAME =:UM_USER_NAME; AND " +
            "ur.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_DOMAIN_NAME =:UM_DOMAIN_NAME;";

    public static final String GET_ROLE_ID_LIST_OF_USER_SQL = "SELECT r.UM_UUID FROM UM_HYBRID_ROLE r INNER " +
            "JOIN UM_HYBRID_USER_ROLE ur ON r.UM_ID = ur.UM_ROLE_ID INNER JOIN UM_DOMAIN d ON " +
            "ur.UM_DOMAIN_ID = d.UM_DOMAIN_ID WHERE ur.UM_USER_NAME =:UM_USER_NAME; AND " +
            "ur.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_DOMAIN_NAME =:UM_DOMAIN_NAME;";

    public static final String GET_ROLE_LIST_OF_GROUP_SQL = "SELECT r.UM_ROLE_NAME, r.UM_UUID, ra.UM_AUDIENCE, " +
            "ra.UM_AUDIENCE_ID FROM UM_HYBRID_ROLE r INNER JOIN UM_HYBRID_ROLE_AUDIENCE ra ON r.UM_AUDIENCE_REF_ID = " +
            "ra.UM_ID INNER JOIN UM_HYBRID_GROUP_ROLE gr ON r.UM_ID = gr.UM_ROLE_ID INNER JOIN UM_DOMAIN d ON " +
            "gr.UM_DOMAIN_ID = d.UM_DOMAIN_ID WHERE gr.UM_GROUP_NAME=:UM_GROUP_NAME; AND " +
            "gr.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_DOMAIN_NAME =:UM_DOMAIN_NAME;";

    public static final String GET_ROLE_ID_LIST_OF_GROUP_SQL = "SELECT r.UM_UUID FROM UM_HYBRID_ROLE r " +
            "INNER JOIN UM_HYBRID_GROUP_ROLE gr ON r.UM_ID = gr.UM_ROLE_ID INNER JOIN UM_DOMAIN d ON " +
            "gr.UM_DOMAIN_ID = d.UM_DOMAIN_ID WHERE gr.UM_GROUP_NAME=:UM_GROUP_NAME; AND " +
            "gr.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_TENANT_ID =:UM_TENANT_ID; AND d.UM_DOMAIN_NAME =:UM_DOMAIN_NAME;";

    public static final String ADD_GROUP_TO_ROLE_SQL = "INSERT INTO UM_HYBRID_GROUP_ROLE (UM_GROUP_NAME, UM_ROLE_ID, "
            + "UM_TENANT_ID, UM_DOMAIN_ID) VALUES (:UM_GROUP_NAME;,(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE "
            + "UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;), :UM_TENANT_ID;, (SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;))";

    public static final String ADD_GROUP_TO_ROLE_SQL_MSSQL = "INSERT INTO UM_HYBRID_GROUP_ROLE (UM_GROUP_NAME, " +
            "UM_ROLE_ID, UM_TENANT_ID,  UM_DOMAIN_ID) SELECT (:UM_GROUP_NAME;),(SELECT UM_ID FROM UM_HYBRID_ROLE " +
            "WHERE UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;), (:UM_TENANT_ID;), (SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;)";

    public static final String ADD_SCIM_ROLE_ID_SQL = "INSERT INTO IDN_SCIM_GROUP (TENANT_ID, ROLE_NAME, ATTR_NAME, " +
            "ATTR_VALUE, AUDIENCE_REF_ID) VALUES (:TENANT_ID;, :ROLE_NAME;, :ATTR_NAME;, :ATTR_VALUE;, " +
            ":AUDIENCE_REF_ID;)";

    public static final String GET_AUDIENCE_BY_ID_SQL = "SELECT UM_AUDIENCE, UM_AUDIENCE_ID FROM " +
            "UM_HYBRID_ROLE INNER JOIN UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE UM_HYBRID_ROLE.UM_UUID=:UM_UUID;";

    public static final String GET_AUDIENCE_REF_BY_ID_SQL = "SELECT AUDIENCE_REF_ID FROM IDN_SCIM_GROUP WHERE " +
            "TENANT_ID=:TENANT_ID; AND ATTR_NAME=:ATTR_NAME; AND ATTR_VALUE=:ATTR_VALUE;";

    public static final String ADD_IDP_GROUPS_SQL = "INSERT INTO UM_IDP_GROUP_ROLE (UM_ROLE_ID, UM_GROUP_ID, " +
            "UM_TENANT_ID) VALUES ((SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;), :UM_GROUP_ID;, :UM_TENANT_ID;)";

    public static final String DELETE_IDP_GROUPS_SQL = "DELETE UM_IDP_GROUP_ROLE WHERE UM_GROUP_ID=:UM_GROUP_ID; AND " +
            "UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;) AND UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String GET_IDP_GROUPS_SQL = "SELECT UM_GROUP_ID FROM UM_IDP_GROUP_ROLE WHERE " +
            "UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID; AND " +
            "UM_TENANT_ID=:UM_TENANT_ID;) AND UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String GET_ROLE_LIST_OF_IDP_GROUPS_SQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_AUDIENCE_ID FROM UM_HYBRID_ROLE INNER JOIN UM_HYBRID_ROLE_AUDIENCE ON " +
            "UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID INNER JOIN UM_IDP_GROUP_ROLE ON " +
            "UM_HYBRID_ROLE.UM_ID = UM_IDP_GROUP_ROLE.UM_ROLE_ID WHERE UM_IDP_GROUP_ROLE.UM_GROUP_ID =:UM_GROUP_ID; " +
            "AND UM_IDP_GROUP_ROLE.UM_TENANT_ID =:UM_TENANT_ID;";

    public static final String GET_ROLE_ID_LIST_OF_IDP_GROUPS_SQL = "SELECT UM_UUID FROM UM_HYBRID_ROLE INNER " +
            "JOIN UM_IDP_GROUP_ROLE ON UM_HYBRID_ROLE.UM_ID = UM_IDP_GROUP_ROLE.UM_ROLE_ID WHERE " +
            "UM_IDP_GROUP_ROLE.UM_GROUP_ID =:UM_GROUP_ID; AND UM_IDP_GROUP_ROLE.UM_TENANT_ID =:UM_TENANT_ID;";

    public static final String UPDATE_ROLE_NAME_SQL = "UPDATE UM_HYBRID_ROLE SET UM_ROLE_NAME=:NEW_UM_ROLE_NAME; " +
            "WHERE UM_UUID=:UM_UUID; AND UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String UPDATE_SCIM_ROLE_NAME_SQL = "UPDATE IDN_SCIM_GROUP SET ROLE_NAME=:NEW_ROLE_NAME; " +
            "WHERE TENANT_ID=:TENANT_ID; AND ROLE_NAME=:ROLE_NAME; AND AUDIENCE_REF_ID=AUDIENCE_REF_ID;";

    public static final String GET_USER_LIST_OF_ROLE_SQL = "SELECT UM_USER_NAME, UM_DOMAIN_NAME FROM " +
            "UM_HYBRID_USER_ROLE, UM_DOMAIN WHERE UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE " +
            "UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_TENANT_ID=:UM_TENANT_ID; AND " +
            "UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID;) AND UM_HYBRID_USER_ROLE.UM_TENANT_ID=:UM_TENANT_ID; AND " +
            "UM_HYBRID_USER_ROLE.UM_DOMAIN_ID=UM_DOMAIN.UM_DOMAIN_ID";

    public static final String DELETE_ROLE_SQL = "DELETE FROM UM_HYBRID_ROLE WHERE UM_UUID=:UM_UUID; AND "
            + "UM_TENANT_ID=:UM_TENANT_ID;";

    public static final String DELETE_SCIM_ROLE_SQL = "DELETE FROM IDN_SCIM_GROUP WHERE TENANT_ID=:TENANT_ID; AND "
            + "ROLE_NAME=:ROLE_NAME; AND AUDIENCE_REF_ID=:AUDIENCE_REF_ID;";

    public static final String GET_GROUP_LIST_OF_ROLE_SQL = "SELECT UM_GROUP_NAME, UM_DOMAIN_NAME FROM " +
            "UM_HYBRID_GROUP_ROLE, UM_DOMAIN WHERE UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE " +
            "UM_ROLE_NAME=:UM_ROLE_NAME; AND UM_TENANT_ID=:UM_TENANT_ID; AND " +
            "UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID;) AND UM_HYBRID_GROUP_ROLE.UM_TENANT_ID=:UM_TENANT_ID; AND " +
            "UM_HYBRID_GROUP_ROLE.UM_DOMAIN_ID=UM_DOMAIN.UM_DOMAIN_ID";

    public static final String GET_ROLE_NAME_BY_ID_SQL = "SELECT UM_ROLE_NAME FROM UM_HYBRID_ROLE WHERE "
            + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_UUID=:UM_UUID;";

    public static final String IS_ROLE_ID_EXIST_SQL = "SELECT COUNT(ID) FROM IDN_SCIM_GROUP WHERE "
            + "TENANT_ID=:TENANT_ID; AND ATTR_NAME=:ATTR_NAME; AND ATTR_VALUE=:ATTR_VALUE;";

    public static final String REMOVE_GROUP_FROM_ROLE_SQL =
            "DELETE FROM UM_HYBRID_GROUP_ROLE WHERE " + "UM_GROUP_NAME=:UM_GROUP_NAME; AND "
                    + "UM_ROLE_ID=(SELECT UM_ID FROM UM_HYBRID_ROLE WHERE UM_ROLE_NAME=:UM_ROLE_NAME; AND "
                    + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID=:UM_AUDIENCE_REF_ID;) AND " +
                    "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_ID=(SELECT UM_DOMAIN_ID FROM UM_DOMAIN WHERE "
                    + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_DOMAIN_NAME=:UM_DOMAIN_NAME;)";

    public static final String COUNT_ROLES_BY_TENANT_MYSQL = "SELECT COUNT(UM_ROLE_NAME) FROM UM_HYBRID_ROLE WHERE "
            + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID != -1";

    public static final String COUNT_ROLES_BY_TENANT_ORACLE = "SELECT COUNT(UM_ROLE_NAME) FROM (SELECT UM_ROLE_NAME, "
            + "rownum AS rnum FROM (SELECT UM_ROLE_NAME FROM UM_HYBRID_ROLE WHERE UM_TENANT_ID=:UM_TENANT_ID; AND " +
            "UM_AUDIENCE_REF_ID != -1))";

    public static final String COUNT_ROLES_BY_TENANT_MSSQL = "SELECT COUNT(UM_ROLE_NAME) FROM UM_HYBRID_ROLE WHERE "
            + "UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID != -1";

    public static final String COUNT_ROLES_BY_TENANT_POSTGRESQL = "SELECT COUNT(UM_ROLE_NAME) FROM UM_HYBRID_ROLE "
            + "WHERE UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID != -1";

    public static final String COUNT_ROLES_BY_TENANT_DB2 = "SELECT COUNT(UM_ROLE_NAME) FROM (SELECT ROW_NUMBER() "
            + "OVER(ORDER BY UM_ID DESC) AS rn,UM_HYBRID_ROLE.* FROM UM_HYBRID_ROLE WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID != -1)";

    public static final String COUNT_ROLES_BY_TENANT_INFORMIX = "SELECT COUNT(UM_ROLE_NAME) FROM UM_HYBRID_ROLE WHERE"
            + " UM_TENANT_ID=:UM_TENANT_ID; AND UM_AUDIENCE_REF_ID != -1";

    // DB queries to list roles.
    public static final String GET_ROLES_BY_TENANT_MYSQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; ORDER BY UM_HYBRID_ROLE.UM_ID DESC LIMIT :OFFSET;, :LIMIT;";

    public static final String GET_ROLES_BY_TENANT_ORACLE = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM (SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, UM_AUDIENCE_ID, " +
            "UM_AUDIENCE_REF_ID, rownum AS rnum FROM (SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; ORDER BY UM_HYBRID_ROLE.UM_ID DESC) WHERE rownum <= :END_INDEX;) WHERE " +
            "rnum > :ZERO_BASED_START_INDEX;";

    public static final String GET_ROLES_BY_TENANT_MSSQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; ORDER BY UM_HYBRID_ROLE.UM_ID DESC OFFSET :OFFSET; ROWS FETCH " +
            "NEXT :LIMIT; ROWS ONLY";

    public static final String GET_ROLES_BY_TENANT_POSTGRESQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID; ORDER BY UM_HYBRID_ROLE.UM_ID DESC LIMIT :LIMIT; OFFSET :OFFSET;";

    public static final String GET_ROLES_BY_TENANT_DB2 = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM (SELECT ROW_NUMBER() " +
            "OVER(ORDER BY UM_HYBRID_ROLE.UM_ID DESC) AS rn, UM_HYBRID_ROLE.* FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE " +
            "UM_TENANT_ID=:UM_TENANT_ID;) WHERE rn BETWEEN :ONE_BASED_START_INDEX; " +
            "AND :END_INDEX;";

    public static final String GET_ROLES_BY_TENANT_INFORMIX = "SELECT SKIP :OFFSET; FIRST :LIMIT; UM_ROLE_NAME, " +
            "UM_UUID, UM_AUDIENCE, UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE " +
            "INNER JOIN UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE UM_TENANT_ID=:UM_TENANT_ID; ORDER BY UM_HYBRID_ROLE.UM_ID DESC";

    // DB queries to filter roles.
    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_MYSQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_MYSQL = " UM_TENANT_ID=:UM_TENANT_ID; ORDER BY " +
            "UM_HYBRID_ROLE.UM_ID DESC LIMIT :OFFSET;, :LIMIT;";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_ORACLE = "SELECT UM_ROLE_NAME, UM_UUID, " +
            "UM_AUDIENCE, UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM (SELECT UM_ROLE_NAME, UM_AUDIENCE, UM_AUDIENCE_ID, " +
            "UM_AUDIENCE_REF_ID, rownum AS rnum FROM (SELECT UM_ROLE_NAME, UM_AUDIENCE," +
            " UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_ORACLE = " UM_TENANT_ID=:UM_TENANT_ID; ORDER " +
            "BY UM_HYBRID_ROLE.UM_ID DESC) WHERE AND rownum <= :END_INDEX;) " +
            "WHERE rnum > :ZERO_BASED_START_INDEX;";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_MSSQL = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_MSSQL = " UM_TENANT_ID=:UM_TENANT_ID; ORDER BY " +
            "UM_HYBRID_ROLE.UM_ID DESC OFFSET :OFFSET; ROWS FETCH NEXT :LIMIT; ROWS ONLY";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_POSTGRESQL = "SELECT UM_ROLE_NAME, UM_UUID, " +
            "UM_AUDIENCE, UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_POSTGRESQL = " UM_TENANT_ID=:UM_TENANT_ID; " +
            "ORDER BY UM_HYBRID_ROLE.UM_ID DESC LIMIT :LIMIT; OFFSET :OFFSET;";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_DB2 = "SELECT UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM (SELECT ROW_NUMBER() " +
            "OVER(ORDER BY UM_HYBRID_ROLE.UM_ID DESC) AS rn, UM_HYBRID_ROLE.* FROM UM_HYBRID_ROLE INNER JOIN " +
            "UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_DB2 = " UM_TENANT_ID=:UM_TENANT_ID;) WHERE " +
            "rn BETWEEN :ONE_BASED_START_INDEX; AND :END_INDEX;";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_INFORMIX = "SELECT SKIP :OFFSET; FIRST :LIMIT; " +
            "UM_ROLE_NAME, UM_UUID, UM_AUDIENCE, UM_HYBRID_ROLE_AUDIENCE.UM_AUDIENCE_ID, UM_AUDIENCE_REF_ID FROM " +
            "UM_HYBRID_ROLE INNER JOIN UM_HYBRID_ROLE_AUDIENCE ON UM_HYBRID_ROLE.UM_AUDIENCE_REF_ID = " +
            "UM_HYBRID_ROLE_AUDIENCE.UM_ID WHERE ";

    public static final String GET_ROLES_BY_TENANT_AND_ROLE_NAME_TAIL_INFORMIX = "UM_TENANT_ID=:UM_TENANT_ID; ORDER " +
            "BY UM_HYBRID_ROLE.UM_ID DESC";

    // Groups related queries.
    public static final String GET_GROUP_NAME_BY_ID_SQL = "SELECT ROLE_NAME FROM IDN_SCIM_GROUP WHERE "
            + "TENANT_ID=:TENANT_ID; AND ATTR_NAME=:ATTR_NAME; AND ATTR_VALUE=:ATTR_VALUE;";

    public static final String GET_GROUP_ID_BY_NAME_SQL = "SELECT ATTR_VALUE FROM IDN_SCIM_GROUP WHERE "
            + "TENANT_ID=:TENANT_ID; AND ROLE_NAME=:ROLE_NAME; AND ATTR_NAME=:ATTR_NAME;";

    public static final String INSERT_MAIN_TO_SHARED_ROLE_RELATIONSHIP = "INSERT INTO UM_SHARED_ROLE " +
            "(UM_SHARED_ROLE_ID, UM_MAIN_ROLE_ID, UM_SHARED_ROLE_TENANT_ID, UM_MAIN_ROLE_TENANT_ID) " +
            "VALUES (:UM_SHARED_ROLE_ID;, :UM_MAIN_ROLE_ID;, :UM_SHARED_ROLE_TENANT_ID;, :UM_MAIN_ROLE_TENANT_ID;)";
}
