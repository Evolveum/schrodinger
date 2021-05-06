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
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SelfRegistrationPage extends LoginPage {

    public SelfRegistrationPage setGivenName(String value) {
        $(By.name("contentArea:staticForm:firstName:input")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        return  this;
    }

    public SelfRegistrationPage setFamilyName(String value) {
        $(By.name("contentArea:staticForm:lastName:input")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        return  this;
    }

    public SelfRegistrationPage setEmail(String value) {
        $(By.name("contentArea:staticForm:email:input")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        return  this;
    }

    public SelfRegistrationPage setPassword(String value) {
        $(Schrodinger.byDataId("password1")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        $(Schrodinger.byDataId("password2")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        return  this;
    }

    public SelfRegistrationPage setCaptcha() {
        SelenideElement captcha = $x(".//img[@data-s-id='image']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String captchaFullId = captcha.getAttribute("data-s-id");
        $(Schrodinger.byDataId("text")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(captchaFullId.substring(5));
//        $(By.name("contentArea:staticForm:firstName:input")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        return  this;
    }

    public SelfRegistrationPage submit() {
        $(Schrodinger.byDataId("text")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue("text");
        $(Schrodinger.byDataId("submitRegistration")).click();
        return this;
    }

    protected static String getBasePath() {
        return "/registration";
    }
}
