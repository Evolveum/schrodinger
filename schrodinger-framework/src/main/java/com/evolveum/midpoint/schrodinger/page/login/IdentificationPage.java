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