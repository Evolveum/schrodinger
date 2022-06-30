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

package com.evolveum.midpoint.schrodinger.page.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.role.RolesPageTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class ServicesPageTable extends AssignmentHolderObjectListTable<ListServicesPage, ServicePage> {

    public ServicesPageTable(ListServicesPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<ServicesPageTable> clickHeaderActionDropDown() {
        //todo looks like the same code for all tables
        $(Schrodinger.bySelfOrAncestorElementAttributeValue("button", "data-toggle", "dropdown", "class", "sortableLabel"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelenideElement dropDown = $(Schrodinger.byDataId("ul", "dropDownMenu"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<ServicesPageTable>(this, dropDown);
    }

    @Override
    public ServicePage getObjectDetailsPage(){
        return new ServicePage();
    }

    public ConfirmationModal<ServicesPageTable> enableService() {
        return enableService(null, null);
    }

    public ConfirmationModal<ServicesPageTable> enableServiceByName(String nameValue) {
        return enableService("ObjectType.name", nameValue);
    }

    public ConfirmationModal<ServicesPageTable> enableService(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.enable");
    }

    public ConfirmationModal<ServicesPageTable> disableService() {
        return disableService(null, null);
    }

    public ConfirmationModal<ServicesPageTable> disableServiceByName(String nameValue) {
        return disableService("ObjectType.name", nameValue);
    }

    public ConfirmationModal<ServicesPageTable> disableService(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.disable");
    }

    public ConfirmationModal<ServicesPageTable> reconcileService() {
        return reconcileService(null, null);
    }

    public ConfirmationModal<ServicesPageTable> reconcileServiceByName(String nameValue) {
        return reconcileService("ObjectType.name", nameValue);
    }

    public ConfirmationModal<ServicesPageTable> reconcileService(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.reconcile");
    }

    public ConfirmationModal<ServicesPageTable> deleteService() {
        return deleteService(null, null);
    }

    public ConfirmationModal<ServicesPageTable> deleteServiceByName(String nameValue) {
        return deleteService("ObjectType.name", nameValue);
    }

    public ConfirmationModal<ServicesPageTable> deleteService(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.delete");
    }
}
