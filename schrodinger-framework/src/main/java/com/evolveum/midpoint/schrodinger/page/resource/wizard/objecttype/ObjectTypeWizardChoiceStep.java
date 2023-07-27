/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceDataPreviewPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;


public class ObjectTypeWizardChoiceStep extends TileListWizardStepPanel<ObjectTypeWizardPage> {

    public ObjectTypeWizardChoiceStep(ObjectTypeWizardPage parent) {
        super(parent);
    }

    public ObjectTypeBasicInformationWizardStep configureBasicAttributes() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.BASIC");
        clickTile(tile);
        return new ObjectTypeBasicInformationWizardStep(getParent());
    }

    public ResourceDataPreviewPanel<ObjectTypeWizardPage, ObjectTypeWizardChoiceStep> previewDataOfObjectType() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.PREVIEW_DATA");
        clickTile(tile);
        return new ResourceDataPreviewPanel<>(ObjectTypeWizardChoiceStep.this, getParent().getChoicePanelElement());
    }
    public void configureMappings() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.ATTRIBUTE_MAPPING");
        clickTile(tile);
    }

    public void configureSynchronization() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.SYNCHRONIZATION_CONFIG");
        clickTile(tile);
    }

    public void configureCorrelation() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.CORRELATION_CONFIG");
        clickTile(tile);
    }

    public void configureCapabilities() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.CAPABILITIES_CONFIG");
        clickTile(tile);
    }

    public void configureActivation() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.ACTIVATION");
        clickTile(tile);
    }

    public void configureCredentials() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.CREDENTIALS");
        clickTile(tile);
    }

    public void configureAssociations() {
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.ASSOCIATIONS");
        clickTile(tile);
    }

    public ResourcePage goToResource() {
        SelenideElement tile = Utils.findTileElementByTitle("Go to resource");
        clickTile(tile);
        return new ResourcePage();
    }

    private void clickTile(SelenideElement tile) {
        Utils.scrollToElement(tile);
        tile.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
    }
}
