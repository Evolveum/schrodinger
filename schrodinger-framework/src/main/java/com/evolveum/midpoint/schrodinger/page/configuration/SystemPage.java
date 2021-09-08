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
package com.evolveum.midpoint.schrodinger.page.configuration;

import com.evolveum.midpoint.schrodinger.component.configuration.*;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SystemPage extends AssignmentHolderDetailsPage {

    public SystemTab systemTab() {
        return new SystemTab(this, getTabSelenideElement("pageSystemConfiguration.system.title"));
    }

    public ObjectPolicyPanel objectPolicyTab() {
        return new ObjectPolicyPanel(this, getTabSelenideElement("pageSystemConfiguration.objectPolicy.title"));
    }

    public NotificationsPanel notificationsTab() {
        return new NotificationsPanel(this, getTabSelenideElement("pageSystemConfiguration.notifications.title"));
    }

    public LoggingPanel loggingTab() {
        return new LoggingPanel(this, getTabSelenideElement("pageSystemConfiguration.logging.title"));
    }

    public ProfilingPanel profilingTab() {
        return new ProfilingPanel(this, getTabSelenideElement("pageSystemConfiguration.profiling.title"));
    }

    public AdminGuiPanel adminGuiTab() {
        return new AdminGuiPanel(this, getTabSelenideElement("pageSystemConfiguration.adminGui.title"));
    }

    public DeploymentInformationPanel deploymentInformationTab() {
        return new DeploymentInformationPanel(this, getTabSelenideElement("pageSystemConfiguration.deploymentInformation.title"));
    }

    public InfrastructurePanel infrastructureTab() {
        return new InfrastructurePanel(this, getTabSelenideElement("pageSystemConfiguration.infrastructure.title"));
    }

    public RoleManagementPanel roleManagementTab(){
        return new RoleManagementPanel(this, getTabSelenideElement("pageSystemConfiguration.roleManagement.title"));
    }

    public InternalsConfigurationPanel internalsConfigurationsTab(){
        return new InternalsConfigurationPanel(this, getTabSelenideElement("pageSystemConfiguration.internals.title"));
    }

    public CleanupPolicyPanel cleanupPolicyTab() {
        return new CleanupPolicyPanel(this, getTabSelenideElement("pageSystemConfiguration.cleanupPolicy.title"));
    }

}
