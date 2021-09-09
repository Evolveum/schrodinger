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

    public SystemPanel systemTab() {
        return new SystemPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.system.title"));
    }

    public ObjectPolicyPanel objectPolicyTab() {
        return new ObjectPolicyPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.objectPolicy.title"));
    }

    public NotificationsPanel notificationsTab() {
        return new NotificationsPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.notifications.title"));
    }

    public LoggingPanel loggingTab() {
        return new LoggingPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.logging.title"));
    }

    public ProfilingPanel profilingTab() {
        return new ProfilingPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.profiling.title"));
    }

    public AdminGuiPanel adminGuiTab() {
        return new AdminGuiPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.adminGui.title"));
    }

    public DeploymentInformationPanel deploymentInformationTab() {
        return new DeploymentInformationPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.deploymentInformation.title"));
    }

    public InfrastructurePanel infrastructureTab() {
        return new InfrastructurePanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.infrastructure.title"));
    }

    public RoleManagementPanel roleManagementTab(){
        return new RoleManagementPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.roleManagement.title"));
    }

    public InternalsConfigurationPanel internalsConfigurationsTab(){
        return new InternalsConfigurationPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.internals.title"));
    }

    public CleanupPolicyPanel cleanupPolicyTab() {
        return new CleanupPolicyPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.cleanupPolicy.title"));
    }

}
