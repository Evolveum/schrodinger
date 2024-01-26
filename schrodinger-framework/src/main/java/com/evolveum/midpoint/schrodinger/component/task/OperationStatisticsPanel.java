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
package com.evolveum.midpoint.schrodinger.component.task;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.task.dto.ExecutedActionDto;
import com.evolveum.midpoint.schrodinger.component.task.dto.SynchronizationSituationTransitionDto;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class OperationStatisticsPanel extends Component<TaskPage, OperationStatisticsPanel> {

    public OperationStatisticsPanel(TaskPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public Integer getSuccessfullyProcessed() {
        return getIntegerValueForTextField("objectsProcessedSuccess");
    }

    public Integer getObjectsFailedToBeProcessed() {
        return getIntegerValueForTextField("objectsProcessedFailure");
    }

    public Integer getObjectsTotalCount() {
        return getIntegerValueForTextField("objectsTotal");
    }

    public OperationStatisticsPanel assertProgressSummaryObjectsCountEquals(int expectedCount) {
        String actualValue = getParentElement().$(Schrodinger.byDataId("h3", "summary")).getText();
        assertion.assertEquals("Objects processed: " + expectedCount, actualValue);
        return this;
    }

    private Integer getIntegerValueForTextField(String fieldName) {
        String textValue = getParentElement().$(Schrodinger.byDataId("span", fieldName)).getText();
        if (textValue == null || textValue.trim().equals("")) {
            return null;
        }
        return Integer.valueOf(textValue);
    }

    private Table<OperationStatisticsPanel, Table> getSynchronizationSituationTransitionsTable() {
        Table<OperationStatisticsPanel, Table> table = new Table<>(this,
                $(Schrodinger.byDataId("synchronizationSituationTransitions"))
                        .$(Schrodinger.byDataId("synchronizationSituationTransitions"))
                                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        return table;
    }

    private Table<OperationStatisticsPanel, Table> getAllExecutedActionsTable() {
        Table<OperationStatisticsPanel, Table> table = new Table<>(this,
                $(Schrodinger.byDataId("allActionsExecuted"))
                                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        return table;
    }

    public OperationStatisticsPanel assertSynchronizationSituationTransitionRecordsMatch(
            List<SynchronizationSituationTransitionDto> records) {
        records.forEach(record -> {
            assertion.assertTrue(syncSituationTransitionRecordExists(record),
                    "Record " + record + " doesn't exist in table.");
        });
        return this;
    }

    public OperationStatisticsPanel assertAllExecutedActionsRecordsMatch(List<ExecutedActionDto> records) {
        records.forEach(record -> {
            assertion.assertTrue(allExecutedActionsRecordExists(record),
                    "Record " + record + " doesn't exist in table.");
        });
        return this;
    }

    private boolean syncSituationTransitionRecordExists(SynchronizationSituationTransitionDto record) {
        Table<?, ?> table = getSynchronizationSituationTransitionsTable();
        int rowCount = table.rowsCount();
        boolean exists = false;
        for (int i = 1; i <= rowCount; i++) {
            try {
                Logger.getLogger(OperationStatisticsPanel.class.getSimpleName()).info("Checking record " + record);
                TableRow<?, ?> tableRow = table.getTableRow(i);
                assertion.assertEquals(record.getOriginalState(),
                        tableRow.getColumnCellTextByColumnName("Original state"),
                        "Original state doesn't match, expected: " + record.getOriginalState());
                assertion.assertEquals(record.getSynchronizationStart(),
                        tableRow.getColumnCellTextByColumnName("Synchronization start"));
                assertion.assertEquals(record.getSynchronizationEnd(),
                        tableRow.getColumnCellTextByColumnName("Synchronization end"));
                assertion.assertEquals(record.getExclusionReason(),
                        tableRow.getColumnCellTextByColumnName("Exclusion reason"));
                assertion.assertEquals(record.getSucceeded(), tableRow.getColumnCellTextByColumnName("Succeeded"));
                assertion.assertEquals(record.getFailed(), tableRow.getColumnCellTextByColumnName("Failed"));
                assertion.assertEquals(record.getSkipped(), tableRow.getColumnCellTextByColumnName("Skipped"));
                assertion.assertEquals(record.getTotal(), tableRow.getColumnCellTextByColumnName("Total"));
                exists = true;
                break;
            } catch (AssertionError e) {
                //nothing to do
            }
        }
        return exists;
    }

    private boolean allExecutedActionsRecordExists(ExecutedActionDto record) {
        Table<?, ?> table = getAllExecutedActionsTable();
        Utils.scrollToElement(table.getParentElement());
        int rowCount = table.rowsCount();
        boolean exists = false;
        for (int i = 1; i <= rowCount; i++) {
            try {
                Logger.getLogger(OperationStatisticsPanel.class.getSimpleName()).info("Checking record " + record);
                TableRow<?, ?> tableRow = table.getTableRow(i);
                if (StringUtils.isNotEmpty(record.getObjectType())) {
                    assertion.assertEquals(record.getObjectType(),
                            tableRow.getColumnCellTextByColumnName("Object type"),
                            "Object type doesn't match, expected: " + record.getObjectType());
                }
                if (StringUtils.isNotEmpty(record.getOperation())) {
                    assertion.assertEquals(record.getOperation(),
                            tableRow.getColumnCellTextByColumnName("Operation"),
                            "Operation doesn't match, expected: " + record.getObjectType());
                }
                if (StringUtils.isNotEmpty(record.getChannel())) {
                    assertion.assertEquals(record.getChannel(),
                            tableRow.getColumnCellTextByColumnName("Channel"),
                            "Channel doesn't match, expected: " + record.getChannel());
                }
                if (StringUtils.isNotEmpty(record.getCountOK())) {
                    assertion.assertEquals(record.getCountOK(),
                            tableRow.getColumnCellTextByColumnName("Count (OK)"),
                            "Count (OK) doesn't match, expected: " + record.getCountOK());
                }
                if (StringUtils.isNotEmpty(record.getLastOK())) {
                    assertion.assertEquals(record.getLastOK(),
                            tableRow.getColumnCellTextByColumnName("Last (OK)"),
                            "Last (OK) doesn't match, expected: " + record.getLastOK());
                }
                if (StringUtils.isNotEmpty(record.getTime())) {
                    assertion.assertEquals(record.getTime(),
                            tableRow.getColumnCellTextByColumnName("Time"),
                            "Time doesn't match, expected: " + record.getTime());
                }
                if (StringUtils.isNotEmpty(record.getCountFailure())) {
                    assertion.assertEquals(record.getCountFailure(),
                            tableRow.getColumnCellTextByColumnName("Count (failure)"),
                            "Count (failure) doesn't match, expected: " + record.getCountFailure());
                }
                exists = true;
                break;
            } catch (AssertionError e) {
                //nothing to do
            }
        }
        return exists;
    }

    public OperationStatisticsPanel assertSucceededCountMatch(int expectedCount) {
        getSynchronizationSituationTransitionsTable().calculateAllIntValuesInColumn("Succeeded");
        assertion.assertEquals(expectedCount, getSynchronizationSituationTransitionsTable().calculateAllIntValuesInColumn("Succeeded"), "The count of successfully processed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsPanel assertSuccessfullyProcessedIsNull() {
        assertion.assertNull(getSuccessfullyProcessed(), "The value of successfully processed objects should be null.");
        return this;
    }

    public OperationStatisticsPanel assertObjectsFailedToBeProcessedCountMatch(int expectedCount) {
        assertion.assertTrue(getObjectsFailedToBeProcessed() == expectedCount, "The count of failed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsPanel assertObjectsFailedToBeProcessedIsNull() {
        assertion.assertNull(getObjectsFailedToBeProcessed(), "The value of failed objects should be null.");
        return this;
    }

    public OperationStatisticsPanel assertObjectsTotalCountMatch(int expectedCount) {
        assertion.assertTrue(getObjectsTotalCount() == expectedCount, "The total count of processed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsPanel assertObjectsTotalIsNull() {
        assertion.assertNull(getObjectsTotalCount(), "The total count of processed objects should be null.");
        return null;
    }

    public OperationStatisticsPanel assertResultsChartIsDisplayed() {
        assertion.assertTrue($(Schrodinger.byDataId("chart")).isDisplayed(), "Chart with operation results should be visible");
        return this;
    }

    public OperationStatisticsPanel assertResultsChartIsNotDisplayed() {
        assertion.assertFalse($(Schrodinger.byDataId("chart")).isDisplayed(), "Chart with operation results shouldn't be visible");
        return this;
    }
}
