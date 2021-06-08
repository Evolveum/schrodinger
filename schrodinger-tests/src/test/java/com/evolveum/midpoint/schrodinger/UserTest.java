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

package com.evolveum.midpoint.schrodinger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.component.DateTimePanel;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UserTest extends AbstractSchrodingerTest {

    private static final String LOCALIZATION_TEST_USER_NAME_ORIG = "localizationTestUserName";
    private static final String LOCALIZATION_TEST_USER_NAME_DE = "localizationTestUserNameDe";
    private static final String LOCALIZATION_VALUE = "de";
    private static final File DELEGATE_FROM_USER_FILE = new File("./src/test/resources/objects/users/delegate-from-user.xml");
    private static final File DELEGATE_TO_USER_FILE = new File("./src/test/resources/objects/users/delegate-to-user.xml");
    private static final File DELEGABLE_END_USER_ROLE_FILE = new File("./src/test/resources/objects/roles/delegable-end-user-role.xml");
    private static final File DELEGATE_END_USER_ROLE_FROM_USER_FILE = new File("./src/test/resources/objects/users/delegate-end-user-role-from-user.xml");
    private static final File DELEGATE_END_USER_ROLE_TO_USER_FILE = new File("./src/test/resources/objects/users/delegate-end-user-role-to-user.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(DELEGATE_FROM_USER_FILE, DELEGATE_TO_USER_FILE,
                DELEGABLE_END_USER_ROLE_FILE, DELEGATE_END_USER_ROLE_FROM_USER_FILE, DELEGATE_END_USER_ROLE_TO_USER_FILE);
    }

    @Test
    public void test0010createUser() {

        //@formatter:off
        Map<String, String> attr = new HashMap<>();
        attr.put("name", "jdoe222323");
        attr.put(UserType.F_GIVEN_NAME.getLocalPart(), "john");
        attr.put(UserType.F_FAMILY_NAME.getLocalPart(), "doe");
        createUser(attr);

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                .search()
                .byName()
                .inputValue("jdoe222323")
                .updateSearch()
                .and()
                .clickByName("jdoe222323")
                .assertName("jdoe222323")
                .assertGivenName("john")
                .assertFamilyName("doe");

    }

    @Test //covers MID-5845
    public void test0020isLocalizedPolystringValueDisplayed(){
        UserPage user = basicPage.newUser();
        user.selectTabBasic()
                        .form()
                        .addAttributeValue("name", LOCALIZATION_TEST_USER_NAME_ORIG)
                        .setPolyStringLocalizedValue(UserType.F_NAME, LOCALIZATION_VALUE, LOCALIZATION_TEST_USER_NAME_DE)
                        .and()
                        .and()
                        .clickSave()
                        .feedback()
                        .assertSuccess();

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.loginWithReloadLoginPage(getUsername(), getPassword(), LOCALIZATION_VALUE);

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(LOCALIZATION_TEST_USER_NAME_ORIG)
                        .updateSearch()
                    .and()
                    .clickByName(LOCALIZATION_TEST_USER_NAME_ORIG)
                        .assertName(LOCALIZATION_TEST_USER_NAME_DE);
    }

    @Test
    public void test0030createDelegationTest() {
        showUser("DelegateFromUser")
                .selectTabDelegations()
                    .clickAddDelegation()
                        .table()
                            .search()
                            .byName()
                            .inputValue("DelegateToUser")
                            .updateSearch()
                        .and()
                        .clickByName("DelegateToUser")
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();

        showUser("DelegateToUser")
                .selectTabDelegatedToMe()
                    .getDelegationDetailsPanel("DelegateFromUser")
                        .expandDetailsPanel("DelegateFromUser")
                        .assertAssignmentPrivilegesNotSelected()
                        .assertAssignmentLimitationsNotSelected()
                        .assertApprovalWorkItemsSelected()
                        .assertCertificationWorkItemsSelected()
                        .assertDescriptionDisabled()
                        .assertValidFromPanelDisabled();

        showUser("DelegateFromUser")
                .selectTabDelegations()
                    .getDelegationDetailsPanel("DelegateToUser")
                        .expandDetailsPanel("DelegateToUser")
                        .assertAssignmentPrivilegesNotSelected()
                        .assertAssignmentLimitationsNotSelected()
                        .assertApprovalWorkItemsSelected()
                        .assertCertificationWorkItemsSelected()
                        .assertDescriptionDisabled()
                        .assertValidFromPanelDisabled();
    }

    @Test
    public void test0040delegateAssignmentPrivileges() {
        basicPage.loggedUser().logout();
        midPoint.formLogin().login("DelegateEndUserRoleToUser", "password")
                        .feedback()
                        .assertError();
        midPoint.formLogin().login(username, password);

        showUser("DelegateEndUserRoleFromUser")
                .selectTabDelegations()
                    .clickAddDelegation()
                        .table()
                            .search()
                            .byName()
                            .inputValue("DelegateEndUserRoleToUser")
                            .updateSearch()
                        .and()
                        .clickByName("DelegateEndUserRoleToUser")
                            .getDelegationDetailsPanel("DelegateEndUserRoleToUser")
                            .getValidFromPanel()
                            .setDateTimeValue("11/11/2019", "10", "30", DateTimePanel.AmOrPmChoice.PM)
                            .and()
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .assertSuccess();

        basicPage.loggedUser().logout();
        midPoint.formLogin().login("DelegateEndUserRoleToUser", "password")
                        .assertUserMenuExist();
    }

}
