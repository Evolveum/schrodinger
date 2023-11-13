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

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ObjectTypeBasicInformationWizardStep<T> extends PrismFormWizardStepPanel<T>
        implements NextStepAction<ObjectTypeResourceDataWizardStep<T>> {
    public ObjectTypeBasicInformationWizardStep(T parent) {
        super(parent);
    }

    public ObjectTypeBasicInformationWizardStep<T> displayName(String value) {
        getFormPanel().addAttributeValue("Display name", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep<T> description(String value) {
        getFormPanel().addAttributeValue("Description", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep<T> kind(String value) {
        getFormPanel().selectOption("Kind", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep<T> intent(String value) {
        getFormPanel().selectOption("Intent", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }


   public ObjectTypeBasicInformationWizardStep<T> securityPolicy(String securityPolicyName) {
        getFormPanel().editRefValue("Security policy")
                .table()
                .clickByName(securityPolicyName);
       Utils.waitForAjaxCallFinish();
        return ObjectTypeBasicInformationWizardStep.this;
    }

    public ObjectTypeBasicInformationWizardStep<T> setDefault(String value) {
        getFormPanel().selectOption("Default", value);
        return ObjectTypeBasicInformationWizardStep.this;
    }

    @Override
    public ObjectTypeResourceDataWizardStep<T> next() {
        clickNext();
        return new ObjectTypeResourceDataWizardStep<>(getParent());
    }

    @Override
    protected String getFormElementId() {
        return "formContainer";
    }
}
