package com.evolveum.midpoint.schrodinger.multitabs;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test class covering behavior of object collection views located on object detail pages.
 * <p>
 * These tests verify that collection views embedded within detail pages (e.g. user Assignments panel,
 * role Inducements panel, etc.) behave correctly, especially when interacting across multiple browser tabs.
 *
 * <p>
 * The primary focus is on validation of UI state persistence stored in session storage,
 * such as:
 * <ul>
 *     <li>Paging (current page, page size)</li>
 *     <li>Search and filter parameters</li>
 * </ul>
 */
public class ObjectDetailsCollectionViewTest extends AbstractSchrodingerTest {

    private static final File SYS_CONFIG_DEFAULT_SETTINGS = new File("./src/test/resources/features/paging/systemConfiguration/sys-config-default-paging-settings.xml");
    private static final File MULTIPLE_USERS = new File("src/test/resources/objects/users/jack-users.xml");

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        importObject(SYS_CONFIG_DEFAULT_SETTINGS);
        reloginAsAdministrator();
    }

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(MULTIPLE_USERS);
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

    @Test
    public void test00100assignmentsPanelPagingForDifferentUsers() {
        showUser(FIRST_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(21)
                .pageSize(110)
                .and()
                .and()
                .and()
                .selectBasicPanel()
                .and()
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(110);


        showUser(SECOND_TAB_ID, "jack2")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(21)
                .pageSize(120)
                .and()
                .and()
                .and()
                .selectBasicPanel()
                .and()
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(120);

        showUser(FIRST_TAB_ID, "jack2")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(110); //should be restored from the session storage of first tab

        showUser(SECOND_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .paging()
                .assertCurrentPageSize(120); //should be restored from the session storage of second tab
    }

    /**
     * Verifies correct behavior of session storage for the Assignments panel search filter
     * across multiple browser tabs.
     *
     * <p>The test ensures that each browser tab maintains its own independent session storage
     * and that search filters are properly stored and restored within the same tab context.</p>
     *
     * <p>Test scenario:</p>
     * <ul>
     *     <li>In the first tab, a search filter ("Name" = "end") is applied on the Assignments panel.</li>
     *     <li>In the second tab, a different search filter ("Name" = "user") is applied.</li>
     *     <li>The test verifies that:
     *         <ul>
     *             <li>The filter in the first tab is preserved and correctly restored when navigating
     *             between different user detail pages.</li>
     *             <li>The filter in the second tab is also preserved independently and restored correctly.</li>
     *             <li>Filters from one tab do not affect the other tab.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * <p>Expected result:</p>
     * <ul>
     *     <li>Each tab restores its own search filter from session storage.</li>
     *     <li>The applied filter is consistently reflected on the Assignments panel
     *     across different user detail pages within the same tab.</li>
     *     <li>No leakage of session storage data occurs between tabs.</li>
     * </ul>
     */
    @Test
    public void test00200assignmentsPanelSearchFromSessionStorage() {
        showUser(FIRST_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .table()
                .search()
                .textInputPanelByItemName("Name")
                .inputValue("end")
                .updateSearch();

        showUser(SECOND_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .table()
                .search()
                .textInputPanelByItemName("Name")
                .and()
                .assertTextSearchItemValue("Name", "")
                .byName()
                .inputValue("user")
                .updateSearch();

        showUser(FIRST_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .table()
                .search()
                .assertSearchItemExists("Name")
                .assertTextSearchItemValue("Name", "end");
        showUser(FIRST_TAB_ID, "jack2")
                .selectAssignmentsPanel()
                .table()
                .search()
                .assertSearchItemExists("Name")
                .assertTextSearchItemValue("Name", "end");

        showUser(SECOND_TAB_ID, "jack1")
                .selectAssignmentsPanel()
                .table()
                .search()
                .assertSearchItemExists("Name")
                .assertTextSearchItemValue("Name", "user");
        showUser(SECOND_TAB_ID, "jack3")
                .selectAssignmentsPanel()
                .table()
                .search()
                .assertSearchItemExists("Name")
                .assertTextSearchItemValue("Name", "user");
    }
}
