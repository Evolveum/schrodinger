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
package com.evolveum.midpoint.schrodinger.labs.fundamental;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsPanel;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.resource.ViewResourcePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

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
 * @author skublik
 */

public class M7SynchronizationFlavours extends AbstractLabTest {

    protected static final String LAB_OBJECTS_DIRECTORY = FUNDAMENTAL_LABS_DIRECTORY + "M7/";
    private static final Logger LOG = LoggerFactory.getLogger(M7SynchronizationFlavours.class);
    private static final File ARCHETYPE_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-employee.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_7 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-7.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File HR_NO_EXTENSION_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr-noextension.xml");
    private static final File CSV_1_RESOURCE_FILE_7 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-7.xml");
    private static final File CSV_2_RESOURCE_FILE_7 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-7.xml");
    private static final File CSV_3_RESOURCE_FILE_7 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-7.xml");

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
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ARCHETYPE_EMPLOYEE_FILE, SYSTEM_CONFIGURATION_FILE_7);
    }

    @Test(groups={"M7"})
    public void mod07test01RunningImportFromResource() throws IOException {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);

        addCsvResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE_7, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE_7, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_7, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        addCsvResourceFromFileAndTestConnection(HR_NO_EXTENSION_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());

        ResourceAccountsPanel<ViewResourcePage> accountTab = basicPage.listResources()
                .table()
                    .clickByName(HR_RESOURCE_NAME)
                        .selectAccountsPanel()
                            .clickSearchInResource();
        accountTab.table()
                .selectCheckboxByName("001212")
                    .clickImport()
                    .and()
                .and()
            .feedback()
                .isSuccess();

        UserPage owner = accountTab.table()
                .clickOnOwnerByName("X001212", "Edit Employee");

        owner.assertName("X001212");

        TaskPage taskPage = basicPage.listResources()
                .table()
                    .clickByName(HR_RESOURCE_NAME)
                        .selectAccountsPanel()
                            .importTask()
                                .clickCreateNew();
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        taskPage
                                    .selectBasicPanel()
                                        .form()
                                            .addAttributeValue("name","Initial import from HR")
                                            .and()
                                        .and()
                                    .clickSaveAndRun()
                                        .feedback()
                                            .isInfo();
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S); // the time for the task to be finished
//todo fix test page
//        showTask("Initial import from HR")
//                .selectTabOperationStatistics()
//                    .assertSuccessfullyProcessedCountMatch(14);
        basicPage.listUsers(ARCHETYPE_EMPLOYEE_PLURAL_LABEL).assertObjectsCountEquals(14);
    }

    @Test (dependsOnMethods = {"mod07test01RunningImportFromResource"}, groups={"M7"})
    public void mod07test02RunningAccountReconciliation() throws IOException {
        createReconTask("CSV-1 Reconciliation", CSV_1_RESOURCE_NAME);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        deselectDryRun("CSV-1 Reconciliation");
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        assertContainsProjection("X001212", CSV_1_RESOURCE_OID, "jsmith");

        createReconTask("CSV-2 Reconciliation", CSV_2_RESOURCE_NAME);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        deselectDryRun("CSV-2 Reconciliation");
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        assertContainsProjection("X001212", CSV_2_RESOURCE_OID, "jsmith");

        createReconTask("CSV-3 Reconciliation", CSV_3_RESOURCE_NAME);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        deselectDryRun("CSV-3 Reconciliation");
        Selenide.sleep(MidPoint.TIMEOUT_LONG_30_S);
        assertContainsProjection("X001212", CSV_3_RESOURCE_OID, "cn=John Smith,ou=ExAmPLE,dc=example,dc=com");
    }

    @Test(dependsOnMethods = {"mod07test02RunningAccountReconciliation"}, groups={"M7"})
    public void mod07test03RunningAttributeReconciliation() throws IOException {
        FileUtils.copyFile(CSV_1_SOURCE_FILE_7_3, csv1TargetFile);

        showTask("CSV-1 Reconciliation", "Reconciliation tasks").clickRunNow();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk")
                        .form()
                        .assertPropertyInputValues("groups", "Internal Employees", "Essential Documents");

    }

    @Test(dependsOnMethods = {"mod07test03RunningAttributeReconciliation"}, groups={"M7"})
    public void mod07test04RunningLiveSync() throws IOException {
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        TaskPage task = basicPage.newTask("Live synchronization task");
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        task.selectBasicPanel()
                .form()
                    .addAttributeValue("objectclass", "AccountObjectClass")
                    .addAttributeValue(TaskType.F_NAME, "HR Synchronization")
                    .selectOption("recurrence","Recurring")
                    .selectOption("binding","Tight")
                    .editRefValue("objectRef")
                        .selectType("Resource")
                        .table()
                            .search()
                                .byName()
                                    .inputValue(HR_RESOURCE_NAME)
                                    .updateSearch()
                                .and()
                            .clickByName(HR_RESOURCE_NAME)
                    .and()
                .and()
            .selectSchedulePanel()
                .form()
                    .addAttributeValue("interval", "5")
                    .and()
                .and()
            .clickSaveAndRun()
                .feedback()
                    .isInfo();

        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_1, hrTargetFile);
        Selenide.sleep(20000);
        showUser("X000999")
                .assertGivenName("Arnold")
                .assertFamilyName("Rimmer")
                .selectBasicPanel()
                    .form()
                        .assertPropertySelectValue("administrativeStatus", "Enabled");

        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_2, hrTargetFile);
        Selenide.sleep(20000);
        showUser("X000999")
                .assertGivenName("Arnold J.");

        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_3, hrTargetFile);
        Selenide.sleep(20000);
        showUser("X000999")
                .selectBasicPanel()
                    .form()
                        .assertPropertySelectValue("administrativeStatus", "Disabled");

        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_4, hrTargetFile);
        Selenide.sleep(20000);
        showUser("X000999")
                .selectBasicPanel()
                    .form()
                        .assertPropertySelectValue("administrativeStatus", "Enabled");

    }

    private Table<ProjectionsPanel<UserPage>> assertContainsProjection(String user, String resourceOid, String accountName) {
       AbstractTableWithPrismView<ProjectionsPanel<UserPage>> table = showUser(user).selectProjectionsPanel().table();
       Selenide.screenshot(user + "_" + resourceOid + "_" + accountName);
       return table
                    .search()
                        .resetBasicSearch()
                        .referencePanelByItemName("Resource")
                            .inputRefOid(resourceOid)
                            .updateSearch()
                        .and()
                    .assertTableContainsText(accountName);
    }

    private void createReconTask(String reconTaskName, String resource){
        TaskPage task = basicPage.newTask("Reconciliation task");
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        task.selectActivityWorkPanel()
                .form()
                    .expandContainerPropertiesPanel("Reconciliation")
                    .expandContainerPropertiesPanel("Resource objects")
                        .getPrismContainerPanel("Resource objects")
                        .getContainerFormFragment()
                            .editRefValue("resourceRef")
                                .table()
                                .search()
                                    .byName()
                                    .inputValue(resource)
                                    .updateSearch()
                                    .and()
                                .clickByName(resource)
                                .and()
                            .and()
                        .addAttributeValue("objectclass", "AccountObjectClass")
                        .setDropDownAttributeValue("kind", "Account")
                        .addAttributeValue("intent", "default")
                    .and()
                .and()
                .selectBasicPanel()
                    .form()
                        .addAttributeValue(TaskType.F_NAME, reconTaskName)
                        .and()
                    .and()
                .selectActivityPanel()
                    .form()
                        .expandContainerPropertiesPanel("Activity")
                            .selectOption("executionMode", "Dry run")
                            .and()
                        .and()
                .clickSaveAndRun()
                    .feedback()
                        .isInfo();
    }

    private void deselectDryRun(String taskName) {
        showTask(taskName)
                .selectActivityPanel()
                .form()
                    .expandContainerPropertiesPanel("Activity")
                        .selectOption("executionMode", "Undefined")
                        .and()
                    .and()
                .clickSaveAndRun()
                    .feedback()
                        .isInfo();
    }
}
