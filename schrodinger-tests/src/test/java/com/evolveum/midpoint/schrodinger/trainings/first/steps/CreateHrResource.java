/*
 * Copyright (c) 2023 Evolveum
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
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class CreateHrResource extends AbstractTrainingTest {

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        hrCsvFile = new File(getTestTargetDir(), CSV_HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_CSV_SOURCE_FILE, hrCsvFile);
    }


    @Test
    public void createHrResource() {
        String csvFilePath = hrCsvFile.getAbsolutePath();
        basicPage
                .newResource()
                .createResourceFromScratch("CsvConnector")
                .name("HR")
                .next()
                .filePath(csvFilePath)
                .next()
                .uniqueAttributeName("empNumber")
                .next()
                .createResource()
                .assertResourceIsCreated()
                // todo NOTE: all accounts are with `kind=unknown` so far
                .previewResourceData()
                .assertTableContainsObjects(20)
                //todo ask Ivan where to go to check Former status? resource data preview table doesn't contain this column
//                .assertTableColumnContainsValue("status", "Former")
                .exitWizard()
                .goToResource()
                .switchToDevelopmentMode()
                .selectSchemaHandlingPanel()
                .addNewObjectType()
                .displayName("HR Person")
                .kind("Account")
                .setDefault("True")
                .next()
                .objectClass("AccountObjectClass")
                .next()
                .type("User")
                .saveSettings()
                .previewDataOfObjectType()
                .assertTableColumnContainsValue("Identifiers", "empNumber: 8000")
                .assertTableColumnContainsValue("Identifiers", "empNumber: 8001")
                .assertTableColumnContainsValue("Identifiers", "empNumber: 8002")
                .assertTableColumnContainsValue("Identifiers", "empNumber: 8003")
                .exitWizard()
                .configureBasicAttributes()
                .next()
                .filter("attributes/empNumber not startsWith \"8\"")
                .next()
                .saveSettings()
                .goToResource()
                .selectAccountsPanel()
                .reclassify()
                .refresh()
                .assertTableDoesntContainColumnWithValue("Identifiers", "empNumber: 8000")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empNumber: 8001")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empNumber: 8002")
                .assertTableDoesntContainColumnWithValue("Identifiers", "empNumber: 8003")
                .configureSynchronization()
                .addSimpleReaction()
                .name("unmatched-add")
                .situation("Unmatched")
                .action("Add focus")
                .and()
                .addSimpleReaction()
                .name("unlinked-link")
                .situation("Unlinked")
                .action("Link")
                .and()
                .addSimpleReaction()
                .name("linked-synchronize")
                .situation("Linked")
                .action("Synchronize")
                .and()
                .addSimpleReaction()
                .name("deleted-inactivate")
                .situation("Deleted")
                .action("Inactivate focus")
                .and()
                .saveSynchronizationSettings()
                .configureMappings()
                .inboundMappings()
                .addInbound()
                .name("empNumber-to-name")
                .ref("empNumber")
                .expression("As is")
                .target("name")
                .and()
                .addInbound()
                .name("empNumber-to-employeeNumber")
                .ref("empNumber")
                .expression("As is")
                .target("employeeNumber")
                .and()
                .addInbound()
                .name("firstname-to-givenName")
                .ref("firstname")
                .expression("As is")
                .target("givenName")
                .and()
                .addInbound()
                .name("lastname-to-familyName")
                .ref("lastname")
                .expression("As is")
                .target("familyName")
                .and()
                .and()
                .saveMappings();
    }
}