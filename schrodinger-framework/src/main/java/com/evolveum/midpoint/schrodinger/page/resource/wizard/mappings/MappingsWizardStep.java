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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class MappingsWizardStep<T> extends WizardStepPanel<T> {

    public MappingsWizardStep(T parent) {
        super(parent);
    }

    public InboundMappingsPanel<MappingsWizardStep<T>> inboundMappings() {
        String linkText = Utils.getPropertyString("AttributeMappingsTableWizardPanel.inboundTable");
        $x(".//a[@data-s-id='link' and contains(text(), \"" + linkText + "\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new InboundMappingsPanel<>(MappingsWizardStep.this);
    }

    public OutboundMappingsPanel<MappingsWizardStep<T>> outboundMappings() {
        String linkText = Utils.getPropertyString("AttributeMappingsTableWizardPanel.outboundTable");
        $x(".//a[@data-s-id='link' and contains(text(), \"" + linkText + "\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new OutboundMappingsPanel<>(MappingsWizardStep.this);
    }

    public T saveMappings() {
        String buttonTitle = Utils.getPropertyString("AttributeMappingsTableWizardPanel.saveButton");
        getStepPanelContentElement()
                .$x(".//a[@title=\"" + buttonTitle + "\"]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }
}
