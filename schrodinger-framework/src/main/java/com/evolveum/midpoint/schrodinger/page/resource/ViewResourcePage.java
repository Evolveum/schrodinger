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

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsTab;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceConfigurationPanel;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

public class ViewResourcePage extends AssignmentHolderDetailsPage {

    public ResourceConfigurationPanel getConnectorConfigurationPanel() {
        SelenideElement element=  getNavigationPanelSelenideElement("Connector configuration")
              .waitUntil(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);

        return new ResourceConfigurationPanel(new EditResourceConfigurationPage(), element);
    }

    public ResourceWizardPage clickShowUsingWizard() {

        $(Schrodinger.byElementAttributeValue("span", "title", "Show using wizard")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        $(Schrodinger.byElementAttributeValue("form", "class", "form-horizontal"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ResourceWizardPage();
    }

    public ResourceAccountsTab<ViewResourcePage> clickAccountsTab() {

        $(Schrodinger.byDataResourceKey("schrodinger", "PageResource.tab.content.account")).parent()
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(By.className("resource-content-selection")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement tabContent = $(By.cssSelector(".tab-pane.active"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceAccountsTab<>(this, tabContent);
    }

    public ResourceAccountsTab<ViewResourcePage> clickGenericsTab() {

        $(Schrodinger.byDataResourceKey("schrodinger", "PageResource.tab.content.generic")).parent()
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(By.className("resource-content-selection")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement tabContent = $(By.cssSelector(".tab-pane.active"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceAccountsTab<>(this, tabContent);
    }

    public ViewResourcePage refreshSchema() {
        $x(".//span[@title='Refresh schema']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public PanelWithContainerWrapper<ViewResourcePage> selectConnectorConfigurationPanel() {
        return new PanelWithContainerWrapper<>(this, getNavigationPanelSelenideElement("Connector configuration"));
    }

}
