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

import com.evolveum.midpoint.schema.SchemaConstantsGenerated;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.ProjectionsTab;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;


import com.evolveum.midpoint.xml.ns._public.common.common_3.ActivationType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class M4ProvisioningToResources extends AbstractLabTest {

    private static final Logger LOG = LoggerFactory.getLogger(M4ProvisioningToResources.class);
    protected static final String LAB_OBJECTS_DIRECTORY = LAB_FUNDAMENTAL_DIRECTORY + "M4/";

    private static final File CSV_1_RESOURCE_FILE_4_2 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-4-2.xml");
    private static final File CSV_3_RESOURCE_FILE_4_2 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-4-2.xml");
    private static final File CSV_1_RESOURCE_FILE_4_3 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access-4-3.xml");
    private static final File CSV_3_RESOURCE_FILE_4_4 = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap-4-4.xml");
    private static final File KIRK_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-user.xml");
    private static final File NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE = new File(LAB_OBJECTS_DIRECTORY + "valuepolicies/numeric-pin-first-nonzero-policy.xml");
    private static final File CSV_1_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(KIRK_USER_FILE);
    }

    @Test(groups={"M4"})
    public void mod04test01BasicProvisioningToMultipleResources() throws IOException {
        importObject(NUMERIC_PIN_FIRST_NONZERO_POLICY_FILE, true);
        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);
        csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);
        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

        addResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        showUser("kirk")
                .selectTabProjections()
                    .clickAddProjection()
                        .table()
                            .search()
                                .byName()
                                    .inputValue(CSV_1_RESOURCE_NAME)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(CSV_1_RESOURCE_NAME)
                                .and()
                            .clickAdd()
                        .and()
                    .clickSave()
                        .feedback()
                            .isSuccess();

        assertShadowExists(CSV_1_RESOURCE_NAME, "Login", "jkirk");

        showUser("kirk")
                .selectTabBasic()
                    .form()
                        .addAttributeValue(UserType.F_GIVEN_NAME, "Jim Tiberius")
                    .and()
                .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<AccountPage> accountForm = shadow.form();
        Selenide.sleep(1000);
        accountForm.assertPropertyInputValue("fname", "Jim Tiberius");

        showUser("kirk")
            .selectTabBasic()
                .form()
                    .setDropDownAttributeValue(ActivationType.F_ADMINISTRATIVE_STATUS, "Disabled")
                .and()
            .and()
            .clickSave()
                .feedback()
                    .isSuccess();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Disabled");
        showUserInTable("kirk")
                .selectAll()
                .and()
                .table()
                        .enableUser()
                            .clickYes();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        accountForm.assertPropertySelectValue("administrativeStatus", "Enabled");

        changeAdministrativeStatusViaProjectionTab("kirk", "jkirk", "Disabled", CSV_1_RESOURCE_NAME);
        changeAdministrativeStatusViaProjectionTab("kirk", "jkirk", "Enabled", CSV_1_RESOURCE_NAME);

        showUser("kirk")
                .selectTabProjections()
                    .clickAddProjection()
                        .table()
                            .search()
                                .byName()
                                    .inputValue(CSV_2_RESOURCE_NAME)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(CSV_2_RESOURCE_NAME)
                        .and()
                    .clickAdd()
                    .clickAddProjection()
                        .table()
                            .search()
                                .byName()
                                    .inputValue(CSV_3_RESOURCE_NAME)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(CSV_3_RESOURCE_NAME)
                        .and()
                    .clickAdd()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        assertShadowExists(CSV_2_RESOURCE_NAME, "Login", "kirk");

        basicPage.listResources()
                .table()
                    .search()
                        .byName()
                            .inputValue(CSV_3_RESOURCE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(CSV_3_RESOURCE_NAME)
                        .clickAccountsTab()
                            .clickSearchInResource()
                                .table()
                                    .search()
                                        .textInputPanelByItemName("Distinguished Name")
                                            .inputValue("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                                        .updateSearch()
                                        .and()
                                    .assertTableContainsText("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");

        showUser("kirk")
                .selectTabProjections()
                    .table()
                        .search()
                            .referencePanelByItemName("Resource")
                                .inputRefOid("10000000-9999-9999-0000-a000ff000003")
                            .updateSearch()
                        .and()
                        .selectAll()
                    .and()
                    .clickHeaderActionDropDown()
                        .delete()
                            .clickYes()
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        assertShadowDoesntExist(CSV_2_RESOURCE_NAME, "Login", "kirk");
    }

    @Test(dependsOnMethods = {"mod04test01BasicProvisioningToMultipleResources"}, groups={"M4"})
    public void mod04test02AddingMappings() throws IOException {
        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        addResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE_4_2, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_4_2, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());

        showUser("kirk")
                .selectTabBasic()
                    .form()
                        .showEmptyAttributes("Properties")
                        .addAttributeValue(UserType.F_DESCRIPTION, "This user is created by midPoint")
                        .addAttributeValue("telephoneNumber","123 / 555 - 1010")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<AccountPage> accountForm = shadow.form();
        Selenide.sleep(1000);
        accountForm.assertPropertyInputValue("phone", "123555-1010");

        showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);

        accountForm.assertPropertyInputValue("telephoneNumber", "123 / 555 - 1010");
        accountForm.assertPropertyInputValue("description", "This user is created by midPoint");

    }

    @Test(dependsOnMethods = {"mod04test02AddingMappings"}, groups={"M4"})
    public void mod04test03ModifyingExistingMappings() throws IOException {
        addResourceFromFileAndTestConnection(CSV_1_RESOURCE_FILE_4_3, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());

        UserPage user = basicPage.newUser();
        user.selectTabBasic()
                .form()
                    .addAttributeValue(UserType.F_NAME, "picard")
                    .addAttributeValue(UserType.F_GIVEN_NAME, "Jean-Luc")
                    .addAttributeValue(UserType.F_FAMILY_NAME, "Picard")
                    .setDropDownAttributeValue(ActivationType.F_ADMINISTRATIVE_STATUS, "Enabled")
                    .addPasswordAttributeValue("abc123")
                    .and()
                .and()
             .clickSave()
                .feedback()
                    .isSuccess();

        showUser("picard")
                .selectTabProjections()
                    .clickAddProjection()
                        .table()
                            .search()
                                .byName()
                                    .inputValue(CSV_1_RESOURCE_NAME)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(CSV_1_RESOURCE_NAME)
                        .and()
                        .clickAdd()
                    .and()
                .clickSave()
                    .feedback()
                        .isSuccess();

        AccountPage shadow = showShadow(CSV_1_RESOURCE_NAME, "Login", "jpicard");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<AccountPage> accountForm = shadow.form();
        Selenide.sleep(1000);
        accountForm.assertPropertyInputValue("lname", "PICARD");

        showUser("kirk")
                .checkReconcile()
                .clickSave()
                    .feedback()
                        .isSuccess();

        showShadow(CSV_1_RESOURCE_NAME, "Login", "jkirk");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        accountForm.assertPropertyInputValue("lname", "KIRK");

    }

    @Test(dependsOnMethods = {"mod04test03ModifyingExistingMappings"}, groups={"M4"})
    public void mod04test04AddingANewAttribute() throws IOException {
        ((PrismFormWithActionButtons<AbstractTableWithPrismView<ProjectionsTab<UserPage>>>)
                ((AbstractTableWithPrismView)showUser("kirk")
                        .selectTabProjections()
                            .table()
                                .search()
                                    .textInputPanelByItemName("Name")
                                        .inputValue("jim tiberius kirk")
                                        .updateSearch()
                                    .and())
                                .clickByName("cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com")
                        .showEmptyAttributes("Attributes")
                        .addAttributeValue("manager", "xxx"))
                        .and()
                    .and()
                .and()
                .clickSave()
                    .feedback()
                        .isSuccess();
        AccountPage shadow = showShadow(CSV_3_RESOURCE_NAME, "Distinguished Name", "cn=Jim Tiberius Kirk,ou=ExAmPLE,dc=example,dc=com");
        shadow.form().assertPropertyInputValue("manager", "xxx");

        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE_4_4, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
    }

    private void changeAdministrativeStatusViaProjectionTab(String userName, String accountName, String status, String resourceName) {
        ((PrismFormWithActionButtons<AbstractTableWithPrismView<ProjectionsTab<UserPage>>>)
                ((AbstractTableWithPrismView)showUser(userName)
                .selectTabProjections()
                    .table()
                        .search()
                            .textInputPanelByItemName("Name")
                                .inputValue(accountName)
                            .updateSearch()
                        .and())
                        .clickByName(accountName)
                            .setDropDownAttributeValue(ActivationType.F_ADMINISTRATIVE_STATUS, status))
                            .and()
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                    .isSuccess();
        showShadow(resourceName, "Login", accountName)
                .form()
                    .assertPropertySelectValue("administrativeStatus", status);
    }
}
