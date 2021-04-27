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
    private static final String ADVANCED_LABS_SOURCES_DIRECTORY = LAB_OBJECTS_DIRECTORY + "sources/";

    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File KIRK_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-user.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_1 = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "source-1.3-update-1.csv");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_2 = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "source-1.3-update-2.csv");
    private static final File HR_SOURCE_FILE_1_3_UPDATE_3 = new File(FUNDAMENTAL_LABS_SOURCES_DIRECTORY + "source-1.3-update-3.csv");
    private static final File SYSTEM_CONFIGURATION_FILE_1_4 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-1-4.xml");
    private static final File CSV_1_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-1.csv");
    private static final File CSV_2_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-2.csv");
    private static final File CSV_3_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "csv-3.csv");
    private static final File HR_SOURCE_FILE = new File(ADVANCED_LABS_SOURCES_DIRECTORY + "source.csv");

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

        addResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());

    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(KIRK_USER_FILE);
    }

    @Test(groups={"advancedM1"})
    public void mod01test01environmentInitialization() throws IOException {
        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Internal Employee");
        showUser("kirk")
                        .selectTabProjections()
                        .assertProjectionExist("jkirk", "CSV-2 (Canteen Ordering System)")
                        .assertProjectionExist("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                        .assertProjectionExist("jkirk", "CSV-1 (Document Access)");

        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Secret Projects I", "Secret Projects II");
        //TODO check CSV-1 groups
        showUser("kirk")
                .selectTabProjections();
        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Secret Projects I");
        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Secret Projects II");
        //TODO check CSV-1 groups
        Utils.removeAllAssignments(showUser("kirk").selectTabAssignments());
        showUser("kirk")
                        .selectTabProjections()
                            .assertProjectionDisabled("jkirk", "CSV-2 (Canteen Ordering System)")
                            .assertProjectionDisabled("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                            .assertProjectionDisabled("jkirk", "CSV-1 (Document Access)");
        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Internal Employee");
        showUser("kirk")
                        .selectTabProjections()
                            .assertProjectionEnabled("jkirk", "CSV-2 (Canteen Ordering System)")
                            .assertProjectionEnabled("cn=Jim Kirk,ou=ExAmPLE,dc=example,dc=com", "CSV-3 (LDAP)")
                            .assertProjectionEnabled("jkirk", "CSV-1 (Document Access)");
    }

    @Test(groups={"advancedM1"})
    public void mod01test02ArchetypeAndObjectCollection() {
        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.loginWithReloadLoginPage(getUsername(), getPassword());

        basicPage.listUsers()
                .newObjectCollection("New employee")
                    .selectTabBasic()
                        .form()
                            .addAttributeValue("Name", "janeway")
                            .addAttributeValue("Given name", "Kathryn")
                            .addAttributeValue("Family name", "Janeway")
                            .selectOption("Administrative status", "Enabled")
                            .addAttributeValue("Password", "qwerty12345XXXX")
                            .and()
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

    @Test(groups={"advancedM1"})
    public void mod01test03EnvironmentExamination() throws IOException {
        changeResourceAttribute(HR_RESOURCE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, hrTargetFile.getAbsolutePath(), true);

        getShadowTable(HR_RESOURCE_NAME, "name", "001212")
                .selectCheckboxByName("001212")
                .clickImport();
        showUser("X001212")
            .assertGivenName("John")
            .assertFamilyName("Smith")
            .assertFullName("John Smith")
            .selectTabBasic()
                .form()
                    .assertPropertySelectValue("isManager", "True")
                    .assertPropertyInputValue("empStatus", "Active Employee")
                    .and()
                .and()
                .selectTabAssignments()
                    .assertAssignmentsWithRelationExist("Member", "Active Employees", "Internal Employee")
                    .and()
                //todo fails because of MID-7009
                .selectTabProjections()
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
                            .clickAccountsTab()
                                .importTask()
                                    .clickCreateNew()
                                        .selectTabBasic()
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

        TaskPage task = basicPage.newTask();
        task.setHandlerUriForNewTask("Live synchronization task");
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        task.selectTabBasic()
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
                .selectScheduleTab()
                    .form()
                        .addAttributeValue("interval", "5")
                        .and()
                    .and()
                .clickSaveAndRun()
                .feedback()
                .isInfo();

       FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_1, hrTargetFile);
       Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        showUser("Arnold J.")
                .assertActivationStateEquals("Disabled")
                .selectTabAssignments()
                    .assertAssignmentsWithRelationExist("Member", "Inactive Employees")
                    .and()
                .selectTabProjections()
                    .assertProjectionDisabled("cn=Arnold J. Rimmer,ou=ExAmPLE,dc=example,dc=com", HR_RESOURCE_NAME);

        FileUtils.copyFile(HR_SOURCE_FILE_1_3_UPDATE_2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        showUser("Arnold J.")
                .assertActivationStateEquals("Enabled");
    }

    @Test(groups={"advancedM1"})
    public void mod01test04objectCollection() {
        addObjectFromFile(OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE);
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_1_4);

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.loginWithReloadLoginPage("administrator", "5ecr3t");

        basicPage.listUsers("Empty Telephones")
                .table()
                    .assertTableObjectsCountNotEquals(0);

    }
}
