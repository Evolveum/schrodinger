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
package com.evolveum.midpoint.schrodinger.flexible.authentication.oidc;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.flexible.authentication.AbstractRemoteAuthModuleTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public abstract class AbstractOidcAuthModuleTest extends AbstractRemoteAuthModuleTest {

    static final String BASE_DIR_FOR_SECURITY_FILES = "src/test/resources/objects/securitypolicies/authentication/oidc/";
    private static final File SECURITY_POLICY_ISSUER_URI_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "issuer-uri.xml");
    private static final File SECURITY_POLICY_ALL_URI_CONFIG_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "with-all-uris-config.xml");
    private static final File SECURITY_POLICY_CLIENT_SECRET_POST_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "client-secret-post.xml");
    private static final File SECURITY_POLICY_PRIVATE_KEY_JWT_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "private-key-jwt.xml");
    private static final File SECURITY_POLICY_PRIVATE_KEY_JWT_BY_KEYSTORE_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "private-key-jwt-by-keystore.xml");
    private static final File SECURITY_POLICY_WRONG_ATTRIBUTE_NAME_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "wrong-attribute-name.xml");

    private static final String CLIENT_ID_KEY = "clientId";
    private static final String ISSUER_URI_KEY = "issuerUri";
    private static final String CLIENT_SECRET_KEY = "clientSecret";
    private static final String AUTHORIZATION_URI_KEY = "authorizationUri";
    private static final String TOKEN_URI_KEY = "tokenUri";
    private static final String JWK_SET_URI_KEY = "jwkSetUri";
    private static final String END_SESSION_URI_KEY = "endSessionUri";
    private static final String PRIVATE_KEY_KEY = "privateKey";
    private static final String PASSWORD_FOR_PRIVATE_KEY = "passwordForPrivateKey";
    private static final String CERTIFICATE_KEY = "certificate";
    private static final String KEY_STORE_PATH_KEY = "keyStorePath";
    private static final String KEY_ALIAS_KEY = "keyAlias";

    private static final String PASSPHRASE_TAG = "||passphrase||";
    private static final String KEY_STORE_PASSWORD_TAG = "||keyStorePassword||";
    private static final String KEY_PASSWORD_TAG = "||keyPassword||";

    private static final String SIGNING_PREFIX = "signedJwt.";
    private static final String PKCE_PREFIX = "pkce.";


    @Override
    protected File getPropertyFile() {
        return new File("src/test/resources/configuration/oidc.properties");
    }

    protected String getSecurityPolicy(File securityPolicyFile) throws IOException {
        String securityContent = super.getSecurityPolicy(securityPolicyFile);
        securityContent = securityContent.replace(createTag(CLIENT_ID_KEY), getProperty(CLIENT_ID_KEY));
        securityContent = securityContent.replace(createTag(ISSUER_URI_KEY), getProperty(ISSUER_URI_KEY));

        return securityContent;
    }

    private String getClientSecret() {
        return getProperty(CLIENT_SECRET_KEY);
    }

    protected void applyBasicSecurityPolicy() throws IOException {
        applyBasicSecurityPolicy(
                getBasicSecurityPolicy(SECURITY_POLICY_ISSUER_URI_FILE));
    }


    private String getBasicSecurityPolicy(File file) throws IOException {
        String securityContent = getSecurityPolicy(file);
        return securityContent.replace(createTag(CLIENT_SECRET_KEY), getClientSecret());
    }

    private String addSigningPrefix(String key) {
        return SIGNING_PREFIX + key;
    }

    @Test
    public void test001SuccessLoginAndLogoutIssuerUri() throws Exception {
        applyBasicSecurityPolicy();

        successLoginAndLogout();
    }

    @Test
    public void test002SuccessLoginAndLogoutAllUrisConfig() throws Exception {
        String securityContent = getSecurityPolicy(SECURITY_POLICY_ALL_URI_CONFIG_FILE);
        securityContent = securityContent.replace(createTag(CLIENT_SECRET_KEY), getClientSecret());
        securityContent = securityContent.replace(createTag(AUTHORIZATION_URI_KEY), getProperty(AUTHORIZATION_URI_KEY));
        securityContent = securityContent.replace(createTag(TOKEN_URI_KEY), getProperty(TOKEN_URI_KEY));
        securityContent = securityContent.replace(createTag(JWK_SET_URI_KEY), getProperty(JWK_SET_URI_KEY));
        securityContent = securityContent.replace(createTag(END_SESSION_URI_KEY), getProperty(END_SESSION_URI_KEY));
        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test003SuccessLoginAndLogoutClientSecretPost() throws Exception {
        applyBasicSecurityPolicy(
                getBasicSecurityPolicy(SECURITY_POLICY_CLIENT_SECRET_POST_FILE));

        successLoginAndLogout();
    }

    @Test
    public void test004SuccessLoginAndLogoutPrivateKeyJWT() throws Exception {
        String securityContent = super.getSecurityPolicy(SECURITY_POLICY_PRIVATE_KEY_JWT_FILE);
        securityContent = securityContent.replace(createTag(CLIENT_ID_KEY), getProperty(addSigningPrefix(CLIENT_ID_KEY)));
        securityContent = securityContent.replace(createTag(ISSUER_URI_KEY), getProperty(addSigningPrefix(ISSUER_URI_KEY)));
        securityContent = securityContent.replace(createTag(PRIVATE_KEY_KEY), getProperty(PRIVATE_KEY_KEY));
        securityContent = securityContent.replace(PASSPHRASE_TAG, getProperty(PASSWORD_FOR_PRIVATE_KEY));
        securityContent = securityContent.replace(createTag(CERTIFICATE_KEY), getProperty(CERTIFICATE_KEY));
        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test005SuccessLoginAndLogoutPrivateKeyJWTByKeyStore() throws Exception {
        String securityContent = super.getSecurityPolicy(SECURITY_POLICY_PRIVATE_KEY_JWT_BY_KEYSTORE_FILE);
        securityContent = securityContent.replace(createTag(CLIENT_ID_KEY), getProperty(addSigningPrefix(CLIENT_ID_KEY)));
        securityContent = securityContent.replace(createTag(ISSUER_URI_KEY), getProperty(addSigningPrefix(ISSUER_URI_KEY)));
        securityContent = securityContent.replace(createTag(KEY_STORE_PATH_KEY), getProperty(KEY_STORE_PATH_KEY));
        securityContent = securityContent.replace(KEY_STORE_PASSWORD_TAG, getProperty(PASSWORD_FOR_PRIVATE_KEY));
        securityContent = securityContent.replace(createTag(KEY_ALIAS_KEY), getProperty(KEY_ALIAS_KEY));
        securityContent = securityContent.replace(KEY_PASSWORD_TAG, getProperty(PASSWORD_FOR_PRIVATE_KEY));
        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    @Test
    public void test006WrongClientSecret() throws Exception {
        String securityContent = getSecurityPolicy(SECURITY_POLICY_ISSUER_URI_FILE);
        securityContent = securityContent.replace(createTag(CLIENT_SECRET_KEY), "wrong_secret");
        applyBasicSecurityPolicy(securityContent);

        try {
            failLogin(
                    getEnabledUserName(),
                    "Currently we are unable to process your request. Kindly try again later.");
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            Selenide.closeWindow();
        }
    }

    @Test
    public void test007WrongAttributeName() throws Exception {
        applyBasicSecurityPolicy(
                getBasicSecurityPolicy(SECURITY_POLICY_WRONG_ATTRIBUTE_NAME_FILE));

        try {
            failLogin(
                    getNonExistUsername(),
                    "Currently we are unable to process your request. Kindly try again later.");
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            Selenide.closeWindow();
        }
    }

    @Test
    public void test008SuccessLoginAndLogoutPKCE() throws Exception {
        String securityContent = super.getSecurityPolicy(getSecurityPolicyPKCEFile());
        securityContent = securityContent.replace(createTag(CLIENT_ID_KEY), getProperty(PKCE_PREFIX + CLIENT_ID_KEY));
        securityContent = securityContent.replace(createTag(ISSUER_URI_KEY), getProperty(ISSUER_URI_KEY));
        securityContent = securityContent.replace(createTag(CLIENT_SECRET_KEY), getClientSecret());

        applyBasicSecurityPolicy(securityContent);

        successLoginAndLogout();
    }

    protected abstract File getSecurityPolicyPKCEFile();
}
