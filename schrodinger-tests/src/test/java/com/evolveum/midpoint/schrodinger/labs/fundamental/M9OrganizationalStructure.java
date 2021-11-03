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

import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;

import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class M9OrganizationalStructure extends AbstractLabTest {

    private static final String LAB_OBJECTS_DIRECTORY = FUNDAMENTAL_LABS_DIRECTORY + "M9/";
    private static final File ARCHETYPE_ORG_FUNCTIONAL_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-functional.xml");
    private static final File ARCHETYPE_ORG_COMPANY_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-company.xml");
    private static final File ARCHETYPE_ORG_GROUP_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-group.xml");
    private static final File ARCHETYPE_ORG_GROUP_LIST_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-org-group-list.xml");
    private static final File KIRK_USER_TIBERIUS_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-tiberius-user.xml");
    private static final File OBJECT_TEMPLATE_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "objecttemplate/object-template-example-user.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File ORG_EXAMPLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "org/org-example.xml");
    private static final File ORG_SECRET_OPS_FILE = new File(LAB_OBJECTS_DIRECTORY + "org/org-secret-ops.xml");
    private static final File CSV_1_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-9.xml");
    private static final File SECRET_I_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-secret-i.xml");
    private static final File SECRET_II_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-secret-ii.xml");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ARCHETYPE_ORG_FUNCTIONAL_FILE, ARCHETYPE_ORG_COMPANY_FILE, ARCHETYPE_ORG_GROUP_FILE,
                ARCHETYPE_ORG_GROUP_LIST_FILE, KIRK_USER_TIBERIUS_FILE, OBJECT_TEMPLATE_USER_FILE);
    }

    @Test(groups={"M9"})
    public void mod09test01ImportStaticOrgStructure() {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login(getUsername(), getPassword());

        importObject(ORG_EXAMPLE_FILE, true);

        OrgTreePage orgTree = basicPage.orgStructure();
        orgTree.selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                    .expandOrg("Software Department")
                    .assertChildOrgExists("ExAmPLE, Inc. - Functional Structure", "Executive Division", "Sales Department",
                                "Human Resources", "Technology Division", "IT Administration Department", "Software Department", "Java Development");
        orgTree.selectTabWithRootOrg("Groups")
                .getOrgHierarchyPanel()
                    .assertChildOrgExists("Groups", "Active Employees", "Administrators", "Contractors", "Former Employees",
                        "Inactive Employees", "Security");

        importObject(ORG_SECRET_OPS_FILE, true);
        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .assertChildOrgExists("Secret Operations", "Transportation and Logistics Department");
    }

    @Test(dependsOnMethods = {"mod09test01ImportStaticOrgStructure"})
    public void mod09test02CreateStaticOrgStructure() {
        OrgPage orgPage = (OrgPage) basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Secret Operations")
                            .expandAll()
                        .selectOrgInTree("Transportation and Logistics Department")
                        .and()
                    .getMemberPanel()
                        .newMember("Create Organization type member with Member relation", "Organization");
        orgPage
                .selectBasicPanel()
                    .form()
                        .addAttributeValue("Name", "0919")
                        .addAttributeValue("Display Name", "Warp Speed Research")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Secret Operations")
                            .expandAll()
                        .showTreeNodeDropDownMenu("Warp Speed Research")
                            .edit()
                                .assertName("0919");

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
                        .rowByColumnLabel("Name", "0919")
                        .clickCheckBox()
                        .and()
                    .and()
                    .clickAdd()
                .and()
            .clickSave()
                .feedback()
                    .isSuccess();

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Secret Operations")
                            .expandAll()
                        .selectOrgInTree("Warp Speed Research")
                        .and()
                    .getMemberPanel()
                        .selectType("User")
                        .table()
                            .assertTableContainsText("kirk");

        showUser("kirk").selectAssignmentsPanel()
                .table()
                    .selectCheckboxByName("Warp Speed Research")
                    .removeByName("Warp Speed Research")
                    .and()
                .and()
            .clickSave()
                .feedback()
                    .isSuccess();
    }

    @Test(dependsOnMethods = {"mod09test02CreateStaticOrgStructure"})
    public void mod09test03OrganizationActingAsARole() throws IOException {
        addObjectFromFile(SECRET_I_ROLE_FILE);
        addObjectFromFile(SECRET_II_ROLE_FILE);

        addCsvResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Secret Operations")
                            .expandAll()
                        .selectOrgInTree("Warp Speed Research")
                        .and()
                    .getMemberPanel()
                        .selectType("User")
                            .table()
                            .assertTableDoesntContainText("kirk");

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Warp Speed Research")
                            .edit()
                                .selectInducementsPanel()
                                    .clickAddInducement()
                                        .table()
                                            .search()
                                                .byName()
                                                    .inputValue("Secret Projects I")
                                                    .updateSearch()
                                                    .and()
                                            .selectCheckboxByName("Secret Projects I")
                                            .and()
                                        .clickAdd()
                                    .and()
                                .clickSave()
                                    .feedback()
                                        .isSuccess();

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .selectOrgInTree("Warp Speed Research")
                        .and()
                    .getMemberPanel()
                        .table()
                                .assign()
                                    .selectType("User")
                                    .table()
                                        .search()
                                            .byName()
                                                .inputValue("kirk")
                                                .updateSearch()
                                            .and()
                                        .selectCheckboxByName("kirk")
                                        .and()
                                    .clickAdd()
                                .and()
                            .and()
                        .and()
                    .feedback()
                        .isInfo();

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
//                .expandOrg()
//                        .showTreeNodeDropDownMenu("Secret Operations")
//                .expandAll()
//                            .expandAll()
                        .selectOrgInTree("Warp Speed Research")
                        .and()
                    .getMemberPanel()
                        .selectType("User")
                            .table()
                            .assertTableContainsText("kirk");

        AccountPage accountPage = showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.screenshot("M9_accountPage");
        accountPage
                        .form()
                        .assertPropertyInputValues("groups", "Internal Employees",
                                "Essential Documents", "Teleportation", "Time Travel");

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Secret Operations")
                            .expandAll()
                        .showTreeNodeDropDownMenu("Warp Speed Research")
                            .edit()
                                .selectInducementsPanel()
                                    .clickAddInducement()
                                        .selectType("Role")
                                        .table()
                                            .search()
                                                .byName()
                                                    .inputValue("Secret Projects II")
                                                    .updateSearch()
                                                .and()
                                            .selectCheckboxByName("Secret Projects II")
                                            .and()
                                        .clickAdd()
                                    .and()
                                .clickSave()
                                    .feedback()
                                        .isSuccess();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk")
                        .form()
                        .assertPropertyInputValues("groups", "Internal Employees",
                                "Essential Documents", "Teleportation", "Time Travel");

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .selectOrgInTree("Warp Speed Research")
                        .and()
                    .getMemberPanel()
                        .table()
                            .recompute()
                                .clickYes()
                            .and()
                        .and()
                    .and()
                .feedback()
                    .isInfo();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk")
                        .form()
                        .assertPropertyInputValues("groups", "Internal Employees", "Essential Documents",
                                "Teleportation", "Time Travel", "Lucky Numbers", "Presidential Candidates Motivation");

        showUser("kirk").selectAssignmentsPanel()
                .selectTypeAllDirectIndirect()
                    .assertIndirectAssignmentsExist("Secret Projects II");
    }

}
