/*
 * Copyright (c) 2025 Evolveum
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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class CampaignTilePanel<T> extends Component<T, CampaignTilePanel<T>> {

    public CampaignTilePanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public CampaignTilePanel<T> startCampaign() {
        clickActionButtonAndConfirmAction();
        Selenide.sleep(10000);   // todo implement proper wait, e.g. wait for the task to be finished
        return this;
    }

    public CampaignTilePanel<T> clickActionButtonAndConfirmAction() {
        SelenideElement actionButton = getActionButtonElement();
        if (actionButton.exists() && actionButton.isDisplayed()) {
            actionButton.click();
            ConfirmationModal<CampaignTilePanel<T>> confirmationModal =
                    new ConfirmationModal<>(CampaignTilePanel.this, Utils.getModalWindowSelenideElement());
            confirmationModal.clickYes();
        }
        return this;
    }

    public CampaignTilePanel<T> assertActionButtonIsInProgress() {
        SelenideElement actionButton = getActionButtonElement();
        SelenideElement spinnerIcon = actionButton
                .find(By.cssSelector(".fa.fa-spinner.fa-spin-pulse"));
        assertion.assertTrue(spinnerIcon.isDisplayed(), "Action button should be in progress but it's not.");
        return CampaignTilePanel.this;
    }

    private SelenideElement getActionButtonElement() {
        return getParentElement().$x(".//button[@data-s-id='actionButton']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public CampaignTilePanel<T>  assertCampaignInReviewStage() {
        String statusTranslated = Utils.translate("CertCampaignStateFilter.IN_REVIEW_STAGE", "In review stage");
        SelenideElement status = getParentElement().$x(".//span[@data-s-id='status']");
        assertion.assertTrue(status.exists() && statusTranslated.equals(status.getText()),
                "Campaign should be in review stage but now the state is: " + status.getText());

        return this;
    }
}
