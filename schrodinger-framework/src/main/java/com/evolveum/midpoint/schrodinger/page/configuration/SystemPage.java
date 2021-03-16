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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.configuration.*;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SystemPage extends AssignmentHolderDetailsPage {

    public SystemTab systemTab() {
        return new SystemTab(this, getTabSelenideElement("pageSystemConfiguration.system.title"));
    }

    public ObjectPolicyTab objectPolicyTab() {
        return new ObjectPolicyTab(this, getTabSelenideElement("pageSystemConfiguration.objectPolicy.title"));
    }

    public NotificationsTab notificationsTab() {
        return new NotificationsTab(this, getTabSelenideElement("pageSystemConfiguration.notifications.title"));
    }

    public LoggingTab loggingTab() {
        return new LoggingTab(this, getTabSelenideElement("pageSystemConfiguration.logging.title"));
    }

    public ProfilingTab profilingTab() {
        return new ProfilingTab(this, getTabSelenideElement("pageSystemConfiguration.profiling.title"));
    }

    public AdminGuiTab adminGuiTab() {
        return new AdminGuiTab(this, getTabSelenideElement("pageSystemConfiguration.adminGui.title"));
    }

    public DeploymentInformationTab deploymentInformationTab() {
        return new DeploymentInformationTab(this, getTabSelenideElement("pageSystemConfiguration.deploymentInformation.title"));
    }

    public InfrastructureTab infrastructureTab() {
        return new InfrastructureTab(this, getTabSelenideElement("pageSystemConfiguration.infrastructure.title"));
    }

    public RoleManagementTab roleManagementTab(){
        return new RoleManagementTab(this, getTabSelenideElement("pageSystemConfiguration.roleManagement.title"));
    }

    public InternalsConfigurationTab internalsConfigurationsTab(){
        return new InternalsConfigurationTab(this, getTabSelenideElement("pageSystemConfiguration.internals.title"));
    }

    public CleanupPolicyTab cleanupPolicyTab() {
        return new CleanupPolicyTab(this, getTabSelenideElement("pageSystemConfiguration.cleanupPolicy.title"));
    }

}
