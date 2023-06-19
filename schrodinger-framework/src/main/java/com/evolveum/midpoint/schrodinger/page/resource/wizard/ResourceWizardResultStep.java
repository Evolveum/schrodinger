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
package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;

import static com.codeborne.selenide.Selenide.$x;

public class ResourceWizardResultStep extends TileListWizardStepPanel<ResourceWizardPage> {

    private static final String RESOURCE_CREATED_HEADER = "A new resource has been created";

    public ResourceWizardResultStep(ResourceWizardPage page) {
        super(page);
    }

    public ResourceDataPreviewPanel<ResourceWizardPage, ResourceWizardResultStep> previewResourceData() {
        selectTileByLabel("Preview resource data");
        return new ResourceDataPreviewPanel<>(ResourceWizardResultStep.this, getChoicePanelElement());
    }

    public ObjectTypeManagerPanel configureObjectTypes() {
        selectTileByLabel("Configure Object Types");
        return new ObjectTypeManagerPanel(ResourceWizardResultStep.this, getChoicePanelElement());
    }

    public ResourcePage goToResource() {
        selectTileByLabel("Go to resource");
        return new ResourcePage();
    }

    public ListResourcesPage exitWizard() {
        $x(".//a[contains(text(), 'Exit wizard')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ListResourcesPage();
    }

    private SelenideElement getChoicePanelElement() {
        return $x(".//div[@data-s-id='choicePanel']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public ResourceWizardResultStep assertResourceIsCreated() {
        SelenideElement header = $x(".//h2[@data-s-resource-key='ResourceWizardPreviewPanel.text']");
        String text = header.text();
        assertion.assertTrue(text.contains(RESOURCE_CREATED_HEADER),
                "Resource creation result doesn't contain a correct message; expected: "
                        + RESOURCE_CREATED_HEADER
                        + "; actual: " + text);
        return ResourceWizardResultStep.this;
    }
}
