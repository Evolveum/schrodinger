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
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class ObjectTypeBasicInformationWizardStep extends WizardStepPanel<ObjectTypeWizardPage>
        implements NextStepAction<ObjectTypeResourceDataWizardStep> {
    public ObjectTypeBasicInformationWizardStep(ObjectTypeWizardPage parent) {
        super(parent);
    }

    public ObjectTypeBasicInformationWizardStep displayName(String value) {
        getFormPanel().addAttributeValue("Display name", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep description(String value) {
        getFormPanel().addAttributeValue("Description", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep kind(String value) {
        getFormPanel().selectOption("Kind", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep intent(String value) {
        getFormPanel().selectOption("Intent", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }


   public ObjectTypeBasicInformationWizardStep securityPolicy(String securityPolicyName) {
        getFormPanel().editRefValue("Security policy")
                .table()
                .clickByName(securityPolicyName);
       Utils.waitForAjaxCallFinish();
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep setDefault(String value) {
        getFormPanel().selectOption("Default", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    private PrismForm<ObjectTypeBasicInformationWizardStep> getFormPanel() {
        SelenideElement formElement = $x(".//div[@data-s-id='form']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new PrismForm<>(ObjectTypeBasicInformationWizardStep.this, formElement);
    }

    @Override
    public ObjectTypeResourceDataWizardStep next() {
        clickNext();
        return new ObjectTypeResourceDataWizardStep(getParent());
    }


}
