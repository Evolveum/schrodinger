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
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;

import com.evolveum.midpoint.schrodinger.component.common.InputBox;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.InlineMenu;

/**
 * Created by Viliam Repan (lazyman).
 */
public class TableRow<X, T extends Table> extends Component<T, TableRow<X, T>> {

    public TableRow(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TableRow<X, T> clickCheckBox() {
        getParentElement().find("input[type=checkbox]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public TableRow<X, T> clickCheckBoxByColumnName(String columnName) {
        int index = getParent().findColumnByLabel(columnName);
        if (index < 0) {
            return null;
        }
        getParentElement().$(By.cssSelector("td:nth-child(" + index + ") checkbox"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public TableRow<X, T> clickColumnByName(String name) {
        return clickColumnByName(name, "a");
    }

    public TableRow<X, T> clickColumnByName(String name, String cellElementTagName) {
        int index = getParent().findColumnByLabel(name);
        if (index < 0) {
            return this;
        }
        SelenideElement a = getParentElement().$(By.cssSelector("td:nth-child(" + index + ") " + cellElementTagName));
        a.click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public TableRow<X, T> setTextToInputFieldByColumnName(String columnName, String textValue) {
        int index = getParent().findColumnByLabel(columnName);
        if (index < 0) {
            return this;
        }
        SelenideElement input = getParentElement().$(By.cssSelector("td:nth-child(" + index + ") input"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        InputBox inputBox = new InputBox(this, input);
        inputBox.inputValue(textValue);
        return this;
    }

    public TableRow<X, T> setValueToDropdownFieldByColumnName(String columnName, String textValue) {
        int index = getParent().findColumnByLabel(columnName);
        if (index < 0) {
            return this;
        }
        SelenideElement select = getParentElement().$(By.cssSelector("td:nth-child(" + index + ") select"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        select.selectOption(textValue);
        return this;
    }

    public TableRow<X, T> setValueToDropDownByColumnName(String columnName, String selectValue) {
        int index = getParent().findColumnByLabel(columnName);
        if (index < 0) {
            return this;
        }
        getParentElement().$(By.cssSelector("td:nth-child(" + index + ") select"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).selectOptionContainingText(selectValue);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public SelenideElement getColumnCellElementByColumnName(String columnName) {
        int index = getParent().findColumnByLabel(columnName);
        if (index < 0) {
            return null;
        }
        SelenideElement cell = getParentElement().$(By.cssSelector("td:nth-child(" + index + ") div"));
        //todo try to comment for now; may be visibility is not needed
//        Utils.scrollToElement(cell);
//        cell.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return cell;
    }

    public String getColumnCellTextByColumnName(String columnName) {
        SelenideElement cell = getColumnCellElementByColumnName(columnName);
        String value = cell.getText();
        if (cell.$(By.tagName("input")).isDisplayed()) {
            value = cell.$(By.tagName("input")).getText();
        }
        if (cell.$(By.tagName("select")).isDisplayed()) {
            value = cell.$(By.tagName("select")).getText();
        }
        return value;
    }

    public TableRow<X, T> clickColumnByKey(String key) {
        // todo implement
        return this;
    }

    public InlineMenu<TableRow<X, T>> getInlineMenu() {
        SelenideElement element = getParentElement().find("td:last-child div.btn-group");
        if (element == null) {
            return null;
        }

        return new InlineMenu<>(this, element);
    }
}
