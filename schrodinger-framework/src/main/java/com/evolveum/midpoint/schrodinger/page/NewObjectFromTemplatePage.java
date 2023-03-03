package com.evolveum.midpoint.schrodinger.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class NewObjectFromTemplatePage<T extends AssignmentHolderDetailsPage> extends BasicPage {


    public T clickTemplateButtonWithTitle(String title, T pageToRedirect) {
        SelenideElement templateButton = $x(".//span[contains(@class, \"text-capitalize\") " +
                "and contains(text(), \"" + title + "\")]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        templateButton.click();
        Utils.waitForMainPanelOnDetailsPage();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        return pageToRedirect;
    }
}
