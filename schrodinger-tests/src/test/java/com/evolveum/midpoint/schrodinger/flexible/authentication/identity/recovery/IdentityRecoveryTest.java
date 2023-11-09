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
package com.evolveum.midpoint.schrodinger.flexible.authentication.identity.recovery;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.ImportOptions;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class IdentityRecoveryTest extends AbstractSchrodingerTest {

    private static final String RESOURCES_PATH = "src/test/resources/features/identity/recovery";
    private static final String ARCHETYPE_PATH = RESOURCES_PATH + "/archetypes";
    private static final String OBJECT_TEMPLATE_PATH = RESOURCES_PATH + "/objectTemplates";
    private static final String SECURITY_POLICY_PATH = RESOURCES_PATH + "/securityPolicies";
    private static final String SYSTEM_CONFIGURATION_PATH = RESOURCES_PATH + "/systemConfiguration";
    private static final String USER_PATH = RESOURCES_PATH + "/users";

    private static final File ARCHETYPE_APPLICANT = new File(ARCHETYPE_PATH + "/archetype-applicant.xml");
    private static final File ARCHETYPE_PARENT = new File(ARCHETYPE_PATH + "/archetype-parent.xml");
    private static final File ARCHETYPE_STUDENT = new File(ARCHETYPE_PATH + "/archetype-student.xml");
    private static final File ARCHETYPE_TEACHER = new File(ARCHETYPE_PATH + "/archetype-teacher.xml");
    private static final File DEFAULT_OBJECT_TEMPLATE = new File(OBJECT_TEMPLATE_PATH + "/default-object-template.xml");
    private static final File OBJECT_TEMPLATE_APPLICANT = new File(OBJECT_TEMPLATE_PATH + "/object-template-applicant.xml");
    private static final File OBJECT_TEMPLATE_PARENT = new File(OBJECT_TEMPLATE_PATH + "/object-template-parent.xml");
    private static final File OBJECT_TEMPLATE_STUDENT = new File(OBJECT_TEMPLATE_PATH + "/object-template-student.xml");
    private static final File OBJECT_TEMPLATE_TEACHER = new File(OBJECT_TEMPLATE_PATH + "/object-template-teacher.xml");
    private static final File SECURITY_POLICY_DEFAULT = new File(SECURITY_POLICY_PATH + "/security-policy-default.xml");
    private static final File SECURITY_POLICY_APPLICANT = new File(SECURITY_POLICY_PATH + "/security-policy-applicant.xml");
    private static final File SECURITY_POLICY_PARENT = new File(SECURITY_POLICY_PATH + "/security-policy-parent.xml");
    private static final File SECURITY_POLICY_STUDENT = new File(SECURITY_POLICY_PATH + "/security-policy-student.xml");
    private static final File SECURITY_POLICY_TEACHER = new File(SECURITY_POLICY_PATH + "/security-policy-teacher.xml");
    private static final File INITIAL_SYSTEM_CONFIGURATION_UPDATED = new File(SYSTEM_CONFIGURATION_PATH + "/initial-system-configuration-updated.xml");

    private static final File USER_APPLICANT_CALEB_JAMES = new File(USER_PATH + "/user-applicant-caleb-james.xml");
    private static final File USER_APPLICANT_CALEB_JAMES_DUPLICATE = new File(USER_PATH + "/user-applicant-caleb-james-duplicate.xml");
    private static final File USER_NO_ARCHETYPE_CALEB_JAMES_1 = new File(USER_PATH + "/user-no-archetype-caleb-james-1.xml");
    private static final File USER_NO_ARCHETYPE_CALEB_JAMES_2 = new File(USER_PATH + "/user-no-archetype-caleb-james-2.xml");
    private static final File USER_NO_ARCHETYPE_CALEB_JAMES_3 = new File(USER_PATH + "/user-no-archetype-caleb-james-3.xml");
    private static final File USER_STUDENT_CALEB_JAMES_ECONOMY = new File(USER_PATH + "/user-student-caleb-james-economy.xml");
    private static final File USER_STUDENT_CALEB_JAMES_IT_VIENNA = new File(USER_PATH + "/user-student-caleb-james-IT-vienna.xml");
    private static final File USER_STUDENT_CALEB_JAMES_IT_SALZBURG = new File(USER_PATH + "/user-student-caleb-james-IT-salzburg.xml");
    private static final File USER_STUDENT_CALEB_JAMES_IT_GRAZ = new File(USER_PATH + "/user-student-caleb-james-IT-graz.xml");
    private static final File USER_TEACHER_BLAKE_ADAMS = new File(USER_PATH + "/user-teacher-blake-adams.xml");
    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(DEFAULT_SECURITY_POLICY_FILE);

        super.beforeClass();

        basicPage.loggedUser().logoutIfUserIsLogin();
    }

    @Override
    protected List<File> getObjectListToImport() {
        return Arrays.asList(DEFAULT_OBJECT_TEMPLATE, OBJECT_TEMPLATE_APPLICANT, OBJECT_TEMPLATE_PARENT,
                OBJECT_TEMPLATE_STUDENT, OBJECT_TEMPLATE_TEACHER, SECURITY_POLICY_DEFAULT, SECURITY_POLICY_APPLICANT,
                SECURITY_POLICY_PARENT, SECURITY_POLICY_STUDENT, SECURITY_POLICY_TEACHER, ARCHETYPE_APPLICANT,
                ARCHETYPE_PARENT, ARCHETYPE_STUDENT, ARCHETYPE_TEACHER,
                INITIAL_SYSTEM_CONFIGURATION_UPDATED, USER_APPLICANT_CALEB_JAMES,
                USER_APPLICANT_CALEB_JAMES_DUPLICATE, USER_NO_ARCHETYPE_CALEB_JAMES_1, USER_NO_ARCHETYPE_CALEB_JAMES_2,
                USER_NO_ARCHETYPE_CALEB_JAMES_3, USER_STUDENT_CALEB_JAMES_ECONOMY, USER_STUDENT_CALEB_JAMES_IT_VIENNA,
                USER_STUDENT_CALEB_JAMES_IT_SALZBURG, USER_STUDENT_CALEB_JAMES_IT_GRAZ, USER_TEACHER_BLAKE_ADAMS);
    }

    @BeforeMethod
    private void openLoginPage() {
        clearBrowser();
        midPoint.open();
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

    @Override
    protected List<String> createImportOptionList() {
        return new ImportOptions(false, true).createOptionList();
    }

    @Test
    public void test00100threeCorrelatorsCalebJamesIsFound() {
        midPoint.formLogin()
                .identityRecovery()
                .selectArchetype("Applicant")
                .send()
                .setAttributeValue("Given name", "Caleb")
                .setAttributeValue("Family name", "James")
                .send()
                .setAttributeValue("Date of birth", "4/23/2003")
                .setAttributeValue("City of birth", "Vienna")
                .setAttributeValue("Country of birth", "Austria")
                .send()
                .setAttributeValue("National ID", "718204-18")
                .send()
                .assertSingleIdentityFound("caleb.james");
    }

    @Test
    public void test00110threeCorrelatorsDuplicateUserIsFound() {
        midPoint.formLogin().login("administrator", "5ecr3t");
        basicPage.listUsers().table();
        Selenide.screenshot("test00100threeCorrelatorsCalebJamesIsFound_usersTable");
        UserPage user = basicPage.listUsers().table().clickByName("caleb.james");
        Utils.waitForAjaxCallFinish();
        Selenide.screenshot("test00100threeCorrelatorsCalebJamesIsFound_userData");
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .identityRecovery()
                .selectArchetype("Applicant")
                .send()
                .setAttributeValue("Given name", "Caleb")
                .setAttributeValue("Family name", "James")
                .send()
                .setAttributeValue("Date of birth", "4/23/2003")
                .setAttributeValue("City of birth", "Vienna")
                .setAttributeValue("Country of birth", "Austria")
                .send()
                .setAttributeValue("National ID", "718204-19")
                .send()
                .assertSingleIdentityFound("caleb.james.duplicate");
    }

    @Test
    public void test00120undefinedArchetypeMultipleIdentities() {
        midPoint.formLogin()
                .identityRecovery()
                .selectArchetype("Undefined")
                .send()
                .setAttributeValue("National ID", "718204-18")
                .send()
                .assertIdentityResultsPagingExist()
                .assertDisplayedIdentitiesCountEquals(3)
                .clickNextPage()
                .assertDisplayedIdentitiesCountEquals(3)
                .assertPageCountEquals(3);
    }

    @Test
    public void test00130teacherIdentityDisplayedInfo() {
        midPoint.formLogin()
                .identityRecovery()
                .selectArchetype("Teacher")
                .send()
                .setAttributeValue("Given name", "Blake")
                .setAttributeValue("Middle name", "Michael")
                .setAttributeValue("Family name", "Adams")
                .setAttributeValue("University ID", "9876543")
                .setAttributeValue("Department", "IT department")
                .setAttributeValue("National ID", "839211-21")
                .send()
                .assertSingleIdentityFound("blake.adams")
                .assertAttributeValueShown("Email", "blake.adams@test.com")
                .assertAttributeValueShown("Nickname", "Fixik");

        openLoginPage();
        midPoint.formLogin().login("administrator", "5ecr3t");

        basicPage.auditLogViewer()
                .table()
                .search()
                .dropDownPanelByItemName("Event type")
                .inputDropDownValue("Information disclosure")
                .updateSearch()
                .and()
                .clickByRowColumnNumber(0, 0)
                .deltaPanel()
                .assertObjectNameValueEquals("blake.adams");

    }

    @Test
    public void test00140candidateLimitIsExceeded() {
        midPoint.formLogin()
                .identityRecovery()
                .selectArchetype("Student")
                .send()
                .setAttributeValue("Given name", "Caleb")
                .setAttributeValue("Family name", "James")
                .setAttributeValue("University ID", "1234567")
                .setAttributeValue("Faculty", "IT")
                .send()
                .assertNoCandidateDisplayed()
                .feedback()
                .assertError();
    }

}
