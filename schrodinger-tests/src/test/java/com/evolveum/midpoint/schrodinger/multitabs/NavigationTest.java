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

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import com.evolveum.midpoint.schrodinger.util.Utils;
import com.evolveum.midpoint.xml.ns._public.common.common_3.RoleType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

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

    private static final Logger LOG = LoggerFactory.getLogger(NavigationTest.class);

    private static final File MULTIPLE_USERS = new File("src/test/resources/objects/users/jack-users.xml");

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
        var rolePage1 = showUser(FIRST_TAB_ID, "administrator")
                .selectAssignmentsPanel()
                .selectTypeRole()
                .table()
                .clickByName("Superuser")
                .navigateToTargetRefDetailsPage(RoleType.class);
        var rolePage2 = showRole(SECOND_TAB_ID, "Superuser");
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

    /**
     * The state of the left side menu items (active/inactive) stored in session storage.
     * Check that each browser tab displays the activated menu items correctly after the page refresh.
     */
    @Test
    public void test00200leftMenuState() {
        tab(FIRST_TAB_ID)
                .getBasicPage()
                .profile();
        var profileMenuElement = basicPage.getMenuItemElementByMenuLabelText(
                ConstantsUtil.SELF_SERVICE_MENU_ITEMS_SECTION_VALUE, "PageAdmin.menu.profile", "");
        tab(SECOND_TAB_ID)
                .getBasicPage()
                .credentials();
        var credentialsMenuElement = basicPage.getMenuItemElementByMenuLabelText(
                ConstantsUtil.SELF_SERVICE_MENU_ITEMS_SECTION_VALUE, "PageAdmin.menu.credentials", "");
        tab(THIRD_TAB_ID)
                .getBasicPage()
                .listServices();
        var servicesMenuElement = basicPage.getMenuItemElementByMenuLabelText(
                ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE, "PageAdmin.menu.top.services",
                "All services");
        //check Profile menu item is active on the first tab
        tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .assertMenuItemActive(profileMenuElement);
        Selenide.refresh();
        tab(FIRST_TAB_ID)
                .getBasicPage()
                .assertMenuItemActive(profileMenuElement);

        //check Credentials menu item is active on the first tab
        tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .assertMenuItemActive(credentialsMenuElement);
        Selenide.refresh();
        tab(SECOND_TAB_ID)
                .getBasicPage()
                .assertMenuItemActive(credentialsMenuElement);

        //check All services menu item is active on the first tab
        tab(THIRD_TAB_ID)
                .activate()
                .getBasicPage()
                .assertMenuItemActive(servicesMenuElement);
        Selenide.refresh();
        tab(THIRD_TAB_ID)
                .getBasicPage()
                .assertMenuItemActive(servicesMenuElement);
    }

    /**
     * If wrong id is passed to URL, the correct one should be still present in url
     */
    @Test
    public void test00300urlWithWrongWindowId() {
        basicPage
                .listUsers();
        String windowId = getCurrentWindowId();
        open("/admin/users?w=wrongId");
        Utils.waitForAjaxCallFinish();
        String windowIdAfterReload = getCurrentWindowId();
        Utils.waitForAjaxCallFinish();
        assertion.assertEquals(windowId, windowIdAfterReload, "Window id should stay the same.");
    }

    /**
     * If no id is passed to URL, the correct one should be still present in url
     * */
    @Test
    public void test00400urlWithoutWindowId() {
        open("/admin/roles");
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(2000);
        String windowId = getCurrentWindowId();
        assertion.assertTrue(StringUtils.isNotEmpty(windowId), "Window id should not be empty.");
    }

    private String getCurrentWindowId() {
        String actualUrl = WebDriverRunner.url();
        String wValue = null;
        try {
            String query = new URL(actualUrl).getQuery();
            if (query == null) {
                return null;
            }
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair[0].equals("w")) {
                    wValue = URLDecoder.decode(pair[1], "UTF-8");
                }
            }
        } catch (Exception e) {
            LOG.error("Wrong url {}", actualUrl);
        }
        return wValue;
    }

}
