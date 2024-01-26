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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class SimulationTaskDetailsTable extends Table<SimulationResultDetailsPage, SimulationTaskDetailsTable> {

    public SimulationTaskDetailsTable(SimulationResultDetailsPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SimulationTaskDetailsTable assertStartTimestampValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Start timestamp", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertEndTimestampValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("End timestamp", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertFinishedInValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Finished in", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertTaskValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Task", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertStatusValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Status", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertConfigurationValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Configuration", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertAddedObjectsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Added objects", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertDeletedObjectsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Deleted objects", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertModifiedObjectsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Modified objects", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertUnmodifiedObjectsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Unmodified objects", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertAllProcessedObjectsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("All processed objects", expectedValue));
        return this;
    }

    public SimulationTaskDetailsTable assertErrorsValueEquals(String expectedValue) {
        assertion.assertTrue(rowWithValuesExists("Errors", expectedValue));
        return this;
    }

    public TaskPage viewTask() {
        SelenideElement row = getTableRowWithFirstColumnValue("Task");
        row.$x(".//a").click();
        Utils.waitForAjaxCallFinish();
        return new TaskPage();
    }

    public ProcessedObjectsPage viewAddedObjects() {
        return clickObjectsCountLink("Added objects");
    }

    public ProcessedObjectsPage viewDeletedObjects() {
        return clickObjectsCountLink("Deleted objects");
    }

    public ProcessedObjectsPage viewModifiedObjects() {
        return clickObjectsCountLink("Modified objects");
    }

    public ProcessedObjectsPage viewUnmodifiedObjects() {
        return clickObjectsCountLink("Unmodified objects");
    }

    public ProcessedObjectsPage viewAllProcessedObjects() {
        return clickObjectsCountLink("All processed objects");
    }

    public ProcessedObjectsPage viewErrors() {
        return clickObjectsCountLink("Errors");
    }

    private boolean rowWithValuesExists(String firstColumnValue, String secondColumnValue) {
        SelenideElement row = getTableRowWithFirstColumnValue(firstColumnValue);
        String secondColumnText = row.$x(".//td[2]").getText();
        return secondColumnText.contains(secondColumnValue);
    }

    private SelenideElement getTableRowWithFirstColumnValue(String firstColumnValue) {
        return getParentElement().$x(".//td[contains(text(), '" + firstColumnValue + "')]").parent();
    }

    private ProcessedObjectsPage clickObjectsCountLink(String firstColumnValue) {
        SelenideElement row = getTableRowWithFirstColumnValue(firstColumnValue);
        row.$x(".//a").click();
        Utils.waitForAjaxCallFinish();
        return new ProcessedObjectsPage();
    }
}
