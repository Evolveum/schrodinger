package com.evolveum.midpoint.schrodinger.scenarios;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

/**
 * This class is intended to test Role details page and the scenarios connected with this page
 */
public class RoleTest extends AbstractSchrodingerTest {

    /**
     * Covers 11584
     */
    @Test
    public void test00100redirectionToBasicPanelAfterMemberUnassignAction() {
        var rolePage = basicPage
                .listRoles()
                .table()
                .search()
                .byName()
                .inputValue("End user")
                .updateSearch()
                .and()
                .clickByName("End user");
        rolePage
                .selectMembersPanel()
                .membersPanel()
                .table()
                .unassign()
                .clickCancel();
        rolePage
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Name", "End user")
                .and()
                .and()
                .assertErrorDoesntExists();
    }
}
