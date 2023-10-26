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
package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class IdentificationPage extends FormLoginPage {

    public LoginPage setNameAndConfirm(String nameValue) {
        setUsernameValue(nameValue);
        clickSendButton();
        return this;
    }

    @Override
    public IdentificationPage setUsernameValue(String nameValue) {
        SelenideElement nameInput = $(Schrodinger.byDataId("attributeValue")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        Utils.setValueToElementAndFireBlurEvent(nameInput, nameValue);
        Utils.waitForAjaxCallFinish();
        Selenide.screenshot("IdentificationPage_setUsername");
        return this;
    }

    public LoginPage clickSendButton() {
        $x(".//input[@type='submit']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        Selenide.screenshot("IdentificationPage_sendButtonClick");
        return this;
    }
}
