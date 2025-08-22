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
                .and()
                .table()
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
                .viewAllProcessedObjects()
                .table()
                .search()
                .byName()
                .inputValue("Ana Lopez")
                .updateSearch()
                .and()
                .clickByName("Ana Lopez")
                .changes()
                .expandVisualizationPanel()
                .screenshot("M7_simulation_task_ana_lopez");
                //todo finish assertions
      /*      *
           * the Simulation results show:
         * AD employeeNumber attribute is being updated for Ana Lopez
         * no passwords are going to be changed
         * no accounts are going to be deleted
            */
    }

    @Test(groups = MODULE_7_GROUP)
    public void test3ADProvisioning() {
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
                .changeLifecycleStateForSelectedRows("Active (production)")
                .and()
                .and()
                .saveMappings()
                .configureActivation()
                .outbound()
                .setLifecycleStateValue("set-account-status-based-on-midpoint-user", "Active (production)")
                .setLifecycleStateValue("Disable instead of delete", "Active (production)")
                .setLifecycleStateValue("Delayed delete", "Active (production)")
                .and()
                .saveSettings()
                .configureCredentials()
                .outbound()
                .setLifecycleStateValue("initial-passwd-generate", "Active (production)")
                .setLifecycleStateValue("passwd-change", "Active (production)")
                .and()
                .saveSettings()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - production simulation")
                .clickRunNowAndWaitToBeClosed()
                .showSimulationResult()
                .simulationTaskDetails()
                .screenshot("M7_production_simulation_task");
                //todo assertions * the Simulation results show:
        //         * 8 objects to be updated in AD just like before
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .clickRunNowAndWaitToBeClosed();
        basicPage
                .auditLogViewer()
                .table()
                .screenshot("M7_reconciliation_with_ad_real_audit_log_viewer");
        /**
            *
         *
         * edit and run Reconciliation with AD (real) task again using Run now and wait for the task completion (task status: closed)
         *
         *
         * go to Audit log viewer and check what has happened (8 AD accounts modifications should be displayed)
         *
         *
         *
         * click the value in Time column to display the change from audit log
         *
         *
         * click the value in Target value to display the object in its current state
         *
         *
         * each event is recorded multiple times. For example, try to check what has happened for user alopez (and her account cn=Ana Lopez,ou=users,dc=example,dc=com):
         *
         *
         * event stage Request contains the information about what was requested from midPoint (in the case of reconciliation, you will not find much information here)
         *
         *
         *
         * event stage Execution contains the information about what midPoint has executed as seen from the Model component perspective (User and his/her account). Employee number change is recorded here.
         *
         *
         * event stage Resource contains the information about what midPoint has executed as seen from the Provisioning component perspective (account).  Employee number change is recorded here.

         * todo what to do with AD checks?
         * In your browser with AD LDAP browser:
         * expand dc=example,dc=com
         * expand ou=users
         * you may need to click refresh button above the directory tree
         * click any account from the previously updated ones, for example:
         * cn=Ana Lopez should have her employeeNumber: 1002
         * cn=Jane Anderson and cn=Laura Shepherd should be disabled (simulated by roomNumber: disabled)
         * From now on, provisioning to AD resource is active for all attributes with mappings with Active lifecycle status, account activation status and credentials.
         * Automatic synchronization between HR and midPoint is not yet configured.
         */
    }
}
