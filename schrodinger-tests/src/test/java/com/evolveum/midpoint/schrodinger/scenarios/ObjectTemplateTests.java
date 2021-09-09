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
package com.evolveum.midpoint.schrodinger.scenarios;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ObjectTemplateTests extends AbstractSchrodingerTest {

    private static final String COMPONENT_OBJECTS_DIRECTORY = "./src/test/resources/objects/";
    private static final String COMPONENT_OBJECT_TEMPLATE = COMPONENT_OBJECTS_DIRECTORY + "objecttemplate/";
    private static final String COMPONENT_SYSTEM_CONFIGURATION = COMPONENT_OBJECTS_DIRECTORY + "systemconfiguration/";
    private static final String COMPONENT_ARCHETYPE = COMPONENT_OBJECTS_DIRECTORY + "archetypes/";
    private static final File FULL_NAME_OBJECT_TEMPLATE_FILE = new File(COMPONENT_OBJECT_TEMPLATE + "object-template-full-name.xml");
    private static final File SYSTEM_CONFIGURATION_WITH_OBJ_TEMPLATE_FILE = new File(COMPONENT_SYSTEM_CONFIGURATION + "system-configuration-with-object-template.xml");
    private static final File SYSTEM_CONFIGURATION_INITIAL_FILE = new File(COMPONENT_SYSTEM_CONFIGURATION + "system-configuration-initial.xml");
    private static final File SYSTEM_CONFIGURATION_WITH_EMPLOYEE_ARCHETYPE = new File(COMPONENT_SYSTEM_CONFIGURATION + "system-configuration-with-employee-archetype.xml");
    private static final File EMPLOYEE_ARCHETYPE_FILE = new File(COMPONENT_ARCHETYPE + "archetype-employee.xml");

    @Test
    public void test00100addObjectTemplateCreateUser() {
        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put("Name", "userWithoutFullname");
        attributesMap.put("Given name", "Mark");
        attributesMap.put("Family name", "Sadman");
        createUser(attributesMap);

        basicPage
                .listUsers()
                    .table()
                        .search()
                            .byName()
                            .inputValue("userWithoutFullname")
                            .updateSearch()
                        .and()
                        .assertTableContainsColumnWithValue("UserType.familyName", "Sadman")
                        .assertTableContainsColumnWithValue("UserType.givenName", "Mark")
                        .assertTableColumnValueIsEmpty("UserType.fullName");

        addObjectFromFile(FULL_NAME_OBJECT_TEMPLATE_FILE, true);
        addObjectFromFile(SYSTEM_CONFIGURATION_WITH_OBJ_TEMPLATE_FILE, true);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword());

        attributesMap.clear();
        attributesMap.put("Name", "userWithFullname");
        attributesMap.put("Given name", "Emil");
        attributesMap.put("Family name", "Happyman");
        createUser(attributesMap);

        basicPage
                .listUsers()
                    .table()
                        .search()
                            .byName()
                            .inputValue("userWithFullname")
                            .updateSearch()
                            .and()
                        .assertTableContainsColumnWithValue("UserType.givenName", "Emil")
                        .assertTableContainsColumnWithValue("UserType.familyName", "Happyman")
                        .assertTableContainsColumnWithValue("UserType.fullName", "Emil Happyman");
    }

    @Test (dependsOnMethods = {"test00100addObjectTemplateCreateUser"})
    public void test00200deleteObjectTemplateCreateUser() {
        basicPage
                .listRepositoryObjects()
                    .table()
                        .deleteObject("Object template", "Full name template");

        addObjectFromFile(SYSTEM_CONFIGURATION_INITIAL_FILE, true);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);

        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put("Name", "userWithoutFullname2");
        attributesMap.put("Given name", "John");
        attributesMap.put("Family name", "Johnson");
        createUser(attributesMap);
        showUser("userWithoutFullname2")
                .selectBasicPanel()
                    .form()
                        .assertPropertyInputValue("Full name", "");
    }

    @Test (dependsOnMethods = {"test00200deleteObjectTemplateCreateUser"})
    public void test00300createArchetypeObjectTemplateExists() {
        addObjectFromFile(EMPLOYEE_ARCHETYPE_FILE, true);
        addObjectFromFile(SYSTEM_CONFIGURATION_WITH_EMPLOYEE_ARCHETYPE, true);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword());

        basicPage
                .listUsers("Employees")
                    .newUser()
                        .selectBasicPanel()
                            .form()
                                .addAttributeValue("Name", "employeeTestUser")
                                .addAttributeValue("Given name", "Kevin")
                                .addAttributeValue("Family name", "Black")
                            .and()
                        .and()
                    .clickSave()
                        .feedback()
                            .assertSuccess();

        UsersPageTable table = basicPage
                .listUsers()
                    .table();
        table
                        .search()
                            .byName()
                            .inputValue("employeeTestUser")
                            .updateSearch()
                        .and()
                        .assertTableContainsColumnWithValue("UserType.givenName", "Kevin")
                        .assertTableContainsColumnWithValue("UserType.familyName", "Black")
                        .assertTableColumnValueIsEmpty("UserType.fullName");
        table.clickByName("employeeTestUser")
                .selectBasicPanel();
        addObjectFromFile(FULL_NAME_OBJECT_TEMPLATE_FILE, true);
        addObjectFromFile(SYSTEM_CONFIGURATION_WITH_OBJ_TEMPLATE_FILE, true);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);

        basicPage
                .listUsers("Employees")
                    .newUser()
                        .selectBasicPanel()
                            .form()
                                .addAttributeValue("Name", "employeeTestUserWithFullname")
                                .addAttributeValue("Given name", "Oskar")
                                .addAttributeValue("Family name", "White")
                            .and()
                        .and()
                    .clickSave()
                        .feedback()
                            .assertSuccess();

        basicPage
                .listUsers("Employees")
                    .table()
                        .search()
                            .byName()
                            .inputValue("employeeTestUserWithFullname")
                            .updateSearch()
                        .and()
                        .assertTableContainsColumnWithValue("UserType.givenName", "Oskar")
                        .assertTableContainsColumnWithValue("UserType.familyName", "White")
                        .assertTableContainsColumnWithValue("UserType.fullName", "Oskar White");
    }

    @Test (dependsOnMethods = {"test00300createArchetypeObjectTemplateExists"})
    public void test00400deleteObjectTemplateCreateEmployee() {
        basicPage
                .listRepositoryObjects()
                .table()
                .deleteObject("Object template", "Full name template");

        addObjectFromFile(SYSTEM_CONFIGURATION_INITIAL_FILE, true);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);

        basicPage
                .listUsers("Employees")
                    .newUser()
                        .selectBasicPanel()
                            .form()
                            .addAttributeValue("Name", "employeeAfterObjTemplRemove")
                            .addAttributeValue("Given name", "David")
                            .addAttributeValue("Family name", "Davidson")
                            .and()
                        .and()
                        .clickSave()
                        .feedback()
                            .assertSuccess();
       showUser("employeeAfterObjTemplRemove")
                .selectBasicPanel()
                    .form()
                    .assertPropertyInputValue("Full name", "");
    }

}
