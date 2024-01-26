/*
 * Copyright (c) 2024  Evolveum
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

package com.evolveum.midpoint.schrodinger.trainings.first.steps;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import com.evolveum.midpoint.schrodinger.trainings.first.steps.util.dto.HRApplicationUserDto;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;

public class M8AutomatingIntegration extends AbstractTrainingTest {

    @Test(groups = MODULE_8_GROUP)
    public void test1generateUsernamesInMidPoint() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .table()
                .editMapping("empnum-to-name")
                .lifecycle("Archived")
                .and()
                .and()
                .saveMappings();
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .table()
                .editMapping("mapping-inbound-username-to-name-for-import")
                .lifecycle("Archived")
                .and()
                .and()
                .saveMappings();
        basicPage
                .listObjectTemplates()
                .table()
                .clickByName("Person Object Template")
                .selectMappingsPanel()
                .table()
                .clickByName("generate-name-jsmith-8-2")
                .setDropDownAttributeValue("Lifecycle state", "Active (production)")
                .and()
                .and()
                .and()
                .clickSave();

        registerNewUsersInHRApplicationAndExportUsers();

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .table()
                .clickReloadToolbarButton()
                .search()
                .byName()
                .inputValue("90")
                .updateSearch()
                .and()
                .importPreview("Name", "9000")
                .selectTaskExecutionMode("Simulated production")
                .select()
                .table()
                .screenshot("import-preview-9000")
                .assertTableContainsText("lcallaha")
                //todo midPoint will display information about new user lcallaha (and not 9000) which would be created
                .and()
                .backToResourcePage()
                .selectAccountsPanel()
                .table()
                .importPreview("Name", "9001")
                .selectTaskExecutionMode("Simulated production")
                .select()
                .table()
                .assertTableContainsText("abaker2")
                //todo midPoint will display information about new user abaker2 (and not 9001) which would be created (midPoint appends a number 2 because abaker user already exists in midPoint)
                .and()
                .backToResourcePage()
                .selectAccountsPanel()
                .tasks()
                .clickCreateTask()
                .reconciliationTask()
                //keep Simulate task value OFF
                .clickCreateTaskButton()
                .configuration()
                .name("HR Reconciliation")
                .next()
                .nextToSchedule()
                .interval("60")
                .next()
                .saveAndRun();

        Selenide.sleep(200000);

        basicPage
                .listUsers("Persons")
                //todo check the new users (no AD accounts have been created for them yet)
                .table()
                .search()
                .advanced("personalNumber startsWith '900'")
                .updateSearch()
                .and()
                .assertVisibleObjectsCountEquals(5)
                .assertTableContainsColumnWithValue("Personal Number", "9000")
                .assertTableContainsColumnWithValue("Name", "lcallaha")
                .assertTableContainsColumnWithValue("Full name", "Louise Callahan")
                .assertTableContainsColumnWithValue("Personal Number", "9001")
                .assertTableContainsColumnWithValue("Name", "abaker2")
                .assertTableContainsColumnWithValue("Full name", "Andreas Baker")
                .assertTableContainsColumnWithValue("Personal Number", "9002")
                .assertTableContainsColumnWithValue("Name", "cwhitehe2")
                .assertTableContainsColumnWithValue("Full name", "Clara Whiteherring")
                .assertTableContainsColumnWithValue("Personal Number", "9003")
                .assertTableContainsColumnWithValue("Name", "cwhitehe3")
                .assertTableContainsColumnWithValue("Full name", "Clara Whiteherring")
                .assertTableContainsColumnWithValue("Personal Number", "9004")
                .assertTableContainsColumnWithValue("Name", "jsmith3")
                .assertTableContainsColumnWithValue("Full name", "Jacques Smith")
                .search()
                .resetBasicSearch();
        basicPage
                .auditLogViewer()
                .table()
                .search()
                .dropDownPanelByItemName("Event Type")
                .inputDropDownValue("Add object operations")
                .dropDownPanelByItemName("Channel")
                .inputDropDownValue("Reconciliation")
                .screenshot("audit-log-viewer");
        //todo add assertions : look for Event type: Add object operations for Channel: Reconciliation
    }

    @Test(groups = MODULE_8_GROUP)
    public void test2automateActiveDirectoryAccountCreationForAllPersons() {
        basicPage
                .listArchetypes()
                .table()
                .clickByName("Person")
                .selectInducementsPanel()
                .selectTypeResource()
                .clickAddApplicationResource()
                .selectResource("AD")
                .next()
                .next()
                .next()
                .saveSettings();
        //todo wait for the next regular reconciliation with HR resource, it will add the AD accounts for the new users (otherwise full recomputation is needed)
        Selenide.sleep(20000);

        checkCreatedUser("lcallaha", "Louise Callahan",
                "cn=Louise Callahan,ou=users,dc=example,dc=com", "No AD DN uniqueness issues");
        checkCreatedUser("abaker2", "Andreas Baker",
                "cn=Andreas Baker,ou=users,dc=example,dc=com", "No AD DN uniqueness issues");
        checkCreatedUser("cwhitehe2", "Clara Whiteherring",
                "cn=Clara Whiteherring,ou=users,dc=example,dc=com", "No AD DN uniqueness issues");
        checkCreatedUser("cwhitehe3", "Clara Whiteherring",
                "cn=Clara Whiteherring (cwhitehe3),ou=users,dc=example,dc=com",
                "Iterated, because cn=Clara Whiteherring,ou=users,dc=example,dc=com already exists (for user cwhitehe2 (Clara Whiteherring)).");
        checkCreatedUser("jsmith3", "Jacques Smith",
                "cn=Jacques Smith,ou=users,dc=example,dc=com", "No AD DN uniqueness issues");

        basicPage
                .listUsers("Persons")
                .table()
                .search()
                .referencePanelByItemName("Users without account")
                .propertySettings()
                .inputRefName("AD")
                .applyButtonClick()
                .and()
                .selectCustomFiler("Users without account")
                .and()
                .assertVisibleObjectsCountEquals(0)
                .search()
                .selectCustomFiler("Users without account");
    }

    @Test(groups = MODULE_8_GROUP)
    public void test3automateADGroupMembershipForAllPersons() {
        basicPage
                .listResources()
.table()
                .clickByName("AD")
                .selectSchemaHandlingPanel()
                //todo notice the object type definition AD Group in Proposed lifecycle state
                .and()
                .selectAccountsPanel()
                .configureAssociations();
                //todo the adGroup association for group’s member attribute is configured in Proposed lifecycle state, ready for simulations.



        /**
         *
         * click Exit wizard
         *
         *
         *
         *
         * As the configuration is in Proposed lifecycle state, even if we have already scheduled reconciliation for HR resource, nothing bad will happen.
         * You don’t need to stop the scheduled task!
         *
         *
         * We will configure Person archetype to make all accounts members of AD’s cn=all-users group.
         *
         *
         *
         *
         * go to Archetypes  Person
         *
         *
         * edit Person archetype
         *
         *
         * click Inducements  Resource
         *
         *
         * edit AD resource inducement
         *
         *
         *
         * click Construction Associations tab
         *
         *
         *
         * click + button
         *
         *
         * in Grant entitlements / Group membership popup click on group cn=all-users,ou=groups,dc=example,dc=com or click Reload first if no groups are displayed
         *
         *
         * click Done
         *
         *
         *
         *
         *
         * click Done
         *
         *
         *
         *
         *
         * click Save
         *
         *
         *
         *
         * We will simulate what will happen for a single account, as usual.
         *
         *
         *
         *
         * go to Resources  All resources
         *
         *
         * edit HR resource
         *
         *
         * click Accounts menu item
         *
         *
         * click context menu for account 9000 and select Import preview
         *
         *
         *
         * in Select task execution mode select: Simulated development and click Select
         *
         *
         *
         *
         *
         *
         *
         * Simulated development mode will evaluate all Active and Proposed configuration items, but there will be no permanent effects on data; we are only simulating. In our particular case, the only Proposed configuration is the association configuration in AD resource.
         *
         *
         *
         *
         *
         *
         *
         *
         *
         * cn=Louise Callahan,ou=users,dc=example,dc=com AD resource account has a new indication Projection entitlement changed. Clicking the account simulation details will reveal a new association with group cn=all-users is going to be made
         *
         *
         * click Back to get to the list of processed objects
         *
         *
         * click Back to get to the list of HR accounts
         *
         *
         *
         *
         * We can run a simulated import or reconciliation task from HR resource to see what will happen for all users:
         *
         *
         *
         *
         * click Tasks, then click Create task item in the context menu to open a simple task creation wizard for HR accounts
         *
         *
         *
         * click Import Task tile
         *
         *
         * toggle Simulate task to ON
         *
         *
         * click Create task and fill in the following details:
         *
         *
         *
         * Name: Import from HR - development simulation
         *
         *
         *
         *
         *
         * click Next: Resource objects
         *
         *
         * click Next: Execution
         *
         *
         * in Execution options page, set the following:
         *
         *
         *
         * select Mode: Preview
         *
         *
         * select Predefined: Development
         *
         *
         *
         *
         *
         * click Next: Distribution
         *
         *
         * click Save & Run
         *
         *
         *
         *
         *
         *
         *
         * Running simulated import task with Development configuration will evaluate all Active and Proposed configuration items, but there will be no permanent effects on data; we are only simulating.
         *
         *
         *
         *
         *
         *
         *
         *
         *
         * click Defined Tasks menu item
         *
         *
         * edit the task Import from HR - development simulation and wait for the task completion (task status: closed)
         *
         *
         * click Show simulation result
         *
         *
         * the Simulation results show:
         *
         *
         *
         * all 5 recently created AD accounts are going to be added to cn=all-users,ou=groups,dc=example,dc=com group (click More info in Projection entitlement changed tile to see more details)
         *
         *
         *
         *
         *
         *
         *
         * Simulation looks OK for all 5 AD accounts.
         * Let’s activate the association configuration.
         *
         *
         *
         *
         * go to Resources  All resources
         *
         *
         * edit AD resource
         *
         *
         * click Accounts menu item
         *
         *
         * click Configure, then click Associations item in the context menu
         *
         *
         *
         * switch adGroup association lifecycle state to: Active (production)
         *
         *
         *
         *
         *
         * click Save association settings
         *
         *
         * wait for next regular reconciliation with HR resource, it will add the AD accounts for the new users to the cn=all-users group
         *
         *
         *
         *
         * To check the account membership after the reconciliation with HR finishes:
         *
         *
         *
         *
         * go to Users  Persons
         *
         *
         * edit any of the 5 recent users, e.g. lcallaha
         *
         *
         * click Projections menu item
         *
         *
         * click AD account
         *
         *
         * scroll down to Associations container
         *
         *
         * AD Group Membership should include the value: cn=all-users,ou=groups,dc=example,dc=com
         *
         *
         *
         *
         * All existing AD accounts before midPoint deployment were already members of the group.
         * All newly created AD accounts for people from HR will be automatically members of the group from now on.
         */
    }

    private void registerNewUsersInHRApplicationAndExportUsers() {
        Selenide.open("http://localhost/hr/");

        List<HRApplicationUserDto> users = new ArrayList<>();
        users.add(new HRApplicationUserDto("Louise", "Callahan", "", "9000",
                "White Stone City", "", "222#Export/Import Coordinator", "FTE", "In"));

        users.add(new HRApplicationUserDto("Andreas", "Baker", "", "9001",
                "White Stone City", "", "222#Export/Import Coordinator", "FTE", "In"));

        users.add(new HRApplicationUserDto("Clara", "Whiteherring", "", "9002",
                "White Stone City", "", "222#Export/Import Coordinator", "FTE", "In"));

        users.add(new HRApplicationUserDto("Clara", "Whiteherring", "", "9003",
                "White Stone City", "", "222#Export/Import Coordinator", "FTE", "In"));

        users.add(new HRApplicationUserDto("Jacques", "Smith", "", "9004",
                "White Stone City", "", "222#Export/Import Coordinator", "FTE", "In"));

        users.forEach(this::registerUserInHRApplication);

        $x(".//a[contains(text(), 'Show users')]").click();
        $x(".//input[@name='exportButton']").click();
        Selenide.sleep(3000);

        Selenide.open("http://localhost/midpoint/");

    }

    private void registerUserInHRApplication(HRApplicationUserDto userDto) {
        $x(".//a[contains(text(), 'Register user')]").click();
        fillInInputInHRApplication(HRApplicationUserDto.FIRST_NAME_FIELD_ID, userDto.getFirstName());
        fillInInputInHRApplication(HRApplicationUserDto.SURNAME_FIELD_ID, userDto.getSurname());
        fillInInputInHRApplication(HRApplicationUserDto.ARTNAME_FIELD_ID, userDto.getArtname());
        fillInInputInHRApplication(HRApplicationUserDto.EMPLOYEE_NUMBER_FIELD_ID, userDto.getEmployeeNumber());
        fillInInputInHRApplication(HRApplicationUserDto.LOCALITY_FIELD_ID, userDto.getLocality());
        fillInInputInHRApplication(HRApplicationUserDto.COUNTRY_FIELD_ID, userDto.getCountry());
        fillInInputInHRApplication(HRApplicationUserDto.JOB_FIELD_ID, userDto.getJob());
        fillInDropDownInHRApplication(HRApplicationUserDto.EMP_TYPE_FIELD_ID, userDto.getEmpType());
        fillInDropDownInHRApplication(HRApplicationUserDto.STATUS_FIELD_ID, userDto.getStatus());
        $x(".//input[@id='submitButtonb']").click();
        Selenide.sleep(3000);

    }

    private void fillInInputInHRApplication(String inputName, String value) {
        $x(".//input[@name='" + inputName + "']").setValue(value);
    }

    private void fillInDropDownInHRApplication(String selectName, String value) {
        $x(".//select[@name='" + selectName + "']").setValue(value);
    }

    private void checkCreatedUser(String username, String fullName, String adDn, String description) {
        basicPage
                .listUsers()
                .table()
                .search()
                .byName()
                .inputValue(username)
                .updateSearch()
                .and()
                .clickByName(username)
                .assertName(username)
                .assertFullName(fullName)
                .assertDescription(description)
                .selectProjectionsPanel()
                .table()
                .assertTableContainsText(adDn);
    }

}
