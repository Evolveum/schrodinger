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
        setAttributeValue("firstName", value);
        return  this;
    }

    public SelfRegistrationPage setFamilyName(String value) {
        setAttributeValue("lastName", value);
        return  this;
    }

    public SelfRegistrationPage setEmail(String value) {
        setAttributeValue("email", value);
        return  this;
    }

    private void setAttributeValue(String attributeName, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement schrodingerElement = $(Schrodinger.byDataId(attributeName));
        if (schrodingerElement.exists()) {
            SelenideElement inputParent = schrodingerElement.parent();
            if (inputParent.exists()) {
                for (SelenideElement el : inputParent.$$x(".//*")) {
                    System.out.println("Parent contains: " + el.getTagName());
                }
                inputParent.$(By.tagName("input"))
                        .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                        .sendKeys(value);
                Selenide.screenshot("setAttributeValue_" + attributeName);
                Utils.waitForAjaxCallFinish();
            }
        }
//        //if the value wasn't set by the previous code, we will try to set it again
//        String nameAttrValue = "contentArea:staticForm:" + attributeName + ":input";
//        $x(".//input[@name='" + nameAttrValue + "']")
//                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
//        Selenide.screenshot("setAttributeValue_" + attributeName);
//        Selenide.sleep(1000);
//        Utils.waitForAjaxCallFinish();
//        $x(".//input[@name='" + nameAttrValue + "']")
//                .shouldHave(Condition.value(value), MidPoint.TIMEOUT_DEFAULT_2_S);
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

    public SelfRegistrationPage setCaptcha() {
        SelenideElement captcha = $x(".//input[@data-s-id='text']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        //todo we need to set any value, it will be ignored during the test
        captcha.setValue("1234");
        Utils.waitForAjaxCallFinish();
        return  this;
    }

    public SelfRegistrationPage submit() {
        $(Schrodinger.byDataId("text")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue("text");
        Utils.waitForAjaxCallFinish();
        $(Schrodinger.byDataId("text")).shouldHave(Condition.value("text"), MidPoint.TIMEOUT_DEFAULT_2_S);
        $(Schrodinger.byDataId("submitRegistration")).click();
        return this;
    }

    protected static String getBasePath() {
        return "/registration";
    }
}
