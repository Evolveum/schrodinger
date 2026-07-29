package com.evolveum.midpoint.schrodinger.scenarios;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;

/**
 * Tests that the visibility of member operation buttons on the Members and Governance panels
 * of the role details page is driven by the proper GUI authorizations:
 * adminAssignMember/adminUnassignMember for the Members panel,
 * adminAssignGovernance/adminUnassignGovernance for the Governance panel.
 *
 * Covers issue #11850.
 */
public class GovernanceMembersAuthorizationsTest extends AbstractSchrodingerTest {

    private static final File ROLES_FILE =
            new File("./src/test/resources/objects/roles/roles-governance-members-authorizations.xml");
    private static final File USERS_FILE =
            new File("./src/test/resources/objects/users/users-governance-members-authorizations.xml");

    private static final String TARGET_ROLE_NAME = "governanceMembersTargetRole";
    private static final String GOVERNANCE_ADMIN_USER = "governanceAdminUser";
    private static final String MEMBER_ADMIN_USER = "memberAdminUser";
    private static final String USERS_PASSWORD = "Password123!";

    private static final String ASSIGN_BUTTON_ICON = "fe fe-assignment";

    @Override
    protected List<File> getObjectListToImport() {
        return Arrays.asList(ROLES_FILE, USERS_FILE);
    }

    /**
     * User with adminAssignGovernance/adminUnassignGovernance (but without
     * adminAssignMember/adminUnassignMember) should see the assign button
     * on the Governance panel and shouldn't see it on the Members panel.
     */
    @Test
    public void test0010governanceAuthorizationsControlGovernancePanelOnly() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().loginWithReloadLoginPage(GOVERNANCE_ADMIN_USER, USERS_PASSWORD);

        RolePage rolePage = openTargetRole();
        rolePage
                .selectMembersPanel()
                .membersPanel()
                .table()
                .assertToolbarButtonNotExist(ASSIGN_BUTTON_ICON);
        rolePage
                .selectGovernancePanel()
                .membersPanel()
                .table()
                .assertToolbarButtonExists(ASSIGN_BUTTON_ICON);
    }

    /**
     * User with adminAssignMember/adminUnassignMember (but without
     * adminAssignGovernance/adminUnassignGovernance) should see the assign button
     * on the Members panel and shouldn't see it on the Governance panel.
     */
    @Test
    public void test0020memberAuthorizationsControlMembersPanelOnly() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().loginWithReloadLoginPage(MEMBER_ADMIN_USER, USERS_PASSWORD);

        RolePage rolePage = openTargetRole();
        rolePage
                .selectMembersPanel()
                .membersPanel()
                .table()
                .assertToolbarButtonExists(ASSIGN_BUTTON_ICON);
        rolePage
                .selectGovernancePanel()
                .membersPanel()
                .table()
                .assertToolbarButtonNotExist(ASSIGN_BUTTON_ICON);
    }

    private RolePage openTargetRole() {
        return basicPage
                .listRoles()
                .table()
                .search()
                .byName()
                .inputValue(TARGET_ROLE_NAME)
                .updateSearch()
                .and()
                .clickByName(TARGET_ROLE_NAME);
    }
}
