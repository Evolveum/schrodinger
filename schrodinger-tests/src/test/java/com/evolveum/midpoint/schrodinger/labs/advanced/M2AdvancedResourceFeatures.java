/*
 * Copyright (c) 2010-2021 Evolveum
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
package com.evolveum.midpoint.schrodinger.labs.advanced;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author honchar
 */
public class M2AdvancedResourceFeatures extends AbstractAdvancedLabTest {
    protected static final String LAB_OBJECTS_DIRECTORY = LAB_ADVANCED_DIRECTORY + "M2/";

    private static final File CONTRACTORS_SOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    protected static final String CONTRACTORS_FILE_SOURCE_NAME = "localhost-contractors.xml";
    protected static final String CONTRACTORS_RESOURCE_NAME = "ExAmPLE, Inc. Contractor DB";
    protected static final String CONTRACTORS_RESOURCE_IMPORT_TASK_NAME = "Initial import from Contractor DB";
    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_1_4 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-1-4.xml");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);
        csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);
        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

        hrTargetFile = new File(getTestTargetDir(), HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_SOURCE_FILE, hrTargetFile);

        contractorsTargetFile = new File(getTestTargetDir(), CONTRACTORS_FILE_SOURCE_NAME);
        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE, contractorsTargetFile);
    }

    @Test(groups={"advancedM2"})
    public void mod02test01reactionSpecificObjectTemplate() throws IOException {
        addResourceFromFileAndTestConnection(CSV_1_SOURCE_FILE, CSV_1_FILE_SOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_SOURCE_FILE, CSV_2_FILE_SOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_SOURCE_FILE, CSV_3_FILE_SOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CONTRACTORS_SOURCE_FILE, CONTRACTORS_FILE_SOURCE_NAME, contractorsTargetFile.getAbsolutePath());

        getShadowTable(CONTRACTORS_RESOURCE_NAME, "cid", "001212")
                .selectCheckboxByName("9a0e3e60-21e4-11e8-b9b8-67f3338057d8")
                .clickImport();

        showUser("aperkeltini")
                .assertGivenName("Antonio")
                .assertFamilyName("Perkeltini")
                .assertFullName("Antonio Perkeltini")
                .selectTabAssignments()
                    .table()
                        .assertTableObjectsCountEquals(0);

        showResource(CONTRACTORS_RESOURCE_NAME)
                .clickAccountsTab()
                    .importTask()
                        .clickCreateNew()
                            .selectTabBasic()
                                .form()
                                    .addAttributeValue("Name", CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                                    .and()
                                .and()
                            .clickSave()
                            .feedback()
                                .isSuccess();
        basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                            .inputValue(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                            .updateSearch()
                        .and()
                    .clickByName(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                .selectTabOperationStatistics()
                    .assertSuccessfullyProcessedCountMatch(11);
        basicPage.listUsers("Externals")
                .table()
                    .assertTableObjectsCountEquals(11);

        //todo check notification file; password is generated message
    }


}
