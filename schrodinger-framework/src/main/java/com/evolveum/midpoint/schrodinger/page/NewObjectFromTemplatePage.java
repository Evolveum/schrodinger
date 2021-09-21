package com.evolveum.midpoint.schrodinger.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class NewObjectFromTemplatePage<T extends AssignmentHolderDetailsPage> extends BasicPage {


    public T clickTemplateButtonWithTitle(String title, T pageToRedirect) {
        SelenideElement templateButton = $(Schrodinger.byElementValue("span",
                "class", "compositedButtonLabel", title)).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        templateButton.click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        $(Schrodinger.byDataId("mainPanel")).waitUntil(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);
        return pageToRedirect;
    }
}
