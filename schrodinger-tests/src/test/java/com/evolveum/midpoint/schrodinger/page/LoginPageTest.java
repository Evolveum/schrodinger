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
import com.evolveum.midpoint.schrodinger.page.login.*;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class LoginPageTest extends AbstractLoginPageTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoginPageTest.class);

    private static final File SEC_QUES_RESET_PASS_SECURITY_POLICY = new File("src/test/resources/objects/securitypolicies/policy-secururity-question-reset-pass.xml");
    private static final File MAIL_NONCE_RESET_PASS_SECURITY_POLICY = new File("src/test/resources/objects/securitypolicies/policy-nonce-reset-pass.xml");

    @Test
    public void test020selfRegistration() throws IOException, InterruptedException {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        open("/");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        SelfRegistrationPage registrationPage = login.register();
        registrationPage.setGivenName("Test").setFamilyName("User")
                .setEmail("test.user@evolveum.com").setPassword("5ecr3t").setCaptcha().submit();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        basicPage.feedback().isSuccess();
        String notification = Utils.readBodyOfLastNotification(Paths.get(notificationFile.getAbsolutePath()));
        String linkTag = "link='";
        String link = notification.substring(notification.indexOf(linkTag) + linkTag.length(), notification.lastIndexOf("'"));
        open(link);
        new RegistrationConfirmationPage()
                .assertSuccessPanelExists();
        String actualUrl = basicPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.endsWith("/registration"));
    }

    @Test
    public void test030resetPasswordMailNonce() throws IOException, InterruptedException {
        basicPage.loggedUser().logoutIfUserIsLogin();

        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        open("/");
        login.forgotPassword()
                .setEmailValue(MAIL_OF_ENABLED_USER)
                .clickSubmitButton();
        TimeUnit.SECONDS.sleep(6);
        String link = Utils.readBodyOfLastNotification(Paths.get(notificationFile.getAbsolutePath()));
        open(link);
        String actualUrl = basicPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.endsWith("/resetPassword"));
    }

    @Test
    public void test031resetPassowordSecurityQuestion() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        open("/");
        login.loginWithReloadLoginPage("administrator", "5ecr3t");
        addObjectFromFile(SEC_QUES_RESET_PASS_SECURITY_POLICY);
        basicPage.loggedUser().logoutIfUserIsLogin();
        login.forgotPassword()
                .setUsernameValue(NAME_OF_ENABLED_USER)
                .setEmailValue(MAIL_OF_ENABLED_USER)
                .clickSubmitButton();
        ForgetPasswordSecurityQuestionsPage securityQuestionsPage = new ForgetPasswordSecurityQuestionsPage();
        securityQuestionsPage
                .getPasswordQuestionsPanel()
                .setMyPasswordTFValue("10")
                .and()
                .clickSendButton();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        String actualUrl = basicPage.getCurrentUrl();
        LOG.info("LoginPageTest actualUrl = " + actualUrl);
        Selenide.screenshot("resetPassowordSecurityQuestion");
        Assert.assertTrue(actualUrl.endsWith("/resetpasswordsuccess"));
    }

    @Test
    public void test040changeLanguageFormPage() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        open("/");

        login.changeLanguage("de");
        login.assertSignInButtonTitleMatch("Anmelden");
    }

    @Override
    protected File getSecurityPolicyMailNonceResetPass() {
        return MAIL_NONCE_RESET_PASS_SECURITY_POLICY;
    }
}
