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

package org.wso2.carbon.identity.core.context;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.core.context.model.Actor;
import org.wso2.carbon.identity.core.context.model.ApplicationActor;
import org.wso2.carbon.identity.core.context.model.Flow;
import org.wso2.carbon.identity.core.context.model.UserActor;
import org.wso2.carbon.identity.core.internal.IdentityContextDataHolder;
import org.wso2.carbon.utils.CarbonUtils;

/**
 * This class is used to store the identity context information of the current thread.
 */
public class IdentityContext extends CarbonContext {

    private final IdentityContextDataHolder identityContextDataHolder;

    /**
     * Creates a IdentityContext using the given IdentityContext data holder as its backing instance.
     *
     * @param identityContextDataHolder the IdentityContext data holder that backs this CarbonContext object.
     */
    protected IdentityContext(IdentityContextDataHolder identityContextDataHolder) {

        super();
        this.identityContextDataHolder = identityContextDataHolder;
    }

    public static IdentityContext getThreadLocalIdentityContext() {

        return new IdentityContext(IdentityContextDataHolder.getThreadLocalIdentityContextHolder());
    }

    /**
     * Set the flow of the request.
     *
     * @param flow flow of the request.
     */
    public void setFlow(Flow flow) {

        if (identityContextDataHolder.getFlow() != null) {
            throw new IllegalStateException("Flow is already set in the IdentityContext.");
        }
        identityContextDataHolder.setFlow(flow);
    }

    /**
     * Get the flow id of the request.
     *
     * @return Flow of the request.
     */
    public Flow getFlow() {

        return identityContextDataHolder.getFlow();
    }

    /**
     * Set the actor of the request.
     *
     * @param actor actor of the request.
     */
    public void setActor(Actor actor) {

        if (identityContextDataHolder.getActor() != null) {
            throw new IllegalStateException("Actor is already set in the IdentityContext.");
        }
        identityContextDataHolder.setActor(actor);
    }

    /**
     * Get the actor of the request.
     *
     * @return Actor of the request.
     */
    public Actor getActor() {

        return identityContextDataHolder.getActor();
    }

    /**
     * Get the User actor of the request.
     *
     * @return UserActor of the request.
     */
    public UserActor getUserActor() {

        if (isUserActor()) {
            return (UserActor) identityContextDataHolder.getActor();
        }
        return null;
    }

    /**
     * Check whether the actor is a User actor.
     *
     * @return true if the actor is a User actor.
     */
    public boolean isUserActor() {

        return identityContextDataHolder.getActor() instanceof UserActor;
    }

    /**
     * Get the Application actor of the request.
     *
     * @return ApplicationActor of the request.
     */
    public ApplicationActor getApplicationActor() {

        if (isApplicationActor()) {
            return (ApplicationActor) identityContextDataHolder.getActor();
        }
        return null;
    }

    /**
     * Check whether the actor is an Application actor.
     *
     * @return true if the actor is an Application actor.
     */
    public boolean isApplicationActor() {

        return identityContextDataHolder.getActor() instanceof ApplicationActor;
    }

    public static void destroyCurrentContext() {

        CarbonUtils.checkSecurity();
        IdentityContextDataHolder.destroyCurrentIdentityContextDataHolder();
    }
}
