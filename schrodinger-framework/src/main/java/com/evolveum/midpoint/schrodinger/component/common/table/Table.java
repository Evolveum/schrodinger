/*
 * Copyright (c) 2010-2018 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
 */

package com.evolveum.midpoint.schrodinger.component.common.table;

import static com.codeborne.selenide.Selectors.byPartialLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.Objects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.Assert;
import org.testng.asserts.Assertion;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Table<T> extends Component<T> {

    public Table(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TableRow<T, Table<T>> rowByColumnLabel(String label, String rowValue) {
        int index = findColumnByLabel(label);
        if (index < 0) {
            return null;
        }
        return getTableRowByIndex(index, rowValue);
    }

    public int findColumnByLabel(String label) {
        ElementsCollection headers = getParentElement().findAll("thead th div span[data-s-id=label]");
        if (headers == null) {
            return -1;
        }
        int index = 1;
        for (SelenideElement header : headers) {
            String value = header.text();
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
        ElementsCollection headers = getParentElement().findAll("thead th div span[data-s-id=label]");
        if (headers == null) {
            return -1;
        }
        int index = 1;
        for (SelenideElement header : headers) {
            String headerResourceKey = header.getAttribute("data-s-resource-key");
            if (headerResourceKey == null) {
                index++;
                continue;
            }

            if (Objects.equals(headerResourceKey, key)) {
                break;
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

    public String getTableCellValue(String columnResourceKey, int rowIndex) {
        int columnIndex = findColumnByResourceKey(columnResourceKey);
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        if (rowIndex > rows.size()) {
            return null;
        }
        SelenideElement element = rows.get(rowIndex -1).find("td:nth-child(" + (columnIndex) + ")");
        if (element == null) {
            return null;
        }
        return element.getText();
    }

    public Search<? extends Table<T>> search() {
        SelenideElement searchElement = getParentElement().$(By.cssSelector(".form-inline.pull-right.search-form"));

        return new Search<>(this, searchElement);
    }

    public <P extends Table<T>> Paging<P> paging() {
        SelenideElement pagingElement = getParentElement().$x(".//div[@class='boxed-table-footer-paging']");

        return new Paging(this, pagingElement);
    }

    public Table<T> selectAll() {

        $(Schrodinger.bySelfOrAncestorElementAttributeValue("input", "type", "checkbox", "data-s-id", "topToolbars"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public boolean currentTableContains(String elementValue) {
        return currentTableContains("Span", elementValue);
    }

    public boolean currentTableContains(String elementName, String elementValue) {

        // TODO replate catch Throwable with some less generic error
        try {
            return $(Schrodinger.byElementValue(elementName, elementValue)).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).is(Condition.visible);
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

    public int countTableObjects() {
        String countStringValue = $(Schrodinger.bySelfOrAncestorElementAttributeValue("div", "class", "dataTables_info", "data-s-id", "count"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).text();
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
        assertion.assertNotNull(rowByColumnLabel(columnLabel, rowValue), "Row with value " + rowValue + " in " + columnLabel + " column doesn't exist.");
        return this;
    }

    public Table<T> assertTableObjectsCountEquals(int expectedObjectsCount) {
        assertion.assertEquals(countTableObjects(), expectedObjectsCount,"Table objects count doesn't equal to expected value " + expectedObjectsCount);
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
        assertion.assertNotNull(rowByColumnResourceKey(columnResourceKey, value));
        return this;
    }

    public Table<T> assertTableColumnValueIsEmpty(String columnResourceKey) {
        assertion.assertEquals(getTableCellValue(columnResourceKey, 1), "");
        return this;
    }

    public Table<T> assertButtonToolBarExists() {
        assertion.assertTrue($(Schrodinger.byDataId("buttonToolbar")).exists(), "Button toolbar is absent");
        return this;
    }

    public Table<T> assertIconColumnExistsByNameColumnValue(String nameColumnValue, String iconClass, String iconColor) {
        if (rowByColumnLabel("Name", nameColumnValue).getParentElement().$(By.className("icon")).exists()) {
            SelenideElement iconColumnElement = rowByColumnResourceKey("Name", nameColumnValue).getParentElement().$(By.className("icon"));
            assertion.assertTrue(iconColumnElement.$x(".//i[@class='" + iconClass + "']").exists());
            if (StringUtils.isNotEmpty(iconColor)) {
                assertion.assertTrue(iconColumnElement.$x(".//i[@style='color: " + iconClass + ";']").exists());
            }
        }
        return this;
    }

}
