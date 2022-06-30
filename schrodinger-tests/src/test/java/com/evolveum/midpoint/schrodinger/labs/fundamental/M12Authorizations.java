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
package com.evolveum.midpoint.schrodinger.labs.fundamental;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;

import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;

import com.evolveum.midpoint.schrodinger.page.self.ProfilePage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author skublik
 */

public class M12Authorizations extends AbstractLabTest {

    private static final Logger LOG = LoggerFactory.getLogger(M12Authorizations.class);
    protected static final String LAB_OBJECTS_DIRECTORY = FUNDAMENTAL_LABS_DIRECTORY + "M12/";

    private static final File ROLE_BASIC_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-basic-user.xml");
    private static final File ROLE_BASIC_USER_FILE_12_1 = new File(LAB_OBJECTS_DIRECTORY + "roles/role-basic-user-12-1.xml");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
    }

    @Test
    public void mod12test01BasicUserAuthorization() {
        addObjectFromFile(ROLE_BASIC_USER_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        showUser("X000005")
                .addPasswordAttributeValue("qwerty12345XXXX")
                .clickSave()
                .feedback()
                    .isSuccess();

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login("X000005", "qwerty12345XXXX")
            .feedback()
                .isError();

        login.login(getUsername(), getPassword());

        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("X000005").selectAssignmentsPanel(), true,  "Basic user");

        basicPage.loggedUser().logoutIfUserIsLogin();
        login.login("X000005", "qwerty12345XXXX");

        ProfilePage profile = basicPage.profile();
        profile.selectProjectionsPanel()
            .table()
                .assertTableContainsLinksTextPartially(""); //TODO projections names

        profile.selectAssignmentsPanel()
                .table()
                    .assertTableContainsLinksTextPartially("Basic user", ""); //TODO roles names

        basicPage.credentials(); //TODO implement credentials page

        basicPage.loggedUser().logoutIfUserIsLogin();
        login.login(getUsername(), getPassword());

        addObjectFromFile(ROLE_BASIC_USER_FILE_12_1);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());

        basicPage.loggedUser().logoutIfUserIsLogin();
        login.login("X000005", "qwerty12345ZZZZ");

    }
}
