/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.simulation;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.SelectableRowTable;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ModalBox;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProcessedObjectsTable<T> extends TableWithPageRedirect<T, ProcessedObjectsTable<T>> {

    public ProcessedObjectsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public SimulationResultDetailsPage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "title", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new SimulationResultDetailsPage().waitForDetailsPanelToBeDisplayed();
    }

    @Override
    public Search<ProcessedObjectsTable<T>> search() {
        SelenideElement searchElement = this.getParentElement().$(By.cssSelector(".search-panel-form"));
        return new Search<>(this, searchElement);
    }

    @Override
    public ProcessedObjectsTable<T> assertTableContainsText (String text) {
        super.assertTableContainsText(text);
        return ProcessedObjectsTable.this;
    }

    @Override
    public ProcessedObjectsTable<T> assertTableContainsColumnWithValue(String columnResourceKey, String value) {
        super.assertTableContainsColumnWithValue(columnResourceKey, value);
        return ProcessedObjectsTable.this;
    }

    public ProcessedObjectsTable<T> markAsProtected(String name) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", name, true);
        row.getInlineMenu().clickInlineMenuButtonByTitle("Mark as Protected");
        Selenide.sleep(3000);
        return getThis();
    }

    public ProcessedObjectsTable<T> addMarks(String name, String... marks) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", name, true);
        row.getInlineMenu().clickInlineMenuButtonByTitle("Add Marks");
        ModalBox<ProcessedObjectsTable<T>> modal = new ModalBox<>(this, Utils.getModalWindowSelenideElement());
        SelectableRowTable<?, ?> table = new SelectableRowTable<>(modal,
                modal.getParentElement().$(Schrodinger.byDataId("div", "table")));
        Arrays.stream(marks).forEach(mark -> table.findRowByColumnLabelAndRowValue("Name", mark).clickCheckBox());
        modal.getParentElement().$(Schrodinger.byDataId("a", "addButton")).click();
        Utils.waitForAjaxCallFinish();
        if (Utils.isModalWindowSelenideElementVisible()) {
            Selenide.sleep(3000);
        }
        return getThis();
    }

    public ProcessedObjectsTable<T> assertRealObjectIsMarked(String objectName, String markName) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", objectName, true);
        SelenideElement mark = row.getParentElement()
                .$x(".//span[@data-s-id='realMarks' and contains(text(), '" + markName + "')]");
        assertion.assertTrue(mark.isDisplayed(), "Mark " + markName + " is not present for object " + objectName);
        return getThis();
    }

    public ProcessedObjectsTable<T> assertProcessedObjectIsMarked(String objectName, String markName) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", objectName, true);
        SelenideElement mark = row.getParentElement()
                .$x(".//span[@data-s-id='processedMarks' and contains(text(), '" + markName + "')]");
        assertion.assertTrue(mark.isDisplayed(), "Mark " + markName + " is not present for object " + objectName);
        return getThis();
    }

    public ProcessedObjectsTable<T> assertAllObjectsAreMarked(String markName) {
        ElementsCollection rows = getParentElement().findAll("tbody tr");
        boolean allMarked = rows.asFixedIterable()
                .stream()
                .allMatch(r -> rowContainsMark(r, markName));
        assertion.assertTrue(allMarked, "Not all objects are marked with " + markName);
        return getThis();
    }

    private boolean rowContainsMark(SelenideElement row, String markName) {
        SelenideElement processedMark = row
                .$x(".//span[@data-s-id='processedMarks' and contains(text(), '" + markName + "')]");
        SelenideElement realMark = row
                .$x(".//span[@data-s-id='realMarks' and contains(text(), '" + markName + "')]");
        return processedMark.isDisplayed() || realMark.isDisplayed();
    }

    private ProcessedObjectsTable<T> getThis() {
        try {
            if (getParentElement().isDisplayed()) {
                return this;
            } else {
                return new ProcessedObjectsTable<>(getParent(), findTableElement());
            }
        } catch (Exception e) {
        }
        return new ProcessedObjectsTable<>(getParent(), findTableElement());
    }

    private SelenideElement findTableElement() {
        ElementsCollection tableElements = $$(Schrodinger.byDataId("div", "itemsTable"));
        return tableElements.asFixedIterable()
                .stream()
                .filter(SelenideElement::isDisplayed)
                .findFirst()
                .orElse(null);
    }
}
