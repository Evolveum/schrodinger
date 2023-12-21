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
                .assertProcessedObjectIsMarked("geena (Geena Green)", "Focus renamed");
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectDefinedTasksPanel()
                .table()
                .clickByName("Reconciliation with AD - development simulation")
                .clickRunNowAndWait()
                .showSimulationResult()
                .assertMarkValueEquals("Focus renamed", 39)
                .selectMark("Focus renamed")
                .table()
                .assertAllObjectsAreMarked("Focus renamed");
    }
}
