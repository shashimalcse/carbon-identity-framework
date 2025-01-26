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

package org.wso2.carbon.identity.api.resource.collection.mgt.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.resource.collection.mgt.constant.APIResourceCollectionManagementConstants;
import org.wso2.carbon.identity.api.resource.collection.mgt.constant.APIResourceCollectionManagementConstants.APIResourceCollectionConfigBuilderConstants;
import org.wso2.carbon.identity.api.resource.collection.mgt.model.APIResourceCollection;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Config builder class for application resource collection configs in api-resource-collection.xml file.
 */
public class APIResourceCollectionMgtConfigBuilder {

    private static final Log LOG = LogFactory.getLog(APIResourceCollectionMgtConfigBuilder.class);
    private static final Map<String, APIResourceCollection> apiResourceCollectionMgtConfigurations = new HashMap<>();
    private static final APIResourceCollectionMgtConfigBuilder INSTANCE = new APIResourceCollectionMgtConfigBuilder();
    private OMElement documentElement;

    public static APIResourceCollectionMgtConfigBuilder getInstance() {

        return INSTANCE;
    }

    private APIResourceCollectionMgtConfigBuilder() {

        loadConfiguration();
    }

    /**
     * Get the API resource collection mgt configurations.
     *
     * @return Map of API resource collection mgt configurations.
     */
    public Map<String, APIResourceCollection> getApiResourceCollectionMgtConfigurations() {

        if (apiResourceCollectionMgtConfigurations.isEmpty()) {
            loadConfiguration();
        }
        return apiResourceCollectionMgtConfigurations;
    }

    /**
     * Read the api-resource-collection.xml file and build the configuration map.
     */
    private void loadConfiguration() {

        String configDirPath = CarbonUtils.getCarbonConfigDirPath();
        File configFile = new File(configDirPath,
                FilenameUtils.getName(APIResourceCollectionManagementConstants.API_RESOURCE_COLLECTION_FILE_NAME));
        if (!configFile.exists()) {
            return;
        }
        try (InputStream stream = Files.newInputStream(configFile.toPath())) {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            // Prevents using external resources when parsing xml.
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            // Prevents using external document type definition when parsing xml.
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader parser = factory.createXMLStreamReader(stream);
            StAXOMBuilder builder = new StAXOMBuilder(parser);
            documentElement = builder.getDocumentElement();
            buildAPIResourceCollectionConfig();
        } catch (IOException e) {
            LOG.error("Error while loading API resource collection management configs.", e);
        } catch (XMLStreamException e) {
            LOG.error("Error while streaming API resource collection management configs.", e);
        }
    }

    private void buildAPIResourceCollectionConfig() {

        Iterator<OMElement> apiResourceCollections = this.documentElement.getChildrenWithName(
                new QName(APIResourceCollectionConfigBuilderConstants.API_RESOURCE_COLLECTION_ELEMENT));

        while (apiResourceCollections.hasNext()) {
            OMElement apiResourceCollection = apiResourceCollections.next();
            APIResourceCollection apiResourceCollectionObj =
                    buildAPIResourceCollectionBasicInfo(apiResourceCollection);
            if (apiResourceCollectionObj == null) {
                continue;
            }
            String collectionVersion = apiResourceCollection
                    .getAttributeValue(new QName(APIResourceCollectionConfigBuilderConstants.VERSION));
            OMElement scopesElement = apiResourceCollection.getFirstChildWithName(
                    new QName(APIResourceCollectionConfigBuilderConstants.SCOPES_ELEMENT));
            if (scopesElement != null) {
                Set<String> readScopeSet = new HashSet<>();
                Set<String> writeScopeSet = new HashSet<>();
                Iterator<?> actionElements = scopesElement.getChildElements();
                while (actionElements.hasNext()) {
                    OMElement actionElement = (OMElement) actionElements.next();
                    if (actionElement == null) {
                        continue;
                    }
                    Iterator<OMElement> scopes = actionElement.getChildrenWithName(
                            new QName(APIResourceCollectionConfigBuilderConstants.SCOPE_ELEMENT));
                    while (scopes.hasNext()) {
                        OMElement scope = scopes.next();
                        String scopeName = scope.getAttributeValue(
                                new QName(APIResourceCollectionConfigBuilderConstants.NAME));
                        // Read and old Feature scope are considered as read scopes.
                        boolean isReadAction = APIResourceCollectionConfigBuilderConstants.READ
                                .equals(actionElement.getLocalName());
                        boolean isFeatureAction = APIResourceCollectionConfigBuilderConstants.FEATURE.
                                equals(actionElement.getLocalName());
                        if (APIResourceCollectionConfigBuilderConstants.COLLECTION_VERSION_V0
                                .equals(collectionVersion)) {
                            if (isReadAction || isFeatureAction) {
                                readScopeSet.add(scopeName);
                            } else {
                                writeScopeSet.add(scopeName);
                            }
                        } else {
                            // Process new scopes with feature scopes.
                            if (isReadAction) {
                                readScopeSet.add(scopeName);
                            } else if (isFeatureAction) {
                                if (isViewFeatureScope(scopeName)) {
                                    apiResourceCollectionObj.setViewFeatureScope(scopeName);
                                    readScopeSet.add(scopeName);
                                } else if (isEditFeatureScope(scopeName)) {
                                    apiResourceCollectionObj.setEditFeatureScope(scopeName);
                                    writeScopeSet.add(scopeName);
                                } else {
                                    readScopeSet.add(scopeName);
                                }
                            } else {
                                writeScopeSet.add(scopeName);
                            }
                        }
                    }
                }
                if (APIResourceCollectionConfigBuilderConstants.COLLECTION_VERSION_V0
                        .equals(collectionVersion)) {
                    apiResourceCollectionObj.setLegacyReadScopes(new ArrayList<>(readScopeSet));
                    apiResourceCollectionObj.setLegacyWriteScopes(new ArrayList<>(writeScopeSet));
                    if (apiResourceCollectionObj.getReadScopes() == null) {
                        apiResourceCollectionObj.setReadScopes(new ArrayList<>(readScopeSet));
                    }
                    if (apiResourceCollectionObj.getWriteScopes() == null) {
                        apiResourceCollectionObj.setWriteScopes(new ArrayList<>(writeScopeSet));
                    }
                } else {
                    apiResourceCollectionObj.setReadScopes(new ArrayList<>(readScopeSet));
                    apiResourceCollectionObj.setWriteScopes(new ArrayList<>(writeScopeSet));
                }
            }
            apiResourceCollectionMgtConfigurations.put(apiResourceCollectionObj.getId(), apiResourceCollectionObj);
        }
    }

    private APIResourceCollection buildAPIResourceCollectionBasicInfo(OMElement element) {

        // Retrieve the name and encode it using base64, then remove "=" characters.
        String name = element.getAttributeValue(new QName(APIResourceCollectionConfigBuilderConstants.NAME));
        String encodedName = Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8)).replace(
                APIResourceCollectionManagementConstants.EQUAL_SIGN, StringUtils.EMPTY);
        if (apiResourceCollectionMgtConfigurations.containsKey(encodedName)) {
            return apiResourceCollectionMgtConfigurations.get(encodedName);
        }

        return new APIResourceCollection.APIResourceCollectionBuilder()
                .id(encodedName)
                .name(element.getAttributeValue(new QName(APIResourceCollectionConfigBuilderConstants.NAME)))
                .displayName(
                        element.getAttributeValue(new QName(APIResourceCollectionConfigBuilderConstants.DISPLAY_NAME)))
                .type(element.getAttributeValue(new QName(APIResourceCollectionConfigBuilderConstants.TYPE)))
                .build();
    }

    private boolean isViewFeatureScope(String scope) {

        return StringUtils.isNotBlank(scope) &&
                scope.startsWith(APIResourceCollectionConfigBuilderConstants.CONSOLE_SCOPE_PREFIX) &&
                scope.endsWith(APIResourceCollectionConfigBuilderConstants.VIEW_FEATURE_SCOPE_SUFFIX);
    }

    private boolean isEditFeatureScope(String scope) {

        return StringUtils.isNotBlank(scope) &&
                scope.startsWith(APIResourceCollectionConfigBuilderConstants.CONSOLE_SCOPE_PREFIX) &&
                scope.endsWith(APIResourceCollectionConfigBuilderConstants.EDIT_FEATURE_SCOPE_SUFFIX);
    }
}
