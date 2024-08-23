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
package com.evolveum.midpoint.schrodinger.component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schema.constants.SchemaConstants;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.org.MemberPanel;
import com.evolveum.midpoint.schrodinger.component.org.MemberTable;
import com.evolveum.midpoint.schrodinger.component.org.OrgRootTab;

import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.role.RolesPageTable;
import com.evolveum.midpoint.schrodinger.page.service.ServicesPageTable;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

/**
 * Created by honchar
 */
public class SearchPanelTest extends AbstractSchrodingerTest {

    private static final String COMPONENT_RESOURCES_DIRECTORY = "./src/test/resources/";
    private static final String COMPONENT_OBJECTS_DIRECTORY = COMPONENT_RESOURCES_DIRECTORY + "objects/";
    private static final String COMPONENT_USERS_DIRECTORY = COMPONENT_OBJECTS_DIRECTORY + "users/";
    private static final String COMPONENT_ROLES_DIRECTORY = COMPONENT_OBJECTS_DIRECTORY + "roles/";
    private static final String COMPONENT_ORGS_DIRECTORY = COMPONENT_OBJECTS_DIRECTORY + "orgs/";
    private static final String COMPONENT_SYSTEM_CONFIG_DIRECTORY = COMPONENT_OBJECTS_DIRECTORY + "systemconfiguration/";

    private static final File SEARCH_CONFIG_SYSTEM_CONFIG_FILE = new File(COMPONENT_SYSTEM_CONFIG_DIRECTORY + "system-configuration-search-configuration.xml");
    private static final File SEARCH_CONFIG_WITHOUT_DEFAULT_ITEM_SYSTEM_CONFIG_FILE = new File(COMPONENT_SYSTEM_CONFIG_DIRECTORY + "system-configuration-search-without-def-search-items.xml");
    private static final File SEARCH_BY_NAME_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "searchByNameUser.xml");
    private static final File SEARCH_BY_GIVEN_NAME_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "searchByGivenNameUser.xml");
    private static final File SEARCH_BY_FAMILY_NAME_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "searchByFamilyNameUser.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_NAME_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-role-membership-name-search.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_OID_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-role-membership-oid-search.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_TYPE_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-role-membership-type-search.xml");
    private static final File ORG_MEMBER_SEARCH_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-org-assignment-member-search.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_RELATION_USER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-role-membership-relation-search.xml");
    private static final File REQUESTABLE_ROLE_FILE = new File(COMPONENT_ROLES_DIRECTORY + "requestableRole.xml");
    private static final File DISABLED_ROLE_FILE = new File(COMPONENT_ROLES_DIRECTORY + "disabledRole.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_NAME_ROLE_FILE = new File(COMPONENT_ROLES_DIRECTORY + "role-membership-search-by-name.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_OID_ROLE_FILE = new File(COMPONENT_ROLES_DIRECTORY + "role-membership-search-by-oid.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_TYPE_ORG_FILE = new File(COMPONENT_ORGS_DIRECTORY + "org-membership-search-by-type.xml");
    private static final File ORG_MEMBER_SEARCH_ROOT_ORG_FILE = new File(COMPONENT_ORGS_DIRECTORY + "org-root-member-search.xml");
    private static final File SEARCH_BY_ROLE_MEMBERSHIP_RELATIONS_ROLE_FILE = new File(COMPONENT_ROLES_DIRECTORY + "role-membership-search-by-relation.xml");
    private static final File SYSTEM_CONFIG_WITH_CONFIGURED_USER_SEARCH = new File(COMPONENT_SYSTEM_CONFIG_DIRECTORY + "system-configuration-with-configured-user-search.xml");
    private static final File USER_WITH_EMPLOYEE_NUMBER_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-with-employee-number.xml");
    private static final File USER_WITH_EMAIL_ADDRESS_FILE = new File(COMPONENT_USERS_DIRECTORY + "user-with-email-address.xml");

    private static final String NAME_ATTRIBUTE = "Name";
    private static final String GIVEN_NAME_ATTRIBUTE = "Given name";
    private static final String FAMILY_NAME_ATTRIBUTE = "Family name";
    private static final String REQUESTABLE_ATTRIBUTE = "Requestable";
    private static final String ADMINISTRATIVE_STATUS_ATTRIBUTE = "Administrative status";
    private static final String ROLE_MEMBERSHIP_ATTRIBUTE = "Role membership";
    private static final String REF_SEARCH_FIELD_VALUE = "roleMembershipByNameSearch";

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(SEARCH_BY_NAME_USER_FILE, SEARCH_BY_GIVEN_NAME_USER_FILE, SEARCH_BY_FAMILY_NAME_USER_FILE,
                REQUESTABLE_ROLE_FILE, DISABLED_ROLE_FILE, SEARCH_BY_ROLE_MEMBERSHIP_NAME_USER_FILE, SEARCH_BY_ROLE_MEMBERSHIP_OID_USER_FILE,
                SEARCH_BY_ROLE_MEMBERSHIP_TYPE_USER_FILE, ORG_MEMBER_SEARCH_USER_FILE, SEARCH_BY_ROLE_MEMBERSHIP_RELATION_USER_FILE,
                SEARCH_BY_ROLE_MEMBERSHIP_NAME_ROLE_FILE, SEARCH_BY_ROLE_MEMBERSHIP_OID_ROLE_FILE, ORG_MEMBER_SEARCH_ROOT_ORG_FILE,
                SEARCH_BY_ROLE_MEMBERSHIP_TYPE_ORG_FILE, SEARCH_BY_ROLE_MEMBERSHIP_RELATIONS_ROLE_FILE, USER_WITH_EMPLOYEE_NUMBER_FILE,
                USER_WITH_EMAIL_ADDRESS_FILE);
    }

    @Test
    public void test0010defaultSearchOnListPage() {
        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> usersListSearch = (Search<UsersPageTable>) table.search();

        usersListSearch
                .textInputPanelByItemName(NAME_ATTRIBUTE)
                .inputValue("searchByNameUser")
                .updateSearch();
        table.assertTableContainsText("searchByNameUser");
        table.assertTableDoesntContainText("search by given name user");
        table.assertTableDoesntContainText("search by family name user");
        usersListSearch.clearTextSearchItemByNameAndUpdate(NAME_ATTRIBUTE);

        usersListSearch
                .textInputPanelByItemName(GIVEN_NAME_ATTRIBUTE)
                .inputValue("searchByGivenNameUser")
                .updateSearch();
        table.assertTableDoesntContainText("searchByNameUser");
        table.assertTableContainsText("search by given name user");
        table.assertTableDoesntContainText("search by family name user");
        usersListSearch.clearTextSearchItemByNameAndUpdate(GIVEN_NAME_ATTRIBUTE);

        usersListSearch
                .textInputPanelByItemName(FAMILY_NAME_ATTRIBUTE)
                .inputValue("searchByFamilyNameUser")
                .updateSearch();
        table.assertTableDoesntContainText("searchByNameUser");
        table.assertTableDoesntContainText("search by given name user");
        table.assertTableContainsText("search by family name user");
        usersListSearch.clearTextSearchItemByNameAndUpdate(FAMILY_NAME_ATTRIBUTE);
    }

    @Test
    public void test0020addSearchAttributeByAddButtonClick() {
        RolesPageTable table = basicPage.listRoles().table();
        Search<RolesPageTable> search = (Search<RolesPageTable>) table.search();
        search.addSearchItemByAddButtonClick(REQUESTABLE_ATTRIBUTE)
                .assertExistSearchItem(REQUESTABLE_ATTRIBUTE);
    }

    @Test
    public void test0030addSearchAttributeByNameLinkClick() {
        ServicesPageTable table = basicPage.listServices().table();
        Search<ServicesPageTable> search = (Search<ServicesPageTable>) table.search();
        search
                .addSearchItemByNameLinkClick(ROLE_MEMBERSHIP_ATTRIBUTE)
                .assertExistSearchItem(ROLE_MEMBERSHIP_ATTRIBUTE);
    }

    @Test
    public void test0040booleanAttributeSearch() {
        logoutLoginToRefreshSearch();
        RolesPageTable table = basicPage.listRoles().table();
        Search<RolesPageTable> search = (Search<RolesPageTable>) table.search();
//        search.resetBasicSearch();
        table.assertAllObjectsCountNotEquals(1);
        search.dropDownPanelByItemName(REQUESTABLE_ATTRIBUTE)
                .inputDropDownValue("True")
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
    }

    @Test
    public void test0050enumAttributeSearch() {
        logoutLoginToRefreshSearch();
        RolesPageTable table = basicPage.listRoles().table();
        Search<RolesPageTable> search = (Search<RolesPageTable>) table.search();
//        search.resetBasicSearch();
        table.assertAllObjectsCountNotEquals(1);
        search.dropDownPanelByItemName(ADMINISTRATIVE_STATUS_ATTRIBUTE)
                .inputDropDownValue("Disabled")
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
    }

    @Test
    public void test0060referenceAttributeByOidSearch() {
        logoutLoginToRefreshSearch();
        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> search = (Search<UsersPageTable>) table.search();
//        search.resetBasicSearch();
        table.assertAllObjectsCountNotEquals(1);
        search.referencePanelByItemName(ROLE_MEMBERSHIP_ATTRIBUTE)
                .propertySettings()
                .inputRefOid("959870f4-5b63-11ed-9b6a-0242ac120002")
                .applyButtonClick()
                .and()
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
        table.assertTableContainsLinkTextPartially("testUserWithRoleMembershipSearchByOid");
    }

    @Test
    public void test0070referenceAttributeByTypeSearch() {
        logoutLoginToRefreshSearch();
        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> search = (Search<UsersPageTable>) table.search();
//        search.resetBasicSearch();
        search.referencePanelByItemName(ROLE_MEMBERSHIP_ATTRIBUTE)
                .propertySettings()
                .inputRefType("Organization")
                .applyButtonClick()
                .and()
                .updateSearch();
        table.assertVisibleObjectsCountEquals(2);
        table.assertTableContainsLinkTextPartially("testUserWithRoleMembershipSearchByType");
    }

    @Test
    public void test0080referenceAttributeByRelationSearch() {
        logoutLoginToRefreshSearch();
        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> search = (Search<UsersPageTable>) table.search();
//        search.resetBasicSearch();
        search.referencePanelByItemName(ROLE_MEMBERSHIP_ATTRIBUTE)
                .propertySettings()
                .inputRefRelation("Manager")
                .applyButtonClick()
                .and()
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
        table.assertTableContainsLinkTextPartially("testUserWithRoleMembershipSearchByRelation");
    }

    @Test
    public void test0090referenceAttributeByNameSearch() {
        logoutLoginToRefreshSearch();
        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> search = (Search<UsersPageTable>) table.search();
        search.referencePanelByItemName(ROLE_MEMBERSHIP_ATTRIBUTE)
                .propertySettings()
                .inputRefName("roleMembershipByNameSearch")
                .applyButtonClick()
                .and()
                .updateSearch();
        table
                .assertVisibleObjectsCountEquals(1)
                .assertTableContainsLinkTextPartially("testUserWithRoleMembershipSearchByName");
        search.referencePanelByItemName(ROLE_MEMBERSHIP_ATTRIBUTE)
                .assertRefSearchFieldValueMatch(REF_SEARCH_FIELD_VALUE);
    }

    @Test
    public void test0100configuredAttributesSearch() {
        addObjectFromFile(SYSTEM_CONFIG_WITH_CONFIGURED_USER_SEARCH);
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage(getUsername(), getPassword());

        UsersPageTable table = basicPage.listUsers().table();
        Search<UsersPageTable> search = (Search<UsersPageTable>) table.search();
        search.textInputPanelByItemName("By employee number", false)
                .inputValue("544")
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
        table.assertTableContainsLinkTextPartially("searchByEmployeeNumberUser");

        search.clearTextSearchItemByNameAndUpdate("By employee number");
        search.textInputPanelByItemName("By email", false)
                .inputValue("testEmailAddress@test.com")
                .updateSearch();
        table.assertVisibleObjectsCountEquals(1);
        table.assertTableContainsLinkTextPartially("searchByEmailAddressUser");
    }

    @Test
    public void test0110dateIntervalSearch() {
        SimpleDateFormat formater = new SimpleDateFormat("MMMM dd, yyyy");
        basicPage.auditLogViewer()
                .table()
                    .search()
                        .dateIntervalPanelByItemName("Time")
                            .getFromDateTimeFieldPanel()
                                .setDateTimeValue(formater.format(new Date()));
    }

    @Test
    public void test012OrgMemberPanelConfiguration() {
        basicPage.orgStructure()
                .selectTabWithRootOrg("orgRootMemberSearch")
                    .getMemberPanel()
                        .table()
                            .assertTableDoesntContainText("orgMembershipByTypeSearch");

        addObjectFromFile(SEARCH_CONFIG_SYSTEM_CONFIG_FILE);
        basicPage.loggedUser().logout();
        basicPage = midPoint.formLogin().login(getUsername(), getPassword());

        MemberTable<MemberPanel<OrgRootTab>> table = basicPage.orgStructure()
                .selectTabWithRootOrg("orgRootMemberSearch")
                    .getMemberPanel()
                        .table();
        table.assertTableContainsColumnWithValue("Name", "orgMembershipByTypeSearch");
        table.search().assertExistSearchItem("Type2").assertHelpTextOfSearchItem("Type2", "Type help")
                .assertActualOptionOfSelectSearchItem("Type2", "Organization");
        table.search().assertExistSearchItem("Relation2").assertHelpTextOfSearchItem("Relation2", "Help relation")
                .assertActualOptionOfSelectSearchItem("Relation2", SchemaConstants.ORG_DEFAULT.getLocalPart());
        table.search().assertExistSearchItem("Scope2").assertHelpTextOfSearchItem("Scope2", "Help scope")
                .assertActualOptionOfSelectSearchItem("Scope2", "Subtree");
        table.search().dropDownPanelByItemName("Scope2").inputDropDownValue("One level");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        table.search().updateSearch();
        table.search().assertExistSearchItem("Indirect2").assertHelpTextOfSearchItem("Indirect2", "Indirect help")
                .assertActualOptionOfSelectSearchItem("Indirect2", "True");
    }

    @Test
    public void test013OrgMemberPanelConfigurationWithoutDefaultSearchItems() {
        addObjectFromFile(SEARCH_CONFIG_WITHOUT_DEFAULT_ITEM_SYSTEM_CONFIG_FILE);
        basicPage.loggedUser().logout();
        basicPage = midPoint.formLogin().login(getUsername(), getPassword());

        MemberTable<MemberPanel<OrgRootTab>> table = basicPage.orgStructure()
                .selectTabWithRootOrg("orgRootMemberSearch")
                .getMemberPanel()
                .table();
        table.assertTableDoesntContainText("orgMembershipByTypeSearch");
        table.search().assertDoesntExistSearchItem("Type2");
        table.search().assertDoesntExistSearchItem("Relation2");
        table.search().assertDoesntExistSearchItem("Scope2");
        table.search().assertDoesntExistSearchItem("Indirect2");
    }

    /**
     * covers MID-9324 (Request access: Cannot change search mode)
     */
    @Test
    public void test014selectAdvancedSearchOnRoleCatalogPage() {
        reimportDefaultSystemConfigurationAndRelogin();
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectAllOrganizationsMenu()
                .search()
                .advanced()
                .assertAdvancedSearchIsSelected();

    }

    /**
     * covers MID-9324 (Request access: Cannot change search mode)
     */
    @Test
    public void test015selectAdvancedSearchOnRoleCatalogTableView() {
        reimportDefaultSystemConfigurationAndRelogin();
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectTableView()
                .selectAllOrganizationsMenu()
                .search()
                .advanced()
                .assertAdvancedSearchIsSelected();

    }

    private void logoutLoginToRefreshSearch() {
        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword());
    }
}
