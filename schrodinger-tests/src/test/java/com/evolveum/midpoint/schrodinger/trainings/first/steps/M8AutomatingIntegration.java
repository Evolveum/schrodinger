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
                .inputDropDownValue("Reconciliation");
        //todo add assertions : look for Event type: Add object operations for Channel: Reconciliation
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

}
