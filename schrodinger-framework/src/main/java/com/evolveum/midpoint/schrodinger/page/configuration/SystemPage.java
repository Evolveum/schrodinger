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
    }

    public SystemPanel systemPanel() {
        panelSelectionButtonClick("System configuration");
        return new SystemPanel(this, getNavigationPanelSelenideElement("System"));
    }

    public ObjectPolicyPanel objectPolicyPanel() {
        panelSelectionButtonClick("Policies");
        return new ObjectPolicyPanel(this, getNavigationPanelSelenideElement("Object policies"));
    }

    public NotificationsPanel notificationsPanel() {
        panelSelectionButtonClick("Notifications");
        return new NotificationsPanel(this, getNavigationPanelSelenideElement("Notifications"));
    }

    public LoggingPanel loggingPanel() {
        panelSelectionButtonClick("Logging");
        return new LoggingPanel(this, getNavigationPanelSelenideElement("Logging"));
    }

    public ProfilingPanel profilingPanel() {
        panelSelectionButtonClick("Profiling");
        return new ProfilingPanel(this, getNavigationPanelSelenideElement("Profiling"));
    }

    public AdminGuiPanel adminGuiPanel() {
        panelSelectionButtonClick("Admin GUI configuration");
        return new AdminGuiPanel(this, getNavigationPanelSelenideElement("Basic"));
    }

    public DeploymentInformationPanel deploymentInformationPanel() {
        panelSelectionButtonClick("System configuration");
        return new DeploymentInformationPanel(this, getNavigationPanelSelenideElement("Deployment"));
    }

    public InfrastructurePanel infrastructurePanel() {
        panelSelectionButtonClick("System configuration");
        return new InfrastructurePanel(this, getNavigationPanelSelenideElement("Infrastructure"));
    }

    public RoleManagementPanel roleManagementPanel(){
        panelSelectionButtonClick("Role management");
        return new RoleManagementPanel(this, getNavigationPanelSelenideElement("Basic"));
    }

    public InternalsConfigurationPanel internalsConfigurationsPanel(){
        return new InternalsConfigurationPanel(this, getNavigationPanelSelenideElement("Internals configuration"));
    }

    public CleanupPolicyPanel cleanupPolicyPanel() {
        panelSelectionButtonClick("Policies");
        return new CleanupPolicyPanel(this, getNavigationPanelSelenideElement("Cleanup policy"));
    }

    private SystemPage panelSelectionButtonClick(String panelTitle) {
        Utils.waitForAjaxCallFinish();
        String translatedTitle = Utils.getPropertyString(panelTitle);
        $x(".//span[@class='compositedButtonLabel' and contains(text(), '" + translatedTitle + "')]")
                .shouldBe(Condition.exist, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

}
