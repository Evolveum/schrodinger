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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.util.ImportOptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public abstract class AbstractRemoteAuthModuleTest extends AbstractSchrodingerTest {

    protected static final String USER_FILE_PREFIX = "src/test/resources/objects/users/";
    protected static final String ENABLED_USER_FILE_SUFFIX = "enabled-user.xml";
    protected static final String DISABLED_USER_FILE_SUFFIX = "disabled-user.xml";

    private static final String USER_PASSWORD_KEY = "midpointUserPassword";

    private static final String ENABLED_USER_NAME = "enabled_user";
    private static final String DISABLED_USER_NAME = "disabled_user";
    private static final String NON_EXIST_USER_NAME = "non_exist_user";

    private Properties properties;

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(DEFAULT_SECURITY_POLICY_FILE);

        super.beforeClass();

        this.properties = loadProperties(getPropertyFile());

        addObjectFromFile(
                new File(getEnabledUserFilePath()));

        addObjectFromFile(
                new File(getDisabledUserFilePath()));


        basicPage.loggedUser().logoutIfUserIsLogin();
        basicPage.getUserMenu().shouldBe(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);

        Selenide.clearBrowserCookies();
        Selenide.closeWindow();
    }

    protected String getDisabledUserFilePath() {
        return USER_FILE_PREFIX + DISABLED_USER_FILE_SUFFIX;
    }

    protected String getEnabledUserFilePath() {
        return USER_FILE_PREFIX + ENABLED_USER_FILE_SUFFIX;
    }

    protected abstract File getPropertyFile();

    protected String getSecurityPolicy(File securityPolicyFile) throws IOException {
        return FileUtils.readFileToString(
                securityPolicyFile,
                StandardCharsets.UTF_8);
    }

    protected String getProperty(String key) {
        return StringEscapeUtils.escapeXml10(properties.getProperty(getServerPrefix() + "." + key));
    }

    protected abstract String getServerPrefix();

    private String getUserPassword() {
        return getProperty(USER_PASSWORD_KEY);
    }

    protected void successLoginAndLogout() {
        successLoginAndLogout(getEnabledUserName());
    }

    protected void successLoginAndLogout(String username) {
        try {
            open("/");

            String password = getUserPassword();
            login(username, password);

            basicPage.assertUserMenuExist();

            basicPage.loggedUser().logout();

            logoutAndCheckIt(username);
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.closeWindow();
        }
    }

    protected void failLoginAndLogout(String username, String expectedError) {
        try {
            failLogin(username, expectedError);

            $x(".//button[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

            logoutAndCheckIt(username);
        } finally {
            Selenide.clearBrowserCookies();
            Selenide.closeWindow();
        }
    }

    protected void failLogin(String username, String expectedError) {
        open("/");

        String password = getUserPassword();

        login(username, password);

        FormLoginPage formLoginPage = new FormLoginPage();
        formLoginPage.assertErrorText(expectedError);
    }

    protected String getEnabledUserName() {
        return ENABLED_USER_NAME;
    }

    protected String getDisabledUsername() {
        return DISABLED_USER_NAME;
    }

    protected String getNonExistUsername() {
        return NON_EXIST_USER_NAME;
    }

    protected abstract void login(String username, String password);

    protected abstract void logoutAndCheckIt(String username);

    @Override
    protected boolean resetToDefaultAfterTests() {
        return false;
    }

    protected String createTag(String key){
        return "||" + key + "||";
    }

    protected void applyBasicSecurityPolicy(String securityPolicy) {
        addObjectFromString(securityPolicy, new ImportOptions(true, true).createOptionList());
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
    }

    protected abstract void applyBasicSecurityPolicy() throws IOException;

    @AfterClass
    @Override
    public void afterClass() {
        Selenide.closeWebDriver();
    }

    @Test
    public void test100LoginWithDisabledUser() throws Exception {
        applyBasicSecurityPolicy();

        failLoginAndLogout(getDisabledUsername(), "User is disabled.");
    }

    @Test
    public void test101LoginWithNonExistUser() throws Exception {
        applyBasicSecurityPolicy();

        failLoginAndLogout(getNonExistUsername(), "Invalid username and/or password.");
    }
}
