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

import static com.codeborne.selenide.Selectors.byPartialLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Table<T> extends Component<T> {

    public Table(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TableRow<T, Table<T>> findRowByColumnLabel(String label, String rowValue) {
        int index = findColumnByLabel(label);
        if (index < 0) {
            Selenide.screenshot("rowByColumnLabel_returns_null_" + System.currentTimeMillis());
            return null;
        }
        TableRow<T, Table<T>> tableRow = getTableRowByIndex(index, rowValue);
        if (tableRow == null) {
            Selenide.screenshot("rowByColumnLabel_returns_null_" + System.currentTimeMillis());
        }
        return tableRow;
    }

    public List<TableRow<T, Table<T>>> findAllRowsByColumnLabel(String label, String rowValue) {
        int index = findColumnByLabel(label);
        return getTableRowsByIndex(index, rowValue);
    }

    public int findColumnByLabel(String label) {
        ElementsCollection headers = getParentElement().findAll("thead th[data-s-id=header]");
        if (headers == null) {
            return -1;
        }
        int index = 1;
        for (SelenideElement header : headers) {
            SelenideElement hasHeader = header.find(Schrodinger.byDataId("span", "label"));
            SelenideElement hasSearchableHeader = header.find(Schrodinger.bySchrodingerDataId("label"));
            String value = null;
            if (hasHeader.exists()) {
                value = hasHeader.text();
            } else if (hasSearchableHeader.exists()) {
                value = hasSearchableHeader.parent().text();
            }

            if (value == null) {
                index++;
                continue;
            }

            if (Objects.equals(label, value)) {
                break;
            }
            index++;
        }
        if (index > headers.size()) {
            return -1;
        }
        return index;
    }

    public int findColumnByResourceKey(String key) {
        ElementsCollection headers = getParentElement().findAll(Schrodinger.byDataId("th", "header"));
        if (headers == null) {
            return -1;
        }
        int index = 1;

        for (SelenideElement header : headers) {
            SelenideElement headerElement = header.find((Schrodinger.byDataResourceKey(key)));
            if (headerElement.exists()) {
                return index;
            }
            headerElement = header.$(byText(key));
            if (headerElement.exists()) {
                return index;
            }
            index++;
        }
        if (index > headers.size()) {
            return -1;
        }
        return index;
    }

    public TableRow<T, Table<T>> rowByColumnResourceKey(String key, String rowValue) {
        int index = findColumnByResourceKey(key);
        if (index < 0) {
            return null;
        }
        return getTableRowByIndex(index, rowValue);
    }

    public List<TableRow<T, Table<T>>> allRowsByColumnResourceKey(String key, String rowValue) {
        int index = findColumnByResourceKey(key);
        if (index < 0) {
            return null;
        }
        return getTableRowsByIndex(index, rowValue);
    }

    private TableRow<T, Table<T>> getTableRowByIndex(int index, String rowValue) {
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        for (SelenideElement row : rows) {
            String value = row.find("td:nth-child(" + index + ")").text();
            if (value == null) {
                continue;
            }
            value = value.trim();

            if (Objects.equals(rowValue, value)) {
                return new TableRow(this, row);
            }
        }
        return null;
    }

    private List<TableRow<T, Table<T>>> getTableRowsByIndex(int index, String rowValue) {
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        List<TableRow<T, Table<T>>> result = new ArrayList<>();
        for (SelenideElement row : rows) {
            String value = row.find("td:nth-child(" + index + ")").text();
            if (value == null) {
                continue;
            }
            value = value.trim();

            if (Objects.equals(rowValue, value)) {
                result.add(new TableRow<>(this, row));
            }
        }
        return result;
    }

    public int calculateAllIntValuesInColumn(String columnName) {
        int index = findColumnByLabel(columnName);
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        int sum = 0;
        for (SelenideElement row : rows) {
            String value = row.find("td:nth-child(" + index + ")").text();
            if (value == null) {
                continue;
            }
            try {
                sum += Integer.parseInt(value);
            } catch (NumberFormatException e) {
                //just skipp
            }
        }
        return sum;
    }

    public String getTableCellValue(String columnResourceKey, int rowIndex) {
        SelenideElement element = getTableCellElement(columnResourceKey, rowIndex);
        if (element == null) {
            return null;
        }
        return element.getText();
    }

    protected SelenideElement getTableCellElement(String columnResourceKey, int rowIndex) {
        int columnIndex = findColumnByResourceKey(columnResourceKey);
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        if (rowIndex > rows.size()) {
            return null;
        }
        SelenideElement element = rows.get(rowIndex -1).find("td:nth-child(" + (columnIndex) + ")");
        if (!element.exists()) {
            return null;
        }
        return element;
    }

    public Search<? extends Table<T>> search() {
        SelenideElement searchElement = getParentElement().$(By.cssSelector(".search-panel-form"));
        return new Search<>(this, searchElement);
    }

    public <P extends Table<T>> Paging<P> paging() {
        SelenideElement pagingElement = getParentElement().$x(".//div[@ " + Schrodinger.DATA_S_ID + "='paging']");

        return new Paging(this, pagingElement);
    }

    public Table<T> selectAll() {

        $(Schrodinger.bySelfOrAncestorElementAttributeValue("input", "type", "checkbox", "data-s-id", "topToolbars"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public boolean currentTableContains(String elementValue) {
        return currentTableContains("Span", elementValue);
    }

    public boolean currentTableContains(String elementName, String elementValue) {

        // TODO replate catch Throwable with some less generic error
        try {
            return $(Schrodinger.byElementValue(elementName, elementValue)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).is(Condition.visible);
        } catch (Throwable t) {
            return false;
        }

    }

    public boolean containsText(String value) {
        return getParentElement().$(byText(value)).is(Condition.visible);
    }

    public boolean containsLinkTextPartially(String value) {
        return $(byPartialLinkText(value)).is(Condition.visible);
    }

    public boolean containsLinksTextPartially(String... values) {
        for (String value : values) {
            if (!containsLinkTextPartially(value)) {
                return false;
            }
        }
        return true;
    }

    public SelenideElement getButtonToolbar() {
        return $(Schrodinger.byDataId("buttonToolbar"));
    }

    public SelenideElement getToolbarButtonByTitleKey(String titleKey){
        String title = Utils.translate(titleKey);
        return getToolbarButtonByTitle(title);
    }

    public SelenideElement getToolbarButtonByTitle(String buttonTitle){
        SelenideElement el = getButtonToolbar().$x(".//a[@title='" + buttonTitle + "']");
        if (el.exists() && el.isDisplayed()) {
            return el;
        }
        return getButtonToolbar().$x(".//button[@title='" + buttonTitle + "']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public SelenideElement getToolbarButtonByCss(String iconCssClass){
        return getButtonToolbar().$x(".//i[contains(@class,\"" + iconCssClass + "\")]");
    }

    public int countTableObjects() {
        String countStringValue = $(Schrodinger.bySelfOrAncestorElementAttributeValue("span", "class", "align-middle", Schrodinger.DATA_S_ID, "count"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).text();
        if (countStringValue == null) {
            return 0;
        }
        if (countStringValue.equals("No matching result found.")) {
            return 0;
        }
        int lastSpaceIndex = countStringValue.lastIndexOf(" ");
        if (lastSpaceIndex < 0) {
            return 0;
        }
        return Integer.parseInt(countStringValue.substring(lastSpaceIndex + 1));
    }

    public Table<T> assertColumnIndexMatches(String columnLabel, int expectedIndex) {
        assertion.assertEquals(findColumnByLabel(columnLabel), expectedIndex, "'" + columnLabel + "' column index doesn't match to " + expectedIndex);
        return this;
    }

    public Table<T> assertTableRowExists(String columnLabel, String rowValue) {
        assertion.assertNotNull(findRowByColumnLabel(columnLabel, rowValue), "Row with value " + rowValue + " in " + columnLabel + " column doesn't exist.");
        return this;
    }

    public Table<T> assertTableObjectsCountEquals(int expectedObjectsCount) {
        assertion.assertEquals(rowsCount(), expectedObjectsCount,"Table objects count doesn't equal to expected value " + expectedObjectsCount);
        return this;
    }

    public Table<T> assertTableObjectsCountNotEquals(int objectsCount) {
        assertion.assertNotEquals(countTableObjects(), objectsCount, "Table objects count equals to expected value " + objectsCount);
        return this;
    }

    public Table<T> assertTableContainsText (String text) {
        assertion.assertTrue(containsText(text), "Table doesn't contain text '" + text + "'.");
        return this;
    }

    public Table<T> assertTableDoesntContainText (String text) {
        assertion.assertFalse(containsText(text), "Table shouldn't contain text '" + text + "'.");
        return this;
    }

    public Table<T> assertTableContainsLinkTextPartially (String linkText) {
        assertion.assertTrue(containsLinkTextPartially(linkText), "Table doesn't contain link text '" + linkText + "'.");
        return this;
    }

    public Table<T> assertTableDoesntContainLinkTextPartially (String linkText) {
        assertion.assertFalse(containsLinkTextPartially(linkText), "Table shouldn't contain link text '" + linkText + "'.");
        return this;
    }

    public Table<T> assertTableContainsLinksTextPartially (String... linkTextValues) {
        assertion.assertTrue(containsLinksTextPartially(linkTextValues), "Table doesn't contain links text.");
        return this;
    }

    public Table<T> assertTableDoesntContainLinksTextPartially (String... linkTextValues) {
        assertion.assertFalse(containsLinksTextPartially(linkTextValues), "Table shouldn't contain links text.");
        return this;
    }

    public Table<T> assertCurrentTableContains(String elementValue) {
        return assertCurrentTableContains("Span", elementValue);
    }

    public Table<T> assertCurrentTableContains(String elementName, String elementValue) {
        assertion.assertTrue(currentTableContains(elementName, elementValue), "Table doesn't contain element " + elementName + " with value " +
                elementValue);
        return this;
    }

    public Table<T> assertCurrentTableDoesntContain(String elementValue) {
        return assertCurrentTableDoesntContain("Span", elementValue);
    }

    public Table<T> assertCurrentTableDoesntContain(String elementName, String elementValue) {
        assertion.assertFalse(currentTableContains(elementName, elementValue), "Table shouldn't contain element " + elementName + " with value " +
                elementValue);
        return this;
    }

    public Table<T> assertTableContainsColumnWithValue(String columnResourceKey, String value) {
        assertion.assertNotNull(rowByColumnResourceKey(columnResourceKey, value), "Table doesn't contain value '" + value +
                "' in column with resource key '" + columnResourceKey + "'.");
        return this;
    }

    public Table<T> assertTableDoesntContainColumnWithValue(String columnResourceKey, String value) {
        assertion.assertNull(rowByColumnResourceKey(columnResourceKey, value), "Table shouldn't contain value '" + value +
                "' in column with resource key '" + columnResourceKey + "'.");
        return this;
    }

    public Table<T> assertTableColumnValueIsEmpty(String columnResourceKey) {
        assertion.assertEquals("", getTableCellValue(columnResourceKey, 1));
        return this;
    }

    public Table<T> assertTableColumnValueIsNull(String columnResourceKey) {
        assertion.assertEquals(null, getTableCellValue(columnResourceKey, 1));
        return this;
    }

    public Table<T> assertButtonToolBarExists() {
        assertion.assertTrue($(Schrodinger.byDataId("buttonToolbar")).exists(), "Button toolbar is absent");
        return this;
    }

    public Table<T> assertIconColumnExistsByNameColumnValue(String nameColumnValue, String iconClass, String iconColor) {
        SelenideElement iconColumnElement = rowByColumnResourceKey("Name", nameColumnValue).getParentElement()
                .$x(".//td[contains(@class, 'icon') or contains(@class, 'composited-icon')]");
        if (iconColumnElement.exists()) {
            assertion.assertTrue(iconColumnElement.$x(".//i[contains(@class, \"" + iconClass + "\")]").exists());
            if (StringUtils.isNotEmpty(iconColor)) {
                assertion.assertTrue(iconColumnElement.$x(".//i[@style='color: " + iconClass + ";' or " +
                        "contains(@class, '" + iconColor + "')]").exists());
            }
        }
        return this;
    }

    public int rowsCount() {
        Utils.waitForAjaxCallFinish();
        return getParentElement()
                .findAll("tbody tr")
                .size();

    }

    public TableRow<T, Table<T>> getTableRow(int rowIndex) {
        ElementsCollection rows = getParentElement()
                .findAll("tbody tr");
        int rowsSize = rows.size();
        if (rowIndex > rowsSize) {
            return null;
        }
        return new TableRow(this, rows.get(rowIndex - 1));
    }

}
