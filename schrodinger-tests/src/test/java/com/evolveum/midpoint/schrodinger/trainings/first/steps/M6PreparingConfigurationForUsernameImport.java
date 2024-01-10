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
import com.evolveum.midpoint.schrodinger.component.task.dto.ExecutedActionDto;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

import java.util.List;

public class M6PreparingConfigurationForUsernameImport extends AbstractTrainingTest {

    @Test(groups = MODULE_6_GROUP)
    public void test1preparingConfigurationForUsernameImport() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .table()
                .editMapping("empnum-to-name")
                .strength("Weak")
                .next()
                .clickDone()
                .saveMappings();

        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .addInbound()
                .name("mapping-inbound-username-to-name-for-import")
                .fromResourceAttribute("uid")
                .expression("As is")
                .target("name")
                .lifecycleState("Proposed (simulation)")
                .and()
                .and()
                .saveMappings();
    }

    @Test(groups = MODULE_6_GROUP)
    public void test2usernameImportSimulation() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .table()
                .search()
                .byName()
                .inputValue("cn=Geena Green,ou=users,dc=example,dc=com")
                .updateSearch()
                .and()
                .importPreview("Name", "cn=Geena Green,ou=users,dc=example,dc=com")
                .selectTaskExecutionMode("Simulated development")
                .select()
                .table()
                .search()
                .resetBasicSearch()
                .and()
                .assertProcessedObjectIsMarked("geena (Geena Green)", "Focus renamed");
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .clickRunNowAndWaitToBeClosed()
                .showSimulationResult()
                .assertMarkValueEquals("Focus renamed", 39)
                .selectMark("Focus renamed")
                .table()
                .assertAllObjectsAreMarked("Focus renamed");
    }

    @Test(groups = MODULE_6_GROUP)
    public void test3usernameImportFromAD() {
        basicPage
                .listUsers("Persons")
                .table()
                .screenshot("users_before_rename");
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .table()
                .lifecycleState("mapping-inbound-username-to-name-for-import", "Active (production)")
                .and()
                .and()
                .saveMappings()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .clickRunNowAndWaitToBeClosed();
        basicPage
                .listUsers("Persons")
                .table()
                .screenshot("users");

        /**
         * todo
         * All users with linked AD account are now renamed in midPoint.
         * The only exception is user 1002 (Ana Lopez) for whom the correlation has failed and does not have a linked AD account.
         * Her AD account is still Unmatched and marked  Correlate later.
         * We will resolve this in later labs.
         * We wanted to emphasize that we can continue the deployment using First steps methodology even if the data is not ideal.
         */
    }

    @Test(groups = MODULE_6_GROUP)
    public void test4deletingOrphanedADAccounts() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureSynchronization()
                .setLifecycleStateValueForSituation("Unmatched", "Active (production)")
                .saveSynchronizationSettings();
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - production simulation")
                .clickRunNowAndWaitToBeClosed()
                .showSimulationResult()
                .simulationTaskDetails()
                .assertModifiedObjectsValueEquals("0")
                .assertDeletedObjectsValueEquals("1")
                .and()
                .assertMarkValueEquals("Projection deactivated", 1)
                .assertMarkValueEquals("Resource object affected", 1)
                .selectMark("Projection deactivated")
                .table()
                .assertVisibleObjectsCountEquals(1)
                .assertTableContainsText("cn=Secret Admin,ou=users,dc=example,dc=com");

        List<ExecutedActionDto> executedActionRecords = List.of(new ExecutedActionDto("Shadow", "Delete",
                "Reconciliation", "1",
                "cn=Secret Admin,ou=users,dc=example,dc=com (ACCOUNT - default - inetOrgPerson)",
                "", ""));
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .selectOperationStatisticsPanel()
                .assertAllExecutedActionsRecordsMatch(executedActionRecords)
                .and()
                .backToResourcePage()
                .selectAccountsPanel()
                .table()
                .search()
                .dropDownPanelByItemName("Situation")
                .inputDropDownValue("Unmatched")
                .updateSearch()
                .and()
                .assertRealObjectIsMarked("cn=Ana Lopez,ou=users,dc=example,dc=com", "Correlate later")
                .assertRealObjectIsMarked("cn=Mail Service Account,ou=users,dc=example,dc=com", "Protected")
                .assertRealObjectIsMarked("cn=Spam Assassin Service Account,ou=users,dc=example,dc=com", "Protected")
                .assertRealObjectIsMarked("cn=Test123,ou=users,dc=example,dc=com", "Do not touch")
                .assertTableDoesntContainColumnWithValue("Name", "cn=Secret Admin,ou=users,dc=example,dc=com");
    }

    @Test(groups = MODULE_6_GROUP)
    public void test5finalizeCorrelation() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .table()
                .search()
                .byName()
                .inputValue("cn=Ana Lopez,ou=users,dc=example,dc=com")
                .updateSearch()
                .and()
                .and()
                .configureCorrelation()
                .table()
                .setEnabled("last-resort-correlation", "True")
                .and()
                .saveCorrelationSettings()
                .table()
                .removeMarks("Name", "cn=Ana Lopez,ou=users,dc=example,dc=com", "Correlate later")
                .and()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .clickRunNowAndWaitToBeClosed()
                .backToResourcePage()
                .selectAccountsPanel()
                .table()
                .assertSituationEquals("cn=Ana Lopez,ou=users,dc=example,dc=com", "DISPUTED")
                .and()
                .configureSynchronization()
                .addReaction()
                .name("disputed-create-case")
                .situation("Disputed")
                .action("Create correlation case")
                .lifecycleState("Active")
                .and()
                .saveSynchronizationSettings()
                .and()
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD (real)")
                .clickRunNowAndWaitToBeClosed();
        basicPage
                .myWorkItems()
                .table()
                .clickByName("Correlation of account 'cn=Ana Lopez,ou=users,dc=example,dc=com' on AD")
                .detailsPanel()
                .deltasToBeApprovedTable()
                .assertCorrelationCandidate1Exists("Ana Lopez (1002)", "Ana", "Lopez",
                        "Hot Lava City", "1002")
                .clickCorrelateButtonForCandidate1()
                .feedback()
                .assertSuccess();

        basicPage
                .listUsers("Persons")
                .table()
                .search()
                .byName()
                .inputValue("alopez")
                .updateSearch()
                .and()
                .clickByName("alopez")
                .selectProjectionsPanel()
                .table()
                .screenshot("M6_projections")
                .assertVisibleObjectsCountEquals(2);

        /**
         * user alopez (formerly 1002, now renamed) has her AD account linked and visible in Projections panel. AD’s employeeNumber is still incorrect, but will be fixed when we enable provisioning to AD in later labs
         */
    }
}
