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
package com.evolveum.midpoint.schrodinger.scenarios;

import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;
import org.testng.annotations.Test;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matus on 5/21/2018.
 */
public class PolyStringTests extends AbstractSchrodingerTest {

    private static final String TEST_USER_JOZKO_NAME = "džordž";
    private static final String TEST_USER_JOZKO_NAME_NO_DIAC = "dzordz";
    private static final String TEST_USER_JOZKO_GIVEN_NAME = "Jožko";
    private static final String TEST_USER_JOZKO_FAMILY_NAME = "Mrkvičkä";
    private static final String TEST_USER_JOZKO_FULL_NAME = "Jožko Jörg Nguyễn Trißtan Guðmund Mrkvičkä";
    private static final String TEST_USER_JOZKO_ADDITIONAL_NAME = "Jörg Nguyễn Trißtan Guðmund ";

    private static final String CREATE_USER_WITH_DIACRITIC_DEPENDENCY = "test0010createUserWithDiacritic";
    private static final String SEARCH_USER_WITH_DIACRITIC_DEPENDENCY = "test0020searchForUserWithDiacritic";

    private static final File SYSTEM_CONFIGURATION_FULLTEXT_FILE = new File("./src/test/resources/objects/systemconfiguration/system-configuration-fulltext.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(SYSTEM_CONFIGURATION_FULLTEXT_FILE);
    }

    @Test
    public void test0010createUserWithDiacritic(){
        UserPage user = basicPage.newUser();

        user.selectBasicPanel()
                    .form()
                        .addAttributeValue("name", TEST_USER_JOZKO_NAME)
                        .addAttributeValue(UserType.F_GIVEN_NAME, TEST_USER_JOZKO_GIVEN_NAME)
                        .addAttributeValue(UserType.F_FAMILY_NAME, TEST_USER_JOZKO_FAMILY_NAME)
                        .addAttributeValue(UserType.F_FULL_NAME,TEST_USER_JOZKO_FULL_NAME)
                        .addAttributeValue(UserType.F_ADDITIONAL_NAME,TEST_USER_JOZKO_ADDITIONAL_NAME)
                        .and()
                    .and()
                .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .assertSuccess();
    }

    @Test (dependsOnMethods = {CREATE_USER_WITH_DIACRITIC_DEPENDENCY})
    public void test0020searchForUserWithDiacritic(){

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                       .table()
                            .search()
                                .resetBasicSearch()
                                .byName()
                                .inputValue(TEST_USER_JOZKO_NAME)
                            .updateSearch()
                       .and()
                       .assertCurrentTableContains(TEST_USER_JOZKO_NAME);

        usersPage
                       .table()
                            .search()
                                .byName()
                                .inputValue(TEST_USER_JOZKO_NAME_NO_DIAC)
                            .updateSearch()
                       .and()
                       .assertCurrentTableContains(TEST_USER_JOZKO_NAME);

    }

    @Test (dependsOnMethods = {SEARCH_USER_WITH_DIACRITIC_DEPENDENCY})
    public void test0030fullTextSearchForUserWithDiacritic(){

        ListUsersPage usersPage = basicPage.listUsers();

        usersPage
                        .table()
                            .search()
                                .byFullText()
                                .inputValue(TEST_USER_JOZKO_NAME)
                            .pressEnter()
                        .and()
                        .assertCurrentTableContains(TEST_USER_JOZKO_NAME);

        usersPage
                        .table()
                            .search()
                                .byFullText()
                                .inputValue(TEST_USER_JOZKO_NAME_NO_DIAC)
                            .pressEnter()
                        .and()
                        .assertCurrentTableContains(TEST_USER_JOZKO_NAME);

    }
}
