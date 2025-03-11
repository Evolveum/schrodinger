package com.evolveum.midpoint.schrodinger.component;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.role.ListRolesPage;
import com.evolveum.midpoint.schrodinger.page.role.RolesPageTable;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

public class TablePagingTest extends AbstractSchrodingerTest {

    private static final File SYS_CONFIG_DEFAULT_SETTINGS = new File("./src/test/resources/features/paging/systemConfiguration/sys-config-default-paging-settings.xml");
    private static final File ARCHETYPE_PERSON = new File("./src/test/resources/features/paging/archetype/702-archetype-person.xml");
    private static final File USER_ENDUSER_1 = new File("./src/test/resources/features/paging/user/enduser1-user.xml");
    private static final File USER_PERSON_1 = new File("./src/test/resources/features/paging/user/person1-user.xml");
    private static final File ROLE_END_USER = new File("./src/test/resources/features/paging/role/040-role-enduser.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(SYS_CONFIG_DEFAULT_SETTINGS);
    }

    /**
     * Check if the object list page size options contains those from configuration.
     * Default page size is set to 50
     * After page size is changed on the users list, check that it was stored in session
     */
    @Test
    public void test0010objectListTableDefaultSettings() {
        ListUsersPage users = basicPage.listUsers();

        Paging<Table<ListUsersPage, UsersPageTable>> usersPaging = users
                .table()
                .paging();

        usersPaging.assertCurrentPageSize(50)
                        .pageSize(105);

        Paging<Table<ListRolesPage, RolesPageTable>> rolesPaging = basicPage.listRoles()
                .table()
                .paging();

        rolesPaging.assertCurrentPageSize(50)
                        .assertPageSizeValuesListContain(15, 25, 50, 55, 105, 115);

        basicPage
                .listUsers()
                        .table()
                                .paging()
                                        .assertCurrentPageSize(105);
    }

    /**
     * Check if the page size options contains those from configuration for the tables which are
     * situated on the object details page.
     * Default page size is set to 21
     */
    @Test
    public void test0020objectDetailsTableDefaultSettings() {
        showUser("administrator")
                .selectAssignmentsPanel()
                    .table()
                        .paging()
                            .assertCurrentPageSize(21)
                            .assertPageSizeValuesListContain(110, 120, 150);

        showRole("End user")
                .selectTabMembers()
                    .membersPanel()
                        .table()
                            .paging()
                                .assertCurrentPageSize(21)
                                .assertPageSizeValuesListContain(110, 120, 150);
    }

    /**
     * Check if the page size options contains those from configuration for Applications panel.
     * Default page size is set to 21
     */
    @Test
    public void test0030userDetailsApplicationsPanelSettings() {
        showUser("administrator")
                .selectApplicationsPanel()
                    .table()
                        .paging()
                            .assertCurrentPageSize(21)
                            .assertPageSizeValuesListContain(1, 2, 5, 11, 21);
    }

    /**
     * Check if the page size options contains those from configuration for Persons list page.
     * Default page size is set to 22
     */
    @Test
    public void test0040personsListPageSettings() {
        basicPage.listUsers("Persons")
                .table()
                    .paging()
                        .assertCurrentPageSize(22)
                        .assertPageSizeValuesListContain(11, 22, 100);
    }

    /**
     * Paging options are overridden in the Persons archetype.
     * Check if the page size options contains those from configuration for Persons details page, Assignments -> All panel.
     * The values are: 10, 30, 60, 80. Default page size is set to 30
     */
    @Test
    public void test0050personsArchetypeSettingOverridingForAssignmentsPanel() {
        addObjectFromFile(ARCHETYPE_PERSON);
        addObjectFromFile(USER_PERSON_1);

        showUser("person1")
                .selectAssignmentsPanel()
                    .selectTypeAll()
                        .table()
                            .paging()
                                .assertCurrentPageSize(30)
                                .assertPageSizeValuesListContain(10, 30, 60, 80);
    }

    /**
     * Paging options are overridden in the Persons archetype.
     * Check if the page size options contains those from configuration for Persons details page, Assignments -> All panel.
     * The values are: 10, 30, 60, 80. Default page size is set to 30
     */
    @Test
    public void test0060personsArchetypeSettingOverridingForPersonsList() {
        addObjectFromFile(ROLE_END_USER);
        addObjectFromFile(USER_ENDUSER_1);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("enduser1", "Password123!");

        basicPage.listUsers("Persons")
                .table()
                    .paging()
                        .assertCurrentPageSize(22)
                        .assertPageSizeValuesListContain(18, 28, 38, 48);
    }

}
