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

import com.codeborne.selenide.ex.ElementNotFound;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.configuration.ObjectPolicyPanel;
import com.evolveum.midpoint.schrodinger.component.org.ManagerPanel;
import com.evolveum.midpoint.schrodinger.component.org.OrgRootTab;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsPanel;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class M10ObjectTemplate extends AbstractLabTest {

    protected static final String LAB_OBJECTS_DIRECTORY = FUNDAMENTAL_LABS_DIRECTORY + "M10/";
    private static final File OBJECT_TEMPLATE_USER_FILE_10_3 = new File(LAB_OBJECTS_DIRECTORY + "objecttemplate/object-template-example-user-10-3.xml");
    private static final File OBJECT_TEMPLATE_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "objecttemplate/object-template-example-user.xml");
    private static final File LOOKUP_EMP_STATUS_FILE = new File(LAB_OBJECTS_DIRECTORY + "lookuptables/lookup-emp-status.xml");
    private static final File CSV_3_RESOURCE_FILE_10_4 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-10-4.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_10 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-10.xml");
    private static final File ARCHETYPE_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-employee.xml");
    private static final File ARCHETYPE_ORG_FUNCTIONAL_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-functional.xml");
    private static final File ARCHETYPE_ORG_COMPANY_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-company.xml");
    private static final File ARCHETYPE_ORG_GROUP_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-group.xml");
    private static final File ARCHETYPE_ORG_GROUP_LIST_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-group-list.xml");
    private static final File KIRK_USER_TIBERIUS_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-tiberius-user.xml");
    private static final File PICARD_USER_TIBERIUS_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/picard-user.xml");
    private static final File INTERNAL_EMPLOYEE_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File ORG_EXAMPLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "org/org-example.xml");
    private static final File ORG_WARP_SPEED_RESEARCH_FILE = new File(LAB_OBJECTS_DIRECTORY + "org/warp-speed-research.xml");
    private static final File ORG_SECRET_OPS_FILE = new File(LAB_OBJECTS_DIRECTORY + "org/org-secret-ops.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File CSV_1_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-10.xml");
    private static final File CSV_3_RESOURCE_FILE_10 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-10.xml");
    private static final File HR_RESOURCE_FILE_10 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_SYNCHRONIZATION_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/task-opendj-livesync-full.xml");
    private static final File HR_IMPORT_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/task-hr-import.xml");
    private static final File OBJECT_TEMPLATE_USER_SIMPLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objecttemplate/object-template-example-user-simple.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-10.xml");
    protected static final File EXTENSION_SCHEMA_FILE = new File(FUNDAMENTAL_LABS_DIRECTORY +"schema/extension-example.xsd");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ARCHETYPE_EMPLOYEE_FILE, ARCHETYPE_ORG_FUNCTIONAL_FILE, ARCHETYPE_ORG_COMPANY_FILE, ARCHETYPE_ORG_GROUP_FILE,
                ARCHETYPE_ORG_GROUP_LIST_FILE, OBJECT_TEMPLATE_USER_SIMPLE_FILE, KIRK_USER_TIBERIUS_FILE, PICARD_USER_TIBERIUS_FILE,
                ORG_EXAMPLE_FILE, ORG_SECRET_OPS_FILE, ORG_WARP_SPEED_RESEARCH_FILE, NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE);
    }

    @Override
    protected File getExtensionSchemaFile() {
        return EXTENSION_SCHEMA_FILE;
    }

    @Test
    public void mod10test01SimpleObjectTemplate() throws IOException {
        hrTargetFile = new File(getTestTargetDir(), HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_4, hrTargetFile);

        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE_10, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());

        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);

        csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);

        addCsvResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());

        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());

        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_10, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        importObject(INTERNAL_EMPLOYEE_ROLE_FILE, true, true);

        importObject(HR_IMPORT_TASK_FILE);
        showTask("Initial import from HR")
                .clickRunNow();
        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M.getSeconds());

        importObject(HR_SYNCHRONIZATION_TASK_FILE);

        //user kirk should have projection with CSV-3 resource
        showUser("kirk")
                .selectProjectionsPanel()
                    .clickAddProjection()
                        .table()
                            .search()
                            .byName()
                            .inputValue(CSV_3_RESOURCE_NAME)
                            .updateSearch()
                            .and()
                        .selectRowByName(CSV_3_RESOURCE_NAME)
                        .and()
                    .clickAdd()
                    .and()
                        .clickSave()
                            .feedback()
                            .isSuccess();

        //user kirk should have projection with CSV-1 resource
        showUser("picard")
                .selectProjectionsPanel()
                    .clickAddProjection()
                        .table()
                            .search()
                            .byName()
                            .inputValue(CSV_1_RESOURCE_NAME)
                            .updateSearch()
                            .and()
                        .selectRowByName(CSV_1_RESOURCE_NAME)
                        .and()
                    .clickAdd()
                    .and()
                .clickSave()
                    .feedback()
                    .isSuccess();

        basicPage.listResources()
                .table()
                    .search()
                        .byName()
                        .inputValue(HR_RESOURCE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(HR_RESOURCE_NAME)
                        .selectAccountsPanel()
                            .clickSearchInResource()
                                .table()
                                .selectRowByName("001212")
                                .importAccount();
        ((PrismFormWithActionButtons<ObjectPolicyPanel>)basicPage.objectPolicy()
                .clickAddObjectPolicy()
                    .selectOption("type", "User")
                    .editRefValue("objectTemplateRef")
                        .table()
                            .clickByName("ExAmPLE User Template"))
                    .clickDone()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword());


        showUser("X001212")
                .checkReconcile()
                .clickSave()
                    .feedback()
                        .isSuccess();

        showUser("X001212")
                .assertFullName("John Smith");

        FileUtils.copyFile(HR_SOURCE_FILE_10_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000998")
                .assertFullName("David Lister");

        TaskPage task = basicPage.newTask("Recomputation task");
        task.selectBasicPanel()
                .form()
                    .addAttributeValue("name", "User Recomputation Task")
                    .and()
                .and()
                .selectActivityWorkPanel()
                    .form()
                        .getPrismContainerPanel("Objects")
                            .getContainerFormFragment()
                            .selectOption("type","User")
                            .and()
                        .and()
                    .and()
                .and()
                .selectSchedulePanel()
                    .form()
                        .selectOption("recurrence","Single")
                        .and()
                    .and()
            .clickSaveAndRun()
                .feedback()
                    .isInfo();

        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M.getSeconds());
        showUser("kirk")
                .assertFullName("Jim Tiberius Kirk");
    }

    @Test(dependsOnMethods = {"mod10test01SimpleObjectTemplate"})
    public void mod10test02AutomaticAssignments() throws IOException {
        importObject(OBJECT_TEMPLATE_USER_FILE, true);

        ResourceAccountsPanel<ResourcePage> accountTab = basicPage.listResources()
                .table()
                    .clickByName(HR_RESOURCE_NAME)
                        .selectAccountsPanel()
                            .clickSearchInResource();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        accountTab.table()
                .selectRowByName("001212")
                    .importAccount()
                    .and()
                .and()
            .feedback()
                .isSuccess();

        AssignmentsPanel<UserPage> tab = accountTab.table()
                .clickOnOwnerByName("X001212", "Edit Employee")
                .selectAssignmentsPanel();
        tab.assertAssignmentsWithRelationExist("Organization", "Member", "Human Resources",
                "Active Employees")
                .assertAssignmentsWithRelationExist("Role", "Member",  "Internal Employee")
                .assertAssignmentsWithRelationExist("Organization", "Human Resources");

        FileUtils.copyFile(HR_SOURCE_FILE_10_2_PART1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000999")
            .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Organization", "Member", "Java Development",
                "Active Employees")
                .assertAssignmentsWithRelationExist("Role", "Member",  "Internal Employee");

        showTask("User Recomputation Task").clickRunNow();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000998")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Organization", "Member", "Java Development",
                        "Active Employees")
                .assertAssignmentsWithRelationExist("Role", "Member",  "Internal Employee");

        FileUtils.copyFile(HR_SOURCE_FILE_10_2_PART2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        UserPage user = showUser("X000998");
        user.assertActivationStateEquals("Disabled");
        user.selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Organization", "Member", "Inactive Employees")
                .assertAssignmentsWithRelationExist("Role", "Member",  "Internal Employee");

        FileUtils.copyFile(HR_SOURCE_FILE_10_2_PART3, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        user = showUser("X000998");
        user.assertActivationStateEquals("Disabled");

        user.selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Organization", "Member", "Former Employees");

        FileUtils.copyFile(HR_SOURCE_FILE_10_2_PART1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        user = showUser("X000998");
        user.assertActivationStateEquals("Enabled");
        showUser("X000998")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Organization", "Member", "Java Development",
                        "Active Employees")
                .assertAssignmentsWithRelationExist("Role", "Member",  "Internal Employee");
    }

    @Test(dependsOnMethods = {"mod10test02AutomaticAssignments"})
    public void mod10test03LookupTablesAndAttributeOverrides() {
        showUser("kirk").selectAssignmentsPanel()
                .clickAddAssignment("New Organization type assignment with Member relation")
                    .table()
                        .paging()
                        .next()
                        .and()
                    .and()
                    .table()
                        .search()
                            .byName()
                            .inputValue("0919")
                            .updateSearch()
                        .and()
                        .findRowByColumnLabelAndRowValue("Name", "0919")
                        .clickCheckBox()
                        .and()
                    .and()
                    .clickAdd()
                .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        PrismForm<AssignmentHolderBasicPanel<UserPage>> form = showUser("kirk")
                .selectBasicPanel()
                    .form();

        form
                .showEmptyAttributes("Properties")
                    .addAttributeValue("Employee Status", "O")
                    .addAttributeValue("Family name", "kirk2");
        boolean existFeedback = false;
        try { existFeedback = form.and().and().feedback().isError(); } catch (ElementNotFound e) { }
        Assert.assertFalse(existFeedback);
        form.assertPropertyWithTitleTextExist("telephoneNumber", "Primary telephone number of the user, org. unit, etc.")
                .assertPropertyWithTitleTextDoesntExist("telephoneNumber", "Mobile Telephone Number")
                .assertPropertyEnabled("honorificSuffix");

        addObjectFromFile(LOOKUP_EMP_STATUS_FILE);
        importObject(OBJECT_TEMPLATE_USER_FILE_10_3, true);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());

        form = showUser("kirk")
                .selectBasicPanel()
                .form();

        form.showEmptyAttributes("Properties");
        form.addAttributeValue("empStatus", "O");
        form.addAttributeValue("familyName", "kirk2");
        form.and().and().feedback().assertError();
        form
                .assertPropertyWithTitleTextDoesntExist("telephoneNumber", "Primary telephone number of the user, org. unit, etc.")
                .assertPropertyWithTitleTextExist("telephoneNumber", "Mobile Telephone Number")
                .assertPropertyDisabled("honorificSuffix");
    }

    @Test(dependsOnMethods = {"mod10test03LookupTablesAndAttributeOverrides"})
    public void mod10test04FinishingManagerMapping() throws IOException {
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        showTask("User Recomputation Task").clickRunNow();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        OrgRootTab rootTab = basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure");
        ManagerPanel<OrgRootTab> managerPanel = rootTab.getOrgHierarchyPanel()
                .showTreeNodeDropDownMenu("Technology Division")
                .expandAll()
                .selectOrgInTree("IT Administration Department")
                .and()
                .getManagerPanel();
        managerPanel
                .assertContainsManager("John Wicks");

        rootTab.getMemberPanel()
//                .selectType("User")
                .table()
                    .search()
                        .byType("User")
                        .updateSearch()
                        .resetBasicSearch()
                    .and()
                .clickByName("X000158");
        new UserPage().selectProjectionsPanel()
                .table()
                    .clickByName("cn=Alice Black,ou=0212,ou=0200,ou=ExAmPLE,dc=example,dc=com")
                        .assertPropertyInputValue("manager", "X000390");
        showUser("X000390").selectProjectionsPanel()
                .table()
                    .clickByName("cn=John Wicks,ou=0212,ou=0200,ou=ExAmPLE,dc=example,dc=com")
                        .assertPropertyInputValue("manager", "X000035");
        showUser("X000035").selectProjectionsPanel()
                .table()
                    .clickByName("cn=James Bradley,ou=0200,ou=ExAmPLE,dc=example,dc=com")
                        .showEmptyAttributes("Attributes")
                        .assertPropertyInputValue("Manager", "");

        showUser("kirk")
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Organization","Member", "Warp Speed Research");
        new UserPage().selectProjectionsPanel()
                .table()
                    .clickByName("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                        .showEmptyAttributes("Attributes")
                        .assertPropertyInputValue("Manager", "");

        showUser("picard")
                .selectAssignmentsPanel()
                    .clickAddAssignment("New Organization type assignment with Manager relation")
                        .selectType("Org")
                            .table()
                                .search()
                                    .byName()
                                        .inputValue("0919")
                                        .updateSearch()
                                    .and()
                                .selectRowByName("0919")
                                .and()
                            .clickAdd()
                            .and()
                        .clickSave()
                            .feedback()
                                .isSuccess();

        showUser("kirk").checkReconcile()
                .clickSave()
                    .feedback()
                        .isSuccess();

        showUser("kirk").selectProjectionsPanel()
                .table()
                    .clickByName("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                        .assertPropertyInputValue("Manager", "picard");

        showUser("picard").selectAssignmentsPanel()
                .table()
                    .selectCheckboxByName("Warp Speed Research")
                    .removeByName("Warp Speed Research")
                    .and()
                .and()
            .clickSave()
                .feedback()
                    .isSuccess();

        showUser("kirk").selectProjectionsPanel()
                .table()
                    .clickByName("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                        .assertPropertyInputValue("Manager", "picard");

        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_10_4, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
//        importObject(CSV_3_RESOURCE_FILE_10_4, true);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
//        changeResourceAttribute(CSV_3_RESOURCE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csv3TargetFile.getAbsolutePath(), true);

        showUser("kirk").checkReconcile()
                .clickSave()
                    .feedback()
                        .isSuccess();

        showUser("kirk").selectProjectionsPanel()
                .table()
                    .clickByName("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                        .showEmptyAttributes("Attributes")
                        .assertPropertyInputValue("Manager", "");
    }

    @Test(dependsOnMethods = {"mod10test03LookupTablesAndAttributeOverrides"})
    public void mod11test01ConfiguringNotifications() throws IOException {
//        showTask("HR Synchronization").clickResume();

        notificationFile = new File(getTestTargetDir(), NOTIFICATION_FILE_NAME);
        notificationFile.createNewFile();

        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_10);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());

        basicPage.notifications()
                .setRedirectToFile(notificationFile.getAbsolutePath())
                .and()
                .clickSave()
                .feedback()
                .isSuccess();

        FileUtils.copyFile(HR_SOURCE_FILE_11_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        String notification = Utils.readBodyOfLastNotification(notificationFile);

        String startOfNotification = "Notification about user-related operation (status: SUCCESS)\n"
                + "\n"
                + "User: Chuck Norris (X000997, oid ";

        String endOfNotification = "The user record was created with the following data:\n"
                + " - Name: X000997\n"
                + " - Full name: Chuck Norris\n"
                + " - Given name: Chuck\n"
                + " - Family name: Norris\n"
                + " - Title: Application Developer\n"
                + " - Email: chuck.norris@example.com\n"
                + " - Employee Number: 000997\n"
                + " - Cost Center: 0211\n"
                + " - Organizational Unit: Java Development\n"
                + " - Extension:\n"
                + "    - Organizational Path: 0200:0210:0211\n"
                + "    - Is Manager: false\n"
                + "    - Employee Status: A\n"
                + " - Credentials:\n"
                + "    - Password:\n"
                + "       - Value: (protected string)\n"
                + " - Activation:\n"
                + "    - Administrative status: ENABLED\n"
                + "    - Valid from: Jul 15, 2010, 8:20:00 AM\n"
                + " - Assignment #1:\n"
                + "    - Target: Employee (archetype) [default]\n"
                + " - Assignment #2:\n"
                + "    - Target: ACTIVE (org) [default]\n"
                + " - Assignment #3:\n"
                + "    - Target: 0211 (org) [default]\n"
                + " - Assignment #4:\n"
                + "    - Target: Internal Employee (role) [default]\n"
                + "\n"
                + "Requester: midPoint Administrator (administrator)\n"
                + "Channel: http://midpoint.evolveum.com/xml/ns/public/common/channels-3#liveSync\n"
                + "\n";

        Assert.assertTrue(notification.startsWith(startOfNotification));
        Assert.assertTrue(notification.endsWith(endOfNotification));
    }

}
