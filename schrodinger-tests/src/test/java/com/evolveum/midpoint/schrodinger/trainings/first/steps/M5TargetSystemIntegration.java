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
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .selectOperationStatisticsPanel();
    }
}
