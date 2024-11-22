/*
 * Copyright (c) 2022 Evolveum
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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.resource.*;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ResourcePage extends AssignmentHolderDetailsPage<ResourcePage> {

    public ConfigurationWizardStep<ResourcePage> selectConnectorConfigurationPanel() {
        return new ConfigurationWizardStep<>(this, getNavigationPanelSelenideElement("Connector configuration"));
    }

    public ResourcePage refreshSchema() {
        $x(".//a[contains(text(), 'Refresh schema')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public ResourceConfigurationPanel getConnectorConfigurationPanel() {
        SelenideElement element=  getNavigationPanelSelenideElement("Connector configuration")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);

        return new ResourceConfigurationPanel(new EditResourceConfigurationPage(), element);
    }

    public TestConnectionModal<ResourcePage> clickTestConnection() {
        $(Schrodinger.byElementAttributeValue("a", "title", "Test connection")).shouldBe(Condition.visible, MidPoint.TIMEOUT_EXTRA_LONG_10_M).click();
//        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        return new TestConnectionModal<>(this, Utils.getModalWindowSelenideElement(MidPoint.TIMEOUT_LONG_1_M));
    }

    public ResourceAccountsPanel<ResourcePage> selectAccountsPanel() {
        SelenideElement tabContent = getNavigationPanelSelenideElement("Accounts")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceAccountsPanel<>(this, tabContent);
    }

    public ResourceDetailsPanel<ResourcePage> selectDetailsPanel() {
        SelenideElement tabContent = getNavigationPanelSelenideElement("Details")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceDetailsPanel<>(this, tabContent);
    }

    public ResourceObjectsPanel<ResourcePage> selectResourceObjectsPanel() {
        SelenideElement tabContent = getNavigationPanelSelenideElement("Resource objects")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceObjectsPanel<>(this, tabContent);
    }

    public ResourceAccountsPanel<ResourcePage> clickGenericsTab() {

        $(Schrodinger.byDataResourceKey("schrodinger", "PageResource.tab.content.generic")).parent()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(By.className("resource-content-selection")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement tabContent = $(By.cssSelector(".tab-pane.active"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceAccountsPanel<>(this, tabContent);
    }

    public SchemaHandlingPanel selectSchemaHandlingPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Schema handling");
        return new SchemaHandlingPanel(this, element);
    }

    public DefinedTasksPanel selectDefinedTasksPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Defined Tasks");
        return new DefinedTasksPanel(this, element);
    }

    //was removed in mp v4.8
//    public ResourcePage switchToDevelopmentMode() {
//        try {
//            String switchToDevelopmentKey = "OperationalButtonsPanel.button.toggleToDevelopment";
//            String title = Utils.translate(switchToDevelopmentKey);
//            SelenideElement button = $(Schrodinger.byElementAttributeValue("a", "title", title))
//                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
//            if (button.exists()) {
//                button.click();
//                Utils.waitForAjaxCallFinish();
//            }
//        } catch (Exception e) {
//            //nothing to do here, we are already in development mode
//        }
//        return ResourcePage.this;
//    }

    public ResourcePage switchToProductionMode() {
        try {
            String switchToProductionKey = "OperationalButtonsPanel.button.toggleToProduction";
            String title = Utils.translate(switchToProductionKey);
            SelenideElement button = $(Schrodinger.byElementAttributeValue("a", "title", title))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            if (button.exists()) {
                button.click();
                Utils.waitForAjaxCallFinish();
            }
        } catch (Exception e) {
            //nothing to do here, we are already in production mode
        }
        return ResourcePage.this;
    }

    public ResourcePage setLifecycleState(String value) {
        SelenideElement lifecycleStatePanel = $(Schrodinger.byDataId("lifecycleStatePanel"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        lifecycleStatePanel.$(Schrodinger.byDataId("select", "panel")).selectOption(value);
        Utils.waitForAjaxCallFinish();
        assertFeedbackExists();
        feedback().assertMessageExists("Change resource lifecycle state (GUI)");
        return ResourcePage.this;
    }
}
