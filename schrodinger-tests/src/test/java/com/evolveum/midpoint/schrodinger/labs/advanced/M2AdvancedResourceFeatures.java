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

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author honchar
 */
public class M2AdvancedResourceFeatures extends AbstractAdvancedLabTest {
    protected static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M2/";

    private static final File CONTRACTORS_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "contractors.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_1 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "contractors-2-1-update-1.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_2 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "contractors-2-1-update-2.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_3 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "contractors-2-1-update-3.csv");
    protected static final String CONTRACTORS_FILE_SOURCE_NAME = "contractors.csv";
    protected static final String CONTRACTORS_RESOURCE_NAME = "ExAmPLE, Inc. Contractor DB";
    protected static final String CONTRACTORS_RESOURCE_IMPORT_TASK_NAME = "Initial import from Contractor DB";
    private static final File OBJECT_TEMPLATE_EXAMPLE_CONTRACTOR_USER = new File(LAB_OBJECTS_DIRECTORY + "objecttemplates/object-template-example-contractor-user.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_1_4 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-1-4.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File CONTRACTORS_RESOURCE_FILE_2_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors-2-1.xml");

    protected static final File CSV_1_SOURCE_FILE = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "csv-1.csv");
    protected static final File CSV_2_SOURCE_FILE = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "csv-2.csv");
    protected static final File CSV_3_SOURCE_FILE = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "csv-3.csv");
    protected static final File HR_SOURCE_FILE = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "source.csv");

    protected static File contractorsTargetFile;

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
        addResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());

        getShadowTabTable(CONTRACTORS_RESOURCE_NAME)
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

        TaskPage task = basicPage.newTask();
        task.setHandlerUriForNewTask("Live Synchronization Task");
        basicPage.newTask()
                .selectTabBasic()
                    .form()
                        .addAttributeValue("Name", "Contractor DB Synchronization")
                        .addAttributeValue("Object Class", "AccountObjectClass")
                        .selectOption("Recurrence","Recurring")
                        .selectOption("Binding","Tight")
                        .editRefValue("Resource reference")
                            .table()
                                .selectCheckboxByName("ExAmPLE, Inc. Contractor DB")
                                .and()
                            .clickAddButton()
                        .and()
                    .and()
                .selectScheduleTab()
                    .form()
                        .addAttributeValue("Interval", "5")
                        .and()
                    .and()
                .clickSave()
                .feedback()
                    .isSuccess();

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_1, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        basicPage.listUsers("Externals")
                .table()
                    .search()
                        .byName()
                        .inputValue("aanderson")
                        .updateSearch()
                        .and()
                    .assertTableObjectsCountEquals(1)
                    .assertTableContainsLinkTextPartially("aanderson")
                    .assertTableContainsColumnWithValue("UserType.givenName", "Alice")
                    .assertTableContainsColumnWithValue("UserType.familyName", "Anderson");

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_2, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        basicPage.listUsers("Externals")
                .table()
                    .search()
                        .byName()
                        .inputValue("aanderson")
                        .updateSearch()
                        .and()
                    .assertTableObjectsCountEquals(1)
                    .assertTableContainsLinkTextPartially("aanderson")
                    .assertTableContainsColumnWithValue("UserType.givenName", "Ann")
                    .assertTableContainsColumnWithValue("UserType.familyName", "Anderson");

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        showUser("aanderson")
                .selectTabBasic()
                    .form()
                        .assertPropertyDropdownValue("Administrative status", "Disabled");

        addObjectFromFile(OBJECT_TEMPLATE_EXAMPLE_CONTRACTOR_USER);
        addResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE_2_1, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());

        //todo in labs desc: delete the row for Raymond Shelteron. BUT there is no such user. Elisabeth Smith is deleted instead
        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_3, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        showUser("esmith")
                .selectTabBasic()
                    .form()
                        .assertPropertyDropdownValue("Administrative status", "Disabled");

    }


}
