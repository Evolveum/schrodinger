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
import com.evolveum.midpoint.schrodinger.component.task.OperationStatisticsPanel;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

public class M5TargetSystemIntegration extends AbstractTrainingTest {

    @Test(groups = MODULE_5_GROUP)
    public void test00100simulatedCorrelationWithAD() {
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
        OperationStatisticsPanel operationStatisticsPanel = basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .selectOperationStatisticsPanel();
        //todo check:
        // 39 accounts previously not linked are now linked to midPoint users; final situation is Linked
        //5 accounts previously not linked are still not linked to midPoint users; final situation is Unmatched - these are orphaned accounts
        //1 account is protected (within the configuration of resource copied from resource template)
        operationStatisticsPanel
                .and()
                .showSimulationResult();
        //todo check
        // 5 deactivated accounts (to be deleted) including Ana Lopez (company CFO, we need to be careful here!)
        //78 modified objects, where:
        //midPoint users indicate added Projection (as a result of correlation of the account and linking it to its owner)
        //AD accounts indicate metadata changes (in midPoint repository only)
    }

    @Test(groups = MODULE_5_GROUP)
    public void test00200markingAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .showSimulationResult();

    }

    @Test(groups = MODULE_5_GROUP)
    public void test00300ignoringOrphanedAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .setLifecycleState("Active (Production)")
                .selectSchemaHandlingPanel();
        Selenide.screenshot("test00300ignoringOrphanedAccounts");

    }
}
