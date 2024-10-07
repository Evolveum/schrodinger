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

import com.evolveum.midpoint.schema.constants.SchemaConstants;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class RequestAccessWizardTest extends AbstractSchrodingerTest {

    private static final File USERS = new File("./src/test/resources/objects/users/request-access-wizard-users.xml");
    private static final File ROLES = new File("./src/test/resources/objects/roles/request-access-wizard-roles.xml");

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

    //todo commented due to ticket #9683
    /**
     * Covers MID-9323
     */
//    @Test
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
}
