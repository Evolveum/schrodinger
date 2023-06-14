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
package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

import static com.codeborne.selenide.Selenide.$x;

public class BasicInformationWizardStep extends WizardStepPanel<ResourceWizardPage>
        implements NextStepAction<ConfigurationWizardStep> {
    public BasicInformationWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    public BasicInformationWizardStep name(String name) {
        getFormPanel().addAttributeValue("Name", name);
        return BasicInformationWizardStep.this;
    }

    public BasicInformationWizardStep description(String description) {
        getFormPanel().addAttributeValue("Description", description);
        return BasicInformationWizardStep.this;
    }

    private PrismForm<BasicInformationWizardStep> getFormPanel() {
        SelenideElement formElement = $x(".//div[@data-s-id='form']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new PrismForm<>(BasicInformationWizardStep.this, formElement);
    }

    private SelenideElement getContentBodyElement() {
        return $x(".//div[@data-s-id='contentBody']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

//    @Override
//    protected ConfigurationWizardStep getNextStepPanel() {
//        return new ConfigurationWizardStep(getParent());
//    }

    @Override
    public ConfigurationWizardStep next() {
        clickNext();
        return new ConfigurationWizardStep(getParent());
    }

//    public ConfigurationWizardStep clickNext() {
//        return next();
//    }


}
