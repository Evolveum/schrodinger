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

import com.codeborne.selenide.*;
import com.codeborne.selenide.ex.ElementShould;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;

import java.util.function.BiConsumer;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SelfRegistrationPage extends LoginPage {

    public SelfRegistrationPage setGivenName(String value) {
        setAttributeValue("firstName", value);
        return this;
    }

    public SelfRegistrationPage setAllGivenNames(String value) {
        setAllAttributeValue("firstName", value);
        return this;
    }

    public SelfRegistrationPage setFamilyName(String value) {
        setAttributeValue("lastName", value);
        return this;
    }

    public SelfRegistrationPage setAllFamilyNames(String value) {
        setAllAttributeValue("lastName", value);
        return this;
    }

    public SelfRegistrationPage setEmail(String value) {
        setAttributeValue("email", value);
        return this;
    }

    public SelfRegistrationPage setAllEmails(String value) {
        setAllAttributeValue("email", value);
        return this;
    }

    private void setAllAttributeValue(String attributeName, String value) {
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(1000);
        String nameAttrValue = "contentArea:staticForm:" + attributeName + ":input";
        ElementsCollection collection = $$x(".//input[@name='" + nameAttrValue + "']");
        collection.asFixedIterable().forEach((inputField) -> setAttributeValue(attributeName, value, inputField));
    }

    private void setAttributeValue(String attributeName, String value) {
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(1000);
        String nameAttrValue = "contentArea:staticForm:" + attributeName + ":input";
        SelenideElement inputField = $x(".//input[@name='" + nameAttrValue + "']");
        setAttributeValue(attributeName, value, inputField);
    }

    private void setAttributeValue(String attributeName, String value, SelenideElement inputField) {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        try {
            js.executeScript("window.stop();");
            inputField
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                    .sendKeys(value);
            js.executeScript("window.stop();");
            Selenide.screenshot("try_setAttributeValue_" + attributeName);
        } catch (ElementShould e) {
            if (!inputField.is(Condition.visible)) {
                js.executeScript("document.getElementById(\"" + inputField.getAttribute("id") + "\").value = \"" + value + "\"");
            }
        } catch (Exception e) {
            Selenide.screenshot("catch_setAttributeValue_" + attributeName);
        }
        Selenide.sleep(1000);
    }

    public SelfRegistrationPage setPassword(String value) {
        Utils.waitForAjaxCallFinish();

        BiConsumer<String, String> processPassword =
                (elementId, password) ->
                        $(Schrodinger.byDataId(elementId)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).sendKeys(password);

        setPassword("password1", value, processPassword);
        setPassword("password2", value, processPassword);

        return this;
    }

    private void setPassword(String elementId, String value, BiConsumer<String, String> processPassword) {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        try {
            processPassword.accept(elementId, value);
            js.executeScript("window.stop();");
            Selenide.screenshot("try_set" + StringUtils.capitalize(elementId) + "_" + value);
        } catch (Exception e) {
            Selenide.screenshot("catch_set_" + elementId);
        }
    }

    public SelfRegistrationPage setAllPasswords(String value) {
        Utils.waitForAjaxCallFinish();

        BiConsumer<String, String> processPassword = (elementId, password) -> $$(Schrodinger.byDataId(elementId)).asFixedIterable()
                .forEach((inputField) -> {
                    try {
                        inputField.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).sendKeys(password);
                    } catch (ElementShould e) {
                        if (!inputField.is(Condition.visible)) {
                            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
                            js.executeScript("document.getElementById(\"" + inputField.getAttribute("id") + "\").value = \"" + value + "\"");
                        }
                    }
                });

        setPassword("password1", value, processPassword);
        setPassword("password2", value, processPassword);

        return this;
    }

    public SelfRegistrationPage submit() {
        if ($(Schrodinger.byDataId("div", "validationPanel")).is(Condition.visible)) {
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            js.executeScript("document.querySelectorAll('div[data-s-id=\"validationPanel\"]').item(0).style.display = \"none\";");
        }
        Selenide.screenshot("self_reg_before_submit");
        $(Schrodinger.byDataId("submitRegistration")).click();
        Selenide.screenshot("self_reg_after_submit");
        Utils.waitForAjaxCallFinish();
        return this;
    }
}
