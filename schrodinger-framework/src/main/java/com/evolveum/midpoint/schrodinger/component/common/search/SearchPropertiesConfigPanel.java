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
package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;

public class SearchPropertiesConfigPanel<T> extends Component<T, SearchPropertiesConfigPanel<T>> {

    public SearchPropertiesConfigPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SearchPropertiesConfigPanel<T> addPropertyToTable(String propertyName) {
        Utils.waitForAjaxCallFinish();
        getPropertyChoiceElement().selectOption(propertyName);
        Utils.waitForAjaxCallFinish();
        getParentElement().$(Schrodinger.byDataId("a", "addButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getPropertiesTable().getParentElement().$(byText(propertyName)).shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        return this;
    }

    public SearchPropertiesConfigPanel<T> removePropertyFromTable(String propertyName) {
        TableRow rowToDelete = getPropertiesTable().rowByColumnResourceKey("SearchPropertiesConfigPanel.table.column.property", propertyName);
        rowToDelete.getInlineMenu().clickInlineMenuButtonByTitle("Delete");
        getPropertiesTable().getParentElement().$(byText(propertyName)).shouldBe(Condition.disappear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    private SelenideElement getPropertyChoiceElement(){
        return getParentElement().$(Schrodinger.bySelfOrAncestorElementAttributeValue("select",
                "class", "form-control form-control-sm", "data-s-id", "propertyChoice"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public Table<SearchPropertiesConfigPanel> getPropertiesTable() {
        SelenideElement tableElement = getParentElement().$(By.tagName("table")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        Table propertiesTable = new Table(this, tableElement);
        return propertiesTable;
    }

    public SearchPropertiesConfigPanel<T> setPropertyTextValue(String propertyName, String value, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        if (propertyRow != null) {
            propertyRow.setTextToInputFieldByColumnName("Value", value);
        }
        return this;
    }

    public SearchPropertiesConfigPanel<T> setPropertyObjectReferenceValue(String propertyName, String objectReferenceOid, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        if (propertyRow != null) {
            ReferenceSearchItemPanel<SearchPropertiesConfigPanel> refConfigPanel =
                    new ReferenceSearchItemPanel<>(this, propertyRow.getColumnCellElementByColumnName("Value"));
            refConfigPanel
                    .propertySettings()
                    .inputRefOid(objectReferenceOid)
                    .applyButtonClick();
        }
        return this;
    }

    public SearchPropertiesConfigPanel<T> setPropertyDropdownValue(String propertyName, String value, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        if (propertyRow != null) {
            propertyRow.setValueToDropdownFieldByColumnName("Value", value);
        }
        return this;
    }

    public SearchPropertiesConfigPanel<T> setPropertyFilterValue(String propertyName, String filterValue, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        propertyRow.setValueToDropDownByColumnName("Filter", filterValue);
        return this;
    }

    public SearchPropertiesConfigPanel<T> setPropertyMatchingRuleValue(String propertyName, String filterValue, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        propertyRow.setValueToDropDownByColumnName("Filter", filterValue);
        return this;
    }

    public SearchPropertiesConfigPanel<T> clickNegotiateCheckbox(String propertyName, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getTableRowForProperty(propertyName, addPropertyIfAbsent);
        propertyRow.clickCheckBoxByColumnName("Negotiate");
        return this;
    }

    public T confirmConfiguration() {
        getParentElement().$(Schrodinger.byDataId("okButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.isModalWindowSelenideElementVisible();
        return getParent();
    }

    private TableRow getTableRowForProperty(String propertyName, boolean addPropertyIfAbsent) {
        TableRow propertyRow = getPropertiesTable().findRowByColumnLabel("Property", propertyName);
        if (propertyRow == null && !addPropertyIfAbsent) {
            return null;
        }
        if (propertyRow == null && addPropertyIfAbsent) {
            addPropertyToTable(propertyName);
            propertyRow = getPropertiesTable().findRowByColumnLabel("Property", propertyName);
        }
        return propertyRow;
    }
}
