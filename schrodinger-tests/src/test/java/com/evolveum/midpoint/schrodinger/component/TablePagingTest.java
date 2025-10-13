package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.role.ListRolesPage;
import com.evolveum.midpoint.schrodinger.page.role.RolesPageTable;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

public class TablePagingTest extends AbstractSchrodingerTest {

    private static final File SYS_CONFIG_VIEWS_SETTINGS = new File("./src/test/resources/features/paging/systemConfiguration/sys-config-views-paging-settings.xml");
    private static final File SYS_CONFIG_DEFAULT_SETTINGS = new File("./src/test/resources/features/paging/systemConfiguration/sys-config-default-paging-settings.xml");
    private static final File ARCHETYPE_PERSON = new File("./src/test/resources/features/paging/archetype/archetype-person-object-details-paging-config.xml");
    private static final File USER_ENDUSER_1 = new File("./src/test/resources/features/paging/user/enduser1-user.xml");
    private static final File USER_PERSON_1 = new File("./src/test/resources/features/paging/user/person1-user.xml");
    private static final File ROLE_END_USER_DEFAULT_PAGING_SETTINGS = new File("./src/test/resources/features/paging/role/role-enduser-default-paging-settings.xml");
    private static final File ROLE_END_USER_PAGING_OPTIONS = new File("./src/test/resources/features/paging/role/role-enduser-paging-options.xml");
    private static final File ROLE_END_USER_MAX_PAGE_SIZE = new File("./src/test/resources/features/paging/role/role-enduser-max-page-size.xml");
    private static final File ROLE_END_USER_MAX_SIZE_2000 = new File("./src/test/resources/features/paging/role/role-enduser-max-size-2000.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(USER_ENDUSER_1, USER_PERSON_1);
    }

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        importObject(SYS_CONFIG_VIEWS_SETTINGS, true);
    }

    /**
     * Check if the object list page size options contains those from configuration.
     * Default page size is set to 50
     * After page size is changed on the users list, check that it was stored in session
     */
    @Test
    public void test0010objectListTableDefaultSettings() {
        reloginAsAdministrator();

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
                        .assertPageSizeValuesListContains(15, 25, 50, 55, 105, 115);

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
                            .assertPageSizeValuesListContains(110, 120, 150);

        showRole("End user")
                .selectTabMembers()
                    .membersPanel()
                        .table()
                            .paging()
                                .assertCurrentPageSize(21)
                                .assertPageSizeValuesListContains(110, 120, 150);
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
                            .assertPageSizeValuesListContains(1, 2, 5, 11, 21, 110, 120, 150);
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
                        .assertPageSizeValuesListContains(15, 25, 55, 105, 115, 11, 22, 100);
    }

    /**
     * Paging options are merged for the Persons archetype.
     * Check if the page size options contains those from configuration for Persons details page, Assignments -> All panel.
     */
    @Test
    public void test0050personsArchetypeSettingMergedForAssignmentsPanel() {
        addObjectFromFile(ARCHETYPE_PERSON);

        reloginAsAdministrator();

        showUser("person1")
                .selectAssignmentsPanel()
                    .selectTypeAll()
                        .table()
                            .paging()
                                .assertCurrentPageSize(30)
                                .assertPageSizeValuesListContains(10, 30, 60, 80, 110, 120, 150);
    }

    /**
     * Paging options are merged for End user role, max page size is taken from Persons view configuration
     * in system configuration.
     */
    @Test
    public void test0060enduserRoleSettingMergedForPersonsList() {
        addObjectFromFile(ROLE_END_USER_PAGING_OPTIONS);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("enduser1", "Password123!");

        basicPage.listUsers("Persons")
                .table()
                    .paging()
                        .assertCurrentPageSize(22)
                        .assertPageSizeValuesListContains(15, 18, 25, 28, 38, 48, 55, 105, 115, 11, 22, 100);
    }

    /**
     * Paging max size is overridden in the End user role, pagingOptions are taken from Persons view configuration
     * in system configuration.
     */
    @Test
    public void test0070personsArchetypeSettingOverridingForPersonsList() {
        addObjectFromFile(ROLE_END_USER_MAX_PAGE_SIZE);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("enduser1", "Password123!");

        basicPage.listUsers("Persons")
                .table()
                .paging()
                .assertCurrentPageSize(17)
                .assertPageSizeValuesListContains(15, 25, 55, 105, 115, 11, 22, 100);
    }

    /**
     * In this test we check if the default paging settings are merged correctly for the End user role.
     */
    @Test
    public void test0080defaultSettingsMergingTest() {
        addObjectFromFile(SYS_CONFIG_DEFAULT_SETTINGS);
        addObjectFromFile(ROLE_END_USER_DEFAULT_PAGING_SETTINGS);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("enduser1", "Password123!");


        basicPage.listUsers()
                .table()
                    .paging()
                        .assertCurrentPageSize(51)
                        .assertPageSizeValuesListContains(16, 26, 56, 106)      //from end user role
                        .assertPageSizeValuesListContains(15, 25, 55, 105, 115);    //from system configuration


        showUser("enduser1")
                .selectAssignmentsPanel()
                .selectTypeAll()
                .table()
                .paging()
                .assertCurrentPageSize(23)
                .assertPageSizeValuesListContains(113, 123, 153)
                .assertPageSizeValuesListContains(110, 120, 150)
                .and()
                .and()
                .and()
                .selectApplicationsPanel()
                .table()
                .paging()
                .assertCurrentPageSize(23)
                .assertPageSizeValuesListContains(113, 123, 153)
                .assertPageSizeValuesListContains(110, 120, 150);
    }

    /**
     * The biggest page size should be restricted by the default max value (1000).
     */
    @Test
    public void test0090defaultSettingsMergingTest() {
        addObjectFromFile(SYS_CONFIG_DEFAULT_SETTINGS);
        addObjectFromFile(ROLE_END_USER_MAX_SIZE_2000);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("enduser1", "Password123!");


        basicPage.listUsers()
                .table()
                    .paging()
                        .assertCurrentPageSize(1000)
                        .assertPageSizeValuesListContains(500, 1000)
                        .assertPageSizeValuesListDoesntContain(1500, 2000);


        showUser("enduser1")
                .selectAssignmentsPanel()
                .selectTypeAll()
                .table()
                .paging()
                .assertCurrentPageSize(1000)
                .assertPageSizeValuesListContains(500, 1000)
                .assertPageSizeValuesListDoesntContain(1500, 2000);
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

}
