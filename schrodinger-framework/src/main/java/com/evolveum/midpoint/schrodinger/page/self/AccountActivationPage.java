package com.evolveum.midpoint.schrodinger.page.self;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class AccountActivationPage extends BasicPage {

    public AccountActivationPage setPasswordValue(String password) {
        $(Schrodinger.byDataId("password")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .setValue(password).shouldBe(Condition.value(password), MidPoint.TIMEOUT_MEDIUM_6_S);
        return this;
    }

    public AccountActivationPage clickCinfirmPasswordButton() {
        $(Schrodinger.byDataId("confirm")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        return this;
    }

    public AccountActivationPage assertActivatedShadowsContainText(String expectedText) {
        $(Schrodinger.byDataId("activatedShadows")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .shouldBe(Condition.text(expectedText), MidPoint.TIMEOUT_MEDIUM_6_S);
        return this;
    }
}
