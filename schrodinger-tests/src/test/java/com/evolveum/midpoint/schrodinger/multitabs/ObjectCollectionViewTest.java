/*
 * Copyright (c) 2026 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.multitabs;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test class covering multi-tab behavior of object collection views.
 * <p>
 * These tests verify that object collection pages opened from the main left sidebar menu
 * behave correctly when used across multiple browser tabs. In particular, they focus on
 * preservation and isolation of UI state stored in browser session storage.
 *
 * <p>
 * The following aspects are validated:
 * <ul>
 *     <li>Opening multiple collection views in separate tabs.</li>
 *     <li>Independent manipulation of collection state (e.g. paging, search filters).</li>
 *     <li>Correct persistence of state within each tab using session storage.</li>
 *     <li>Consistency of data after performing actions (e.g. updates, navigation).</li>
 * </ul>
 */
public class ObjectCollectionViewTest extends AbstractSchrodingerTest {

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
    public void test00100allUsersAndPersonViewsPaging() {
        ListUsersPage allUsersView = tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .paging()
                .assertCurrentPageSize(50)
                .pageSize(105)
                .and()
                .and();

        ListUsersPage personsView = tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers("Persons")
                .table()
                .paging()
                .assertCurrentPageSize(50)
                .pageSize(25)
                .and()
                .and();

        //reopen users list page, check the page size is restored from session storage
        tab(FIRST_TAB_ID)
                .activate()
                .lastActive(allUsersView)
                .table()
                .paging()
                .assertCurrentPageSize(105)
                .and()
                .and()
                .listUsers()
                .table()
                .paging()
                .assertCurrentPageSize(105);

        //reopen Persons view page, check the page size is restored from session storage
        tab(SECOND_TAB_ID)
                .activate()
                .lastActive(personsView)
                .table()
                .paging()
                .assertCurrentPageSize(25)
                .and()
                .and()
                .listUsers("Persons")
                .table()
                .paging()
                .assertCurrentPageSize(25);
    }

    /**
     * Verifies that search state is stored in session storage independently per browser tab.
     * <p>
     * Test goals:
     * 1. Each tab maintains its own search criteria (isolation between tabs).
     * 2. Search values persist after navigation to a different page.
     * 3. Search values are restored from session storage after returning back.
     * 4. Search state is not overridden by actions in another tab.
     * <p>
     * Scenario:
     * - Tab 1: search by Nickname = "Ja"
     * - Tab 2: search by Nickname = "JJ"
     * - Verify both tabs keep their own state
     * - Navigate to another page ("Persons") and perform a different search
     * - Return back and verify original search is restored
     */
    @Test
    public void test00200allUsersViewSearch() {
        // Set search in first tab (Nickname = "Ja")
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .textInputPanelByItemName("Nickname")
                .inputValue("Ja")
                .updateSearch()
                .and()
                .assertAllObjectsCountEquals(2);

        // Set different search in second tab (Nickname = "JJ")
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .textInputPanelByItemName("Nickname")
                .inputValue("JJ")
                .updateSearch()
                .and()
                .assertAllObjectsCountEquals(1);

        // Verify first tab still has its own search preserved
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .assertSearchItemExists("Nickname")
                .assertTextSearchItemValue("Nickname", "Ja")
                .and()
                .assertAllObjectsCountEquals(2);

        // Verify second tab still has its own search preserved
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .assertSearchItemExists("Nickname")
                .assertTextSearchItemValue("Nickname", "JJ")
                .and()
                .assertAllObjectsCountEquals(1);

        // In first tab, navigate to another page and perform a different search
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers("Persons")
                .table()
                .search()
                .byName()
                .inputValue("admin")
                .updateSearch()
                .and()
                .and()
                .listUsers()
                .table()
                .search()
                .assertSearchItemExists("Nickname")
                .assertTextSearchItemValue("Nickname", "Ja")
                .and()
                .assertAllObjectsCountEquals(2);

        // Repeat navigation scenario in second tab
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers("Persons")
                .table()
                .search()
                .assertTextSearchItemValue("Name", "")
                .byName()
                .inputValue("jack")
                .updateSearch()
                .and()
                .and()
                .listUsers()
                .table()
                .search()
                .assertSearchItemExists("Nickname")
                .assertTextSearchItemValue("Nickname", "JJ")
                .and()
                .assertAllObjectsCountEquals(1);
    }

    /**
     * Verifies that fulltext search filter is stored in session storage independently per browser tab.
     * <p>
     * Test goals:
     * 1. Each tab maintains its own fulltext search filter.
     * 2. Search values persist after page reloading.
     * 3. Search filters are restored from session storage after page reloading.
     * 4. Search state is not overridden by actions in another tab.
     * <p>
     * Scenario:
     * - Tab 1: search on roles list page by fulltext search value = "approval decisions"
     * - Tab 2: search on roles list page by fulltext search value = "delegate"
     * - Reload the page on each tab and verify both tabs keep their own state
     */
    @Test
    public void test00300fullTextSearchOnRolesListPage() {
        importObject(SYSTEM_CONFIGURATION_FULLTEXT_FILE);
        basicPage.aboutPage().reindexRepositoryObjects();
        reloginAsAdministrator();

        // Set search in first tab (Fulltext search = "approval decisions")
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .assertFulltextSearchIsDisplayed()
                .fullText("approval decisions") //from Approver role description
                .updateSearch()
                .and()
                .assertAllObjectsCountEquals(1)
                .assertTableContainsText("Approver");

        // Set search in second tab (Fulltext search = "delegate")
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .assertFulltextSearchIsDisplayed()
                .assertFulltextValueEquals("")
                .fullText("delegate ") //from Delegator role description
                .updateSearch()
                .and()
                .assertAllObjectsCountEquals(1)
                .assertTableContainsText("Delegator");

        // Verify first tab still has its own search preserved
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .assertFulltextSearchIsDisplayed()
                .assertFulltextValueEquals("approval decisions")
                .and()
                .assertAllObjectsCountEquals(1);

        // Verify second tab still has its own search preserved
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .assertFulltextSearchIsDisplayed()
                .assertFulltextValueEquals("delegate")
                .and()
                .assertAllObjectsCountEquals(1);
    }
}
