/*
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.identity.application.authentication.framework;

import org.wso2.carbon.identity.application.authentication.framework.exception.UserSessionException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt.SessionManagementException;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.model.ExpressionNode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Defines the session management service operations.
 */
public interface UserSessionManagementService {

    /**
     * Terminates all active sessions of the given user.
     *
     * @param username        username
     * @param userStoreDomain userstore domain of the user
     * @param tenantDomain    tenant domain of the user
     * @throws UserSessionException
     */
    void terminateSessionsOfUser(String username, String userStoreDomain, String tenantDomain) throws
            UserSessionException;

    /**
     * Get all the active sessions of the given user id.
     *
     * @param userId unique id of the user
     * @return list of user session objects. In the default method, null is returned.
     * @throws SessionManagementException if the session retrieval fails
     */
    default List<UserSession> getSessionsByUserId(String userId) throws SessionManagementException {

        return null;
    }

    /**
     * Terminate all the active sessions of the given user id.
     *
     * @param userId unique id of the user
     * @return whether the sessions termination is success or not. In default method, false is returned.
     * @throws SessionManagementException if the session termination fails
     */
    default boolean terminateSessionsByUserId(String userId) throws SessionManagementException {

        return false;
    }

    /**
     * Get a specific session of the given user id.
     *
     * @param userId    unique id of the user
     * @param sessionId unique id of the session
     * @return user session object. In the default method, null is returned.
     * @throws SessionManagementException if the session retrieval fails
     */
    default Optional<UserSession> getSessionBySessionId(String userId, String sessionId)
            throws SessionManagementException {

        return Optional.empty();
    }

    /**
     * Terminate the session of the given id.
     *
     * @param userId    unique id of the user
     * @param sessionId unique id for the session
     * @return whether the session termination is success or not. In default method, false is returned.
     */
    default boolean terminateSessionBySessionId(String userId, String sessionId) throws SessionManagementException {

        return false;
    }

    /**
     * Get all the active sessions of the user relevant to the given Idp.
     *
     * @param user  user object
     * @param idpId id of the user's identity provider
     * @return whether the sessions termination is success or not. In default method, false is returned.
     * @throws SessionManagementException if the session termination fails
     */
    default List<UserSession> getSessionsByUser(User user, int idpId) throws SessionManagementException {

        return null;
    }

    /**
     * Terminate all the active sessions of the user relevant to the given Idp.
     *
     * @param user  user object
     * @param idpId id of the user's identity provider
     * @return whether the sessions termination is success or not. In default method, false is returned.
     * @throws SessionManagementException if the session termination fails
     */
    default boolean terminateSessionsByUser(User user, int idpId) throws SessionManagementException {

        return false;
    }

    /**
     * Terminate the session of the given id.
     *
     * @param user      user object
     * @param idpId     id of the user's identity provider
     * @param sessionId unique id for the session
     * @return whether the session termination is success or not. In default method, false is returned.
     */
    default boolean terminateSessionBySessionId(User user, int idpId, String sessionId) throws
            SessionManagementException {

        return false;
    }

    /**
     * Get active sessions that fulfill the criteria determined by the filter parameter value.
     *
     * @param tenantDomain context tenant domain
     * @param filter       criteria to search for sessions
     * @param limit        maximum number of sessions to be returned in the result set
     * @param sortOrder    sort direction for results (ASC, DESC)
     * @return list of session search results. In the default method, an empty list is returned.
     * @throws SessionManagementException if the session retrieval fails
     */
    default List<UserSession> getSessions(String tenantDomain, List<ExpressionNode> filter, Integer limit,
                                                  String sortOrder) throws SessionManagementException {

        return Collections.emptyList();
    }

    /**
     * Terminate active sessions that fulfill the criteria determined by the filter parameter value.
     * This method depends on the getSessions methods based on filters.
     *
     * @param tenantDomain context tenant domain
     * @param filter       criteria to search for sessions
     * @param limit        maximum number of sessions to be returned in the result set
     * @param sortOrder    sort direction for results (ASC, DESC)
     * @return whether the session termination is a success or not. In default method, false is returned.
     * @throws SessionManagementException if the session retrieval fails or if the retrieved session list is empty.
     */
    default boolean terminateFilteredSessions(String tenantDomain, List<ExpressionNode> filter, Integer limit,
                                              String sortOrder) throws SessionManagementException {

        return false;
    }
}
