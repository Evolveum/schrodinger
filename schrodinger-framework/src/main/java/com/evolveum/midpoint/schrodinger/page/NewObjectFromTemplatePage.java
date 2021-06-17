package com.evolveum.midpoint.schrodinger.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class NewObjectFromTemplatePage<T extends AssignmentHolderDetailsPage> extends BasicPage {


    public T clickTemplateButtonWithTitle(String title, T pageToRedirect) {
        SelenideElement templateButton = $(Schrodinger.byElementValue("div",
                "data-s-id", "buttonDescription", title)).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        templateButton.click();
        templateButton.waitUntil(Condition.disappears, MidPoint.TIMEOUT_MEDIUM_6_S);
        return pageToRedirect;
    }
}
