/*
 * Copyright (c) 2025 Evolveum and contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.scenarios;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ObjectCollectionViewTests extends AbstractSchrodingerTest {

    private static final File END_USER_ROLE_FILE = new File("src/test/resources/objects/roles/role-enduser-hidden-person-view.xml");
    private static final File END_USER_FILE = new File("src/test/resources/objects/users/enduser-user-hidden-person-view.xml");

    private static final String ENDUSER_NAME = "enduser";
    private static final String ENDUSER_PASSWORD = "Password123!";


    @Override
    protected List<File> getObjectListToImport() {
        return Arrays.asList(END_USER_ROLE_FILE, END_USER_FILE);
    }

    //covers #10648 Unable to completely hide ObjectCollection
    //the hidden collection view shouldn't be present in Object collection search item;
    //as there is only Person option in the Object collection search item by default,
    //the whole search item should be hidden when Person is hidden
    @Test
    public void test00100hiddenPersonViewIsNotPresentInGui() {
        basicPage
                .listUsers()
                .table()
                .search()
                .assertSearchItemExists("Object collection")
                .dropDownPanelByItemName("Object collection")
                .inputDropDownValue("Person")
                .updateSearch()
                .assertActualOptionOfSelectSearchItem("Object collection", "Person");

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login(ENDUSER_NAME, ENDUSER_PASSWORD);

        basicPage.assertMenuItemDoesntExist(ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE, "PageAdmin.menu.top.users", "Person");

        basicPage.listUsers()
                .table()
                .search()
                .assertSearchItemDoesntExist("Object collection");
    }

}
