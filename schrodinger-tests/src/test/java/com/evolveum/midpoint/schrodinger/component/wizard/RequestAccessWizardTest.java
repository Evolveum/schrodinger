/*
 * Copyright (c) 2023. Evolveum
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.schema.constants.SchemaConstants;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.PersonOfInterestStepPanel;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.RequestAccessPage;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.RoleCatalogItemsTable;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.RoleCatalogStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class RequestAccessWizardTest extends AbstractSchrodingerTest {

    private static final File USERS = new File("./src/test/resources/objects/users/request-access-wizard-users.xml");
    private static final File ROLES = new File("./src/test/resources/objects/roles/request-access-wizard-roles.xml");
    private static final File ROLES_WITH_CONFLICT_AND_PRUNE_ACTION = new File("./src/test/resources/objects/roles/conflicting-roles-and-prune-action.xml");
    private static final File USER_SOD_PRUNE_TEST = new File("./src/test/resources/objects/users/user-sod-prune-test.xml");
    private static final File COLLECTION_ROLES = new File("./src/test/resources/objects/objectcollections/all-roles-custom.xml");
    private static final File COLLECTION_SERVICES = new File("./src/test/resources/objects/objectcollections/all-services-custom.xml");
    private static final File SYSTEM_CONFIGURATION_ROLE_CATALOG = new File("./src/test/resources/objects/systemconfiguration/system-configuration-role-catalog-customized.xml");
    private static final File SYSTEM_CONFIGURATION_ROLE_CATALOG_WITH_FULLTEXT_SEARCH = new File("./src/test/resources/objects/systemconfiguration/system-configuration-role-catalog-with-fulltext-search.xml");
    private static final File ORG_MONKEY_ISLAND_SOURCE_FILE = new File("./src/test/resources/objects/orgs/org-monkey-island-simple.xml");
    private static final File REQUEST_ACCESS_ENDUSER = new File("./src/test/resources/objects/users/request-access-enduser.xml");
    private static final File REQUEST_ACCESS_TEAMMATE_USER = new File("./src/test/resources/objects/users/request-access-teammate.xml");
    private static final File REQUEST_ACCESS_AUTH_ROLE = new File("./src/test/resources/objects/roles/enduser-role-with-base-request-access-auth.xml");
    private static final File REQUEST_ACCESS_REQUESTABLE_ROLE = new File("./src/test/resources/objects/roles/request-access-requestable-role.xml");
    private static final File REQUEST_ACCESS_DIRECT_CATALOG_OBJECTS = new File("./src/test/resources/objects/misc/13482-ticket-objects.xml");
    private static final File ROLE_CATALOG_SERVICES_OBJECTS = new File("./src/test/resources/objects/misc/13480-ticket-objects.xml");

    private static final String REQUEST_ACCESS_CATALOG_STEP_PATH = "/self/requestAccess?step=catalog";

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(USERS, ROLES, ROLES_WITH_CONFLICT_AND_PRUNE_ACTION, USER_SOD_PRUNE_TEST, REQUEST_ACCESS_AUTH_ROLE,
                REQUEST_ACCESS_REQUESTABLE_ROLE, REQUEST_ACCESS_ENDUSER, REQUEST_ACCESS_TEAMMATE_USER,
                REQUEST_ACCESS_DIRECT_CATALOG_OBJECTS, ROLE_CATALOG_SERVICES_OBJECTS);
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

    @Test
    public void test0010requestEndUserRoleForLoggedInUser() {
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectAllRolesMenu()
                .addItemToCart("End user")
                .next()
                .clickSubmitButton()
                .feedback()
                .assertSuccess();
        showUser("administrator")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Role", SchemaConstants.ORG_DEFAULT.getLocalPart(), "End user");
    }

   @Test
    public void test0020requestEndUserRoleForUserGroup() {
        basicPage
                .requestAccess()
                .selectGroup("ra_wizard_user_1", "ra_wizard_user_2")
                .selectDefaultRelation()
                .selectAllRolesMenu()
                .addItemToCart("End user")
                .next()
                .clickSubmitButton()
                .feedback()
                .assertInfo();
        showUser("ra_wizard_user_1")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Role", SchemaConstants.ORG_DEFAULT.getLocalPart(), "End user");
        showUser("ra_wizard_user_2")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Role", SchemaConstants.ORG_DEFAULT.getLocalPart(), "End user");
    }

   @Test
   public void test003requestRolesOfTeammate() {
       basicPage
               .requestAccess()
               .selectGroup("ra_wizard_user_1")
               .selectManagerRelation()
               .selectRolesOfTeammateMenu("ra_wizard_user_with_assignments")
               .addItemToCart("ra_role_to_assign")
               .next()
               .clickSubmitButton()
               .feedback()
               .assertSuccess();
        showUser("ra_wizard_user_1")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Role", "Manager", "ra_role_to_assign");
    }

    /**
     * Covers MID-9323
     */
    @Test
   public void test004selectAllItemsOnTableViewPanel() {
       basicPage
               .requestAccess()
               .selectMyself()
               .selectDefaultRelation()
               .selectTableView()
               .assertShoppingCartIsEmpty()
               .table()
               .addAll()
               .assertShoppingCartCountEqualsTableItemsCount();
    }

    @Test
    void test0050checkNameColumn() {
        RoleCatalogItemsTable<RoleCatalogStepPanel> table =  basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectTableView()
                .table();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
        table.assertTableCellClass("Name", 2, "name-min-width");
    }

    @Test
    void test0060checkCustomColumnSettings() {
        importObject(COLLECTION_ROLES);
        importObject(COLLECTION_SERVICES);
        importObject(SYSTEM_CONFIGURATION_ROLE_CATALOG, true);
        reloginAsAdministrator();

        RoleCatalogStepPanel panel =  basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectTableView();

        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());

        RoleCatalogItemsTable<RoleCatalogStepPanel> table1 = panel.table();
        table1.assertTableCellStyle("Name ID", 2, "400px");
        table1.assertTableCellStyle("Description ID", 2, "400px");

        RoleCatalogItemsTable<RoleCatalogStepPanel> table2 = panel.selectAllServicesMenu().table();
        table2.assertTableCellStyle("Name General", 1, "345px");
        table2.assertTableCellStyle("Description General", 2, "543px");
    }

    //covers #10819
    @Test
    void test0080fullTextSearchTest() {
        importObject(ORG_MONKEY_ISLAND_SOURCE_FILE, true);
        importObject(COLLECTION_ROLES, true);
        importObject(SYSTEM_CONFIGURATION_ROLE_CATALOG_WITH_FULLTEXT_SEARCH, true);

        basicPage.aboutPage().reindexRepositoryObjects();
        Selenide.sleep(30000);

        reloginAsAdministrator();

        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .assertTableViewIsSelected()
                .search()
                .assertFulltextSearchIsDisplayed()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue("Abstract role")
                .updateSearch()
                .and()
                .selectMenuByLabel("Ministry of Offense")
                .search()
                .fullText("Swashbuckler Section")
                .updateSearch()
                .and()
                .table()
                .assertTableContainsColumnWithValue("Name", "Swashbuckler Section")
                .assertTableDoesntContainColumnWithValue("Name", "Scumm Bar")
                .and()
                .selectMenuByLabel("Swashbuckler Section")
                .table()
                .assertVisibleObjectsCountEquals(0)
                .and()
                .search()
                .fullText("Ministry of Health")
                .updateSearch()
                .and()
                .table()
                .assertTableContainsColumnWithValue("Name", "Ministry of Health")
                .assertTableDoesntContainColumnWithValue("Name", "Ministry of Illness")
                .and()
                .selectMenuByLabel("All roles custom")
                .search()
                .assertFulltextSearchIsDisplayed()
                .fullText("End user")
                .updateSearch()
                .and()
                .table()
                .assertTableContainsColumnWithValue("Name ID", "End user")
                .and()
                .search()
                .fullText("Reviewer")
                .updateSearch()
                .and()
                .table()
                .assertTableContainsColumnWithValue("Name ID", "Reviewer");
    }

    //covers #10861
    @Test
    void test0095requestAssignmentWithExtensionAttribute() {
        reloginAsAdministrator();
        importObject(SYSTEM_CONFIGURATION_INITIAL_FILE, true);
        reloginAsAdministrator();
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .assertTilesViewIsSelected()
                .search()
                .byName()
                .inputValue("Approver")
                .updateSearch()
                .and()
                .addItemToCart("Approver")
                .next()
                .getShoppingCartItemsTable()
                .editShoppingCartItem("Approver")
                .addAttributeValue("testExtension", "1")
                .clickSaveButton()
                .editShoppingCartItem("Approver")
                .assertPropertyInputValue("testExtension", "1")
                .addAttributeValue("testExtension", "12")
                .clickSaveButton()
                .editShoppingCartItem("Approver")
                .assertPropertyInputValue("testExtension", "12")
                .clickCloseButton()
                .and()
                .clickSubmitButton();
        basicPage
                .profile()
                .selectAssignmentsPanel()
                .table()
                .clickByName("Approver")
                .assertPropertyInputValue("testExtension", "12");
    }

    /**
     * Tests that conflict is not displayed in case conflicting roles are to be resolved by prune action.
     * covers #11250
     */
    @Test
    void test0100hiddenConflictsForPruneAction() {
        String testUserName = "sodPruneTestUser";
        String conflictingRoleName = "TEST SoD prune - Role B";
        basicPage
                .requestAccess()
                .selectGroup(testUserName)
                .next()
                .addItemToCart(conflictingRoleName)
                .next()
                .assertOpenConflictNotExist();
    }

    /**
     * Tests that the user with the basic request access rights sees the roles of teammates
     * in the Role catalog (the user doesn't have ArchetypeType read auth, neither OrgType read auth)
     * Covers 11221
     */
    @Test
    void test0110rolesOfTeammatesAuthorizationsTest() {
        final String enduserName = "enduser";
        final String teammateUserName = "teammate";
        final String requestableRoleName = "Requestable role";
        loginAsUser(enduserName, getPassword());
        basicPage
                .requestAccess()
                .selectMyself()
                .next()
                .selectRolesOfTeammateMenu(teammateUserName)
                .assertAccessesPanelContainsItems(requestableRoleName);
    }

    /**
     * Opening the role catalog step directly via URL (?step=catalog), without a person of interest selected,
     * must restart the wizard at the person of interest step. Otherwise, the catalog lists all readable roles,
     * not only the ones allowed by the #assign authorizations.
     * Covers #13482
     */
    @Test
    void test0120directAccessToRoleCatalogStepWithoutPersonOfInterest() {
        final String requestableRole1 = "requestable-role1";
        final String requestableRole2 = "requestable-role2";
        final String internalRole = "internal-role1";
        loginAsUser("limited1", "Test5ecr3t");

        Selenide.open(REQUEST_ACCESS_CATALOG_STEP_PATH);
        Utils.waitForAjaxCallFinish();

        RequestAccessPage requestAccessPage = new RequestAccessPage();
        requestAccessPage.assertPersonOfInterestStepActive();

        new PersonOfInterestStepPanel(requestAccessPage)
                .selectMyself();
        requestAccessPage.assertSelectedStepByLabel("Role catalog");
        Utils.waitForAjaxCallFinish();

        RoleCatalogStepPanel roleCatalog = new RoleCatalogStepPanel(requestAccessPage)
                .selectTableView();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());

        roleCatalog
                .table()
                .assertTableContainsColumnWithValue("Name", requestableRole1)
                .assertTableContainsColumnWithValue("Name", requestableRole2)
                .assertTableDoesntContainColumnWithValue("Name", internalRole);
    }

    /**
     * Covers #13480
     */
    @Test
    void test0130rolesListNotAffectedByServicesInRoleCatalog() {
        loginAsUser("viewer1", "Test5ecr3t");

        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .assertTilesViewIsSelected()
                .selectAllServicesMenu()
                .assertAccessesPanelContainsItems("sample-service1");

        basicPage
                .listRoles()
                .table()
                .search()
                .byName()
                .inputValue("sample-")
                .updateSearch()
                .and()
                .assertTableContainsColumnWithValue("Name", "sample-role1")
                .assertTableContainsColumnWithValue("Name", "sample-role2")
                .assertTableDoesntContainColumnWithValue("Name", "sample-service1")
                .assertTableDoesntContainColumnWithValue("Name", "sample-service2");
    }
}
