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

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class ConfigurationWizardStep extends PrismFormWizardStepPanel<ResourceWizardPage>
        implements NextStepAction<DiscoveryWizardStep>, PreviousStepAction<BasicInformationWizardStep> {
    public ConfigurationWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    /**
     * for CSV resource configuration
     */
    public ConfigurationWizardStep filePath(String value) {
        getFormPanel().addAttributeValue("filePath", value);
        return this;
    }

    /**
     * should be used for LDAP resource configuration
     */
    public ConfigurationWizardStep host(String value) {
        getFormPanel().addAttributeValue("Host", value);
        return this;
    }

    /**
     * for LDAP resource configuration
     */
    public ConfigurationWizardStep port(String value) {
        getFormPanel().addAttributeValue("Port number", value);
        return this;
    }

    /**
     * for LDAP resource configuration
     */
    public ConfigurationWizardStep bindDN(String value) {
        getFormPanel().addAttributeValue("Bind DN", value);
        return this;
    }

    /**
     * for LDAP resource configuration
     */
    public ConfigurationWizardStep bindPassword(String value) {
        getFormPanel().addAttributeValue("Bind password", value);
        return this;
    }

    @Override
    public DiscoveryWizardStep next() {
        clickNext();
        return new DiscoveryWizardStep(getParent());
    }

    @Override
    public BasicInformationWizardStep back() {
        clickBack();
        return new BasicInformationWizardStep(getParent());
    }
 }
