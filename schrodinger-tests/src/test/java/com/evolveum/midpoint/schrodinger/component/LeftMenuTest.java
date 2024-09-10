/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.component;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LeftMenuTest extends AbstractSchrodingerTest {

    private static final File END_USER = new File("./src/test/resources/objects/users/end-user-tasks-view-auth.xml");
    private static final File END_USER_ROLE_WITH_TASKS_VIEW_AUTH = new File("./src/test/resources/objects/roles/role-enduser-task-views-auth.xml");
    private static final File SYSTEM_CONFIG_DEFAULT = new File("./src/test/resources/objects/systemconfiguration/000-system-configuration.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(SYSTEM_CONFIG_DEFAULT, END_USER_ROLE_WITH_TASKS_VIEW_AUTH, END_USER);
    }

    //covers MID-9267
    @Test
    public void test00100tasksViewAuth() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login("tasksViewTestUser", "password");

        assertTaskMenuItemExists("Approval-related task");
        assertTaskMenuItemExists("Asynchronous update tasks");
        assertTaskMenuItemExists("Certification-related tasks");
        assertTaskMenuItemExists("Cleanup tasks");
        assertTaskMenuItemExists("Import tasks");
        assertTaskMenuItemExists("Iterative action tasks");
        assertTaskMenuItemExists("Live synchronization tasks");
        assertTaskMenuItemExists("Recomputation tasks");
        assertTaskMenuItemExists("Reconciliation tasks");
        assertTaskMenuItemExists("Report tasks");
        assertTaskMenuItemExists("Single action tasks");
        assertTaskMenuItemExists("System tasks");
        assertTaskMenuItemExists("Utility tasks");
        assertTaskMenuItemDoesntExists("All tasks");
        assertTaskMenuItemDoesntExists("New task");

    }

    private void assertTaskMenuItemExists(String menuItemLabel) {
        basicPage.assertMenuItemExists(ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE,
                "PageAdmin.menu.top.serverTasks", menuItemLabel);
    }

    private void assertTaskMenuItemDoesntExists(String menuItemLabel) {
        basicPage.assertMenuItemDoesntExist(ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE,
                "PageAdmin.menu.top.serverTasks", menuItemLabel);
    }
}
