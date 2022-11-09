package com.evolveum.midpoint.schrodinger.component.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.TabWithTableAndPrismView;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ObjectCollectionViewsPanel extends TabWithTableAndPrismView<SystemPage> {

    public ObjectCollectionViewsPanel(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public PrismForm<ObjectCollectionViewsPanel> clickAddButton() {
        table().getButtonToolbar().$x(".//i[contains(@class, \"fa fa-plus\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new PrismFormWithActionButtons<>(ObjectCollectionViewsPanel.this, getPrismViewPanel());
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return getParentElement().$x(".//div[@class='card-body']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
