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

package com.evolveum.midpoint.schrodinger.trainings.first.steps;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

public class M3ImportSourceData extends AbstractTrainingTest {

    private static final String STATUS_EXPRESSION_SCRIPT_VALUE = "switch (input) { case 'In': 'active'; break; case 'Long-term leave': 'suspended'; break; case 'Former employee': 'archived'; break; }";

    @Test(groups = MODULE_3_GROUP)
    public void test00100singleSourceSystemEntryImportSimulation() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .importPreview("Name", "1001")
                .selectTaskExecutionMode("Simulated development")
                .select()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .assertTableContainsText("Focus activated")
                .assertTableContainsColumnWithValue("SimulationResultProcessedObjectType.type", "User")
                .assertTableContainsColumnWithValue("SimulationResultProcessedObjectType.state", "Added")
                .clickByName("1001")
                .changes()
                .header()
                .assertChangeTypeEquals("Add")
                .assertChangedObjectTypeEquals("User")
                .assertChangedObjectNameEquals("1001")
                .and()
                .assertItemsDeltasSizeEquals(5)
                .assertItemValueEquals("Name", "1001")
                //todo check other item values
                .assertItemValueNotPresent("Locality")
                .assertItemValueNotPresent("Lifecycle status")
                .and()
                .back();

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .addInbound()
                .name("locality-to-locality")
                .fromResourceAttribute("locality")
                .expression("As is")
                .target("locality")
                .lifecycleState("Active")
                .and()
                .addInbound()
                .name("status-to-lifecycle-state")
                .fromResourceAttribute("status")
                .scriptExpression("Groovy (default)", STATUS_EXPRESSION_SCRIPT_VALUE)
                .target("lifecycleState")
                .lifecycleState("Active")
                .and()
                .and()
                .saveMappings()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .importPreview("Name", "1001")
                .selectTaskExecutionMode("Simulated development")
                .select()
                .table()
                .clickByName("1001")
                .changes()
                .header()
                .assertChangeTypeEquals("Add")
                .assertChangedObjectTypeEquals("User")
                .assertChangedObjectNameEquals("1001")
                .and()
                .assertItemValueEquals("Locality", "Small Red Rock City")
                .assertItemValueEquals("Lifecycle state", "active")
                .and()
                .back();

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .setLifecycleState("Active (production)");
    }

    @Test(groups = MODULE_3_GROUP)
    public void test00110sourceSystemDataImport() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .tasks()
                .clickCreateTask()
                .importTask()
                .clickCreateTaskButton()
                .configuration()
                .next()
                .nextToDistribution()
                .saveAndRun();
        basicPage
                .listTasks("Import tasks")
                .table()
                .clickByName("Import task: HR: HR Person")
                .summary()
                .assertSummaryTagWithTextExists("Success")
                .and()
                .selectOperationStatisticsPanel()
                .assertProgressSummaryObjectsCountEquals(40);
        //todo assert the count of the imported accounts

        basicPage
                .listUsers()
                .table()
                .assertTableContainsColumnWithValue("Name", "0001")
                .assertTableContainsColumnWithValue("Name", "0013")
                .assertTableContainsColumnWithValue("Name", "0016")
                .search()
                .byName()
                .inputValue("0001")
                .updateSearch()
                .and()
                .assertTableColumnValueIsEmpty("UserType.fullName")
                .search()
                .byName()
                .inputValue("80")
                .updateSearch()
                .and()
                .assertCurrentTableDoesntContain("8000")
                .assertCurrentTableDoesntContain("8001")
                .assertCurrentTableDoesntContain("8002")
                .assertCurrentTableDoesntContain("8003");

        basicPage
                .listUsers("Persons")
                .table()
                .assertTableObjectsCountEquals(0);

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureBasicAttributes()
                .next()
                .next()
                .archetype("Person")
                .saveSettingsWithoutRedirect()
                .exitWizard()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Import task: HR: HR Person")
                .selectOperationStatisticsPanel()
                //todo what concretely should be checked here?
                .and()
                .clickRunNow();

        Selenide.sleep(30000);

        UsersPageTable personsTable = basicPage
                .listUsers("Persons")
                .table();
        personsTable
                .assertTableContainsColumnWithValue("Name", "0001")
                .assertTableContainsColumnWithValue("Name", "0013")
                .assertTableContainsColumnWithValue("Name", "0016")
                .assertTableContainsColumnWithValue("UserType.fullName", "Geena Green");
        //todo check more full names?
    }
}
