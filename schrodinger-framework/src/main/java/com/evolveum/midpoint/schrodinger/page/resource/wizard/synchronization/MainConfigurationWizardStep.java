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
package com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization;

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.BasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;

public class MainConfigurationWizardStep<T> extends PrismFormWizardStepPanel<T>
        implements NextStepAction<ActionWizardStep> {

    public MainConfigurationWizardStep(T parent) {
        super(parent);
    }

    public MainConfigurationWizardStep<T> name(String name) {
        getFormPanel().addAttributeValue("Name", name);
        return MainConfigurationWizardStep.this;
    }

    public MainConfigurationWizardStep<T> description(String description) {
        getFormPanel().addAttributeValue("Description", description);
        return MainConfigurationWizardStep.this;
    }

    public MainConfigurationWizardStep<T> situation(String value) {
        getFormPanel().selectOption("Situation", value);
        return MainConfigurationWizardStep.this;
    }

    @Override
    public ActionWizardStep next() {
        clickNext();
        return new ActionWizardStep(getParent());
    }
}
