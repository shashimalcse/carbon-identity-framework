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

package org.wso2.carbon.identity.webhook.management.api.model;

import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;
import org.wso2.carbon.identity.webhook.management.internal.service.impl.WebhookManagementServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class for Webhook subscription with two builders.
 */
public class Webhook {

    private final String uuid;
    private final String endpoint;
    private final String name;
    private final String secret;
    private final int tenantId;
    private final String eventProfileName;
    private final String eventProfileUri;
    private final WebhookStatus status;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private List<String> eventsSubscribed;

    private Webhook(Builder builder) {

        this.uuid = builder.uuid;
        this.endpoint = builder.endpoint;
        this.name = builder.name;
        this.secret = builder.secret;
        this.tenantId = builder.tenantId;
        this.eventProfileName = builder.eventProfileName;
        this.eventProfileUri = builder.eventProfileUri;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.eventsSubscribed = builder.eventsSubscribed;
    }

    public String getUuid() {

        return uuid;
    }

    public String getEndpoint() {

        return endpoint;
    }

    public String getName() {

        return name;
    }

    public String getSecret() {

        return secret;
    }

    public int getTenantId() {

        return tenantId;
    }

    public String getEventProfileName() {

        return eventProfileName;
    }

    public String getEventProfileUri() {

        return eventProfileUri;
    }

    public WebhookStatus getStatus() {

        return status;
    }

    public Timestamp getCreatedAt() {

        return createdAt;
    }

    public Timestamp getUpdatedAt() {

        return updatedAt;
    }

    public List<String> getEventsSubscribed() throws WebhookMgtException {

        if (eventsSubscribed != null) {
            return eventsSubscribed;
        }
        // Fetch from service and cache the result
        this.eventsSubscribed = WebhookManagementServiceImpl.getInstance()
                .getWebhookEvents(getUuid(), IdentityTenantUtil.getTenantDomain(tenantId));
        return eventsSubscribed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Webhook webhook = (Webhook) o;
        return tenantId == webhook.tenantId &&
                Objects.equals(uuid, webhook.uuid) &&
                Objects.equals(endpoint, webhook.endpoint) &&
                Objects.equals(name, webhook.name) &&
                Objects.equals(eventProfileName, webhook.eventProfileName) &&
                Objects.equals(eventProfileUri, webhook.eventProfileUri) &&
                status == webhook.status &&
                Objects.equals(createdAt, webhook.createdAt) &&
                Objects.equals(updatedAt, webhook.updatedAt) &&
                Objects.equals(eventsSubscribed, webhook.eventsSubscribed);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, endpoint, name, secret, tenantId,
                eventProfileName, eventProfileUri, status, createdAt, updatedAt, eventsSubscribed);
    }

    /**
     * Builder class with all fields (including secret).
     */
    public static class Builder {

        private String uuid;
        private String endpoint;
        private String name;
        private String secret;
        private int tenantId;
        private String eventProfileName;
        private String eventProfileUri;
        private WebhookStatus status;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private List<String> eventsSubscribed = new ArrayList<>();

        public Builder uuid(String uuid) {

            this.uuid = uuid;
            return this;
        }

        public Builder endpoint(String endpoint) {

            this.endpoint = endpoint;
            return this;
        }

        public Builder name(String name) {

            this.name = name;
            return this;
        }

        public Builder secret(String secret) {

            this.secret = secret;
            return this;
        }

        public Builder tenantId(int tenantId) {

            this.tenantId = tenantId;
            return this;
        }

        public Builder eventProfileName(String eventProfileName) {

            this.eventProfileName = eventProfileName;
            return this;
        }

        public Builder eventProfileUri(String eventProfileUri) {

            this.eventProfileUri = eventProfileUri;
            return this;
        }

        public Builder status(WebhookStatus status) {

            this.status = status;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {

            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Timestamp updatedAt) {

            this.updatedAt = updatedAt;
            return this;
        }

        public Builder eventsSubscribed(List<String> eventsSubscribed) {

            this.eventsSubscribed = eventsSubscribed != null ? new ArrayList<>(eventsSubscribed) : new ArrayList<>();
            return this;
        }

        public Builder addEventSubscribed(String event) {

            this.eventsSubscribed.add(event);
            return this;
        }

        public Webhook build() {

            return new Webhook(this);
        }
    }
}
