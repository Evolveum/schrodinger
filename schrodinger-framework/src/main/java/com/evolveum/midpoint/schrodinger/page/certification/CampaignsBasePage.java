package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.jetbrains.annotations.NotNull;

import static com.codeborne.selenide.Selenide.$$x;

public class CampaignsBasePage<CP extends CampaignsBasePage> extends BasicPage {

    public CampaignsBasePage() {
    }

    public CP assertCampaignInReviewStage(String campaignName) {
        SelenideElement campaignTile = getCampaignTileElement(campaignName);

        if (campaignTile == null) {
            throw new IllegalStateException("Campaign with name '" + campaignName + "' not found.");
        }
        String statusTranslated = Utils.translate("CertCampaignStateFilter.IN_REVIEW_STAGE", "In review stage");
        SelenideElement status = campaignTile.$x(".//span[@data-s-id='status']");
        assertion.assertTrue(status.exists() && statusTranslated.equals(status.getText()),
                "Campaign should be in review stage but now the state is: " + status.getText());

        return (CP) this;
    }

    protected SelenideElement getCampaignTileElement(@NotNull String campaignName) {
        ElementsCollection campaignTiles = $$x(".//div[contains(@class, 'campaign-tile-panel')]");
        for (SelenideElement campaignTile : campaignTiles) {
            SelenideElement campaignTitle = campaignTile.$(Schrodinger.byDataId("title"));
            if (campaignTitle.exists() && campaignTitle.isDisplayed() && campaignName.equals(campaignTitle.getText())) {
                return campaignTile;
            }
        }
        return null;
    }
}
