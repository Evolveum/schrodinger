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
        basicPage
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
                .assertTableContainsText("Anna Lopez")
                .and()
                .backToSimulationResultPage()
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("78")
                .viewModifiedObjects()
                .table()
                .clickByName("1001 (Geena Green)")
                .changes()
                .assertItemValueEquals("Projections", "cn=Geena Green,ou=users,dc=example,dc=com")
                .and()
                .back()
                .table()
                .search()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue("Shadow")
                .updateSearch()
                .and()
                .clickByName("cn=Geena Green,ou=users,dc=example,dc=com (Account cn=Geena Green,ou=users,dc=example,dc=com (default) on AD)")
                .changes();
        //todo check
        //AD accounts indicate metadata changes (in midPoint repository only)
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
                .markAsProtected("cn=Spam Assassin Service Account,ou=users,dc=example,dc=com");
        //todo the processed object list immediately refreshes to show the marks
        //(also Resource Accounts page now shows the marks)

        basicPage
                .listTasks()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .clickRunNowAndWait()
                .showSimulationResult()
                .assertMarkValueEquals("Projection deactivated", 1)
                .simulationTaskDetails()
                .assertDeletedObjectsValueEquals("1")
                .viewDeletedObjects()
                .table()
                .assertTableContainsText("cn=Secret Admin,ou=users,dc=example,dc=com")
                .and()
                .backToSimulationResultPage()
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("78");
        //todo check midPoint users indicate added Projection (as a result of correlation of the account and linking it to its owner)
        //AD accounts indicate metadata changes (in midPoint repository only)
    }

    @Test(groups = MODULE_5_GROUP)
    public void test3ignoringOrphanedAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .setLifecycleState("Active (production)")
                .selectSchemaHandlingPanel();

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
}
