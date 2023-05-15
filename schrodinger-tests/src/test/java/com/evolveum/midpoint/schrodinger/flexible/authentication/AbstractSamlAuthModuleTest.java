/*
 * Copyright (c) 2023 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.flexible.authentication;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public abstract class AbstractSamlAuthModuleTest extends AbstractRemoteAuthModuleTest {

    private static final String BASE_DIR_FOR_SECURITY_FILES = "src/test/resources/objects/securitypolicies/saml/";
    private static final String SECURITY_POLICY_METADATA_URL_SUFFIX = "-metadata-url.xml";
    private static final String SECURITY_POLICY_PATH_TO_XML_SUFFIX = "-path-to-xml.xml";
    private static final String SECURITY_POLICY_XML_FILE_SUFFIX = "-xml-file.xml";
    private static final String SECURITY_POLICY_SIGNING_SUFFIX = "-signing.xml";
    private static final String SECURITY_POLICY_SIGNING_KEYSTORE_SUFFIX = "-signing-keystore.xml";
    private static final String SECURITY_POLICY_DECRYPTION_SUFFIX = "-decryption.xml";
    private static final String SECURITY_POLICY_DECRYPTION_KEYSTORE_SUFFIX = "-decryption-keystore.xml";
    private static final String SECURITY_POLICY_WRONG_ATTRIBUTE_NAME = "-wrong-attribute-name.xml";

    private static final String ENTITY_ID_KEY = "entityId";
    private static final String METADATA_URL_KEY = "metadataUrl";
    private static final String PATH_TO_FILE_KEY = "pathToFile";
    private static final String XML_FILE_KEY = "xml";
    private static final String PRIVATE_KEY_KEY = "privateKey";
    private static final String PASSWORD_FOR_PRIVATE_KEY = "passwordForPrivateKey";
    private static final String CERTIFICATE_KEY = "certificate";
    private static final String KEY_STORE_PATH_KEY = "keyStorePath";
    private static final String KEY_ALIAS_KEY = "keyAlias";

    private static final String SIGNING_PREFIX = "signing.";
    private static final String DECRYPTION_PREFIX = "decryption.";

    private static final String PASSPHRASE_TAG = "passphrase";
    private static final String KEY_STORE_PASSWORD_TAG = "keyStorePassword";
    private static final String KEY_PASSWORD_TAG = "keyPassword";

    @Override
    protected File getPropertyFile() {
        return new File("src/test/resources/configuration/saml.properties");
    }

    protected String getSecurityPolicy(File securityPolicyFile) throws IOException {
        String securityContent = super.getSecurityPolicy(securityPolicyFile);
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(PRIVATE_KEY_KEY)),
                getProperty(addSigningPrefix(PRIVATE_KEY_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(PASSPHRASE_TAG)),
                getProperty(addSigningPrefix(PASSWORD_FOR_PRIVATE_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(CERTIFICATE_KEY)),
                getProperty(addSigningPrefix(CERTIFICATE_KEY)));

        return securityContent;
    }

    protected void applyBasicSecurityPolicy() throws IOException {
        applyBasicSecurityPolicy(
                getBasicSecurityPolicy(
                        new File(
                                BASE_DIR_FOR_SECURITY_FILES +
                                        getServerPrefix() +
                                        SECURITY_POLICY_METADATA_URL_SUFFIX)));
    }


    private String getBasicSecurityPolicy(File file) throws IOException {
        String securityContent = getSecurityPolicy(file);
        securityContent = securityContent.replace(createTag(ENTITY_ID_KEY), getProperty(ENTITY_ID_KEY));
        securityContent = securityContent.replace(createTag(METADATA_URL_KEY), getProperty(METADATA_URL_KEY));
        return securityContent;
    }

    private String addSigningPrefix(String key) {
        return SIGNING_PREFIX + key;
    }

    private String addDecryptionPrefix(String key) {
        return DECRYPTION_PREFIX + key;
    }

    @Test
    public void test001SuccessLoginAndLogoutMetadataUrl() throws Exception {
        applyBasicSecurityPolicy();

        successLoginAndLogout();
    }

    @Test
    public void test002SuccessLoginAndLogoutPathToXmlFile() throws Exception {
        String securityContent = getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_PATH_TO_XML_SUFFIX));
        securityContent = securityContent.replace(createTag(ENTITY_ID_KEY), getProperty(ENTITY_ID_KEY));
        securityContent = securityContent.replace(createTag(PATH_TO_FILE_KEY), getProperty(PATH_TO_FILE_KEY));

        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test003SuccessLoginAndLogoutXml() throws Exception {
        String securityContent = getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_XML_FILE_SUFFIX));
        securityContent = securityContent.replace(createTag(ENTITY_ID_KEY), getProperty(ENTITY_ID_KEY));
        securityContent = securityContent.replace(createTag(XML_FILE_KEY), getProperty(XML_FILE_KEY));

        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test004SuccessLoginAndLogoutSigningAuthRequest() throws Exception {
        String securityContent = getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_SIGNING_SUFFIX));
        securityContent = securityContent.replace(
                createTag(ENTITY_ID_KEY),
                getProperty(addSigningPrefix(ENTITY_ID_KEY)));
        securityContent = securityContent.replace(
                createTag(METADATA_URL_KEY),
                getProperty(addSigningPrefix(METADATA_URL_KEY)));

        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test005SuccessLoginAndLogoutSigningKeystore() throws Exception {
        String securityContent = super.getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_SIGNING_KEYSTORE_SUFFIX));
        securityContent = securityContent.replace(
                createTag(ENTITY_ID_KEY),
                getProperty(addSigningPrefix(ENTITY_ID_KEY)));
        securityContent = securityContent.replace(
                createTag(METADATA_URL_KEY),
                getProperty(addSigningPrefix(METADATA_URL_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(KEY_STORE_PATH_KEY)),
                getProperty(addSigningPrefix(KEY_STORE_PATH_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(KEY_ALIAS_KEY)),
                getProperty(addSigningPrefix(KEY_ALIAS_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(KEY_STORE_PASSWORD_TAG)),
                getProperty(addSigningPrefix(PASSWORD_FOR_PRIVATE_KEY)));
        securityContent = securityContent.replace(
                createTag(addSigningPrefix(KEY_PASSWORD_TAG)),
                getProperty(addSigningPrefix(PASSWORD_FOR_PRIVATE_KEY)));

        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test006SuccessLoginAndLogoutDecryptionAssertion() throws Exception {
        String securityContent = getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_DECRYPTION_SUFFIX));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(PRIVATE_KEY_KEY)),
                getProperty(addDecryptionPrefix(PRIVATE_KEY_KEY)));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(PASSPHRASE_TAG)),
                getProperty(addDecryptionPrefix(PASSWORD_FOR_PRIVATE_KEY)));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(CERTIFICATE_KEY)),
                getProperty(addDecryptionPrefix(CERTIFICATE_KEY)));
        securityContent = securityContent.replace(
                createTag(ENTITY_ID_KEY),
                getProperty(addDecryptionPrefix(ENTITY_ID_KEY)));
        securityContent = securityContent.replace(
                createTag(METADATA_URL_KEY),
                getProperty(addDecryptionPrefix(METADATA_URL_KEY)));
        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test007SuccessLoginAndLogoutDecryptionAssertionKeystore() throws Exception {
        String securityContent = getSecurityPolicy(new File(
                BASE_DIR_FOR_SECURITY_FILES +
                        getServerPrefix() +
                        SECURITY_POLICY_DECRYPTION_KEYSTORE_SUFFIX));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(KEY_STORE_PATH_KEY)),
                getProperty(addDecryptionPrefix(KEY_STORE_PATH_KEY)));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(KEY_ALIAS_KEY)),
                getProperty(addDecryptionPrefix(KEY_ALIAS_KEY)));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(KEY_STORE_PASSWORD_TAG)),
                getProperty(addDecryptionPrefix(PASSWORD_FOR_PRIVATE_KEY)));
        securityContent = securityContent.replace(
                createTag(addDecryptionPrefix(KEY_PASSWORD_TAG)),
                getProperty(addDecryptionPrefix(PASSWORD_FOR_PRIVATE_KEY)));
        securityContent = securityContent.replace(
                createTag(ENTITY_ID_KEY),
                getProperty(addDecryptionPrefix(ENTITY_ID_KEY)));
        securityContent = securityContent.replace(
                createTag(METADATA_URL_KEY),
                getProperty(addDecryptionPrefix(METADATA_URL_KEY)));
        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test008WrongAttributeName() throws Exception {
        applyBasicSecurityPolicy(
                getBasicSecurityPolicy(
                        new File(
                                BASE_DIR_FOR_SECURITY_FILES +
                                        getServerPrefix() +
                                        SECURITY_POLICY_WRONG_ATTRIBUTE_NAME)));

        failLoginAndLogout(
                getNonExistUsername(),
                "Unsuccessful authentication, please contact Identity Manager's administrators.");
    }
}
