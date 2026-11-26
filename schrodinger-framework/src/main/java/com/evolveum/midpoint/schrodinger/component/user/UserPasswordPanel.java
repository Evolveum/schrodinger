/*
 * Copyright (c) 2021 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.user;

import java.util.List;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class UserPasswordPanel<T> extends PanelWithContainerWrapper<T> {

    public UserPasswordPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public UserPasswordPanel<T> setPasswordValue(String value) {

        boolean existValue = getParentElement().$x(".//a[@data-s-id='changePasswordLink']").exists();
        if (existValue) {
            getParentElement().$x(".//a[@data-s-id='changePasswordLink']").click();
        }

        checkPasswordField(".//input[@data-s-id='password1']", value);
        checkPasswordField(".//input[@data-s-id='password2']", value);

        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public UserPasswordPanel<T> assertHintNotDisplayed(String hintValue) {
        Utils.waitForAjaxCallFinish();
        assertion.assertFalse(getParentElement().$x(".//*[@data-s-id='property' and contains(@data-s-qname, '#hint')]").exists(),
                "Password hint property shouldn't be displayed.");
        assertion.assertFalse(getParentElement().$x(".//input[@data-s-id='hint']").exists(),
                "Password hint input shouldn't be present.");
        assertion.assertFalse(Selenide.webdriver().driver().source().contains(hintValue),
                "Password hint value shouldn't be present in the page markup.");
        return this;
    }

    /**
     * Password strength meter is a script (static/vendors-passwords.js) that the password field adds to the page.
     * It is checked by what the script defines, not by status of its request: a wrong URL may be answered
     * with a redirect to some page, which looks like a successful request.
     */
    public UserPasswordPanel<T> assertPasswordStrengthScriptLoaded() {
        Utils.waitForAjaxCallFinish();
        String script = "return typeof window.Score === 'function' && typeof window.strengthMeterOptions === 'object';";
        long end = System.currentTimeMillis() + MidPoint.TIMEOUT_MEDIUM_6_S.toMillis();
        boolean loaded = Boolean.TRUE.equals(Selenide.executeJavaScript(script));
        while (!loaded && System.currentTimeMillis() < end) {
            Selenide.sleep(200);
            loaded = Boolean.TRUE.equals(Selenide.executeJavaScript(script));
        }
        List<String> requests = Selenide.executeJavaScript(
                "return performance.getEntriesByType('resource')"
                        + ".filter(e => e.name.includes('vendors-passwords.js')).map(e => e.name);");
        assertion.assertTrue(loaded, "Password strength script is not loaded, requested as: " + requests);
        return this;
    }

    public UserPasswordPanel<T> showEmptyFields() {
        form().showEmptyAttributes("Password");
        return this;
    }

    public UserPasswordPanel<T> assertHintEditable() {
        assertion.assertTrue(getHintInput().isEnabled(), "Password hint input should be editable.");
        return this;
    }

    private SelenideElement getHintInput() {
        return getParentElement().$x(".//input[@data-s-id='hint']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    private void checkPasswordField(String identifier, String value) {
        SelenideElement passwordField = getParentElement().$x(identifier);
        passwordField.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        Utils.waitForAjaxCallFinish();

        SelenideElement eyeIcon = passwordField.$x("following::i[contains(@class, 'fa-eye')][1]");
        eyeIcon.shouldBe(Condition.visible).click();
        Utils.waitForAjaxCallFinish();
        passwordField.shouldBe(Condition.value(value));
    }

}
