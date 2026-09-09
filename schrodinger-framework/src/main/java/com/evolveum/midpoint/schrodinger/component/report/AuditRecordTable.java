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
package com.evolveum.midpoint.schrodinger.component.report;

import java.util.ArrayList;
import java.util.List;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class AuditRecordTable<T> extends TableWithPageRedirect<T, AuditLogViewerDetailsPage, AuditRecordTable<T>> {

    public AuditRecordTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public AuditLogViewerDetailsPage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new AuditLogViewerDetailsPage();
    }

    public AuditLogViewerDetailsPage openDetailsPageForTargetObject(String targetObjectName) {
        TableRow<?, ?> row = rowByColumnResourceKey("Target", targetObjectName);
        row.clickColumnByName("Time");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new AuditLogViewerDetailsPage();
    }

    public AuditLogViewerDetailsPage clickByRowColumnNumber(int rowNumber, int columnNumber) {
        getCell(rowNumber, columnNumber)
                .$(By.tagName("a")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new AuditLogViewerDetailsPage();
    }

    public void checkInitiator(int row, String name) {
        checkTextInColumn(row, 2, name);
    }

    public void checkEventType(int row, String name) {
        checkTextInColumn(row, 4, name);
    }

    public void checkChannel(int row, String name) {
        checkTextInColumn(row, 7, name);
    }

    public void checkOutcome(int row, String name) {
        checkTextInColumn(row, 8, name);
    }

    public void checkTextInColumn(int row, int column, String name) {
        $(By.cssSelector(".table.table-hover"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        if (StringUtils.isEmpty(name)) {
            getCell(row, column).shouldBe(Condition.empty);
        } else {
            getCell(row, column).shouldHave(Condition.text(name));
        }
    }

    public void checkRow(int row, String initiatorName, String channelName, String outcome) {
        checkInitiator(row, initiatorName);
        checkChannel(row, channelName);
        checkOutcome(row, outcome);
    }

    public void checkRowsByInitiatorAndChannel(String initiatorName, String channelName, String... expectedOutcomes) {
        $(By.cssSelector(".table.table-hover")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        List<String> actualOutcomes = getOutcomesByInitiatorAndChannel(initiatorName, channelName);

        assertion.assertEquals(actualOutcomes.size(), expectedOutcomes.length,
                "Unexpected number of audit rows for initiator '" + initiatorName + "' and channel '" + channelName
                        + "'. Expected outcomes " + java.util.Arrays.toString(expectedOutcomes)
                        + ", but found " + actualOutcomes);

        for (int i = 0; i < expectedOutcomes.length; i++) {
            String expected = expectedOutcomes[i];
            String actual = actualOutcomes.get(i);
            if (StringUtils.isEmpty(expected)) {
                assertion.assertTrue(StringUtils.isEmpty(actual),
                        "Outcome of row " + (i + 1) + " for initiator '" + initiatorName + "' and channel '"
                                + channelName + "' should be empty, but was '" + actual + "'.");
            } else {
                assertion.assertEquals(actual, expected,
                        "Outcome of row " + (i + 1) + " for initiator '" + initiatorName + "' and channel '"
                                + channelName + "' doesn't match.");
            }
        }
    }

    private List<String> getOutcomesByInitiatorAndChannel(String initiatorName, String channelName) {
        SelenideElement tbody = getParentElement().$(Schrodinger.byElementAttributeValue("tbody", "data-s-id", "body"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        ElementsCollection rows = tbody.findAll(By.tagName("tr"));

        List<String> outcomes = new ArrayList<>();
        for (SelenideElement row : rows) {
            ElementsCollection cells = row.findAll(By.tagName("td"));
            String initiator = cells.get(1).getText().trim();
            String channel = cells.get(6).getText().trim();
            if (Strings.CI.equals(initiator, initiatorName) && Strings.CI.equals(channel, channelName)) {
                outcomes.add(cells.get(7).getText().trim());
            }
        }
        return outcomes;
    }

    public SelenideElement getCell(int row, int column) {
        SelenideElement tbody = getParentElement().$(Schrodinger.byElementAttributeValue("tbody", "data-s-id", "body")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        ElementsCollection rowsElement = tbody.findAll(By.tagName("tr"));
        SelenideElement rowElement = rowsElement.get(row > 0 ? (row - 1) : row);
        ElementsCollection columnsElement = rowElement.findAll(By.tagName("td"));
        return columnsElement.get(column > 0 ? (column - 1) : column);
    }

    @Override
    public Search<AuditRecordTable<T>> search() {
        return (Search<AuditRecordTable<T>>) super.search();
    }

    @Override
    public AuditLogViewerDetailsPage getObjectDetailsPage() {
        return new AuditLogViewerDetailsPage();
    }
}
