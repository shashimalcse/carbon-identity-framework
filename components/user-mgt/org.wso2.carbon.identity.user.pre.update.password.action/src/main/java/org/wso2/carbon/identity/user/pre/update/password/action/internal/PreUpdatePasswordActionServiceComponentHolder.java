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

package org.wso2.carbon.identity.user.pre.update.password.action.internal;

import org.wso2.carbon.identity.certificate.management.service.CertificateManagementService;

/**
 * Service component Holder for the Pre Update Password Action Service.
 */
public class PreUpdatePasswordActionServiceComponentHolder {

    private CertificateManagementService certificateManagementService;

    public static final PreUpdatePasswordActionServiceComponentHolder INSTANCE =
            new PreUpdatePasswordActionServiceComponentHolder();

    private PreUpdatePasswordActionServiceComponentHolder() {

    }

    /**
     * Get the instance of PreUpdatePasswordActionActionServiceComponentHolder.
     *
     * @return ActionMgtServiceComponentHolder instance.
     */
    public static PreUpdatePasswordActionServiceComponentHolder getInstance() {

        return INSTANCE;
    }

    /**
     * Get the CertificateManagementService.
     *
     * @return CertificateManagementService instance.
     */
    public CertificateManagementService getCertificateManagementService() {

        return certificateManagementService;
    }

    /**
     * Set the CertificateManagementService.
     *
     * @param certificateManagementService CertificateManagementService instance.
     */
    public void setCertificateManagementService(CertificateManagementService certificateManagementService) {

        this.certificateManagementService = certificateManagementService;
    }
}
