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
package com.evolveum.midpoint.schrodinger.component.self;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.page.self.AssignmentsListPage;
import com.evolveum.midpoint.schrodinger.page.self.RequestRolePage;

import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModalTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

/**
 * Created by honchar
 */
public class RequestRoleTab extends Component<RequestRolePage> {

    public RequestRoleTab(RequestRolePage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public Search<RequestRoleTab> search() {
        SelenideElement searchElement = getParentElement().$(By.cssSelector(".form-inline.pull-right.search-form"));
        return new Search<>(this, searchElement);
    }

    public RequestRoleItemsPanel getItemsPanel() {
        SelenideElement itemsElement = $(Schrodinger.byDataId("div", "shoppingCartItemsPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new RequestRoleItemsPanel(this, itemsElement);
    }

    public RequestRoleTab setRequestingForUser(String... userNames) {
        if (userNames == null) {
            return this;
        }
        $(Schrodinger.byDataId("userSelectionButton")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        ObjectBrowserModal<RequestRoleTab> userSelectionModal = new ObjectBrowserModal(this, Utils.getModalWindowSelenideElement());
        ObjectBrowserModalTable<RequestRoleTab, ObjectBrowserModal<RequestRoleTab>> table = userSelectionModal.table();
        if (userSelectionModal != null) {
            for (String userName : userNames) {
                table.search()
                        .byName()
                        .inputValue(userName)
                        .updateSearch()
                        .and()
                        .selectCheckboxByName(userName);
            }
            userSelectionModal.clickAddButton();
        }
        return this;
    }

    public RequestRoleTab setRelation(String relationValue) {
        $(Schrodinger.byDataId("relation")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .selectOption(relationValue);
        return this;
    }

    public RequestRoleTab addAll() {
        $(Schrodinger.byDataId("addAllButton")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public AssignmentsListPage goToShoppingCart() {
        $(Schrodinger.byDataId("goToShoppingCart")).shouldBe(Condition.visible).click();
        $(Schrodinger.byDataResourceKey("PageAssignmentsList.title")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new AssignmentsListPage();
    }
}
