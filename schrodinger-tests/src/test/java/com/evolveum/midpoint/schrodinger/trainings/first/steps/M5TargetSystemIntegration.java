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

import com.evolveum.midpoint.schrodinger.component.task.dto.SynchronizationSituationTransitionDto;
import com.evolveum.midpoint.schrodinger.simulation.ProcessedObjectsPage;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class M5TargetSystemIntegration extends AbstractTrainingTest {

    @Test(groups = MODULE_5_GROUP)
    public void test1simulatedCorrelationWithAD() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .tasks()
                .clickCreateTask()
                .reconciliationTask()
                .turnOnTaskSimulating()
                .clickCreateTaskButton()
                .configuration()
                .name("Reconciliation with AD - development simulation")
                .next()
                .nextToExecution()
                .mode("Preview")
                .predefined("Development")
                .next()
                .next()
                .saveAndRun();
        ProcessedObjectsPage processedObjectsPage = basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .selectOperationStatisticsPanel()
                .assertSynchronizationSituationTransitionRecordsMatch(prepareSyncSituationTransitionRecords())
                .and()
                .showSimulationResult()
                .assertMarkValueEquals("Projection deactivated", 5)
                .simulationTaskDetails()
                .assertDeletedObjectsValueEquals("5")
                .viewDeletedObjects()
                .table()
                .assertTableContainsText("Ana Lopez")
                .and()
                .backToSimulationResultPage()
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("78")
                .viewModifiedObjects();
        processedObjectsPage = checkProjectionWasAddedToUser(processedObjectsPage);
        checkMetadataChangesForShadow(processedObjectsPage);
    }

    @Test(groups = MODULE_5_GROUP)
    public void test2markingAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .showSimulationResult()
                .selectMark("Projection deactivated")
                .table()
                .addMarks("cn=Ana Lopez,ou=users,dc=example,dc=com", "Correlate later")
                .markAsProtected("cn=Mail Service Account,ou=users,dc=example,dc=com")
                .markAsProtected("cn=Spam Assassin Service Account,ou=users,dc=example,dc=com")
                .assertRealObjectIsMarked("cn=Ana Lopez,ou=users,dc=example,dc=com", "Correlate later")
                .assertRealObjectIsMarked("cn=Mail Service Account,ou=users,dc=example,dc=com", "Protected")
                .assertRealObjectIsMarked("cn=Spam Assassin Service Account,ou=users,dc=example,dc=com", "Protected");
        //(also Resource Accounts page now shows the marks)

        ProcessedObjectsPage processedObjectsPage = basicPage
                .listTasks()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .clickRunNowAndWaitToBeClosed()
                .showSimulationResult()
                .assertMarkValueEquals("Projection deactivated", 1) //todo ask Vix
                .selectMark("Projection deactivated")
                .table()
                .assertTableContainsText("cn=Secret Admin,ou=users,dc=example,dc=com")
                .and()
                .backToSimulationResultPage()
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("78")
                .viewModifiedObjects();
        processedObjectsPage = checkProjectionWasAddedToUser(processedObjectsPage);
        checkMetadataChangesForShadow(processedObjectsPage);
    }

    @Test(groups = MODULE_5_GROUP)
    public void test3ignoringOrphanedAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .setLifecycleState("Active (production)")
                .selectSchemaHandlingPanel()
                .getSchemaHandlingTable()
                .setLifecycleStateValue("Normal Account", "Active (production)")
                .and()
                .and()
                .clickSave();
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureSynchronization()
                .setLifecycleStateValueForSituation("Unlinked", "Active (production)")
                .setLifecycleStateValueForSituation("Linked", "Active (production)")
                .setLifecycleStateValueForSituation("Deleted", "Active (production)")
                .setLifecycleStateValueForSituation("Unmatched", "Draft (disabled)")
                .saveSynchronizationSettings()
                .tasks()
                .clickCreateTask()
                .reconciliationTask()
                .turnOnTaskSimulating()
                .clickCreateTaskButton()
                .configuration()
                .name("Reconciliation with AD - production simulation")
                .next()
                .nextToExecution()
                .mode("Preview")
                .predefined("Production")
                .next()
                .next()
                .saveAndRun();
        //todo fix in guide
        //1. In your browser with midPoint, in Accounts panel for AD resource:
        //click Defined Tasks menu item
        // (not connected to Accounts panel)
        //
        //2. Active (production) in guide Active (Production)
        ProcessedObjectsPage processedObjectsPage = basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - production simulation")
                .selectOperationStatisticsPanel()
                .screenshot("test3ignoringOrphanedAccounts_selectOperationStatisticsPanel")
/**
 * scroll down to Synchronization situation transitions section. Here you can see how the resource accounts were classified before/after the task execution. Please note the operations were not actually executed as we have run the reconciliation in simulation mode.
 * 39 accounts previously not linked are now linked to midPoint users; final situation is Linked
 * 1 account previously not linked is still not linked to midPoint users; final situation is Unmatched - this is orphaned account
 * 4+1 accounts are protected (4 using marks including Ana Lopez, one from the configuration of resource copied from resource template)
 */
                .and()
                .showSimulationResult()
                .assertMarkValueEquals("Projection deactivated", 0)
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("78")
                .viewModifiedObjects();
        processedObjectsPage = checkProjectionWasAddedToUser(processedObjectsPage);
        checkMetadataChangesForShadow(processedObjectsPage);
    }

    @Test(groups = MODULE_5_GROUP)
    public void test4realCorrelationWithAD() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .tasks()
                .clickCreateTask()
                .reconciliationTask()
                .clickCreateTaskButton()
                .configuration()
                .name("Reconciliation with AD (real)")
                .next()
                .nextToSchedule()
                .next()
                .saveAndRun();
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .selectOperationStatisticsPanel()
                        .screenshot("test4realCorrelationWithAD_selectOperationStatisticsPanel");
                // check correlation results. The results are the same as during the simulations.
        basicPage
                .listUsers("Persons")
                //all linkable AD accounts are linked to their owners, 2 accounts are reported for all users (except 1002 - Ana Lopez)
                .table()
                .search()
                .byName()
                .inputValue("1006")
                .updateSearch()
                .and()
                .clickByName("1006")
                .selectProjectionsPanel()
                .table()
                .clickByName("cn=Martin Knight,ou=users,dc=example,dc=com")
                        .screenshot("test4realCorrelationWithAD_projectionsPanel");
                //click AD account to display user’s AD account attributes

        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .table()
                .search()
                .dropDownPanelByItemName("Situation")
                .inputDropDownValue("Unmatched")
                .updateSearch()
                .screenshot("test4realCorrelationWithAD_unmatchedAccounts");
                //All Unmatched accounts except cn=Secret Admin,ou=users,dc=example,dc=com are already marked from earlier steps
                //Any new Unmatched accounts (created meanwhile in AD) would have no marks
        // Ana Lopez will be resolved late
    }

    private List<SynchronizationSituationTransitionDto> prepareSyncSituationTransitionRecords() {
        List<SynchronizationSituationTransitionDto> list = new ArrayList();
        list.add(new SynchronizationSituationTransitionDto()
                .originalState(SynchronizationSituationTransitionDto.State.NO_RECORD)
                .synchronizationStart(SynchronizationSituationTransitionDto.State.UNLINKED)
                .synchronizationEnd(SynchronizationSituationTransitionDto.State.LINKED)
                .succeeded("39")
                .failed("0")
                .skipped("0")
                .total("39"));
        list.add(new SynchronizationSituationTransitionDto()
                .originalState(SynchronizationSituationTransitionDto.State.NO_RECORD)
                .synchronizationStart(SynchronizationSituationTransitionDto.State.UNMATCHED)
                .synchronizationEnd(SynchronizationSituationTransitionDto.State.UNMATCHED)
                .succeeded("5")
                .failed("0")
                .skipped("0")
                .total("5"));
        list.add(new SynchronizationSituationTransitionDto()
                .originalState(SynchronizationSituationTransitionDto.State.NO_RECORD)
                .synchronizationStart(SynchronizationSituationTransitionDto.State.NO_RECORD)
                .synchronizationEnd(SynchronizationSituationTransitionDto.State.NO_RECORD)
                .exclusionReason(SynchronizationSituationTransitionDto.State.PROTECTED)
                .succeeded("0")
                .failed("0")
                .skipped("1")
                .total("1"));
        return list;
    }

    private ProcessedObjectsPage checkProjectionWasAddedToUser(ProcessedObjectsPage processedObjectsPage) {
        return processedObjectsPage
                .table()
                .clickByName("1001 (Geena Green)")
                .changes()
                .assertItemExists("Projections")
                .assertNewValueExists("cn=Geena Green,ou=users,dc=example,dc=com")
                .and()
                .back();
    }

    private ProcessedObjectsPage checkMetadataChangesForShadow(ProcessedObjectsPage processedObjectsPage) {
        return processedObjectsPage
                .table()
                .search()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue("Shadow")
                .updateSearch()
                .and()
                .clickByName("cn=Geena Green,ou=users,dc=example,dc=com (Account cn=Geena Green,ou=users,dc=example,dc=com (default) on AD)")
                .changes()
                .advancedView()
                .showOperationalItems()
                .showOperationalItems("Metadata")
                .assertItemValueEquals("Metadata", "Modification channel", "http://midpoint.evolveum.com/xml/ns/public/common/channels-3#reconciliation")
                .assertItemExists("Metadata", "Modified at")
                .assertItemValueEquals("Metadata", "Modifier", "midPoint Administrator (administrator)")
                .assertItemValueEquals("Metadata", "Modified by task", "Reconciliation with AD - development simulation")
                .and()
                .back();
    }
}
