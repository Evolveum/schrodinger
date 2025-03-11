package com.evolveum.midpoint.schrodinger.component.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithTableAndPrismView;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class UserApplicationsPanel extends PanelWithTableAndPrismView<UserPage> {

    public UserApplicationsPanel(UserPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return $(Schrodinger.byDataId("div", "valueForm"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
