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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schema.SchemaConstantsGenerated;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicTab;
import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.component.InducementsTab;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.table.DirectIndirectAssignmentTable;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;
import com.evolveum.midpoint.schrodinger.page.archetype.ArchetypePage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ActivationType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.RoleType;

import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.namespace.QName;

/**
 * @author skublik
 */

public class M5AccountsAssignmentsAndRoles extends AbstractLabTest {

    private static final Logger LOG = LoggerFactory.getLogger(M5AccountsAssignmentsAndRoles.class);
    protected static final String LAB_OBJECTS_DIRECTORY = LAB_FUNDAMENTAL_DIRECTORY + "M5/";

    private static final File TOP_SECRET_I_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-top-secret-i.xml");
    private static final File CSV_1_RESOURCE_FILE_5_5 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-5-5.xml");
    private static final File CSV_2_RESOURCE_FILE_5_5 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-5-5.xml");
    private static final File CSV_3_RESOURCE_FILE_5_5 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-5-5.xml");
    private static final String ARCHETYPE_EMPLOYEE_NAME = "Employee";
    private static final String ARCHETYPE_EMPLOYEE_LABEL = "Employee";
    private static final File INTERNAL_EMPLOYEE_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File CSV_1_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File INCOGNITO_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-incognito.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_5_7 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-5-7.xml");
    private static final File ARCHETYPE_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-employee.xml");
    private static final File ARCHETYPE_EXTERNAL_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-external.xml");
    private static final File SECRET_I_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-secret-i.xml");
    private static final File SECRET_II_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-secret-ii.xml");
    protected static final File KIRK_USER_TIBERIUS_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-tiberius-user.xml");

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
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(KIRK_USER_TIBERIUS_FILE);
    }

    @Test(groups={"M5"})
    public void mod05test01UsingRBAC() throws IOException {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);

        addResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        addObjectFromFile(INTERNAL_EMPLOYEE_ROLE_FILE);
        addObjectFromFile(INCOGNITO_ROLE_FILE);
        addObjectFromFile(SECRET_I_ROLE_FILE);
        addObjectFromFile(SECRET_II_ROLE_FILE);
        addObjectFromFile(TOP_SECRET_I_ROLE_FILE);

        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Secret Projects I", "Secret Projects II");
        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk")
                .form()
                    .assertPropertyInputValues("groups", "Lucky Numbers",
                            "Teleportation", "Time Travel", "Presidential Candidates Motivation");

        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Secret Projects I");

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk")
                    .form()
                        .assertPropertyInputValues("groups", "Lucky Numbers",
                                "Presidential Candidates Motivation");

        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Secret Projects II");

        assertShadowDoesntExist(CSV_1_RESOURCE_NAME, "Login", "jkirk");

        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Internal Employee");

        assertShadowExists(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        assertShadowExists(CSV_2_RESOURCE_NAME, "Login", "jkirk");
        assertShadowExists(CSV_3_RESOURCE_NAME, "Distinguished Name",
                "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");

    }

    @Test(dependsOnMethods = {"mod05test01UsingRBAC"}, groups={"M5"})
    public void mod05test02SegregationOfDuties() {
        showUser("kirk").selectTabAssignments()
                .clickAddAssignemnt()
                    .table()
                        .search()
                            .byName()
                                .inputValue("Incognito")
                                .updateSearch()
                            .and()
                        .selectCheckboxByName("Incognito")
                    .and()
                .clickAdd()
                .and()
            .clickSave()
                .feedback()
                    .isError();
    }

    @Test(dependsOnMethods = {"mod05test02SegregationOfDuties"}, groups={"M5"})
    public void mod05test04CreatingRoles() {
        InducementsTab<AbstractRolePage> tab = basicPage.newRole()
                .selectTabBasic()
                    .form()
                        .addAttributeValue(RoleType.F_NAME, "Too Many Secrets")
                        .addAttributeValue(RoleType.F_DISPLAY_NAME, "Too Many Secrets")
                    .and()
                .and()
                .selectTabInducements();
        Utils.addAsignments(tab, "Secret Projects I", "Secret Projects II", "Top Secret Projects I");

        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Too Many Secrets");
        assertShadowExists(CSV_1_RESOURCE_NAME, "Login", "jkirk");

        DirectIndirectAssignmentTable<AssignmentsTab<UserPage>> table = showUser("kirk").selectTabAssignments()
                .selectTypeAllDirectIndirect();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        table.assertIndirectAssignmentsExist("Secret Projects I",
                "Secret Projects II", "Top Secret Projects I", CSV_1_RESOURCE_NAME, CSV_2_RESOURCE_NAME,
                CSV_3_RESOURCE_NAME);

        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Too Many Secrets");
    }

    @Test(dependsOnMethods = {"mod05test04CreatingRoles"}, groups={"M5"})
    public void mod05test05DisableOnUnassign() throws IOException {
        addResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE_5_5, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE_5_5, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_5_5, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Internal Employee");

        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.sleep(2000);
        PrismForm<AccountPage> accountForm = shadow.form();
        accountForm
                .assertPropertySelectValue("administrativeStatus", "Disabled")
                .showEmptyAttributes("Attributes")
                .assertPropertyInputValues("groups", new ArrayList<String>());

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Disabled");

        showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        accountForm.assertPropertySelectValue("administrativeStatus", "Disabled");

        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Internal Employee");
        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        accountForm
                .assertPropertySelectValue("administrativeStatus", "Enabled")
                .assertPropertyInputValues("groups", "Internal Employees",
                        "Essential Documents");

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Enabled");

        showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        accountForm.assertPropertySelectValue("administrativeStatus", "Enabled");
    }

    @Test(dependsOnMethods = {"mod05test05DisableOnUnassign"}, groups={"M5"})
    public void mod05test06InactiveAssignment() {
        Utils.addAsignments(showUser("kirk").selectTabAssignments(), "Too Many Secrets");
        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<AccountPage> accountForm = shadow.form();
        accountForm.assertPropertyInputValues("groups", "Internal Employees",
                        "Essential Documents", "Lucky Numbers", "Teleportation",
                        "Time Travel", "Presidential Candidates Motivation",
                        "Area 52 Managers", "Area 52 News Obfuscators", "Abduction Professional Services",
                        "Immortality Training", "Telekinesis In Practice", "IDDQD");
        accountForm.assertPropertyInputValues("groups", "Lucky Numbers",
                        "Teleportation", "Time Travel", "Presidential Candidates Motivation");

        Utils.setStatusForAssignment(showUser("kirk").selectTabAssignments(), "Too Many Secrets", "Disabled");

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        accountForm
                .assertPropertySelectValue("administrativeStatus", "Enabled")
                .assertPropertyInputValues("groups", "Internal Employees", "Essential Documents");

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Enabled");

        showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        accountForm.assertPropertySelectValue("administrativeStatus", "Enabled");

        Utils.setStatusForAssignment(showUser("kirk").selectTabAssignments(), "Internal Employee", "Disabled");

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        accountForm
                .assertPropertySelectValue("administrativeStatus", "Disabled")
                .showEmptyAttributes("Attributes")
                .assertPropertyInputValues("groups", new ArrayList<String>());

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Disabled");

        showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        accountForm.assertPropertySelectValue("administrativeStatus", "Disabled");

        Utils.setStatusForAssignment(showUser("kirk").selectTabAssignments(), "Internal Employee", "Undefined");
        Utils.removeAssignments(showUser("kirk").selectTabAssignments(), "Too Many Secrets");
    }

    @Test(dependsOnMethods = {"mod05test06InactiveAssignment"}, groups={"M5"})
    public void mod05test07ArchetypesIntroduction() {

        addObjectFromFile(ARCHETYPE_EMPLOYEE_FILE);
        addObjectFromFile(ARCHETYPE_EXTERNAL_FILE);

        PrismForm<AssignmentHolderBasicTab<ArchetypePage>> archetypePolicyForm = basicPage.listArchetypes()
                .table()
                    .clickByName(ARCHETYPE_EMPLOYEE_NAME)
                        .selectTabArchetypePolicy()
                            .form();

        archetypePolicyForm.assertPropertyInputValue("label", ARCHETYPE_EMPLOYEE_LABEL);
        archetypePolicyForm.assertPropertyInputValue("pluralLabel", ARCHETYPE_EMPLOYEE_PLURAL_LABEL);
        archetypePolicyForm.assertPropertyInputValue("cssClass", "fa fa-user");
        archetypePolicyForm.assertPropertyInputValue("color", "darkgreen");

        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_5_7);

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login(getUsername(), getPassword());

        basicPage.listUsers().newObjectCollection("New "+ARCHETYPE_EMPLOYEE_LABEL.toLowerCase())
                .selectTabBasic()
                    .form()
                        .addAttributeValue(UserType.F_NAME, "janeway")
                        .addAttributeValue(UserType.F_GIVEN_NAME, "Kathryn")
                        .addAttributeValue(UserType.F_FAMILY_NAME, "Janeway")
                        .setDropDownAttributeValue(ActivationType.F_ADMINISTRATIVE_STATUS, "Enabled")
                        .addPasswordAttributeValue("abc123")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        AssignmentsTab<UserPage> assigmentsTab = showUser("janeway")
                .selectTabAssignments();
        assigmentsTab
                .table()
                .assertTableDoesntContainText(ARCHETYPE_EMPLOYEE_NAME);
        Utils.addAsignments(assigmentsTab, "Secret Projects I");
    }
}
