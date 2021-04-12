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
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author honchar
 */
public class EnvironmentalPerformanceTab extends Component<TaskPage> {

    public EnvironmentalPerformanceTab(TaskPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public StatisticsPanel<EnvironmentalPerformanceTab> getStatisticsPanel() {
        return new StatisticsPanel<>(this, $(Schrodinger.byDataId("statisticsPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public Table<EnvironmentalPerformanceTab> getMappingsEvaluationInformationTable() {
        SelenideElement tableElement = $(Schrodinger.byDataId("mappingsStatisticsLines"))
                .$x(".//table[@data-s-id='table']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new Table<EnvironmentalPerformanceTab>(this, tableElement);
    }

    public Table<EnvironmentalPerformanceTab> getNotificationsInformationTable() {
        SelenideElement tableElement = $(Schrodinger.byDataId("notificationsStatisticsLines"))
                .$x(".//table[@data-s-id='table']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new Table<EnvironmentalPerformanceTab>(this, tableElement);
    }

    public String getLastStatusMessage() {
        if ($(Schrodinger.byDataId("lastMessage")).exists()) {
            return $(Schrodinger.byDataId("lastMessage")).getText();
        }
        return "";
    }

    public void assertLastStatusMessageEquals(String expectedValue) {
        String actualMessageValue = getLastStatusMessage();
        assertion.assertEquals(actualMessageValue, expectedValue, "Last status message doesn't match");
    }

    public void assertLastStatusMessageContains(String valueToContain) {
        String actualMessageValue = getLastStatusMessage();
        assertion.assertTrue(actualMessageValue != null && actualMessageValue.contains(valueToContain), "Last status message doesn't contain");
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationContainingObjectValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.Object", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationInvocationsCountValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.Count", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationAvgTimeValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.AverageTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationMinValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.MinTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationMaxValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.MaxTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertMappingsEvaluationTotalTimeValueMatch(String expectedValue) {
        assertMappingsEvaluationTableContainsValue("MappingsStatistics.TotalTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsTransportValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.Transport", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsSuccessfulValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.CountSuccess", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsFailedValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.CountFailure", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsAvgTimeValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.AverageTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsMinValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.MinTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsMaxValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.MaxTime", expectedValue);
        return this;
    }

    public EnvironmentalPerformanceTab assertNotificationsTotalTimeValueMatch(String expectedValue) {
        assertNotificationsTableContainsValue("NotificationsStatistics.TotalTime", expectedValue);
        return this;
    }

    private void assertMappingsEvaluationTableContainsValue(String columnResourceKey, String expectedValue) {
        if (expectedValue == null) {
            getMappingsEvaluationInformationTable().assertTableColumnValueIsNull(columnResourceKey);
        } else if (StringUtils.isEmpty(expectedValue)) {
            getMappingsEvaluationInformationTable().assertTableColumnValueIsEmpty(columnResourceKey);
        } else {
            getMappingsEvaluationInformationTable().assertTableContainsColumnWithValue(columnResourceKey, expectedValue);
        }
    }

    private void assertNotificationsTableContainsValue(String columnResourceKey, String expectedValue) {
        if (expectedValue == null) {
            getNotificationsInformationTable().assertTableColumnValueIsNull(columnResourceKey);
        } else if (StringUtils.isEmpty(expectedValue)) {
            getNotificationsInformationTable().assertTableColumnValueIsEmpty(columnResourceKey);
        } else {
            getNotificationsInformationTable().assertTableContainsColumnWithValue(columnResourceKey, expectedValue);
        }
    }
}
