/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.core.context.model;

import java.util.EnumSet;

/**
 * Enum for flows.
 * This enum holds the flow type and the action that is being executed.
 */
public class Flow {

    /**
     * Enum for types.
     */
    public enum Type {

        PASSWORD_RECOVERY(EnumSet.of(Action.INVITE, Action.UPDATE, Action.RESET));

        private final EnumSet<Action> states;

        Type(EnumSet<Action> states) {
            this.states = states;
        }

        public boolean isValidAction(Action action) {
            return states.contains(action);
        }

        public EnumSet<Action> getActions() {
            return states;
        }
    }

    /**
     * Enum for actions.
     */
    public enum Action {
        INVITE,
        UPDATE,
        RESET
    }

    private final Type type;
    private final Action action;

    public Flow(Type type, Action action) {

        if (type.isValidAction(action)) {
            this.type = type;
            this.action = action;
            return;
        }
        throw new IllegalArgumentException("Invalid action or the flow type provided.");
    }

    public Type getType() {
        return type;
    }

    public Action getAction() {
        return action;
    }
}
