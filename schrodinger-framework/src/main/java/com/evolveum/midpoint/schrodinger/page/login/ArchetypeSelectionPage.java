package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class ArchetypeSelectionPage {

    public ArchetypeSelectionPage selectArchetype(String archetypeLabel) {
        $x(".//div[@data-s-id='archetype' and contains(., '" + archetypeLabel + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public CorrelationPage send() {
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new CorrelationPage();
    }
}
