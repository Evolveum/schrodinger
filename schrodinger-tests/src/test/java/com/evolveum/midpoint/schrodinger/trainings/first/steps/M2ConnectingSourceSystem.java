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
import com.evolveum.midpoint.schrodinger.page.resource.wizard.DiscoveryWizardStep;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class M2ConnectingSourceSystem extends AbstractTrainingTest {

    @BeforeClass(alwaysRun = true, dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        hrCsvFile = new File(getTestTargetDir(), CSV_HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_CSV_SOURCE_FILE, hrCsvFile);
    }


    @Test(groups = MODULE_2_GROUP)
    public void test1createHrResource() {
        String csvFilePath = hrCsvFile.getAbsolutePath();
        basicPage
                .newResource()
                .fromScratch()
                .selectCsvConnector()
                .name("HR")
                .lifecycle("Proposed")
                .next()
                .filePath(csvFilePath)
                .next()
                .uniqueAttributeName("empnum")
                .next()
                .createResource()
                .assertResourceIsCreated("HR")
                .previewResourceData()
                .screenshot()
                .assertTableContainsObjects(20)
                .clickBack()
                .configureObjectTypes()
                .addObjectType()
                .displayName("HR Person")
                .kind("Account")
                .setDefault("True")
                .next()
                .objectClass("AccountObjectClass")
                .next()
                .type("User")
                .saveSettings()
                .previewData()
                .assertTableColumnContainsValue("Identifiers", "empnum: 8000")
                .assertTableColumnContainsValue("Identifiers", "empnum: 8001")
                .assertTableColumnContainsValue("Identifiers", "empnum: 8002")
                .assertTableColumnContainsValue("Identifiers", "empnum: 8003")
                .clickBack()
                .configureBasicAttributes()
                .next()
                .filter("attributes/empnum not startsWith \"8\"")
                .next()
                .saveSettings()
                .backToObjectTypes()
                .exitWizard()
                .goToResource()
                .selectResourceObjectsPanel()
                .reclassify()
                .and()
                .selectAccountsPanel()
                .assertTableDoesntContainColumnWithValue("Identifiers", "empnum: 8000")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empnum: 8001")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empnum: 8002")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empnum: 8003");
    }

    @Test(groups = MODULE_2_GROUP)
    public void test2configureHrResource() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureSynchronization()
                .addReaction()
                .name("unmatched-add")
                .situation("Unmatched")
                .action("Add focus")
                .lifecycleState("Active")
                .and()
                .addReaction()
                .name("linked-synchronize")
                .situation("Linked")
                .action("Synchronize")
                .lifecycleState("Active")
                .and()
                .saveSynchronizationSettings()
                //todo check Active lifecycle for mappings and Proposed for resource
                .configureMappings()
                .inboundMappings()
                .addInbound()
                .name("empnum-to-name")
                .fromResourceAttribute("empnum")
                .expression("As is")
                .target("name")
                .lifecycleState("Active")
                .and()
                .addInbound()
                .name("empnum-to-persNumber")
                .fromResourceAttribute("empnum")
                .expression("As is")
                .screenshot()
                .target("personalNumber")
                .lifecycleState("Active")
                .and()
                .addInbound()
                .name("firstname-to-givenName")
                .fromResourceAttribute("firstname")
                .expression("As is")
                .target("givenName")
                .lifecycleState("Active")
                .and()
                .addInbound()
                .name("surname-to-familyName")
                .fromResourceAttribute("surname")
                .expression("As is")
                .target("familyName")
                .lifecycleState("Active")
                .and()
                .and()
                .saveMappings()
                .and()
                .selectDetailsPanel()
                .changeCapabilityState("Create")
                .changeUpdateCapabilityState(Collections.singletonMap("Enabled", "False"))
                .changeCapabilityState("Delete")
                .and()
                .clickSave();
    }

}
