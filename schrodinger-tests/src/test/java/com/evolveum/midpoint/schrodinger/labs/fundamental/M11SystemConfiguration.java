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

public class M11SystemConfiguration extends AbstractLabTest {

    private static final Logger LOG = LoggerFactory.getLogger(M11SystemConfiguration.class);
    protected static final String LAB_OBJECTS_DIRECTORY = LAB_FUNDAMENTAL_DIRECTORY + "M11/";

    private static final File SYSTEM_CONFIGURATION_FILE_11_2 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-11-2.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_11_3 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-11-3.xml");
    private static final File OBJECT_COLLECTION_ACTIVE_EMP_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-active-employees.xml");
    private static final File OBJECT_COLLECTION_INACTIVE_EMP_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-inactive-employees.xml");
    private static final File OBJECT_COLLECTION_FORMER_EMP_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-former-employees.xml");
    private static final File ARCHETYPE_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-employee.xml");
    private static final File ARCHETYPE_EXTERNAL_FILE = new File(LAB_OBJECTS_DIRECTORY + "archetypes/archetype-external.xml");
    private static final File OBJECT_TEMPLATE_USER_FILE_11 = new File(LAB_OBJECTS_DIRECTORY + "objecttemplate/object-template-example-user-11.xml");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
    }

    @Test
    public void mod11test02ConfiguringDeploymentInformation() {
        addObjectFromFile(OBJECT_TEMPLATE_USER_FILE_11);
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_11_2);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.loginWithReloadLoginPage(getUsername(), getPassword());

        basicPage.assertMainHeaderPanelStyleMatch("rgba(48, 174, 48, 1)")
                .assertPageTitleStartsWith("DEV:");

        basicPage.deploymentInformation()
                .form()
                    .addAttributeValue("headerColor", "lightblue")
                    .and()
                .and()
            .clickSave()
                .feedback()
                    .assertSuccess()
                    .and()
                .assertMainHeaderPanelStyleMatch("rgba(173, 216, 230, 1)")
                .deploymentInformation()
                .form()
                    .addAttributeValue("headerColor", "#30ae30")
                    .and()
                .and()
            .clickSave()
                .feedback()
                    .assertSuccess()
                .and()
                .assertMainHeaderPanelStyleMatch("rgba(48, 174, 48, 1)");
    }

    @Test
    public void mod11test03ConfiguringObjectCollectionsAndViews() {
        addObjectFromFile(OBJECT_COLLECTION_ACTIVE_EMP_FILE);
        addObjectFromFile(OBJECT_COLLECTION_INACTIVE_EMP_FILE);
        addObjectFromFile(OBJECT_COLLECTION_FORMER_EMP_FILE);
        addObjectFromFile(ARCHETYPE_EMPLOYEE_FILE);
        addObjectFromFile(ARCHETYPE_EXTERNAL_FILE);
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_11_3);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);

        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login(getUsername(), getPassword());

        basicPage.listUsers("Employees");
        basicPage.listUsers("Externals");
        basicPage.listUsers("Inactive Employees");
        basicPage.listUsers("Former Employees");
        basicPage.listUsers("Active Employees");

    }

}
