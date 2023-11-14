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
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsPanel;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

import org.apache.commons.io.FileUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class M8ExtendingMidPointXMLSchema extends AbstractLabTest {

    protected static final String LAB_OBJECTS_DIRECTORY = FUNDAMENTAL_LABS_DIRECTORY + "M8/";
    private static final File INTERNAL_EMPLOYEE_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File HR_RESOURCE_FILE_8_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File CSV_1_RESOURCE_FILE_8 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-8.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-8.xml");
    private static final File CSV_3_RESOURCE_FILE_8 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-8.xml");
    protected static final File EXTENSION_SCHEMA_FILE = new File(FUNDAMENTAL_LABS_DIRECTORY +"schema/extension-example.xsd");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        hrTargetFile = new File(getTestTargetDir(), HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_SOURCE_FILE_7_4_PART_4, hrTargetFile);

        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);

        csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(INTERNAL_EMPLOYEE_ROLE_FILE);
    }

    @Override
    protected File getExtensionSchemaFile() {
        return EXTENSION_SCHEMA_FILE;
    }

    @Test
    public void mod08test01ExtendingMidPointXMLSchema() throws IOException {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);

        PrismForm<AssignmentHolderBasicPanel<UserPage>> form = basicPage.newUser()
                .selectBasicPanel()
                    .form();

        form.findProperty("ouNumber");
        form.findProperty("ouPath");
        form.findProperty("isManager");
        form.findProperty("empStatus");

//        showTask("HR Synchronization").clickSuspend();

        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE_8_1, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE_8, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_8, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        ResourceAccountsPanel<ResourcePage> accountTab = basicPage.listResources()
                .table()
                    .search()
                        .byName()
                            .inputValue(HR_RESOURCE_NAME)
                            .updateSearch()
                        .and()
                    .clickByName(HR_RESOURCE_NAME)
                        .selectAccountsPanel()
                        .clickSearchInResource();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        accountTab.table()
                .selectCheckboxByName("001212")
                        .importAccount()
                    .and()
                .and()
            .feedback()
                .isSuccess();

        form = accountTab.table()
                .clickOnOwnerByName("X001212", "Edit user") //todo should the title be Edit Employee?
                .selectBasicPanel()
                .form();

        form.assertPropertyInputValue("ouPath", "0300")
                .assertPropertySelectValue("isManager", "True")
                .assertPropertyInputValue("empStatus", "A");

        form.and()
                .and()
            .selectAssignmentsPanel()
                .clickAddAssignment()
                    .table()
                        .search()
                            .byName()
                                .inputValue("Internal Employee")
                                .updateSearch()
                            .and()
                        .selectCheckboxByName("Internal Employee")
                        .and()
                    .clickAdd()
                .and()
            .clickSave()
                .feedback()
                    .isSuccess();

        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jsmith");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        PrismForm<AccountPage> accountForm = shadow.form();
        Selenide.sleep(1000);
        accountForm.assertPropertyInputValue("dep", "Human Resources");

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jsmith");
        accountForm.assertPropertyInputValue("department", "Human Resources");

        assertShadowExists(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=John Smith,ou=0300,ou=ExAmPLE,dc=example,dc=com");
    }
}
