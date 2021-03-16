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
package com.evolveum.midpoint.schrodinger.page.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.self.RequestRoleTab;
import com.evolveum.midpoint.schrodinger.component.self.RoleCatalogViewTab;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class RequestRolePage extends BasicPage {

    public RoleCatalogViewTab selectRoleCatalogViewTab(){
        SelenideElement tabElement = getTabPanel().clickTab("AssignmentViewType.ROLE_CATALOG_VIEW");
        return new RoleCatalogViewTab(this, tabElement);
    }

    public RequestRoleTab selectAllRolesViewTab(){
        SelenideElement tabElement = getTabPanel().clickTab("AssignmentViewType.ROLE_TYPE");
        return new RequestRoleTab(this, tabElement);
    }

    public RequestRoleTab selectAllOrganizationsViewTab(){
        SelenideElement tabElement = getTabPanel().clickTab("AssignmentViewType.ORG_TYPE");
        return new RequestRoleTab(this, tabElement);
    }

    public RequestRoleTab selectAllServicesViewTab(){
        SelenideElement tabElement = getTabPanel().clickTab("AssignmentViewType.SERVICE_TYPE");
        return new RequestRoleTab(this, tabElement);
    }

    public RequestRoleTab selectUserAssignmentsTab(){
        SelenideElement tabElement = getTabPanel().clickTab("AssignmentViewType.USER_TYPE");
        return new RequestRoleTab(this, tabElement);
    }

    private TabPanel<RequestRolePage> getTabPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "viewsTabPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new TabPanel<>(this, tabPanelElement);
    }

}
