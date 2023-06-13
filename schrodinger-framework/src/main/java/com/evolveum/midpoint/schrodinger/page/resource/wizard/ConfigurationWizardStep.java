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

import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ConfigurationStepConfigurationPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ConfigurationStepConnectorPoolPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ConfigurationStepResultsHandlersPanel;
import com.evolveum.midpoint.schrodinger.page.resource.ConfigurationStepTimeoutsPanel;

public class ConfigurationWizardStep extends WizardStepPanel<ResourceWizardPage>
        implements NextStepAction<DiscoveryWizardStep>, PreviousStepAction<BasicInformationWizardStep> {
    public ConfigurationWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    @Override
    public DiscoveryWizardStep next() {
        return new DiscoveryWizardStep(getParent());
    }

    @Override
    public BasicInformationWizardStep back() {
        return new BasicInformationWizardStep(getParent());
    }
 }
