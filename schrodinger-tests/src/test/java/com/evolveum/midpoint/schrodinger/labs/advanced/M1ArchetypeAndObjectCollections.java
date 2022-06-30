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
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import com.evolveum.midpoint.schrodinger.scenarios.ScenariosCommons;

import com.evolveum.midpoint.xml.ns._public.common.common_3.TaskType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author honchar
 */
public class M1ArchetypeAndObjectCollections extends AbstractAdvancedLabTest {
    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M1/";
    private static final String ADVANCED_LABS_SOURCES_DIRECTORY = ADVANCED_LABS_DIRECTORY + "sources/";

    private static final Logger LOG = LoggerFactory.getLogger(M1ArchetypeAndObjectCollections.class);

    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File KIRK_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-user.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_1 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "source-1.3-update-1.csv");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_2 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "source-1.3-update-2.csv");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_3 = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "source-1.3-update-3.csv");
    private static final File SYSTEM_CONFIGURATION_FILE_1_4 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-1-4.xml");
    private static final File CSV_1_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-1-lab-1-initial.csv");
    private static final File CSV_2_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-2-lab-1-initial.csv");
    private static final File CSV_3_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-3-lab-1-initial.csv");
    private static final File HR_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "source-lab-1-initial.csv");

    String notificationCheck1 = "User: Jim Kirk (kirk, oid ca233e9a-f474-1ed7-9a22-1nkdea34bb50)";
    String notificationCheck2 = "The user record was modified. Modified attributes are:";

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        LOG.info("Test target dir path: " + getTestTargetDir().getAbsolutePath());
        try {
            csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
            FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);
            csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
            FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);
            csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
            FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

            hrTargetFile = new File(getTestTargetDir(), HR_FILE_SOURCE_NAME);
            FileUtils.copyFile(HR_SOURCE_FILE, hrTargetFile);
        } catch (IOException e) {
            LOG.error("Cannot copy resource source file, {}", e);
        }
        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(KIRK_USER_FILE);
    }

    @Test(groups={"advancedM1"})
    public void mod01test01environmentInitialization() throws IOException {
        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("kirk").selectAssignmentsPanel(), true,  "Internal Employee");
        showUser("kirk")
                        .selectProjectionsPanel()
                        .assertProjectionExist("jkirk", "CSV-2 (Canteen Ordering System)")
                        .assertProjectionExist("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                        .assertProjectionExist("jkirk", "CSV-1 (Document Access)");

        assertLastNotificationBodyContains(notificationFile, notificationCheck1);
        assertLastNotificationBodyContains(notificationFile, notificationCheck2);
        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("kirk").selectAssignmentsPanel(), true,  "Secret Projects I", "Secret Projects II");
        assertLastNotificationBodyContains(notificationFile, notificationCheck1);
        assertLastNotificationBodyContains(notificationFile, notificationCheck2);
        showUser("kirk")
                .selectProjectionsPanel()
                    .viewProjectionDetails("jkirk", "CSV-1 (Document Access)")
                        .assertPropertyInputValues("Groups", "Internal Employees", "Essential Documents",
                                "Time Travel", "Teleportation", "Lucky Numbers", "Presidential Candidates Motivation");
        Utils.removeAssignments(showUser("kirk").selectAssignmentsPanel(), "Secret Projects I");
        Utils.removeAssignments(showUser("kirk").selectAssignmentsPanel(), "Secret Projects II");
        assertLastNotificationBodyContains(notificationFile, notificationCheck1);
        assertLastNotificationBodyContains(notificationFile, notificationCheck2);
        showUser("kirk")
                .selectProjectionsPanel()
                .viewProjectionDetails("jkirk", "CSV-1 (Document Access)")
                .assertPropertyInputValues("Groups", "Internal Employees", "Essential Documents");
        Utils.removeAllAssignments(showUser("kirk").selectAssignmentsPanel());
        showUser("kirk")
                        .selectProjectionsPanel()
                            .assertProjectionDisabled("jkirk", "CSV-2 (Canteen Ordering System)")
                            .assertProjectionDisabled("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                            .assertProjectionDisabled("jkirk", "CSV-1 (Document Access)");
        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("kirk").selectAssignmentsPanel(), true, "Internal Employee");
        showUser("kirk")
                        .selectProjectionsPanel()
                            .assertProjectionEnabled("jkirk", "CSV-2 (Canteen Ordering System)")
                            .assertProjectionEnabled("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                            .assertProjectionEnabled("jkirk", "CSV-1 (Document Access)");
        assertLastNotificationBodyContains(notificationFile, notificationCheck1);
        assertLastNotificationBodyContains(notificationFile, notificationCheck2);
    }

    @Test(dependsOnMethods = {"mod01test01environmentInitialization"}, groups={"advancedM1"})
    public void mod01test02ArchetypeAndObjectCollection() {
        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.loginWithReloadLoginPage(getUsername(), getPassword());

        basicPage.listUsers()
                .newObjectButtonClick("New employee")
                    .selectBasicPanel()
                        .form()
                            .addAttributeValue("Name", "janeway")
                            .addAttributeValue("Given name", "Kathryn")
                            .addAttributeValue("Family name", "Janeway")
                            .and()
                        .and()
                    .selectActivationPanel()
                        .form()
                            .selectOption("Administrative status", "Enabled")
                            .and()
                        .and()
                    .selectPasswordPanel()
                        .setPasswordValue("qwerty12345XXXX")
                        .and()
                    .clickSave()
                        .feedback()
                            .assertSuccess();

        basicPage
                .listUsers()
                    .table()
                        .assertIconColumnExistsByNameColumnValue("janeway", "fa fa-user", "darkgreen");

        showUser("janeway")
                .summary()
                    .assertSummaryTagWithTextExists("Employee");

        basicPage
                .listUsers("Employees")
                    .table()
                        .assertTableObjectsCountEquals(1)
                        .assertCurrentTableContains("janeway");
    }

    @Test(dependsOnMethods = {"mod01test02ArchetypeAndObjectCollection"}, groups={"advancedM1"})
    public void mod01test03EnvironmentExamination() throws IOException {
        changeResourceAttribute(HR_RESOURCE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, hrTargetFile.getAbsolutePath(), true);

        getShadowTable(HR_RESOURCE_NAME, "name", "001212")
                .selectCheckboxByName("001212")
                .clickImport();
        showUser("X001212")
            .assertGivenName("John")
            .assertFamilyName("Smith")
            .assertFullName("John Smith")
            .selectBasicPanel()
                .form()
                    .assertPropertySelectValue("isManager", "True")
                    .assertPropertyInputValue("empStatus", "Active Employee")
                    .and()
                .and()
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Member", "Active Employees", "Internal Employee")
                    .and()
                //todo fails because of MID-7009
                .selectProjectionsPanel()
                    .assertProjectionExist("jsmith", "CSV-2")
                    .assertProjectionExist("cn=John Smith,ou=0300,ou=ExAmPLE,dc=example,dc=com", "CSV-3")
                    .assertProjectionExist("smith", "CSV-1");

        basicPage.listResources()
                    .table()
                        .search()
                            .byName()
                            .inputValue(HR_RESOURCE_NAME)
                            .updateSearch()
                        .and()
                        .clickByName(HR_RESOURCE_NAME)
                            .selectAccountsPanel()
                                .importTask()
                                    .clickCreateNew()
                                        .selectBasicPanel()
                                        .form()
                                        .addAttributeValue("Name", "Initial import from HR")
                                        .and()
                                    .and()
                                .clickSaveAndRun()
                                .feedback()
                                .assertSuccess();
        basicPage
                .listUsers()
                    .table()
                        .assertTableObjectsCountEquals(15);

        TaskPage task = basicPage.newTask("Live synchronization task");
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
        task.selectBasicPanel()
                .form()
                    .addAttributeValue(TaskType.F_NAME, "HR Synchronization")
                    .addAttributeValue("objectclass", "AccountObjectClass")
                    .editRefValue("objectRef")
                        .selectType("Resource")
                        .table()
                            .search()
                                .byName()
                                .inputValue(HR_RESOURCE_NAME)
                                .updateSearch()
                                .and()
                            .clickByName(HR_RESOURCE_NAME)
                    .selectOption("recurrence","Recurring")
                    .selectOption("binding","Tight")
                    .and()
                .and()
                .selectSchedulePanel()
                    .form()
                        .addAttributeValue("interval", "5")
                        .and()
                    .and()
                .clickSaveAndRun()
                .feedback()
                    .assertInfo();

       FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_1, hrTargetFile);
       Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage
                .listUsers()
                    .table()
                        .search()
                        .byName()
                        .inputValue("000999")
                        .updateSearch()
                    .and()
                    .assertTableObjectsCountEquals(1);

        FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage
                .listUsers()
                    .table()
                        .search()
                        .byName()
                        .inputValue("Arnold J.")
                        .updateSearch()
                    .and()
                    .assertTableObjectsCountEquals(1);

        FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_3, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("Arnold J.")
                .assertActivationStateEquals("Disabled")
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Member", "Inactive Employees")
                    .and()
                .selectProjectionsPanel()
                    .assertProjectionDisabled("cn=Arnold J. Rimmer,ou=ExAmPLE,dc=example,dc=com", HR_RESOURCE_NAME);

        FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("Arnold J.")
                .assertActivationStateEquals("Enabled");
    }

    @Test(dependsOnMethods = {"mod01test03EnvironmentExamination"}, groups={"advancedM1"})
    public void mod01test04objectCollection() throws IOException {
        addObjectFromFile(OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE);
        addObjectFromFile(Utils.changeAttributeIfPresent(SYSTEM_CONFIGURATION_FILE_1_4, "redirectToFile",
                fetchTestHomeDir() + "/example-mail-notifications.log", fetchTestHomeDir()));

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.loginWithReloadLoginPage("administrator", "5ecr3t");

        basicPage.listUsers("Empty Telephones")
                .table()
                    .assertTableObjectsCountNotEquals(0);

    }
}
