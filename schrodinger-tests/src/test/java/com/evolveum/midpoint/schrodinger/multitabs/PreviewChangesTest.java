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

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(MULTIPLE_USERS);
    }

    @Test
    public void test00100previewChangesForTwoUsersAndSave() {
        PreviewPage preview1 = showUser(FIRST_TAB_ID, "jack1")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Given name", "Jackson")
                .and()
                .and()
                .clickPreview();

        PreviewPage preview2 = showUser(SECOND_TAB_ID, "jack2")
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
        PreviewPage preview1 = showUser(FIRST_TAB_ID, "jack3")
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

        PreviewPage preview2 = showRole(SECOND_TAB_ID, "End user")
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

    @Test
    public void test00300previewUserExtensionAttributes() {
        PreviewPage jack10Preview = showUser(FIRST_TAB_ID, "jack10")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Middle name", "Tenth")
                .and()
                .and()
                .clickPreview();

        PreviewPage jack11Preview = showUser(SECOND_TAB_ID, "jack11")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Middle name", "Eleventh")
                .and()
                .and()
                .clickPreview();

        PreviewPage jack12Preview = showUser(THIRD_TAB_ID, "jack12")
                .selectBasicPanel()
                .form()
                .addAttributeValue("Middle name", "Twelfth")
                .and()
                .and()
                .clickPreview();

        tab(FIRST_TAB_ID)
                .activate()
                .lastActive(jack10Preview)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");
        showUser(FIRST_TAB_ID, "jack10")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Middle name", "Tenth");

        tab(THIRD_TAB_ID)
                .activate()
                .lastActive(jack12Preview)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");
        showUser(THIRD_TAB_ID, "jack12")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Middle name", "Twelfth");

        tab(SECOND_TAB_ID)
                .activate()
                .lastActive(jack11Preview)
                .clickSave()
                .feedback()
                .assertSuccess()
                .and()
                .assertPageTitleStartsWith("All users");
        showUser(THIRD_TAB_ID, "jack11")
                .selectBasicPanel()
                .form()
                .assertPropertyInputValue("Middle name", "Eleventh");
    }

        //todo tests for configured preview changes page ()
}
