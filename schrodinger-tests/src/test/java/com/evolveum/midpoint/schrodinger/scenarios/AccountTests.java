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

import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.common.ProjectionFormPanelWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import java.io.File;
import java.io.IOException;

/**
 * Created by matus on 3/22/2018.
 */
public class AccountTests extends AbstractSchrodingerTest {

    private static File csvTargetFile;

    private static final File CSV_RESOURCE_MEDIUM = new File("./src/test/resources/objects/resources/resource-csv-username.xml");

    protected static final File CSV_INITIAL_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-username.csv");

    protected static final String IMPORT_CSV_RESOURCE_DEPENDENCY= "test0020importCsvResource";
    protected static final String CREATE_MP_USER_DEPENDENCY= "test0010createMidpointUser";
    protected static final String CHANGE_RESOURCE_FILE_PATH_DEPENDENCY= "test0030changeResourceFilePath";
    protected static final String ADD_ACCOUNT_DEPENDENCY= "test0040addAccount";
    protected static final String DISABLE_ACCOUNT_DEPENDENCY= "test0070disableAccount";
    protected static final String ENABLE_ACCOUNT_DEPENDENCY= "test0080enableAccount";
    protected static final String MODIFY_ACCOUNT_PASSWORD_DEPENDENCY= "test0060modifyAccountPassword";
    protected static final String TEST_GROUP_BEFORE_USER_DELETION = "beforeDelete";

    protected static final String CSV_RESOURCE_NAME= "Test CSV: username";

    protected static final String CSV_RESOURCE_ATTR_FILE_PATH= "File path";

    protected static final String TEST_USER_MIKE_NAME= "michelangelo";
    protected static final String TEST_USER_MIKE_LAST_NAME_OLD= "di Lodovico Buonarroti Simoni";
    protected static final String TEST_USER_MIKE_LAST_NAME_NEW= "di Lodovico Buonarroti Simoni Il Divino";

    private static final String DIRECTORY_CURRENT_TEST = "accountTests";
    private static final String FILE_RESOUCE_NAME = "midpoint-accounttests.csv";


    @Test(priority = 1, groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0010createMidpointUser() throws IOException {

        initTestDirectory(DIRECTORY_CURRENT_TEST);

        csvTargetFile = new File(testTargetDir, FILE_RESOUCE_NAME);
        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);

        UserPage user = basicPage.newUser();

        user.selectBasicPanel()
                    .form()
                        .addAttributeValue("Name", TEST_USER_MIKE_NAME)
                        .addAttributeValue(UserType.F_GIVEN_NAME, "Michelangelo")
                        .addAttributeValue(UserType.F_FAMILY_NAME, "di Lodovico Buonarroti Simoni")
                        .and()
                    .and()
                .addPasswordAttributeValue("5ecr3tPassword")
                .checkKeepDisplayingResults()
                    .clickSave()
                    .feedback()
                    .assertSuccess();
    }

    @Test(priority = 2, groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0020importCsvResource(){
        basicPage.importObject()
                .getObjectsFromFile()
                    .chooseFile(CSV_RESOURCE_MEDIUM)
                    .checkOverwriteExistingObject()
                    .clickImportFileButton()
                .feedback()
                    .assertSuccess();
    }


    @Test (priority = 3, dependsOnMethods = {IMPORT_CSV_RESOURCE_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0030changeResourceFilePath(){
        ListResourcesPage listResourcesPage = basicPage.listResources();

        listResourcesPage
                .table()
                .clickByName(CSV_RESOURCE_NAME)
                    .selectConnectorConfigurationPanel()
                    .selectConfigurationTab()
                        .form()
                        .changeAttributeValue(CSV_RESOURCE_ATTR_FILE_PATH, ScenariosCommons.CSV_SOURCE_OLDVALUE, csvTargetFile.getAbsolutePath())
                        .changeAttributeValue(CSV_RESOURCE_ATTR_UNIQUE,"","username")
                    .and()
                .and()
                .and()
                .clickSave()
                .feedback()
                    .assertSuccess();
        listResourcesPage
                .testConnectionClick(CSV_RESOURCE_NAME)
                .feedback()
                .assertSuccess();
        refreshResourceSchema(CSV_RESOURCE_NAME);
    }

    @Test(priority = 4, dependsOnMethods = {CREATE_MP_USER_DEPENDENCY,CHANGE_RESOURCE_FILE_PATH_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0040addAccount() {
        ListUsersPage users = basicPage.listUsers();
        users
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectProjectionsPanel()
                    .clickAddProjection()
                            .table()
                            .selectCheckboxByName(CSV_RESOURCE_NAME)
                        .and()
                        .clickAdd()
                    .and()
                    .checkKeepDisplayingResults()
                        .clickSave()
                        .feedback()
                        .assertSuccess();
    }

    @Test (priority = 5, dependsOnMethods = {ADD_ACCOUNT_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0050modifyAccountAttribute(){
        ListUsersPage users = basicPage.listUsers();
                users
                    .table()
                        .search()
                        .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(TEST_USER_MIKE_NAME)
                        .selectProjectionsPanel()
                            .table()
                            .clickByName(TEST_USER_MIKE_NAME)
                                .changeAttributeValue("lastname",TEST_USER_MIKE_LAST_NAME_OLD,TEST_USER_MIKE_LAST_NAME_NEW)
                            .and()
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                        .clickSave()
                        .feedback()
            ;
    }

    @Test (priority = 6, dependsOnMethods = {ADD_ACCOUNT_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0060modifyAccountPassword(){
        ProjectionsPanel<UserPage> projectionsPanel = basicPage.listUsers()
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectProjectionsPanel();
        ProjectionFormPanelWithActionButtons projectionaPanel = (ProjectionFormPanelWithActionButtons) projectionsPanel
                        .table()
                        .clickByName(TEST_USER_MIKE_NAME);
        projectionaPanel
                .selectPasswordPanel()
                .setPasswordValue("5ecr3t")
                .and();
        //TODO create projections panel table
//                .and()
//                .and()
//                .checkKeepDisplayingResults()
//                .clickSave()
//                    .feedback()
//                    .isSuccess();
    }

    @Test (priority = 7, dependsOnMethods = {ADD_ACCOUNT_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0070disableAccount(){
        ListUsersPage users = basicPage.listUsers();
            users
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectProjectionsPanel()
                        .table()
                        .clickByName(TEST_USER_MIKE_NAME)
                            .selectOption("administrativeStatus","Disabled")
                        .and()
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .isSuccess();
    }

    @Test (priority = 8, dependsOnMethods = {ADD_ACCOUNT_DEPENDENCY, DISABLE_ACCOUNT_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0080enableAccount(){
        ListUsersPage users = basicPage.listUsers();
            users
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_MIKE_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_MIKE_NAME)
                    .selectProjectionsPanel()
                        .table()
                        .clickByName(TEST_USER_MIKE_NAME)
                            .selectOption("administrativeStatus","Enabled")
                        .and()
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .isSuccess();
    }

    @Test(priority = 9, dependsOnMethods = {ENABLE_ACCOUNT_DEPENDENCY},groups = TEST_GROUP_BEFORE_USER_DELETION)
    public void test0090deleteAccount(){
        ListUsersPage users = basicPage.listUsers();
                users
                    .table()
                        .search()
                        .byName()
                        .inputValue(TEST_USER_MIKE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(TEST_USER_MIKE_NAME)
                        .selectProjectionsPanel()
                            .table()
                            .selectCheckboxByName(TEST_USER_MIKE_NAME)
                        .and()
                            .clickHeaderActionDropDown()
                            .delete()
                                .clickYes()
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .isSuccess();
    }
}
