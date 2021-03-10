/*
 * Copyright (c) 2010-2019 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
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

    private static final String CREATE_USER_WITH_DIACRITIC_DEPENDENCY = "createUserWithDiacritic";
    private static final String SEARCH_USER_WITH_DIACRITIC_DEPENDENCY = "searchForUserWithDiacritic";

    private static final File SYSTEM_CONFIGURATION_FULLTEXT_FILE = new File("./src/test/resources/configuration/objects/systemconfig/system-configuration-fulltext.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(SYSTEM_CONFIGURATION_FULLTEXT_FILE);
    }

    @Test
    public void createUserWithDiacritic(){
        UserPage user = basicPage.newUser();

        user.selectTabBasic()
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
    public void searchForUserWithDiacritic(){

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                       .table()
                            .search()
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
    public void fullTextSearchForUserWithDiacritic(){

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
