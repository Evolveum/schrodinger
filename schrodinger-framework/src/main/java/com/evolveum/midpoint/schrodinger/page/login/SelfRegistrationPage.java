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
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SelfRegistrationPage extends LoginPage {

    public SelfRegistrationPage setGivenName(String value) {
        $(By.name("contentArea:staticForm:firstName:input")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();
        $(By.name("contentArea:staticForm:firstName:input")).shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
        return  this;
    }

    public SelfRegistrationPage setFamilyName(String value) {
        $(By.name("contentArea:staticForm:lastName:input")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();
        $(By.name("contentArea:staticForm:lastName:input")).shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
        return  this;
    }

    public SelfRegistrationPage setEmail(String value) {
        $(By.name("contentArea:staticForm:email:input")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();
        $(By.name("contentArea:staticForm:email:input")).shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
        return  this;
    }

    public SelfRegistrationPage setPassword(String value) {
        Utils.waitForAjaxCallFinish();
        $(Schrodinger.byDataId("password1")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();
        $(Schrodinger.byDataId("password1")).shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
        $(Schrodinger.byDataId("password2")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();
        $(Schrodinger.byDataId("password2")).shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
        return  this;
    }

    public SelfRegistrationPage submit() {
        $(Schrodinger.byDataId("submitRegistration")).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    protected static String getBasePath() {
        return "/registration";
    }
}
