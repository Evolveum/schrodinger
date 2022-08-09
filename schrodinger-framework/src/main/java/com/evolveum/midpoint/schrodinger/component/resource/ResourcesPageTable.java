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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class ResourcesPageTable extends AssignmentHolderObjectListTable<ListResourcesPage, ResourcePage> {

    public ResourcesPageTable(ListResourcesPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<ResourcesPageTable> clickHeaderActionDropDown() {
        return null;
    }

    @Override
    public ResourcePage clickByName(String name) {
        Utils.waitForAjaxCallFinish();
        getParentElement().$(Schrodinger.byElementValue("span", Schrodinger.DATA_S_ID, "label", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        ResourcePage detailsPage = new ResourcePage();
        //TODO what is this?
        if (detailsPage.isUseTabbedPanel()) {
            detailsPage.getTabPanel();
        } else {
            detailsPage.getNavigationPanel();
        }
        return new ResourcePage();
    }

    @Override
    public ResourcePage getObjectDetailsPage() {
        return new ResourcePage();
    }
}
