package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CorrelationPage {

    public CorrelationPage setAttributeValue(String attributeLabel, String attributeValue) {
        SelenideElement attributePanel = findAttributePanel(attributeLabel);
        attributePanel.$x(".//input").setValue(attributeValue);
        return this;
    }

    private SelenideElement findAttributePanel(String attributeLabel) {
//        $x(".//div[@data-s-id='archetype' and contains(., '" + archetypeLabel + "')]")
//                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("label",
                "data-s-id", "attributeName", "data-s-id", "label", attributeLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent();
    }

    public CorrelationPage send() {
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }
}
