package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class ActiveCampaignsBasePage<ACP extends ActiveCampaignsBasePage> extends CampaignsBasePage<ACP> {

    public ActiveCampaignsBasePage() {
    }

    public CertificationItemsPage showAllItems() {
        getNavigationPanelElement().$(Schrodinger.byDataId("next")).click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(1000);
        Utils.waitForAjaxCallFinish();
        return new CertificationItemsPage();
    }

    public CertificationItemsPage showItemsForCampaign(String campaignName) {
        selectTilesView().findTileByLabel(campaignName).$(Schrodinger.byDataId("details")).click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(1000);
        Utils.waitForAjaxCallFinish();
        return new CertificationItemsPage();
    }

    private SelenideElement getNavigationPanelElement() {
        return $(Schrodinger.byDataId("navigationPanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

}
