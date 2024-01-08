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

public class M7EnableProvisioningToTargetSystem extends AbstractTrainingTest {

    @Test(groups = MODULE_7_GROUP)
    public void test1reviewingADResourceProvisioningConfiguration() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureMappings()
                .outboundMappings()
                .table()
                .assertVisibleObjectsCountEquals(12)    //todo check if 12 is right number
                .assertAllLifecycleStateValuesEqual("Draft")
                .and()
                .and()
                .exitWizard()
                .configureActivation()
                .outbound()
                .assertTilesNumberEquals(3)
                .assertTilesLifecycleStateValueEquals("set-account-status-based-on-midpoint-user", "Draft")
                .assertTilesLifecycleStateValueEquals("Disable instead of delete", "Draft")
                .assertTilesLifecycleStateValueEquals("Delayed delete", "Draft")
                .scenarioConfigurationSettings("Delayed delete")
                .assertPropertyInputValueEquals("Delete after", "5")
                .assertPropertyDropdownValueEquals("Delete after", "Minutes")
                .done()
                .and()
                .exitWizard()
                .configureCredentials()
                .outbound()
                .assertTilesNumberEquals(2)
                .assertTilesLifecycleStateValueEquals("initial-passwd-generate", "Draft")
                .assertTilesLifecycleStateValueEquals("passwd-change", "Draft")
                .and()
                .exitWizard();
    }

    @Test(groups = MODULE_7_GROUP)
    public void test2ADProvisioningSimulation() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureMappings()
                .outboundMappings()
                .table()
                .selectRowByName("mapping-dn")
                .selectRowByName("mapping-cn-weak")
                .selectRowByName("mapping-displayName")
                .selectRowByName("mapping-sn")
                .selectRowByName("mapping-givenName")
                .selectRowByName("mapping-uid")
                .selectRowByName("mapping-l")
                .selectRowByName("mapping-employeeNumber")
                .changeLifecycleStateForSelectedRows("Proposed (simulation)")
                .and()
                .and()
                .saveMappings()
                .configureActivation()
                .outbound()
                .setLifecycleStateValue("set-account-status-based-on-midpoint-user", "Proposed (simulation)")
                .setLifecycleStateValue("Disable instead of delete", "Proposed (simulation)")
                .setLifecycleStateValue("Delayed delete", "Proposed (simulation)")
                .and()
                .saveSettings()
                .configureCredentials()
                .outbound()
                .setLifecycleStateValue("initial-passwd-generate", "Proposed (simulation)")
                .setLifecycleStateValue("passwd-change", "Proposed (simulation)")
                .and()
                .saveSettings()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .clickRunNowAndWaitToBeClosed()
                .showSimulationResult()
                .assertMarkValueEquals("Projection renamed", 5)
                .assertMarkValueEquals("Projection deactivated", 2)
                .assertMarkValueEquals("Projection password changed", 0)    //todo is it correct to check projection password or user password should be checked?
                .simulationTaskDetails()
                .viewAllProcessedObjects();
                //todo finish assertions
      /*      *
           * the Simulation results show:
         * AD employeeNumber attribute is being updated for Ana Lopez
         * no passwords are going to be changed
         * no accounts are going to be deleted
            */
    }
}
