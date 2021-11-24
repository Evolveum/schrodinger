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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceConfigurationPanel;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceConnectorPoolTab;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceResultsHandlersTab;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceTimeoutsTab;
import com.evolveum.midpoint.schrodinger.component.resource.TestConnectionModal;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import io.github.classgraph.ResourceList;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class EditResourceConfigurationPage extends BasicPage {

    private static final String CONFIGURATION_TAB_NAME = "Configuration";
    private static final String CONNECTORPOOL_TAB_NAME = "Connector pool";
    private static final String TIMEOUTS_TAB_NAME = "Timeouts";
    private static final String RESULTHANDLERS_TAB_NAME = "Results handlers";

    private TabPanel findTabPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "tabs-container"));
        return new TabPanel<>(this, tabPanelElement);
    }

    public ResourceConfigurationPanel selectTabconfiguration() {
        SelenideElement element = findTabPanel().clickTabWithName(CONFIGURATION_TAB_NAME);

        return new ResourceConfigurationPanel(this, element);
    }

    public ResourceConnectorPoolTab selectTabConnectorPool() {
        SelenideElement element = findTabPanel().clickTabWithName(CONNECTORPOOL_TAB_NAME);

        return new ResourceConnectorPoolTab(this, element);
    }

    public ResourceTimeoutsTab selectTabTimeouts() {
        SelenideElement element = findTabPanel().clickTabWithName(TIMEOUTS_TAB_NAME);

        return new ResourceTimeoutsTab(this, element);
    }

    public ResourceResultsHandlersTab selectTabResultHandlers() {
        SelenideElement element = findTabPanel().clickTabWithName(RESULTHANDLERS_TAB_NAME);

        return new ResourceResultsHandlersTab(this, element);
    }

    public ListResourcesPage clickSave() {
        $x(".//i[contains(@class,\"fa fa-save\")]")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        return new ListResourcesPage();
    }

    public TestConnectionModal<EditResourceConfigurationPage> clickTestConnection() {
        $(Schrodinger.byElementAttributeValue("span", "title", "Test connection")).waitUntil(Condition.visible, MidPoint.TIMEOUT_EXTRA_LONG_10_M).click();
//        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        return new TestConnectionModal<>(this, Utils.getModalWindowSelenideElement(MidPoint.TIMEOUT_LONG_1_M));
    }


}
