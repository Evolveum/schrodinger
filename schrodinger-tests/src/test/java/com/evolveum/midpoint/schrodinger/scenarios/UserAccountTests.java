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

import com.evolveum.midpoint.schrodinger.page.self.HomePage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.testng.annotations.Test;

/**
 * Created by matus on 5/9/2018.
 */
public class UserAccountTests extends AccountTests {

    private static final String DISABLE_MP_USER_DEPENDENCY = "test0030disableUser";
    private static final String ENABLE_MP_USER_DEPENDENCY = "test0040enableUser";
    private static final String BULK_DISABLE_MP_USER_DEPENDENCY = "test0050bulkDisableUsers";

    @Test (dependsOnMethods = {CREATE_MP_USER_DEPENDENCY}, groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0010modifyUserAttribute(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                    .table()
                        .search()
                        .byName()
                            .inputValue(TEST_USER_MIKE_NAME)
                            .updateSearch()
                    .and()
                    .clickByName(TEST_USER_MIKE_NAME)
                        .selectBasicPanel()
                            .form()
                            .changeAttributeValue("Given name","Michelangelo","Michael Angelo")
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .isSuccess();
    }
    @Test (dependsOnMethods = {CREATE_MP_USER_DEPENDENCY, MODIFY_ACCOUNT_PASSWORD_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0020modifyUserPassword(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                    .table()
                        .search()
                        .byName()
                            .inputValue(TEST_USER_MIKE_NAME)
                            .updateSearch()
                    .and()
                    .clickByName(TEST_USER_MIKE_NAME)
                        .selectBasicPanel()
                            .form()
                            .showEmptyAttributes("Password")
                            .addPasswordAttributeValue("S36re7")
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .isSuccess();

    }

    @Test (dependsOnMethods = {CREATE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0030disableUser(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                .table()
                    .search()
                    .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                        .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectBasicPanel()
                        .form()
                        .showEmptyAttributes("Activation")
                        .selectOption("Administrative status","Disabled")
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .isSuccess();
    }

    @Test (dependsOnMethods = {DISABLE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0040enableUser(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                .table()
                    .search()
                    .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                        .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectBasicPanel()
                        .form()
                        .showEmptyAttributes("Activation")
                        .selectOption("Administrative status","Enabled")
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .isSuccess();
    }

    @Test (dependsOnMethods = {ENABLE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0050bulkDisableUsers(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .selectAll()
                .and()
                    .table()
                        .disableUser()
                            .clickYes()
                        .and()
                    .feedback()
                        .isSuccess()
            ;
    }

    @Test (dependsOnMethods = {BULK_DISABLE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0060bulkEnableUsers(){
        ListUsersPage usersPage = basicPage.listUsers();
            usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .selectAll()
                .and()
                    .table()
                    .enableUser()
                        .clickYes()
                    .and()
                .feedback()
                .isSuccess()
        ;
    }

    @Test (dependsOnMethods = {CREATE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0070searchUser(){
    ListUsersPage usersPage = basicPage.listUsers();
    usersPage
                       .table()
                            .search()
                                .byName()
                                .inputValue(TEST_USER_MIKE_NAME)
                            .updateSearch()
                       .and()
                       .assertCurrentTableContains(TEST_USER_MIKE_NAME);
    }

    @Test (dependsOnMethods = {CREATE_MP_USER_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0080searchUserFromHome(){
        HomePage homePage = basicPage.home();
        homePage
                    .search()
                        .clickSearchFor()
                    .clickUsers()
                    .inputValue(TEST_USER_MIKE_NAME)
                        .clickSearch()
                        .assertCurrentTableContains(TEST_USER_MIKE_NAME);

    }

    @Test (dependsOnGroups = {TEST_GROUP_BEFORE_USER_DELETION})
    public void test0090bulkDeleteUsers(){
        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                    .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .selectAll()
                .and()
                    .table()
                        .deleteUser()
                        .clickYes()
                    .and()
                .feedback()
                    .isSuccess();
    }
}
