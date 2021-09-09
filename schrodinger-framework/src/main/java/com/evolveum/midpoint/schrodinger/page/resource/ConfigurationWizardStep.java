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
package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;

/**
 * Created by honchar.
 */
public class ConfigurationWizardStep extends Component<ResourceWizardPage> {
        public ConfigurationWizardStep(ResourceWizardPage parent, SelenideElement parentElement) {
            super(parent, parentElement);
        }

        public TabPanel<ConfigurationWizardStep> getTabPanel() {
            return new TabPanel<>(this, getParentElement());
        }

        public ConfigurationStepConfigurationPanel selectConfigurationTab() {
            return new ConfigurationStepConfigurationPanel(this, getTabPanel().clickTabWithName("Configuration"));
        }

        public ConfigurationStepConnectorPoolPanel selectConnectorPoolTab() {
            return new ConfigurationStepConnectorPoolPanel(this, getTabPanel().clickTabWithName("Connector pool"));
        }

        public ConfigurationStepResultsHandlersPanel selectResultsHandlerTab() {
            return new ConfigurationStepResultsHandlersPanel(this, getTabPanel().clickTabWithName("Results handlers"));
        }

        public ConfigurationStepTimeoutsPanel selectTimeoutsTab() {
            return new ConfigurationStepTimeoutsPanel(this, getTabPanel().clickTabWithName("Timeouts"));
        }
}
