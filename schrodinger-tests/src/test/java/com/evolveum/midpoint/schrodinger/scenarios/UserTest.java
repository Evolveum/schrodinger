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
import java.util.*;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.assertj.core.api.Assertions;
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
    private static final File OBJECT_TEMPLATE_REQUIRED_EMAIL_FILE = new File("./src/test/resources/objects/objecttemplate/object-template-required-email.xml");
    private static final File SYSTEM_CONFIGURATION_REQUIRED_EMAIL_TEMPLATE_FILE = new File("./src/test/resources/objects/systemconfiguration/system-configuration-email-required-template.xml");
    private static final File DELEGATION_REQUIRES_VALID_TO_ROLE_FILE = new File("./src/test/resources/objects/roles/role-delegation-requires-valid-to.xml");
    private static final File DELEGATION_POLICY_FROM_USER_FILE = new File("./src/test/resources/objects/users/delegation-policy-from-user.xml");
    private static final File DELEGATION_POLICY_TO_USER_FILE = new File("./src/test/resources/objects/users/delegation-policy-to-user.xml");

    private static final String DELEGATION_POLICY_FROM_USER_NAME = "DelegationPolicyFromUser";
    private static final String DELEGATION_POLICY_TO_USER_NAME = "DelegationPolicyToUser";
    private static final String DELEGATION_POLICY_TO_USER_OID = "3c0e6b7a-5d0c-4e4b-9d33-12364a000003";
    private static final String DELEGATION_POLICY_VIOLATION_MESSAGE = "Delegation must have the valid to date set";

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(DELEGATE_FROM_USER_FILE, DELEGATE_TO_USER_FILE,
                DELEGABLE_END_USER_ROLE_FILE, DELEGATE_END_USER_ROLE_FROM_USER_FILE, DELEGATE_END_USER_ROLE_TO_USER_FILE,
                DELEGATION_REQUIRES_VALID_TO_ROLE_FILE,
                DELEGATION_POLICY_FROM_USER_FILE, DELEGATION_POLICY_TO_USER_FILE);
    }

    @Test
    public void test0010createUser() {
        //@formatter:off
        Map<String, String> attr = new HashMap<>();
        attr.put("Name", "jdoe");
        attr.put("Given name", "john");
        attr.put("Family name", "doe");
        createUser(attr);
        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                   .search()
                        .byName()
                        .inputValue("jdoe")
                        .updateSearch()
                        .and()
                    .clickByName("jdoe")
                        .assertName("jdoe")
                        .assertGivenName("john")
                        .assertFamilyName("doe");

    }

    @Test //covers MID-5845
    public void test0020isLocalizedPolystringValueDisplayed(){
        UserPage user = basicPage.newUser();
        user.selectBasicPanel()
                .form()
                    .addAttributeValue("name", LOCALIZATION_TEST_USER_NAME_ORIG)
                        .setPolyStringLocalizedValue(UserType.F_NAME, LOCALIZATION_VALUE, LOCALIZATION_TEST_USER_NAME_DE)
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();

        basicPage.changeLanguageAfterLogin(LOCALIZATION_VALUE);

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(LOCALIZATION_TEST_USER_NAME_ORIG)
                        .updateSearch()
                    .and()
                    .clickByName(LOCALIZATION_TEST_USER_NAME_ORIG)
                        .assertElementWithValueExists(LOCALIZATION_TEST_USER_NAME_DE);
        basicPage.changeLanguageAfterLogin("de", "us");
    }

    @Test
    public void test0030createDelegationTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        int prevYear = calendar.get(Calendar.YEAR);

        showUser("DelegateFromUser")
                .selectDelegationsPanel()
                    .clickAddDelegation()
                        .table()
                            .search()
                            .byName()
                            .inputValue("DelegateToUser")
                            .updateSearch()
                        .and()
                        .clickByName("DelegateToUser")
                        .getDelegationDetailsPanel("DelegateToUser")
                        .getValidFromPanel()
                            .setDateTimeValueByPicker(
                                    1,
                                    1,
                                    prevYear,
                                    1,
                                    10,
                                    DateTimePanel.AmOrPmChoice.AM)
                            .and()
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertInfo();

        showUser("DelegateToUser")
                .selectDelegatedToMePanel()
                    .getDelegationDetailsPanel("DelegateFromUser")
                        .expandDetailsPanel()
                        .assertAssignmentPrivilegesNotSelected()
                        .assertAssignmentLimitationsNotSelected()
                        .assertApprovalWorkItemsSelected()
                        .assertCertificationWorkItemsSelected()
                        .assertDescriptionDisabled()
                        .assertValidFromPanelDisabled()
                        .getValidFromPanel()
                            .assertDateTimeValueEquals("January 1, " + prevYear + ", 01:10 AM");

        showUser("DelegateFromUser")
                .selectDelegationsPanel()
                    .getDelegationDetailsPanel("DelegateToUser")
                        .expandDetailsPanel()
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
        midPoint.formLogin().login("DelegateEndUserRoleToUser", "pAssword123")
                        .feedback()
                        .assertError();
        midPoint.formLogin().login(username, password);

        showUser("DelegateEndUserRoleFromUser")
                .selectDelegationsPanel()
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
                            .setDateTimeValue("November 11, 2019", "10", "30", DateTimePanel.AmOrPmChoice.PM)
                            .and()
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .assertInfo();

        basicPage.loggedUser().logout();
        midPoint.formLogin().login("DelegateEndUserRoleToUser", "pAssword123")
                        .assertUserMenuExist();
        basicPage.loggedUser().logout();
        midPoint.formLogin().login("administrator", "Test5ecr3t");

    }

    /**
     * check if validation error is visible after form submitting in case when required field is empty.
     * also checks that validation error is single
     *  inspired by mid-8886
     */
    @Test
    private void test0050checkRequiredFieldValidationErrorVisibility() {
        importObject(OBJECT_TEMPLATE_REQUIRED_EMAIL_FILE, true);
        importObject(SYSTEM_CONFIGURATION_REQUIRED_EMAIL_TEMPLATE_FILE, true);

        basicPage.newUser()
                .clickSave()
                .feedbackContainer()
                .assertFeedbackMessagesCountEquals(1)
                .and()
                .feedback()
                .assertMessageExists("Required emailAddress");
    }

    /**
     * check if validation error is visible after second form submitting in case when required field is empty.
     * also checks that validation error is single
     *  inspired by mid-8886
     */
    @Test
    private void test0060checkRequiredFieldValidationErrorVisibilityAfterSecondFormSubmit() {
        importObject(OBJECT_TEMPLATE_REQUIRED_EMAIL_FILE, true);
        importObject(SYSTEM_CONFIGURATION_REQUIRED_EMAIL_TEMPLATE_FILE, true);

        UserPage page = basicPage.newUser();
        page
                .clickSave()
                .feedbackContainer()
                .assertFeedbackMessagesCountEquals(1)
                .and()
                .feedback()
                .assertMessageExists("Required emailAddress");

        page.clickSave()
                .feedbackContainer()
                .assertFeedbackMessagesCountEquals(1)
                .and()
                .feedback()
                .assertMessageExists("Required emailAddress");
    }

    @Test
    private void test0070checkWrongEmailValidationError() {
        importObject(OBJECT_TEMPLATE_REQUIRED_EMAIL_FILE, true);
        importObject(SYSTEM_CONFIGURATION_REQUIRED_EMAIL_TEMPLATE_FILE, true);

        UserPage page = basicPage.newUser();
        page
                .selectBasicPanel()
                .form()
                .addAttributeValue("Email", "a")
                .and()
                .and()
                .clickSave()
                .feedbackContainer()
                .assertFeedbackMessagesCountEquals(1)
                .and()
                .feedback()
                .assertMessageExists("The emailAddress is invalid: a");

    }

    /**
     * Delegation is saved to the deputy user separately from the edited (delegator) user.
     * When a policy rule prevented the delegation from being saved, the policy violation
     * message was displayed only for a moment. Then the page was redirected to the users list
     * with only the "Save (GUI)" info message, so the user didn't know why the delegation was not created.
     * DelegationPolicyToUser has a role with a policy rule which doesn't allow a delegation without "Valid to" date.
     * Covers #12364
     */
    @Test
    public void test0100delegationPolicyViolationMessageIsVisible() throws Exception {
        reloginAsAdministrator();
        showUser(DELEGATION_POLICY_FROM_USER_NAME)
                .selectDelegationsPanel()
                .clickAddDelegation()
                .table()
                .search()
                .byName()
                .inputValue(DELEGATION_POLICY_TO_USER_NAME)
                .updateSearch()
                .and()
                .clickByName(DELEGATION_POLICY_TO_USER_NAME)
                .and()
                .clickSave();

        // the redirect to the users list came a few seconds after the save, give it time to happen
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
        Utils.waitForAjaxCallFinish();

        basicPage.feedbackContainer()
                .assertFeedbackMessageContains(DELEGATION_POLICY_VIOLATION_MESSAGE);

        UserType deputy = getUser(DELEGATION_POLICY_TO_USER_OID);
        Assertions.assertThat(deputy.getDelegatedRef())
                .as("Delegation should not be created, it violates the policy rule")
                .isEmpty();
    }

}
