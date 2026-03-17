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
import com.evolveum.midpoint.schrodinger.page.PreviewPage;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * Covers the Preview changes functionality while executing on multiple browser tabs
 */
public class PreviewChangesTest extends AbstractSchrodingerTest {

    private static final File MULTIPLE_USERS = new File("src/test/resources/objects/users/jack-users.xml");

    private static final String SECOND_TAB_ID = "secondTab";

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(MULTIPLE_USERS);
    }

    @Test
    public void test00100previewChangesForTwoUsersAndSave() {
        PreviewPage preview1 = tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .byName()
                .inputValue("jack1")
                .updateSearch()
                .and()
                .clickByName("jack1")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Given name", "Jackson")
                .and()
                .and()
                .clickPreview();

        PreviewPage preview2 = tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .byName()
                .inputValue("jack2")
                .updateSearch()
                .and()
                .clickByName("jack2")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Given name", "Jacky")
                .and()
                .and()
                .clickPreview();

        tab(FIRST_TAB_ID)
                .activate()
                .lastActive(preview1)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");


        tab(SECOND_TAB_ID)
                .activate()
                .lastActive(preview2)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");

        showUser("jack1")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Given name", "Jackson");

        showUser("jack2")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Given name", "Jacky");
    }

    @Test
    public void test00200previewUserAndRoleChanges() {
        PreviewPage preview1 = tab(FIRST_TAB_ID)
                .activate()
                .getBasicPage()
                .listUsers()
                .table()
                .search()
                .byName()
                .inputValue("jack3")
                .updateSearch()
                .and()
                .clickByName("jack3")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Family name", "Black")
                .and()
                .and()
                .selectAssignmentsPanel()
                .clickAddAllAssignment()
                .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ROLE)
                .table()
                .search()
                .byName()
                .inputValue("End user")
                .updateSearch()
                .and()
                .selectRowByName("End user")
                .and()
                .clickAdd()
                .and()
                .clickPreview();

        PreviewPage preview2 = tab(SECOND_TAB_ID)
                .activate()
                .getBasicPage()
                .listRoles()
                .table()
                .search()
                .byName()
                .inputValue("End user")
                .updateSearch()
                .and()
                .clickByName("End user")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Description", "Role for end user")
                .and()
                .and()
                .clickPreview();

        tab(FIRST_TAB_ID)
                .activate()
                .lastActive(preview1)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");


        tab(SECOND_TAB_ID)
                .activate()
                .lastActive(preview2)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All roles");

        showUser("jack3")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Family name", "Black")
                .and()
                .and()
                .selectAssignmentsPanel()
                .assertAssignmentExists("End user");

        showRole("End user")
                .selectBasicPanel()
                .form()
                .assertPropertyTextareaValueContainsText("Description", "Role for end user");
    }

        //todo tests for configured preview changes page ()
}
