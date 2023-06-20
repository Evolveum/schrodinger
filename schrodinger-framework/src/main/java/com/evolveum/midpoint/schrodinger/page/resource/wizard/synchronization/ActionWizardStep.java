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
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class ActionWizardStep<T> extends PrismFormWizardStepPanel<T>
        implements NextStepAction<OptionalSettingsWizardStep<T>>, PreviousStepAction<MainConfigurationWizardStep<T>> {

    public ActionWizardStep(T parent) {
        super(parent);
    }

    public ActionWizardStep<T> action(String value) {
        getFormPanel().selectOption("Action", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> name(String name) {
        getFormPanel().addAttributeValue("Name", name);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> documentation(String value) {
        getFormPanel().addAttributeValue("Documentation", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> order(String value) {
        getFormPanel().addAttributeValue("Order", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> synchronize(String value) {
        getFormPanel().selectOption("Synchronize", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> reconcile(String value) {
        getFormPanel().selectOption("Reconcile", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> reconcileAll(String value) {
        getFormPanel().selectOption("Reconcile all", value);
        return ActionWizardStep.this;
    }

    public ActionWizardStep<T> limitPropagation(String value) {
        getFormPanel().selectOption("Limit propagation", value);
        return ActionWizardStep.this;
    }

    @Override
    public OptionalSettingsWizardStep<T> next() {
        clickNext();
        return new OptionalSettingsWizardStep<>(getParent());
    }

    @Override
    public MainConfigurationWizardStep<T> back() {
        clickBack();
        return new MainConfigurationWizardStep<>(getParent());
    }
}
