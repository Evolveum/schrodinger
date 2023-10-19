package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.assertEquals;

public class CorrelationPage {

    public CorrelationPage setAttributeValue(String attributeLabel, String attributeValue) {
        SelenideElement attributePanel = findAttributePanel(attributeLabel);
        attributePanel.$x(".//input").setValue(attributeValue);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    private SelenideElement findAttributePanel(String attributeLabel) {
        return $(Schrodinger.byAncestorPrecedingSiblingDescendantOrSelfElementEnclosedValue("div",
                "data-s-id", "attributeValue", "data-s-id", "attributeName", attributeLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public CorrelationPage send() {
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public CorrelationPage assertSingleIdentityFound(String identityName) {
        ElementsCollection detailsPanels = $$x(".//div[@data-s-id='detailsPanel']");
        assertEquals(1, detailsPanels.size());
        SelenideElement userDetailsPanel = detailsPanels.get(0);
        userDetailsPanel.$x(".//div[@data-s-id='displayName' and contains(., '" + identityName + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }


}
