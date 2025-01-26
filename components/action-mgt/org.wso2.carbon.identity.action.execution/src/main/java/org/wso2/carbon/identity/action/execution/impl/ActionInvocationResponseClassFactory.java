/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.action.execution.impl;

import org.wso2.carbon.identity.action.execution.ActionInvocationResponseClassProvider;
import org.wso2.carbon.identity.action.execution.DefaultActionInvocationResponseClassProvider;
import org.wso2.carbon.identity.action.execution.model.ActionType;
import org.wso2.carbon.identity.action.execution.model.ResponseData;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the class for Action Invocation Responses for different action types.
 * The ActionInvocationResponseClassFactory is the component that is responsible for providing the
 * classes from the downstream component based on the action type.
 */
public class ActionInvocationResponseClassFactory {

    private static final Map<ActionType, ActionInvocationResponseClassProvider> classProviders = new HashMap<>();

    /**
     * Get the extended ResponseData class for invocation success response based on the action type.
     *
     * @param actionType Action type.
     * @return The extended ResponseData class.
     */
    public static Class<? extends ResponseData> getInvocationSuccessResponseContextClass(ActionType actionType) {

        ActionInvocationResponseClassProvider classProvider = classProviders.get(actionType);
        if (classProvider != null) {
            return classProvider.getSuccessResponseContextClass();
        }
        return DefaultActionInvocationResponseClassProvider.getInstance().getSuccessResponseContextClass();
    }

    /**
     * Register the ActionInvocationResponseClassProvider based on the action type.
     *
     * @param provider The ActionInvocationResponseClassProvider.
     */
    public static void registerActionInvocationResponseClassProvider(
            ActionInvocationResponseClassProvider provider) {

        classProviders.put(provider.getSupportedActionType(), provider);
    }

    /**
     * Unregister the ActionInvocationResponseClassProvider based on the action type.
     *
     * @param provider The ActionInvocationResponseClassProvider.
     */
    public static void unregisterActionInvocationResponseClassProvider(
            ActionInvocationResponseClassProvider provider) {

        classProviders.remove(provider.getSupportedActionType());
    }
}
