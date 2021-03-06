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
package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.Objects;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class FormLoginPage extends LoginPage {

    public SelfRegistrationPage register() {
        $(Schrodinger.byDataId("selfRegistration")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(byText("Self Registration")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new SelfRegistrationPage();
    }

    public LoginPage forgotPassword() {
        $(Schrodinger.byDataId("forgetpassword")).click();
        String url = getCurrentUrl();
        if (url.endsWith(SecurityQuestionsPage.getBasePath())) {
            return new SecurityQuestionsPage();
        } else if (url.endsWith(MailNoncePage.getBasePath())) {
            return new MailNoncePage();
        }
        return this;
    }

    public BasicPage loginIfUserIsNotLog(String username, String password){
        open("/login");
        Selenide.sleep(5000);
        if(!userMenuExists()) {
            return login(username, password);
        }
        return new BasicPage();
    }

    public BasicPage loginWithReloadLoginPage(String username, String password) {
        return loginWithReloadLoginPage(username, password, null);
    }

    public BasicPage loginWithReloadLoginPage(String username, String password, String locale) {
        open("/login");
        Selenide.sleep(5000);

        return login(username, password, locale);
    }

    public BasicPage login(String username, String password) {
        return login(username, password, null);
    }

    public BasicPage login(String username, String password, String locale) {
        if (StringUtils.isNotEmpty(locale)){
            changeLanguage(locale);
        }
        $(By.name("username")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(username);
        $(By.name("password")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(password);
        $x("//input[@type='submit']").click();

        return new BasicPage();
    }


    public FeedbackBox<? extends FormLoginPage> feedback() {
        SelenideElement feedback = $(By.cssSelector("div.feedbackContainer")).waitUntil(Condition.appears, MidPoint.TIMEOUT_LONG_1_M);
        return new FeedbackBox<>(this, feedback);
    }

    public static String getBasePath() {
        return "/login";
    }

    public FormLoginPage assertSignInButtonTitleMatch(String title) {
        assertion.assertTrue(Objects.equals(title, $(By.cssSelector(".btn.btn-primary"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .getValue()), "Sign in button title doesn't equal to " + title);
        return this;
    }
}
