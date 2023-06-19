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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceDataPreviewPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeWizardChoiceStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeWizardPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$$x;

public class AccountsTopButtonPanel extends Component<ResourceAccountsPanel> {

    public AccountsTopButtonPanel(ResourceAccountsPanel parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ObjectTypeBasicInformationWizardStep configureBasicAttributes() {
        clickButton("ResourceObjectTypePreviewTileType.BASIC");
        return new ObjectTypeBasicInformationWizardStep(new ObjectTypeWizardPage());
    }

    public void configureMappings() {
        clickButton("ResourceObjectTypePreviewTileType.ATTRIBUTE_MAPPING");
    }

    public void configureSynchronization() {
        clickButton("ResourceObjectTypePreviewTileType.SYNCHRONIZATION_CONFIG");
    }

    public void configureCorrelation() {
        clickButton("ResourceObjectTypePreviewTileType.CORRELATION_CONFIG");
    }

    public void configureCapabilities() {
        clickButton("ResourceObjectTypePreviewTileType.CAPABILITIES_CONFIG");
    }

    public void configureActivation() {
        clickButton("ResourceObjectTypePreviewTileType.ACTIVATION");
    }

    public void configureCredentials() {
        clickButton("ResourceObjectTypePreviewTileType.CREDENTIALS");
    }

    public void configureAssociations() {
        clickButton("ResourceObjectTypePreviewTileType.ASSOCIATIONS");
    }

    private void clickButton(String title) {
        String translatedTitle = Utils.getPropertyString(title);
        ElementsCollection buttons = $$x(".//a");
        SelenideElement button = buttons
                .stream()
                .filter(b -> Utils.elementContainsTextCaseInsensitive(b, translatedTitle))
                .findFirst()
                .orElse(null);
        if (button == null) {
            return;
        }
        button.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
    }

}
