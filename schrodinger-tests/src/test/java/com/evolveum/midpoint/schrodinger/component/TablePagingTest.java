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

    @Test
    public void test0010objectListTablePaging() {
        ListUsersPage users = basicPage.listUsers();

        Paging<Table<ListUsersPage, UsersPageTable>> usersPaging = users
                .table()
                .paging();

        usersPaging.assertCurrentPage(50)
                        .pageSize(105);

        Paging<Table<ListRolesPage, RolesPageTable>> rolesPaging = basicPage.listRoles()
                .table()
                .paging();

        rolesPaging.assertCurrentPage(50)
                        .assertPageSizeValuesListContain(15, 25, 50, 55, 105, 115);

        basicPage
                .listUsers()
                        .table()
                                .paging()
                                        .assertCurrentPage(105);
    }

}
