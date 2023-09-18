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

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.configuration.AdminGuiPanel;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

/**
 * Created by honchar
 */
public class ObjectListArchetypeTests extends AbstractSchrodingerTest {

    private static final File EMPLOYEE_ARCHETYPE_FILE = new File("src/test/resources/objects/archetypes/archetype-employee.xml");
    private static final String ARCHETYPE_OBJECT_NAME = "Employee";
    private static final String ARCHETYPE_PLURAL_LABEL = "Employees";
    private static final String ARCHETYPE_ICON_CSS_STYLE = "fa fa-male";
    private static final String EMPLOYEE_USER_NAME_VALUE = "TestEmployee";

    public static final String OBJECT_LIST_ARCHETYPE_TESTS_GROUP = "ObjectListArchetypeTests";

    @Override
    protected List<File> getObjectListToImport() {
        return Collections.singletonList(EMPLOYEE_ARCHETYPE_FILE);
    }

    @Test(priority = 1, groups = OBJECT_LIST_ARCHETYPE_TESTS_GROUP)
    public void test0010configureArchetypeObjectListView(){
        AdminGuiPanel adminGuiTab = basicPage.adminGui();
        adminGuiTab
                .addNewObjectCollection(ARCHETYPE_PLURAL_LABEL, "User", "Archetype", ARCHETYPE_PLURAL_LABEL)
                    .feedback()
                        .isSuccess();
    }



    @Test(priority = 2, dependsOnMethods ={"test0010configureArchetypeObjectListView"}, groups = OBJECT_LIST_ARCHETYPE_TESTS_GROUP)
    public void test0020actualizeArchetypeConfiguration() {
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage(getUsername(), getPassword());

        //check archetype pluralLabel
        basicPage
                .listUsers(ARCHETYPE_PLURAL_LABEL)
                .assertAdministrationMenuItemIconClassContains("PageAdmin.menu.top.users", ARCHETYPE_PLURAL_LABEL, "fa fa-male");
        basicPage
                .listUsers(ARCHETYPE_PLURAL_LABEL)
                .table()
                .assertButtonToolBarExists()
                .getToolbarButtonByCss(ARCHETYPE_ICON_CSS_STYLE)        //fails here because of MID-8254
                .shouldBe(Condition.visible)
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

    }

    @Test(priority = 3, dependsOnMethods ={"test0020actualizeArchetypeConfiguration"}, groups = OBJECT_LIST_ARCHETYPE_TESTS_GROUP)
    public void test0030createNewEmployeeUser(){
        ListUsersPage collectionListPage = basicPage.listUsers(ARCHETYPE_PLURAL_LABEL);

        collectionListPage
                .table()
                    .newObjectButtonByCssClick(ARCHETYPE_ICON_CSS_STYLE)
                        .selectBasicPanel()
                            .form()
                                .addAttributeValue("name", EMPLOYEE_USER_NAME_VALUE)
                            .and()
                        .and()
                    .clickSave()
                .feedback()
                .isSuccess();

        basicPage.listUsers(ARCHETYPE_PLURAL_LABEL)
                .table()
                    .search()
                        .byName()
                            .inputValue(EMPLOYEE_USER_NAME_VALUE)
                                .updateSearch()
                            .and()
                        .clickByName(EMPLOYEE_USER_NAME_VALUE);

    }

    @Test(priority = 4, dependsOnMethods ={"test0020actualizeArchetypeConfiguration"})
    public void test0040checkNewObjectTemplatePanel(){
        basicPage.listUsers()
                .table()
                .assertTemplatePanelButtonsCountEquals(3);
    }

}

