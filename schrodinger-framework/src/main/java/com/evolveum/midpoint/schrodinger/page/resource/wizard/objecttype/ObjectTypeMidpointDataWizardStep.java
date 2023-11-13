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
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class ObjectTypeMidpointDataWizardStep<T> extends PrismFormWizardStepPanel<T>
        implements PreviousStepAction<ObjectTypeResourceDataWizardStep<T>> {

    public ObjectTypeMidpointDataWizardStep(T parent) {
        super(parent);
    }

    public ObjectTypeMidpointDataWizardStep<T> type(String value) {
        getFormPanel().selectOption("Type", value);
        return ObjectTypeMidpointDataWizardStep.this;
    }

    public ObjectTypeMidpointDataWizardStep<T> archetype(String archetypeName) {
        getFormPanel().editRefValue("Archetype")
                .table()
                .clickByName(archetypeName);
        Utils.waitForAjaxCallFinish();
        return ObjectTypeMidpointDataWizardStep.this;
    }

    public ObjectTypeWizardChoiceStep<T> saveSettings() {
        $(Schrodinger.byDataId("submit"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new ObjectTypeWizardChoiceStep<>(getParent());
    }

    @Override
    public ObjectTypeResourceDataWizardStep<T> back() {
        clickBack();
        return new ObjectTypeResourceDataWizardStep<>(getParent());
    }

    @Override
    protected String getFormElementId() {
        return "formContainer";
    }
}
