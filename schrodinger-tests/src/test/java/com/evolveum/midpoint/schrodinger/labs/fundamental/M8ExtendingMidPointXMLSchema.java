/*
 * Copyright (c) 2010-2019 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
 */
package com.evolveum.midpoint.schrodinger.labs.fundamental;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicTab;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsTab;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.resource.ViewResourcePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;
import com.evolveum.midpoint.schrodinger.scenarios.ScenariosCommons;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class M8ExtendingMidPointXMLSchema extends AbstractLabTest {

    protected static final String LAB_OBJECTS_DIRECTORY = LAB_FUNDAMENTAL_DIRECTORY + "M8/";
    private static final File INTERNAL_EMPLOYEE_ROLE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File HR_RESOURCE_FILE_8_1 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File CSV_1_RESOURCE_FILE_8 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-8.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen-8.xml");
    private static final File CSV_3_RESOURCE_FILE_8 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-8.xml");

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

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextBeforeTestClass" })
    @Override
    protected void springTestContextPrepareTestInstance() throws Exception {
        String home = System.getProperty("midpoint.home");
        File mpHomeDir = new File(home);
        File schemaDir = new File(home, "schema");
        if (!mpHomeDir.exists()) {
            super.springTestContextPrepareTestInstance();
        }

        if (!schemaDir.mkdir()) {
            if (schemaDir.exists()) {
                FileUtils.cleanDirectory(schemaDir);
            } else {
                throw new IOException("Creation of directory \"" + schemaDir.getAbsolutePath() + "\" unsuccessful");
            }
        }
        File schemaFile = new File(schemaDir, EXTENSION_SCHEMA_NAME);
        FileUtils.copyFile(EXTENSION_SCHEMA_FILE, schemaFile);

        super.springTestContextPrepareTestInstance();
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(INTERNAL_EMPLOYEE_ROLE_FILE);
    }

    @Test
    public void mod08test01ExtendingMidPointXMLSchema() throws IOException {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);

        PrismForm<AssignmentHolderBasicTab<UserPage>> form = basicPage.newUser()
                .selectTabBasic()
                    .form();

        form.findProperty("ouNumber");
        form.findProperty("ouPath");
        form.findProperty("isManager");
        form.findProperty("empStatus");

//        showTask("HR Synchronization").clickSuspend();

        importResourceAndTestConnection(HR_RESOURCE_FILE_8_1, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        importResourceAndTestConnection(CSV_1_RESOURCE_FILE_8, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        importResourceAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        importResourceAndTestConnection(CSV_3_RESOURCE_FILE_8, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        ResourceAccountsTab<ViewResourcePage> accountTab = basicPage.listResources()
                .table()
                    .search()
                        .byName()
                            .inputValue(HR_RESOURCE_NAME)
                            .updateSearch()
                        .and()
                    .clickByName(HR_RESOURCE_NAME)
                        .clickAccountsTab()
                        .clickSearchInResource();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        accountTab.table()
                .selectCheckboxByName("001212")
                        .clickImport()
                    .and()
                .and()
            .feedback()
                .isSuccess();

        form = accountTab.table()
                .clickOnOwnerByName("X001212")
                .selectTabBasic()
                .form();

        form.assertPropertyInputValue("ouPath", "0300")
                .assertPropertySelectValue("isManager", "True")
                .assertPropertyInputValue("empStatus", "A");

        form.and()
                .and()
            .selectTabAssignments()
                .clickAddAssignemnt()
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
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<AccountPage> accountForm = shadow.form();
        Selenide.sleep(1000);
        accountForm.assertPropertyInputValue("dep", "Human Resources");

        showShadow(CSV_2_RESOURCE_NAME, "Login", "jsmith");
        accountForm.assertPropertyInputValue("department", "Human Resources");

        assertShadowExists(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=John Smith,ou=0300,ou=ExAmPLE,dc=example,dc=com");
    }
}
