/*
 * Copyright (c) 2010-2021 Evolveum
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

package com.evolveum.midpoint.schrodinger.page;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.page.login.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class LoginPageWithAuthenticationConfigTest extends AbstractLoginPageTest {

    private static final File FLEXIBLE_AUTHENTICATION_SEC_QUES_RESET_PASS_SECURITY_POLICY = new File("src/test/resources/objects/securitypolicies/flexible-authentication-policy-secururity-question-reset-pass.xml");
    private static final File FLEXIBLE_AUTHENTICATION_MAIL_NONCE_RESET_PASS_SECURITY_POLICY = new File("src/test/resources/objects/securitypolicies/flexible-authentication-policy-nonce-reset-pass.xml");
    private static final File BULK_TASK = new File("src/test/resources/objects/tasks/add-archetype-to-node-bulk-task.xml");

    private static final String ARCHETYPE_NODE_GROUP_GUI_OID = "05b6933a-b7fc-4543-b8fa-fd8b278ff9ee";

    @Test
    public void failWholeAuthenticationFlow() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        open("/auth/emergency/internalLoginForm");
        open("/");
        FormLoginPage login = midPoint.formLogin();
        login.login("bad_username", "secret");

        FeedbackBox feedback = login.feedback();
        feedback.assertError("0");


        login.login("bad_username", "secret");
        feedback
                .assertError("0")
                .assertError("1");
    }

    @Test
    public void test020loginAndLogoutForAdministrators() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        open("/auth/emergency/internalLoginForm");
        FormLoginPage login = midPoint.formLogin();
        login.login("administrator", "5ecr3t");
        basicPage.loggedUser().logout();
        basicPage.assertUserMenuDoesntExist();
    }

    @Test
    public void test021negativeLoginForAdministrators() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        open("/auth/emergency/internalLoginForm");
        unsuccessfulLogin("user_without_superuser", "5ecr3t");
    }

    @Test
    public void test030resetPassowordMailNonce() throws IOException, InterruptedException {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        MailNoncePage mailNonce = (MailNoncePage) login.forgotPassword();
        mailNonce.setMail(MAIL_OF_ENABLED_USER);
        TimeUnit.SECONDS.sleep(6);
        String notification = readLastNotification();
        String bodyTag = "body='";
        String link = notification.substring(notification.indexOf(bodyTag) + bodyTag.length(), notification.lastIndexOf("'"));
        open(link);
        String actualUrl = basicPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.endsWith("/resetPassword"));
    }

    @Test
    public void test031resetPasswordSecurityQuestion() throws InterruptedException {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        open("/");
        login.loginWithReloadLoginPage("administrator", "5ecr3t");
        importObject(FLEXIBLE_AUTHENTICATION_SEC_QUES_RESET_PASS_SECURITY_POLICY, true);
        TimeUnit.SECONDS.sleep(4);
        basicPage.loggedUser().logoutIfUserIsLogin();
        SecurityQuestionsPage securityQuestion = (SecurityQuestionsPage) login.forgotPassword();
        securityQuestion.setUsername(NAME_OF_ENABLED_USER)
            .setAnswer(0, "10")
            .submit();
        String actualUrl = basicPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.endsWith("/resetPassword"));
    }

    @Test
    public void test040selfRegistration() throws IOException, InterruptedException {
        System.setProperty("midpoint.schrodinger","true");
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        open("/");
        TimeUnit.SECONDS.sleep(2);
        SelfRegistrationPage registrationPage = login.register();
        registrationPage.setGivenName("Test").setFamilyName("User").setEmail("test.user@evolveum.com").setPassword("5ecr3t").submit();
        TimeUnit.SECONDS.sleep(6);
        String notification = readLastNotification();
//        String usernameTag = "username='";
        String linkTag = "link='";
//        String username = notification.substring(notification.indexOf(usernameTag) + usernameTag.length(), notification.lastIndexOf("', " + linkTag));
        String link = notification.substring(notification.indexOf(linkTag) + linkTag.length(), notification.lastIndexOf("''"));
        open(link);

        new RegistrationFinishPage().assertSuccessPanelExists();
        String actualUrl = basicPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.endsWith("/registration/result"));
    }

    @Test
    public void test050UseSequenceForNodeArchetype() throws Exception {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();

        open("/");
        login.loginWithReloadLoginPage("enabled_user", "5ecr3t");
        basicPage.loggedUser().logoutIfUserIsLogin();

        login.loginWithReloadLoginPage("administrator", "5ecr3t");
        importObject(BULK_TASK);
        basicPage.listTasks().table().clickByName("Add archetype"); //.clickRunNow(); the task is running after import, no need to click run now button
        screenshot("addArchetypeBulkActionTask");
        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M);
        basicPage.loggedUser().logoutIfUserIsLogin();

        open("/");
        unsuccessfulLogin("enabled_user", "5ecr3t");
        login.loginWithReloadLoginPage("administrator", "5ecr3t");
        FeedbackBox feedback = login.feedback();
        basicPage.loggedUser().logoutIfUserIsLogin();
    }

    @Override
    protected File getSecurityPolicyMailNonceResetPass() {
        return FLEXIBLE_AUTHENTICATION_MAIL_NONCE_RESET_PASS_SECURITY_POLICY;
    }
}
