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
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.task.OperationStatisticsPanel;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.task.ListTasksPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class TaskPageTest extends AbstractSchrodingerTest {

    private static final File OPERATION_STATISTICS_CLEANUP_TASK_FILE = new File("./src/test/resources/objects/tasks/operation-statistics-clean-up.xml");
    private static final File ENVIRONMENTAL_PERFORMANCE_CLEANUP_TASK_FILE = new File("./src/test/resources/objects/tasks/environmental-performance-clean-up.xml");
    private static final File RESULTS_CLEANUP_TASK_FILE = new File("./src/test/resources/objects/tasks/results-clean-up.xml");

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        resetToDefault();
        FormLoginPage login = midPoint.formLogin();
        basicPage = login.loginIfUserIsNotLog(username, password);
    }

//    @Override
//    protected List<File> getObjectListToImport(){
//        return Arrays .asList(OPERATION_STATISTICS_CLEANUP_TASK_FILE, ENVIRONMENTAL_PERFORMANCE_CLEANUP_TASK_FILE, RESULTS_CLEANUP_TASK_FILE);
//    }

    @Test
    public void test001createNewTask() {

        String name = "NewTest";
        String handler = "Recomputation task";
        TaskPage task = basicPage.newTask(handler);
        task.selectBasicPanel()
                .form()
                    .addAttributeValue("name", name)
                    .and()
                .and()
                .selectSchedulePanel()
                    .form()
                        .selectOption("recurrence","Single")
                        .and()
                    .and()
                    .selectActivityWorkPanel()
                        .form()
                            .getPrismContainerPanel("Objects")
                                .getContainerFormFragment()
                                .selectOption("type","User")
                                .and()
                            .and()
                            .getPrismContainerPanel("Execution options")
                                .getContainerFormFragment()
                                    .getPrismContainerPanel("Focus conflict resolution")
                                        .getContainerFormFragment()
                                        .selectOption("action","None")
                                        .and()
                                    .and()
                                .and()
                            .and()
                        .and()
                    .and()
             .clickSave()
                .feedback()
                    .isSuccess();

        ListTasksPage tasksPage = basicPage.listTasks();
        PrismForm<AssignmentHolderBasicPanel<TaskPage>> taskForm = tasksPage
                .table()
                    .search()
                        .byName()
                            .inputValue(name)
                            .updateSearch()
                        .and()
                    .clickByName(name)
                        .selectBasicPanel()
                            .form();

        taskForm.assertPropertyInputValue("name", name)
                .and()
                    .and()
                        .assertPageTitleStartsWith("Edit Recomputation task");
    }

    @Test
    public void test002cleanupOperationStatistics() {
        importObject(OPERATION_STATISTICS_CLEANUP_TASK_FILE, true, true);
        TaskPage taskPage = basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                        .inputValue("OperationStatisticsCleanupTest")
                        .updateSearch()
                    .and()
                    .clickByName("OperationStatisticsCleanupTest");
        OperationStatisticsPanel operationStatisticsPanel = taskPage
                .selectOperationStatisticsPanel();
        operationStatisticsPanel.assertResultsChartIsDisplayed();
        taskPage
                .cleanupEnvironmentalPerformanceInfo()
                .clickYes()
                .feedback()
                .assertSuccess();
        operationStatisticsPanel = taskPage
                .selectOperationStatisticsPanel();
        operationStatisticsPanel.assertResultsChartIsNotDisplayed();

    }

    @Test
    public void test003cleanupEnvironmentalPerformance() {
        importObject(ENVIRONMENTAL_PERFORMANCE_CLEANUP_TASK_FILE, true, true);
        TaskPage taskPage = basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                        .inputValue("EnvironmentalPerformanceCleanupTest")
                        .updateSearch()
                    .and()
                    .clickByName("EnvironmentalPerformanceCleanupTest");
        taskPage
                .selectEnvironmentalPerformancePanel()
                        .assertMappingsEvaluationContainingObjectValueMatch("ManRes")
                        .assertMappingsEvaluationInvocationsCountValueMatch("4")
                        .assertMappingsEvaluationMaxValueMatch("1")
                        .assertMappingsEvaluationTotalTimeValueMatch("2");
        taskPage
                .cleanupEnvironmentalPerformanceInfo()
                .clickYes()
                .feedback()
                .assertSuccess();
        taskPage
                .selectEnvironmentalPerformancePanel()
                        .assertMappingsEvaluationContainingObjectValueMatch(null)
                        .assertMappingsEvaluationInvocationsCountValueMatch(null)
                        .assertMappingsEvaluationMaxValueMatch(null)
                        .assertMappingsEvaluationTotalTimeValueMatch(null);
    }

    @Test
    public void test004cleanupTaskResults() {
        importObject(RESULTS_CLEANUP_TASK_FILE, true, true);
        TaskPage taskPage = basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                        .inputValue("ResultCleanupTest")
                        .updateSearch()
                    .and()
                    .clickByName("ResultCleanupTest");
        String tokenValue = "1000000000000003831";
        taskPage
                .selectResultPanel()
                    .getResultsTable()
                        .assertTableContainsText(tokenValue)
                        .and()
                    .assertOperationValueByTokenMatch(tokenValue, "run")
                    .assertStatusValueByTokenMatch(tokenValue, "IN_PROGRESS")
                    .assertTimestampValueByTokenMatch(tokenValue, "1/1/20, 12:00:00 AM")
                    .assertMessageValueByTokenMatch(tokenValue, "Test results");
        taskPage
                .cleanupResults()
                    .clickYes()
                        .feedback()
                        .assertSuccess();
        taskPage
                .selectResultPanel()
                    .getResultsTable()
                        .assertTableDoesntContainText(tokenValue)
                        .and()
                    .assertOperationValueByTokenMatch(tokenValue, null)
                    .assertStatusValueByTokenMatch(tokenValue, null)
                    .assertTimestampValueByTokenMatch(tokenValue, null)
                    .assertMessageValueByTokenMatch(tokenValue, null);

    }
}
