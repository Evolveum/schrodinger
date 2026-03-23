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
        addObjectFromFile(SYS_CONFIG_DEFAULT_SETTINGS);
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
}
