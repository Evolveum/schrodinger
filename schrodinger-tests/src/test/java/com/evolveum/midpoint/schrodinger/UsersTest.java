/*
 * Copyright (c) 2010-2021 Evolveum
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

package com.evolveum.midpoint.schrodinger;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UsersTest extends AbstractSchrodingerTest {

    private static final File LOOKUP_TABLE_SUBTYPES = new File("src/test/resources/objects/lookuptable/subtypes.xml");
    private static final File OT_FOR_LOOKUP_TABLE_SUBTYPES = new File("src/test/resources/objects/objecttemplate/object-template-for-lookup-table-subtypes.xml");
    private static final File SYSTEM_CONFIG_WITH_LOOKUP_TABLE = new File("src/test/resources/objects/systemconfig/system-configuration-with-lookup-table.xml");
    private static final File MULTIPLE_USERS = new File("src/test/resources/objects/users/jack-users.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(LOOKUP_TABLE_SUBTYPES, OT_FOR_LOOKUP_TABLE_SUBTYPES, SYSTEM_CONFIG_WITH_LOOKUP_TABLE, MULTIPLE_USERS);
    }

    @Test
    public void test001UserTablePaging() {
        ListUsersPage users = basicPage.listUsers();

        Paging paging = users
                .table()
                .paging();

        paging.pageSize(5);
        Selenide.sleep(3000);

        paging
                .next()
                .last()
                .previous()
                .first()
                .actualPagePlusOne()
                .actualPagePlusTwo()
                .actualPageMinusTwo()
                .actualPageMinusOne();
    }

    @Test
    public void test002SearchWithLookupTable() {

        Map<String, String> attr = new HashMap<>();
        attr.put("name", "searchUser");
        attr.put("title", "PhD.");
        createUser(attr);

        ListUsersPage users = basicPage.listUsers();

        users
                .table()
                    .search()
                        .textInputPanelByItemName("title")
                            .inputValue("PhD.")
                    .updateSearch()
                    .and()
                .assertCurrentTableContains("searchUser");

        users
                .table()
                    .search()
                        .textInputPanelByItemName("title")
                            .inputValue("PhD")
                    .updateSearch()
                    .and()
                .assertCurrentTableContains("searchUser");

        users
                .table()
                    .search()
                        .textInputPanelByItemName("title")
                            .inputValue("Ing.")
                    .updateSearch()
                    .and()
                .assertCurrentTableDoesntContain("searchUser");

        users
                .table()
                    .search()
                        .textInputPanelByItemName("title")
                            .inputValue("Ing")
                    .updateSearch()
                    .and()
                .assertCurrentTableDoesntContain("searchUser");

    }
}
