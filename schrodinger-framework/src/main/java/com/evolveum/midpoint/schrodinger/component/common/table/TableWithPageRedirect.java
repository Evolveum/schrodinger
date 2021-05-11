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
package com.evolveum.midpoint.schrodinger.component.common.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.common.InlineMenu;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/2/2018.
 */
public abstract class TableWithPageRedirect<T> extends Table<T> {

    public TableWithPageRedirect(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public abstract <E extends BasicPage> E clickByName(String name);

    public abstract TableWithPageRedirect<T> selectCheckboxByName(String name);

    protected abstract <P extends TableWithPageRedirect<T>> TableHeaderDropDownMenu<P> clickHeaderActionDropDown();

    protected SelenideElement clickAndGetHeaderDropDownMenu() {

        SelenideElement inlineMenuPanel = getParentElement().$x(".//div[@data-s-id='inlineMenuPanel']")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        inlineMenuPanel.click();
        SelenideElement dropDownMenu = inlineMenuPanel.$x(".//ul[@data-s-id='dropDownMenu']")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);

        return dropDownMenu;
    }

    public InlineMenu<TableWithPageRedirect<T>> getHeaderInlineMenuPanel() {
        SelenideElement element = getParentElement().find("th:last-child div.btn-group");
        if (element == null) {
            return null;
        }

        return new InlineMenu<>(this, element);
    }

    protected  void clickMenu(String columnTitleKey, String rowValue, String menuItemKey) {
        clickMenuItem(columnTitleKey, rowValue, menuItemKey);
    }

    protected  <P extends TableWithPageRedirect<T>> ConfirmationModal<P> clickMenuItemWithConfirmation(String columnTitleKey, String rowValue, String menuItemKey) {
        clickMenuItem(columnTitleKey, rowValue, menuItemKey);
        return new ConfirmationModal<P>((P) this, Utils.getModalWindowSelenideElement(MidPoint.TIMEOUT_SHORT_4_S));
    }

    protected  <P extends TableWithPageRedirect<T>> ConfirmationModal<P> clickButtonMenuItemWithConfirmation(String columnTitleKey, String rowValue, String iconClass) {
        clickMenuItemButton(columnTitleKey, rowValue, iconClass);
        return new ConfirmationModal<P>((P) this, Utils.getModalWindowSelenideElement(MidPoint.TIMEOUT_SHORT_4_S));
    }

    protected  <P extends TableWithPageRedirect> FocusSetAssignmentsModal<P> clickMenuItemWithFocusSetAssignmentsModal(String columnTitleKey, String rowValue, String menuItemKey) {
        clickMenuItem(columnTitleKey, rowValue, menuItemKey);
        return new FocusSetAssignmentsModal<P>((P) this, Utils.getModalWindowSelenideElement());
    }

    /**
     * click menu item for the row specified by columnTitleKey and rowValue
     * or click menu item from header menu drop down if no row is specified
     * @param columnTitleKey
     * @param rowValue
     * @param menuItemKey
     */
    private void clickMenuItem(String columnTitleKey, String rowValue, String menuItemKey){
        if (columnTitleKey == null && rowValue == null) {
            SelenideElement menuItem = clickAndGetHeaderDropDownMenu()
                    .$(Schrodinger.byDescendantElementAttributeValue("a", Schrodinger.DATA_S_RESOURCE_KEY, menuItemKey))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            menuItem.click();
            menuItem.waitUntil(Condition.disappears, MidPoint.TIMEOUT_SHORT_4_S);
        } else {
            rowByColumnResourceKey(columnTitleKey, rowValue)
                    .getInlineMenu()
                    .clickItemByKey(menuItemKey);

        }
    }

    /**
     * click button menu for the row specified by columnTitleKey and rowValue
     * or click button menu from header if no row is specified
     * @param columnTitleKey
     * @param rowValue
     * @param iconClass
     */
    public void clickMenuItemButton(String columnTitleKey, String rowValue, String iconClass){
        if (columnTitleKey == null && rowValue == null) {
            getHeaderInlineMenuPanel()
                    .clickInlineMenuButtonByIconClass(iconClass);
        } else {
            TableRow tableRow = rowByColumnResourceKey(columnTitleKey, rowValue);
            if (tableRow == null) {
                Selenide.screenshot("tableRowIsNull");
                return;
            }
            tableRow
                    .getInlineMenu()
                    .clickInlineMenuButtonByIconClass(iconClass);
        }
    }

}
