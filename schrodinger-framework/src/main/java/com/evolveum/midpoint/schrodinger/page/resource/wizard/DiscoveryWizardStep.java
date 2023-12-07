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

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;


public class DiscoveryWizardStep extends PrismFormWizardStepPanel<ResourceWizardPage>
        implements NextStepAction<SchemaWizardStep>, PreviousStepAction<ConfigurationWizardStep> {
    public DiscoveryWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    /**
     * for CSV resource configuration
     */
    public DiscoveryWizardStep fieldDelimiter(String value) {
        getFormPanel().addAttributeValue("Field delimiter", value);
        return this;
    }

    /**
     * for CSV resource configuration
     */
    public DiscoveryWizardStep userPasswordAttributeName(String value) {
        getFormPanel().addAttributeValue("User password attribute name", value);
        return this;
    }

    /**
     * for CSV resource configuration
     */
    public DiscoveryWizardStep nameAttribute (String value) {
        getFormPanel().addAttributeValue("Name attribute", value);
        return this;
    }

    /**
     * for CSV resource configuration
     */
    public DiscoveryWizardStep uniqueAttributeName (String value) {
        getFormPanel().addAttributeValue("Unique attribute name", value);
        return this;
    }

    /**
     * for LDAP resource configuration
     */
    public DiscoveryWizardStep baseContext (String value) {
        getFormPanel().setDropDownAttributeValue("Base context", value);
        return this;
    }

    @Override
    public SchemaWizardStep next() {
        clickNext();
        return new SchemaWizardStep(getParent());
    }

    @Override
    public ConfigurationWizardStep back() {
        clickBack();
        return new ConfigurationWizardStep(getParent());
    }

}
