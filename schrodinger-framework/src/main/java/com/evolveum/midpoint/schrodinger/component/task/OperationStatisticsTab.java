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

import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * @author skublik
 */

public class OperationStatisticsTab extends Component<TaskPage> {

    public OperationStatisticsTab(TaskPage parent, SelenideElement parentElement) {
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

    private Integer getIntegerValueForTextField(String fieldName) {
        String textValue = getParentElement().$(Schrodinger.byDataId("span", fieldName)).getText();
        if (textValue == null || textValue.trim().equals("")) {
            return null;
        }
        return Integer.valueOf(textValue);
    }

    public OperationStatisticsTab assertSuccessfullyProcessedCountMatch(int expectedCount) {
        assertion.assertTrue(getSuccessfullyProcessed() == expectedCount, "The count of successfully processed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsTab assertSuccessfullyProcessedIsNull() {
        assertion.assertNull(getSuccessfullyProcessed(), "The value of successfully processed objects should be null.");
        return this;
    }

    public OperationStatisticsTab assertObjectsFailedToBeProcessedCountMatch(int expectedCount) {
        assertion.assertTrue(getObjectsFailedToBeProcessed() == expectedCount, "The count of failed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsTab assertObjectsFailedToBeProcessedIsNull() {
        assertion.assertNull(getObjectsFailedToBeProcessed(), "The value of failed objects should be null.");
        return this;
    }

    public OperationStatisticsTab assertObjectsTotalCountMatch(int expectedCount) {
        assertion.assertTrue(getObjectsTotalCount() == expectedCount, "The total count of processed objects doesn't match to " + expectedCount);
        return this;
    }

    public OperationStatisticsTab assertObjectsTotalIsNull() {
        assertion.assertNull(getObjectsTotalCount(), "The total count of processed objects should be null.");
        return null;
    }
}
