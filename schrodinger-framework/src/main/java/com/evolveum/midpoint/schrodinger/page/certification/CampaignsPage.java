/*
 * Copyright (c) 2010-2021 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.jetbrains.annotations.NotNull;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class CampaignsPage extends CampaignsBasePage<CampaignsPage> {

    public CampaignsPage() {
    }

    public CampaignsPage startCampaign(String campaignName) {
        SelenideElement campaignTile = getCampaignTileElement(campaignName);

        if (campaignTile == null) {
            throw new IllegalStateException("Campaign with name '" + campaignName + "' not found.");
        }
        SelenideElement startCampaignButton = campaignTile.$x(".//button[@data-s-id='actionButton']");
        if (startCampaignButton.exists() && startCampaignButton.isDisplayed()) {
            startCampaignButton.click();

            ConfirmationModal<CampaignsPage> confirmationModal = new ConfirmationModal<>(this, Utils.getModalWindowSelenideElement());
            confirmationModal.clickYes();

            Selenide.sleep(10000);   // todo implement proper wait, e.g. wait for the task to be finished
        }

        return this;
    }

}
