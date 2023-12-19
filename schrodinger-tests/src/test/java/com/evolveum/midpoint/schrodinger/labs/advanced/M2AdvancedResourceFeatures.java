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
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author honchar
 */
public class M2AdvancedResourceFeatures extends AbstractAdvancedLabTest {
    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M2/";
    private static final String M2_LAB_SOURCES_DIRECTORY = ADVANCED_LABS_DIRECTORY + "sources/";

    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File CONTRACTORS_SOURCE_FILE = new File(M2_LAB_SOURCES_DIRECTORY + "contractors-lab-2-initial.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_1 = new File(M2_LAB_SOURCES_DIRECTORY + "contractors-2-1-update-1.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_2 = new File(M2_LAB_SOURCES_DIRECTORY + "contractors-2-1-update-2.csv");
    private static final File CONTRACTORS_SOURCE_FILE_UPDATE_3 = new File(M2_LAB_SOURCES_DIRECTORY + "contractors-2-1-update-3.csv");
    private static final File OBJECT_TEMPLATE_EXAMPLE_CONTRACTOR_USER = new File(LAB_OBJECTS_DIRECTORY + "objecttemplates/object-template-example-contractor-user.xml");
    private static final File SYSTEM_CONFIGURATION_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE_LAB_2_2_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-lab-2-2-update-1.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE_LAB_2_5_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-lab-2-5-update-1.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_2_RESOURCE_FILE_LAB_2_2_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-lab-2-2-update-1.xml");
    private static final File CSV_2_RESOURCE_FILE_LAB_2_3_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-lab-2-3-update-1.xml");
    private static final File CSV_2_RESOURCE_FILE_LAB_2_4_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-lab-2-4-update-1.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File CSV_3_RESOURCE_FILE_LAB_2_2_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-lab-2-2-update-1.xml");
    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File CONTRACTORS_RESOURCE_FILE_2_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors-2-1.xml");
    private static final File OPENLDAP_CORPORATE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/openldap-new-corporate-directory.xml");

    private static final File CSV_1_SOURCE_FILE = new File(M2_LAB_SOURCES_DIRECTORY + "csv-1-lab-2-initial.csv");
    private static final File CSV_2_SOURCE_FILE = new File(M2_LAB_SOURCES_DIRECTORY + "csv-2-lab-2-initial.csv");
    private static final File CSV_3_SOURCE_FILE = new File(M2_LAB_SOURCES_DIRECTORY + "csv-3-lab-2-initial.csv");
    private static final File HR_SOURCE_FILE = new File(M2_LAB_SOURCES_DIRECTORY + "source-lab-2-initial.csv");
    private static final File HR_SOURCE_FILE_LAB_2_2_UPDATE_1 = new File(M2_LAB_SOURCES_DIRECTORY + "source-lab-2-2-update-1.csv");
    private static final File HR_SOURCE_FILE_LAB_2_2_UPDATE_2 = new File(M2_LAB_SOURCES_DIRECTORY + "source-lab-2-2-update-2.csv");
    private static final File HR_SOURCE_FILE_LAB_2_2_UPDATE_3 = new File(M2_LAB_SOURCES_DIRECTORY + "source-lab-2-2-update-3.csv");
    private static final File HR_SOURCE_FILE_LAB_2_4_UPDATE_1 = new File(M2_LAB_SOURCES_DIRECTORY + "source-lab-2-4-update-1.csv");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File RIMSY_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/rimsy-user.xml");
    private static final File HR_SYNCHRONIZATION_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/hr-synchronization.xml");
    private static final File INITIAL_IMPORT_FROM_HR_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/initial-import-from-hr.xml");

    private static File csv2AfterAddScript;
    private static File csv2AfterModifyScript;
    private static final String SCRIPTS_DIRECTORY = LAB_OBJECTS_DIRECTORY + "scripts/";
    private static final File CSV_2_AFTER_ADD_SCRIPT_FILE = new File(SCRIPTS_DIRECTORY + "csv2-after-add-script.sh");
    private static final File CSV_2_AFTER_MODIFY_SCRIPT_FILE = new File(SCRIPTS_DIRECTORY + "csv2-after-mod-script.sh");

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

        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(OPENLDAP_CORPORATE_RESOURCE_FILE, HR_RESOURCE_NAME);

        addObjectFromFile(INITIAL_IMPORT_FROM_HR_TASK_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M.getSeconds());
        addObjectFromFile(HR_SYNCHRONIZATION_TASK_FILE);

        addObjectFromFile(OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE);
    }

    @Override
    protected File getModuleInitialSystemConfigXml() {
        return SYSTEM_CONFIGURATION_FILE;
    }

    @Test(groups={"advancedM2"})
    public void mod02test01reactionSpecificObjectTemplate() throws IOException {
        getShadowTabTable(CONTRACTORS_RESOURCE_NAME)
                .selectCheckboxByName("9a0e3e60-21e4-11e8-b9b8-67f3338057d8")
                .importAccount();

        showUser("aperkeltini")
                .assertGivenName("Antonio")
                .assertFamilyName("Perkeltini")
                .assertFullName("Antonio Perkeltini")
                .selectAssignmentsPanel()
                    .table()
                        .assertVisibleObjectsCountEquals(0);

        showResource(CONTRACTORS_RESOURCE_NAME)
                .selectAccountsPanel()
                .tasks()
                    .clickCreateTask()
                        .importTask()
                        .clickCreateTaskButton()
                            .configuration()
                                    .name(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                                    .next()
                                .nextToSchedule()   //todo fix
                .next()
                            .saveAndRun()
                            .feedback()
                                .isInfo();
        basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                            .inputValue(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                            .updateSearch()
                        .and()
                    .clickByName(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                .selectOperationStatisticsPanel()
                    .assertProgressSummaryObjectsCountEquals(11)
                    .assertSucceededCountMatch(11);
        basicPage.listUsers("Externals")
                .table()
                    .assertVisibleObjectsCountEquals(11);

        //todo check notification file; password is generated message

        TaskPage task = basicPage.newTask("Live synchronization task");
        task
                .selectBasicPanel()
                    .form()
                        .addAttributeValue("Name", "Contractor DB Synchronization")
                        .addAttributeValue("Object class", "AccountObjectClass")
                        .selectOption("Recurrence","Recurring")
                        .selectOption("Binding","Tight")
                        .editRefValue("Object")
                            .selectType("Resource")
                            .table()
                                .clickByName("ExAmPLE, Inc. Contractor DB")
                        .and()
                    .and()
                .selectSchedulePanel()
                    .form()
                        .addAttributeValue("Interval", "5")
                        .and()
                    .and()
                .clickSaveAndRun()
                .feedback()
                    .isInfo();

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_1, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.listUsers("Externals")
                .table()
                    .search()
                        .byName()
                        .inputValue("aanderson")
                        .updateSearch()
                        .and()
                    .assertVisibleObjectsCountEquals(1)
                    .assertTableContainsLinkTextPartially("aanderson")
                    .assertTableContainsColumnWithValue("UserType.givenName", "Alice")
                    .assertTableContainsColumnWithValue("UserType.familyName", "Anderson");

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_2, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.listUsers("Externals")
                .table()
                    .search()
                        .byName()
                        .inputValue("aanderson")
                        .updateSearch()
                        .and()
                    .assertVisibleObjectsCountEquals(1)
                    .assertTableContainsLinkTextPartially("aanderson")
                    .assertTableContainsColumnWithValue("UserType.givenName", "Ann")
                    .assertTableContainsColumnWithValue("UserType.familyName", "Anderson");

        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("aanderson")
                .selectBasicPanel()
                    .form()
                        .assertPropertyDropdownValue("Administrative status", "Disabled");

        addObjectFromFile(OBJECT_TEMPLATE_EXAMPLE_CONTRACTOR_USER);
        addCsvResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE_2_1, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());

        //todo in labs desc: delete the row for Raymond Shelteron. BUT there is no such user. Elisabeth Smith is deleted instead
        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE_UPDATE_3, contractorsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("rshelteron")
                .selectBasicPanel()
                    .form()
                        .assertPropertyTextareaValueContainsText("Description", "Contractor disabled:")
                        .assertPropertyDropdownValue("Administrative status", "Disabled");

    }

    @Test(dependsOnMethods = {"mod02test01reactionSpecificObjectTemplate"}, groups={"advancedM2"})
    public void mod02test02iterators() throws IOException{
        addObjectFromFile(RIMSY_USER_FILE);
        showUser("rimsy")
                .selectAssignmentsPanel()
                    .clickAddAssignment()
                        .table()
                            .selectCheckboxByName("Internal Employee")
                            .and()
                        .clickAdd()
                        .and()
                    .clickSave()
                    .feedback()
                //todo fails because of MID-7009. actual result is success, because arimmer wasn't created while HR import task
                        .assertError()
                        .assertMessageExists("Error processing account");

        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE_LAB_2_2_UPDATE_1, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE_LAB_2_2_UPDATE_1, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_LAB_2_2_UPDATE_1, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("rimsy").selectAssignmentsPanel(), true, "Internal Employee");
        showUser("rimsy")
                .selectProjectionsPanel()
                .assertProjectionExist("arimmer1", "CSV-2 (Canteen Ordering System)")
                .assertProjectionExist("cn=Arnold J. Rimmer2,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                .assertProjectionExist("arimmer1", "CSV-1 (Document Access)");

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_2_2_UPDATE_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000997")
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Member","Internal Employee")
                    .and()
                .selectProjectionsPanel()
                    .assertProjectionExist("adewries1", "CSV-1 (Document Access)")
                    .assertProjectionExist("adewrie1", "CSV-2 (Canteen Ordering System)")
                    .assertProjectionExist("cn=Ann Dewries,ou=0110,ou=0100,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)");

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_2_2_UPDATE_2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000997")
                .selectProjectionsPanel()
                    .assertProjectionExist("adewries1", "CSV-1 (Document Access)")
                    .assertProjectionExist("adewrie1", "CSV-2 (Canteen Ordering System)")
                    .assertProjectionExist("cn=Ann De Wries2,ou=0110,ou=0100,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)");

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_2_2_UPDATE_3, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000997")
                .selectProjectionsPanel()
                    .assertProjectionExist("anewman", "CSV-1 (Document Access)")
                    .assertProjectionExist("anewman", "CSV-2 (Canteen Ordering System)")
                    .assertProjectionExist("cn=Ann De Wries2,ou=0110,ou=0100,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)");
    }

    @Test(dependsOnMethods = {"mod02test02iterators"}, groups={"advancedM2"})
    public void mod02test03provisioningDependencies() {
        //todo impossible to test because of openldap resource
    }

    @Test(dependsOnMethods = {"mod02test02iterators"}, groups={"advancedM2"})
    public void mod02test04provisioningScripts() throws IOException {
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE_LAB_2_4_UPDATE_1, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());

        String home = fetchMidpointHome();
        File scriptsDir = new File(home, "scripts");
        if (!scriptsDir.exists()) {
            if (!scriptsDir.mkdir()) {
                throw new IOException("Creation of directory " + scriptsDir.getAbsolutePath() + " unsuccessful");
            }
        }
        csv2AfterAddScript = new File(scriptsDir, "csv2-after-add-script.sh");
        FileUtils.copyFile(CSV_2_AFTER_ADD_SCRIPT_FILE, csv2AfterAddScript);

        csv2AfterModifyScript = new File(scriptsDir, "csv2-after-mod-script.sh");
        FileUtils.copyFile(CSV_2_AFTER_MODIFY_SCRIPT_FILE, csv2AfterModifyScript);

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_2_4_UPDATE_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("kkochanski")
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Member", "Internal Employee");

        //todo check log files created by scripts
    }

    @Test(dependsOnMethods = {"mod02test04provisioningScripts"}, groups={"advancedM2"})
    public void mod02test05delayedAccountDeletion() throws IOException {
        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE_LAB_2_5_UPDATE_1, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());

        showUser("picard")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Member", "Internal Employee")
                .table()
                    .removeByName("Internal Employee")
                    .and()
                .and()
                .clickSave()
                    .feedback()
                    .assertSuccess();

        showUser("picard")
                .selectProjectionsPanel()
                .assertProjectionDisabled("jpicard", "CSV-2 (Canteen Ordering System)")
                .assertProjectionDisabled("cn=Jean-Luc PICARD,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                .assertProjectionDisabled("jpicard", "CSV-1 (Document Access)");

        basicPage
                .listRepositoryObjects()
                    .table()
                        .showObjectInTableByTypeAndName("Shadow", "jpicard")
                        .clickByName("jpicard");
        //todo check shadow contains
        /**
         * <trigger id="3">
         *
         * <timestamp>2019-09-23T16:23:28.970+02:00</timestamp>
         *
         * <handlerUri>http://midpoint.evolveum.com/xml/ns/public/model/trigger/
         * recompute/handler-3</handlerUri>
         *
         * <originDescription>Delayed delete after account is unassigned
         * and disabled</originDescription>
         *
         * </trigger>
         */

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_LONG_3_M.getSeconds());

        showUser("picard")
                .selectProjectionsPanel()
                .assertProjectionForResourceDoesntExist("CSV-2 (Canteen Ordering System)");

        /**
         * todo check notifications
         * Mon Sep 23 16:23:43 CEST 2019
         * Message{to='[idm@example.com]', cc='[]', bcc='[]',
         *  subject='[IDM] SUCCESS: account DELETE operation succeeded for picard',
         *  contentType='null', body='Notification about account-related operation
         * User: Jean-Luc Picard (picard, oid 654ca8cf-98cf-437e-9e01-176b6a369b5c)
         * Notification created on: Mon Sep 23 16:23:43 CEST 2019
         * Resource: CSV-1 (Document Access) (oid 10000000-9999-9999-0000-a000ff000002)
         * Account: jpicard
         * The account has been successfully removed from the resource.
         * Requester: midPoint Administrator (administrator)
         * Channel: null
         * ', attachmentsCount: 0
         */
    }

}
