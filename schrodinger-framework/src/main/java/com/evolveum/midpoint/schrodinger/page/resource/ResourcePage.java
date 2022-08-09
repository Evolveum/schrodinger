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
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsPanel;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceConfigurationPanel;
import com.evolveum.midpoint.schrodinger.component.resource.TestConnectionModal;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ResourcePage extends AssignmentHolderDetailsPage<ResourcePage> {

    public PanelWithContainerWrapper<ResourcePage> selectConnectorConfigurationPanel() {
        return new PanelWithContainerWrapper<>(this, getNavigationPanelSelenideElement("Connector configuration"));
    }

    public ResourcePage refreshSchema() {
        $(Schrodinger.byElementAttributeValue("a", "title", "Refresh schema"))
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

    public ResourceAccountsPanel<ResourcePage> clickGenericsTab() {

        $(Schrodinger.byDataResourceKey("schrodinger", "PageResource.tab.content.generic")).parent()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(By.className("resource-content-selection")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement tabContent = $(By.cssSelector(".tab-pane.active"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceAccountsPanel<>(this, tabContent);
    }

    public SchemaStepSchemaPanel selectSchemaPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Schema");
        return new SchemaStepSchemaPanel(this, element);
    }
}
