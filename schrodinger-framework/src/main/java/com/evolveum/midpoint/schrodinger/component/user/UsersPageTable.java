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

package com.evolveum.midpoint.schrodinger.component.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UsersPageTable extends AssignmentHolderObjectListTable<ListUsersPage, UserPage> {

    public UsersPageTable(ListUsersPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<UsersPageTable> clickHeaderActionDropDown() {

        $(Schrodinger.bySelfOrAncestorElementAttributeValue("button", "data-toggle", "dropdown", "class", "sortableLabel"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelenideElement dropDown = $(Schrodinger.byDataId("ul", "dropDownMenu"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<UsersPageTable>(this, dropDown);

    }

    public ConfirmationModal<UsersPageTable> enableUser() {
        return enableUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> enableUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.enable");
    }

    public ConfirmationModal<UsersPageTable> disableUser() {
        return disableUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> disableUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.disable");
    }

    public ConfirmationModal<UsersPageTable> reconcileUser() {
        return reconcileUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> reconcileUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.reconcile");
    }

    public ConfirmationModal<UsersPageTable> unlockUser() {
        return unlockUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> unlockUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageUsers.menu.unlock");
    }

    public ConfirmationModal<UsersPageTable> deleteUser() {
        return deleteUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> deleteUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "FocusListInlineMenuHelper.menu.delete");
    }

    public ConfirmationModal<UsersPageTable> mergeUser() {
        return mergeUser(null, null);
    }

    public ConfirmationModal<UsersPageTable> mergeUser(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageUsers.menu.merge");
    }

    @Override
    public UserPage getObjectDetailsPage(){
        return new UserPage();
    }

    public UsersPageTable assertButtonToolBarExists() {
        assertion.assertTrue($(Schrodinger.byDataId("buttonToolbar")).exists(), "Button toolbar is absent");
        return this;
    }
}
