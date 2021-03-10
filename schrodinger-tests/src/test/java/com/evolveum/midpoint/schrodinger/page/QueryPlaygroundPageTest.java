/*
 * Copyright (c) 2010-2018 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
 */
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.page.configuration.QueryPlaygroundPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

/**
 * Created by Kate Honchar.
 */
public class QueryPlaygroundPageTest extends AbstractSchrodingerTest {

    @Test //covers MID-5346
    public void test001useInObjectListOptionTest() {
        createUser("a_start");
        createUser("b_start");

        QueryPlaygroundPage queryPlaygroundPage = basicPage.queryPlayground();
        queryPlaygroundPage
                .setQuerySampleValue(QueryPlaygroundPage.QueryPlaygroundSample.FIRST_10_USERS_WITH_FIRST_A)
                .useInObjectListButtonClick();

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                        .assertTableContainsLinkTextPartially("a_start");

        usersPage
                .table()
                        .assertTableDoesntContainLinkTextPartially("b_start");
    }
}
