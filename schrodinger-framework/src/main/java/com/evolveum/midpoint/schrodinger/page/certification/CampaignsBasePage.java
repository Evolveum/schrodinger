package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class CampaignsBasePage<CP extends CampaignsBasePage> extends BasicPage {

    private static final String TABLE_VIEW_TOGGLE_ICON_CLASS = "fa-table-list";
    private static final String TILES_VIEW_TOGGLE_ICON_CLASS = "fa-table-cells";

    public CampaignsBasePage() {
    }

    public CP assertCampaignInReviewStage(String campaignName) {
        SelenideElement campaignTile = selectTilesView().findTileByLabel(campaignName);

        if (campaignTile == null) {
            throw new IllegalStateException("Campaign with name '" + campaignName + "' not found.");
        }
        String statusTranslated = Utils.translate("CertCampaignStateFilter.IN_REVIEW_STAGE", "In review stage");
        SelenideElement status = campaignTile.$x(".//span[@data-s-id='status']");
        assertion.assertTrue(status.exists() && statusTranslated.equals(status.getText()),
                "Campaign should be in review stage but now the state is: " + status.getText());

        return (CP) this;
    }

    public CampaignsTablePanel selectTableView() {
        clickHeaderToggleButton(TABLE_VIEW_TOGGLE_ICON_CLASS);
        return new CampaignsTablePanel((CP) this, getContentPanelElement());
    }

    public CampaignTilesListPanel<CP> selectTilesView() {
        clickHeaderToggleButton(TILES_VIEW_TOGGLE_ICON_CLASS);
        return new CampaignTilesListPanel<>((CP) this, getContentPanelElement());
    }

    private void clickHeaderToggleButton(String iconClass) {
        SelenideElement iconElement = getToggleButtonIconElement(iconClass);
        if (iconElement.has(Condition.attribute("aria-pressed", "true"))) {
            return;
        }
        iconElement.click();
        iconElement.shouldHave(Condition.attribute("aria-pressed", "true"),
                MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    private SelenideElement getContentPanelElement() {
        return $(Schrodinger.byDataId("mainForm")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public CP assertTableViewIsSelected() {
        SelenideElement buttonElement = getToggleButtonIconElement(TABLE_VIEW_TOGGLE_ICON_CLASS);
        assertion.assertTrue(buttonElement.getAttribute("aria-pressed").equals("true"),
                "Table view should be selected but now it is not.");
        return (CP) this;
    }

    public CP assertTileViewIsSelected() {
        SelenideElement iconElement = getToggleButtonIconElement(TABLE_VIEW_TOGGLE_ICON_CLASS);
        assertion.assertTrue(iconElement.getAttribute("aria-pressed").equals("true"),
                "Table view should be selected but now it is not.");
        return (CP) this;
    }

    private SelenideElement getToggleButtonIconElement(String iconClass) {
        return getToggleButtonElement().$x(".//i[contains(@class, '" + iconClass + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent();
    }

    private SelenideElement getToggleButtonElement() {
        return $(Schrodinger.byDataId("div", "viewToggle"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
