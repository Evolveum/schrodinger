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
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class OperationStatisticsPanel extends Component<TaskPage> {

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

    public Table<OperationStatisticsPanel> getSynchronizationSituationTransitionsTable() {
        Table<OperationStatisticsPanel> table = new Table<>(this,
                $(Schrodinger.byDataId("synchronizationSituationTransitions")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        return table;
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
