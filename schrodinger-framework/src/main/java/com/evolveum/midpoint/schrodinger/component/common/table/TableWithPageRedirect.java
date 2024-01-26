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
import com.evolveum.midpoint.schrodinger.page.ObjectDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.screenshot;

/**
 * Created by matus on 5/2/2018.
 */
public abstract class TableWithPageRedirect<T, DP extends BasicPage,
        TWPR extends TableWithPageRedirect<T, DP, TWPR>> extends SelectableRowTable<T, TWPR> {

    public TableWithPageRedirect(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public abstract DP clickByName(String name);

    protected <P extends TableWithPageRedirect<T, DP, TWPR>> TableHeaderDropDownMenu<P> clickHeaderActionDropDown() {
        return null;
    }

    protected SelenideElement clickAndGetHeaderDropDownMenu() {

        SelenideElement inlineMenuPanel = getParentElement().find(Schrodinger.byDataId("div", "inlineMenuPanel"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        inlineMenuPanel.click();
        SelenideElement dropDownMenu = inlineMenuPanel.find(Schrodinger.byDataId("div", "dropDownMenu"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);

        return dropDownMenu;
    }

    protected  void clickMenu(String columnTitleKey, String rowValue, String menuItemKey) {
        clickMenuItem(columnTitleKey, rowValue, menuItemKey);
    }

    protected  <P extends TableWithPageRedirect<T, DP, TWPR>> ConfirmationModal<P> clickMenuItemWithConfirmation
            (String columnTitleKey, String rowValue, String menuItemKey) {
        clickMenuItem(columnTitleKey, rowValue, menuItemKey);
        return new ConfirmationModal<P>((P) this, Utils.getModalWindowSelenideElement());
    }

    protected  <P extends TableWithPageRedirect<T, DP, TWPR>> ConfirmationModal<P> clickButtonMenuItemWithConfirmation
            (String columnTitleKey, String rowValue, String iconClass) {
        clickMenuItemButton(columnTitleKey, rowValue, iconClass);
        return new ConfirmationModal<P>((P) this, Utils.getModalWindowSelenideElement());
    }

    protected  <P extends TableWithPageRedirect> FocusSetAssignmentsModal<P> clickMenuItemWithFocusSetAssignmentsModal
            (String columnTitleKey, String rowValue, String menuItemKey) {
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
        Utils.waitForAjaxCallFinish();
        if (columnTitleKey == null && rowValue == null) {
            SelenideElement menuItem = clickAndGetHeaderDropDownMenu()
                    .$(Schrodinger.byDescendantElementAttributeValue("a", Schrodinger.DATA_S_RESOURCE_KEY, menuItemKey))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            menuItem.click();
            menuItem.shouldBe(Condition.disappear, MidPoint.TIMEOUT_SHORT_4_S);
        } else {
            rowByColumnResourceKeyAndPartialText(columnTitleKey, rowValue)
                    .getInlineMenu()
                    .clickItemByKey(menuItemKey);

        }
        Utils.waitForAjaxCallFinish();
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
            clickHeaderInlineMenuButton(iconClass);
        } else {
            TableRow tableRow = rowByColumnResourceKey(columnTitleKey, rowValue);
            assertion.assertNotNull(tableRow, "Unable to find table row with columnTitleKey='" + columnTitleKey
                    + "' and rowValue='" + rowValue + "'; ") ;
            tableRow
                    .getInlineMenu()
                    .clickInlineMenuButtonByIconClass(iconClass);
        }
    }

    public DP newObjectButtonByCssClick(String iconCssClass){
        if (!getToolbarButtonByCss(iconCssClass).isDisplayed()) {
            getToolbarButtonByCss("fa fa-plus")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
//            Selenide.sleep(2000);
        } else {
            getToolbarButtonByCss(iconCssClass).click();
        }
        Utils.waitForAjaxCallFinish();
        try {
            Utils.getModalWindowSelenideElement();
        } catch (Error e) {
            //nothing to do here; the window appears depending on configuration
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//i[contains(@class, \"" + iconCssClass + "\")]").click();
            Utils.waitForAjaxCallFinish();
        }
        Utils.waitForMainPanelOnDetailsPage();
        return getObjectDetailsPage();
    }

    public DP newObjectButtonByTitleClick(String buttonTitle){
        if (!getToolbarButtonByTitleKey(buttonTitle).isDisplayed()) {
            getToolbarButtonByCss("fa fa-plus")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
            Selenide.sleep(2000);
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//button[@title=\"" + buttonTitle + "\"]").click();
        } else {
            getToolbarButtonByTitleKey(buttonTitle).click();
        }
        Utils.waitForMainPanelOnDetailsPage();
        return getObjectDetailsPage();
    }

    public abstract DP getObjectDetailsPage();


}
