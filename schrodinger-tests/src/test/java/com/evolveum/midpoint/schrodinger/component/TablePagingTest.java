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

public class TablePagingTest extends AbstractSchrodingerTest {

    private static final File SYS_CONFIG_DEFAULT_SETTINGS = new File("./src/test/resources/features/paging/systemConfiguration/sys-config-default-paging-settings.xml");

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

}
