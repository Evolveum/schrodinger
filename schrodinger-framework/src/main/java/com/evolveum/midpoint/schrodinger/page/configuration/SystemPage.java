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
import com.evolveum.midpoint.schrodinger.component.configuration.*;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SystemPage extends AssignmentHolderDetailsPage {

    public SystemPage() {
        super(true);
    }

    public SystemPanel systemPanel() {
        return new SystemPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.system.title"));
    }

    public ObjectPolicyPanel objectPolicyPanel() {
        return new ObjectPolicyPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.objectPolicy.title"));
    }

    public NotificationsPanel notificationsPanel() {
        return new NotificationsPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.notifications.title"));
    }

    public LoggingPanel loggingPanel() {
        return new LoggingPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.logging.title"));
    }

    public ProfilingPanel profilingPanel() {
        return new ProfilingPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.profiling.title"));
    }

    public AdminGuiPanel adminGuiPanel() {
        return new AdminGuiPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.adminGui.title"));
    }

    public DeploymentInformationPanel deploymentInformationPanel() {
        return new DeploymentInformationPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.deploymentInformation.title"));
    }

    public InfrastructurePanel infrastructurePanel() {
        return new InfrastructurePanel(this, getNavigationPanelSelenideElement("Infrastructure"));
    }

    public RoleManagementPanel roleManagementPanel(){
        return new RoleManagementPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.roleManagement.title"));
    }

    public InternalsConfigurationPanel internalsConfigurationsPanel(){
        return new InternalsConfigurationPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.internals.title"));
    }

    public CleanupPolicyPanel cleanupPolicyPanel() {
        return new CleanupPolicyPanel(this, getNavigationPanelSelenideElement("pageSystemConfiguration.cleanupPolicy.title"));
    }

    public SystemPage panelSelectionButtonClick(String panelTitle) {
        Utils.waitForAjaxCallFinish();
        $x(".//span[@class='compositedButtonLabel' and contains(text(), '" + panelTitle + "')]")
                .shouldBe(Condition.exist, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

}
