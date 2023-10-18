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
package com.evolveum.midpoint.schrodinger.flexible.authentication.ldap;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.flexible.authentication.AbstractRemoteAuthModuleTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public abstract class AbstractLdapAuthModuleTest extends AbstractRemoteAuthModuleTest {

    private static final String BASE_DIR_FOR_SECURITY_FILES = "src/test/resources/objects/securitypolicies/authentication/ldap/";
    private static final File SECURITY_POLICY_SUCCESS_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "success.xml");

    private static final File SECURITY_POLICY_SUCCESS_WITHOUT_NAMINGATTR_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "success-without-namingAttr.xml");

    private static final File SECURITY_POLICY_SUCCESS_SUBTREE_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "success-subtree.xml");

    private static final File SECURITY_POLICY_WRONG_ATTRIBUTE_NAME_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "wrong-attribute-name.xml");

    private static final String HOST = "host";
    private static final String USER_DN = "userDn";
    private static final String USER_PASSWORD = "userPassword";


    @Override
    protected File getPropertyFile() {
        return new File("src/test/resources/configuration/ldap.properties");
    }

    protected String getSecurityPolicy(File securityPolicyFile) throws IOException {
        String securityContent = super.getSecurityPolicy(securityPolicyFile);
        securityContent = securityContent.replace(createTag(HOST), getProperty(HOST));
        securityContent = securityContent.replace(createTag(USER_DN), getProperty(USER_DN));
        securityContent = securityContent.replace(createTag(USER_PASSWORD), getProperty(USER_PASSWORD));

        return securityContent;
    }

    protected void applyBasicSecurityPolicy() throws IOException {
        applyBasicSecurityPolicy(getSecurityPolicy(SECURITY_POLICY_SUCCESS_FILE));
    }

    @Test
    public void test010SuccessLoginAndLogout() throws Exception {
        applyBasicSecurityPolicy();

        successLoginAndLogout();
    }

    @Test
    public void test020SuccessLoginAndLogout() throws Exception {
        applyBasicSecurityPolicy(getSecurityPolicy(SECURITY_POLICY_SUCCESS_WITHOUT_NAMINGATTR_FILE));

        successLoginAndLogout();
    }

    @Test
    public void test030UserFromSubtreeUnsuccessfully() throws Exception {
        applyBasicSecurityPolicy();

        try {
            failLogin(
                    "sub_enabled_user",
                    "Invalid username and/or password.");
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            Selenide.closeWindow();
        }
    }

    @Test
    public void test040UserFromSubtreeSuccessfully() throws Exception {
        applyBasicSecurityPolicy(getSecurityPolicy(SECURITY_POLICY_SUCCESS_SUBTREE_FILE));

        successLoginAndLogout("sub_enabled_user");
    }

    @Test
    public void test050WrongAttributeName() throws Exception {
        applyBasicSecurityPolicy(getSecurityPolicy(SECURITY_POLICY_WRONG_ATTRIBUTE_NAME_FILE));

        try {
            failLogin(
                    getNonExistUsername(),
                    "Invalid username and/or password.");
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            Selenide.closeWindow();
        }
    }

    @AfterClass
    @Override
    public void afterClass() {
        try {
            applyBasicSecurityPolicy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.afterClass();
    }
}
