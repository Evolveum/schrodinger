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
import com.evolveum.midpoint.xml.ns._public.common.common_3.RoleType;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * Test class covering navigation behavior within the multi-tab feature.
 * <p>
 * These tests verify that navigation elements behave correctly when interacting
 * with views opened in multiple browser tabs.
 *
 * <p>
 * The following aspects should be validated:
 * <ul>
 *     <li>Navigation between views within a tab.</li>
 *     <li>Correct behavior of breadcrumbs reflecting the current location.</li>
 *     <li>Navigation using breadcrumbs across different hierarchy levels.</li>
 *     <li>Functionality of the in-application back button.</li>
 *     <li>Consistency of navigation state when working with multiple tabs.</li>
 * </ul>
 */
public class NavigationTest extends AbstractSchrodingerTest {

    private static final File MULTIPLE_USERS = new File("src/test/resources/objects/users/jack-users.xml");

    private static final String SECOND_TAB_ID = "secondTab";

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(MULTIPLE_USERS);
    }

    /**
     * This test examines the scenario when the user gets to Role details page in different ways:
     * 1. Through user details page -> Assignments panel -> assignment details -> navigating to Role details page
     * 2. From role list page
     * The role details pages are opened on different browser tabs.
     * Navigation back (Back button) should work in both cases correctly
     */
    @Test
    public void test00100navigationToRoleDetailsPageAndBack() {
        var rolePage1 = tab(FIRST_TAB_ID)
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .byName()
                .inputValue("administrator")
                .updateSearch()
                .and()
                .clickByName("administrator")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .clickByName("Superuser")
                .navigateToTargetRefDetailsPage(RoleType.class);
        var rolePage2 = tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .byName()
                .inputValue("Superuser")
                .and()
                .clickByName("Superuser");
        tab(FIRST_TAB_ID)
                .activate()
                .lastActive(rolePage1)
                .clickBack()
                .assertPageTitleStartsWith("Edit System user administrator");
        tab(SECOND_TAB_ID)
                .activate()
                .lastActive(rolePage2)
                .clickBack()
                .assertPageTitleStartsWith("All roles");
    }

}
