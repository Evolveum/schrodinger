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
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class LoginPage extends BasicPage {

    public LoginPage changeLanguage(String countryCode) {
        Validate.notNull(countryCode, "Country code must not be null");

        $(Schrodinger.byDataId("localeIcon"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        String flagIconCss = "flag-" + countryCode.trim().toLowerCase();
        $(By.className(flagIconCss))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent()
                .$(By.tagName("a"))
                .click();

        return this;
    }

    public LoginPage setUsernameValue(String emailValue) {
        $(Schrodinger.byDataId("username")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(emailValue);
        return this;
    }

    public LoginPage setEmailValue(String emailValue) {
        $(Schrodinger.byDataId("email")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(emailValue);
        return this;
    }

    public void clickSubmitButton() {
        $(Schrodinger.byDataId("submitButton")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
    }
}
