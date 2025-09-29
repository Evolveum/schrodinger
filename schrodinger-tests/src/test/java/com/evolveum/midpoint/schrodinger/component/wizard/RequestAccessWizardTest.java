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
import com.evolveum.midpoint.schema.constants.SchemaConstants;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.RoleCatalogItemsTable;
import com.evolveum.midpoint.schrodinger.page.self.accessrequest.RoleCatalogStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class RequestAccessWizardTest extends AbstractSchrodingerTest {

    private static final File USERS = new File("./src/test/resources/objects/users/request-access-wizard-users.xml");
    private static final File ROLES = new File("./src/test/resources/objects/roles/request-access-wizard-roles.xml");
    private static final File SYSTEM_CONFIGURATION_MANDATORY_VALIDITY = new File("./src/test/resources/objects/systemconfiguration/sys-config-request-access-mandatory-validity.xml");
    private static final File COLLECTION_ORGANIZATIONS = new File("./src/test/resources/objects/objectcollections/all-organizations-custom.xml");
    private static final File COLLECTION_ROLES = new File("./src/test/resources/objects/objectcollections/all-roles-custom.xml");
    private static final File COLLECTION_SERVICES = new File("./src/test/resources/objects/objectcollections/all-services-custom.xml");
    private static final File SYSTEM_CONFIGURATION_ROLE_CATALOG = new File("./src/test/resources/objects/systemconfiguration/system-configuration-role-catalog-customized.xml");
    private static final File SYSTEM_CONFIGURATION_ROLE_CATALOG_WITH_FULLTEXT_SEARCH = new File("./src/test/resources/objects/systemconfiguration/system-configuration-role-catalog-with-fulltext-search.xml");
    private static final File SYSTEM_CONFIGURATION_ROLE_CATALOG_ONLY_DEFAULT_RELATION_ALLOWED = new File("./src/test/resources/objects/systemconfiguration/system-configuration-role-catalog-only-default-relation-allowed.xml");
    private static final File OWNER_OF_ENDUSER_ROLE = new File("./src/test/resources/objects/users/owner-of-enduser-role.xml");
    private static final File ORG_MONKEY_ISLAND_SOURCE_FILE = new File("./src/test/resources/objects/orgs/org-monkey-island-simple.xml");

    private static final String OWNER_OF_ENDUSER_ROLE_NAME = "owner_of_enduser_role";
    @Override
    protected List<File> getObjectListToImport(){
        return List.of(USERS, ROLES);
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
        String relationTranslated = Utils.translate("RelationTypes." + SchemaConstants.ORG_DEFAULT.getLocalPart());
        showUser("administrator")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Role", relationTranslated, "End user");
    }

    //todo commented due to ticket #9683
//   @Test
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

    //todo commented due to ticket #9683
//   @Test
   public void test0030requestRolesOfTeammate() {
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

    //todo commented due to ticket #9683
    /**
     * Covers MID-9323
     */
//    @Test
   public void test0040selectAllItemsOnTableViewPanel() {
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

    /**
     * Covers MID-10459
     */
    @Test
    public void test0050mandatoryValidityConfigurationTest() {
        importObject(SYSTEM_CONFIGURATION_MANDATORY_VALIDITY);
        reloginAsAdministrator();
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectAllRolesMenu()
                .addItemToCart("validity_test_role")
                .next()
                .assertValidityOptionPresent("10 Years")
                .selectValidityOption("10 Years")
                .comment("Test comment")
                .clickSubmitButton()
                .feedback()
                .assertSuccess();
        showUser("administrator")
                .selectAssignmentsPanel()
                .assertAssignmentExists("validity_test_role")
                .table()
                .clickByName("validity_test_role")
                .selectFormTabByName("Activation")
                .assertPropertyIsNotEmpty("validFrom")
                .assertPropertyIsNotEmpty("validTo");
    }

    @Test
    void test0060checkNameColumn() {
        RoleCatalogItemsTable table =  basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .selectTableView()
                .table();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
        table.assertTableCellClass("Name", 2, "name-min-width");
    }

    @Test
    void test0070checkCustomColumnSettings() {
        importObject(COLLECTION_ORGANIZATIONS);
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

        RoleCatalogItemsTable table1 = panel.table();
        table1.assertTableCellStyle("Name ID", 2, "400px");
        table1.assertTableCellStyle("Description ID", 2, "400px");

        RoleCatalogItemsTable table2 = panel.selectAllServicesMenu().table();
        table2.assertTableCellStyle("Name SRV", 1, "300px");
        table2.assertTableCellStyle("Description SRV", 2, "500px");

        RoleCatalogItemsTable table3 = panel.selectAllOrganizationsMenu().table();
        table3.assertTableCellStyle("Name General", 1, "345px");
        table3.assertTableCellStyle("Description General", 2, "543px");
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

    //covers #10851
    @Test
    void test0090requestSameAssignmentWithAnotherRelationTest() {
        importObject(OWNER_OF_ENDUSER_ROLE, true);
        importObject(SYSTEM_CONFIGURATION_ROLE_CATALOG_ONLY_DEFAULT_RELATION_ALLOWED, true);

        reloginAsAdministrator();

        basicPage
                .requestAccess()
                .selectGroup(OWNER_OF_ENDUSER_ROLE_NAME)
                .next(true)
                .assertTableViewIsSelected()
                .search()
                .basic()
                .byName()
                .inputValue("End user")
                .updateSearch()
                .and()
                .table()
                .selectRowByName("End user")
                .addSelectedToCart()
                .and()
                .assertShoppingCartCountEquals("1");
    }

    //covers #10861
    @Test
    void test0100requestAssignmentWithExtensionAttribute() {
        reloginAsAdministrator();
        importObject(SYSTEM_CONFIGURATION_INITIAL_FILE, true);
        reloginAsAdministrator();
        basicPage
                .requestAccess()
                .selectMyself()
                .selectDefaultRelation()
                .assertTilesViewIsSelected()
                .search()
                .basic()
                .byName()
                .inputValue("End user")
                .updateSearch()
                .and()
                .addItemToCart("End user")
                .next()
                .getShoppingCartItemsTable()
                .editShoppingCartItem("End user")
                .addAttributeValue("testExtension", "1")
                .clickSaveButton()
                .editShoppingCartItem("End user")
                .assertPropertyInputValue("testExtension", "1")
                .addAttributeValue("testExtension", "12")
                .clickSaveButton()
                .editShoppingCartItem("End user")
                .assertPropertyInputValue("testExtension", "12")
                .clickCloseButton()
                .and()
                .clickSubmitButton();
        basicPage
                .profile()
                .selectAssignmentsPanel()
                .table()
                .clickByName("End user")
                .assertPropertyInputValue("testExtension", "12");
    }

}
