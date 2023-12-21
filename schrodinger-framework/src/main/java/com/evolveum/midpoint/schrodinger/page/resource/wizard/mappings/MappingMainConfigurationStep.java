/*
 * Copyright (c) 2023  Evolveum
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

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class MappingMainConfigurationStep<T> extends PrismFormWizardStepPanel<T>
        implements NextStepAction<MappingOptionalConfigurationStep<T>> {

    public MappingMainConfigurationStep(T parent) {
        super(parent);
    }

    public MappingMainConfigurationStep<T> fromResourceAttribute(String value) {
        getFormPanel().setDropDownAttributeValue("From resource attribute", value);
        return this;
    }

    public MappingMainConfigurationStep<T> name(String value) {
        getFormPanel().addAttributeValue("Name", value);
        return this;
    }

    public MappingMainConfigurationStep<T> strength(String value) {
        getFormPanel().setDropDownAttributeValue("Strength", value);
        return this;
    }

    public MappingMainConfigurationStep<T> source(String objectType, String value) {
        //TODO not sure it works
        getFormPanel().setDropDownAttributeValue("Source", objectType);
        getFormPanel().addAttributeValue("Source", value);
        return this;
    }

    public MappingMainConfigurationStep<T> expression(String value) {
        getFormPanel().setDropDownAttributeValue("Expression", value);
        return this;
    }

    public MappingMainConfigurationStep<T> target(String value) {
        getFormPanel().addAttributeValue("Target", value);
        return this;
    }

    public MappingMainConfigurationStep<T> languageCondition(String value) {
        getFormPanel().setDropDownAttributeValue("Condition", value);
        return this;
    }

    public MappingMainConfigurationStep<T> code(String value) {
        getFormPanel().setAceEditorAreaValue("Code", value);
        return this;
    }

    @Override
    public MappingOptionalConfigurationStep<T> next() {
        clickNext();
        return new MappingOptionalConfigurationStep<>(getParent());
    }


}
