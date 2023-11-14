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

import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.page.DashboardPage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.task.ListTasksPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import java.io.File;
import java.io.IOException;

/**
 * Created by matus on 5/21/2018.
 */
public class SynchronizationTests extends AbstractSchrodingerTest {

    private static File csvTargetFile;

    private static final Logger LOG = LoggerFactory.getLogger(SynchronizationTests.class);

    private static final File CSV_INITIAL_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups-authoritative-initial.csv");
    private static final File CSV_UPDATED_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups-authoritative-updated.csv");

    private static final String RESOURCE_AND_SYNC_TASK_SETUP_DEPENDENCY = "test0010setUpResourceAndSynchronizationTask";
    private static final String NEW_USER_AND_ACCOUNT_CREATED_DEPENDENCY = "test0020newResourceAccountUserCreated";
    private static final String NEW_USER_ACCOUNT_CREATED_LINKED_DEPENDENCY = "test0040newResourceAccountCreatedLinked";
    private static final String LINKED_USER_ACCOUNT_MODIFIED = "test0050alreadyLinkedResourceAccountModified";
    private static final String LINKED_USER_ACCOUNT_DELETED = "test0060alreadyLinkedResourceAccountDeleted";
    private static final String RESOURCE_ACCOUNT_CREATED_WHEN_UNREACHABLE = "test0080resourceAccountCreatedWhenResourceUnreachable";

    private static final String FILE_RESOUCE_NAME = "midpoint-advanced-sync.csv";
    private static final String DIRECTORY_CURRENT_TEST = "synchronizationTests";


    @BeforeClass
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        resetToDefaultAndRelogin();

        initTestDirectory(DIRECTORY_CURRENT_TEST);

        csvTargetFile = new File(testTargetDir, FILE_RESOUCE_NAME);
        LOG.info("Resource source file is created, {}", csvTargetFile.getAbsolutePath());

        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);
        LOG.info("Resource source file is created, {}", csvTargetFile.getAbsolutePath());

    }

    @Test(priority = 0)
    public void test0010setUpResourceAndSynchronizationTask() throws IOException {
        //reset system configuration to initial state to have all configured collection views
        addObjectFromFile(ScenariosCommons.SYSTEM_CONFIGURATION_INITIAL_FILE);
        //relogin
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        basicPage = login.loginIfUserIsNotLog(username, password);
        //just for debugging, remove later
        basicPage.dashboard();
        Selenide.screenshot("dashboardPage");

        addCsvResourceFromFileAndTestConnection(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_FILE, ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, csvTargetFile.getAbsolutePath());
        addObjectFromFile(ScenariosCommons.USER_TEST_RAPHAEL_FILE);

        //changeResourceFilePath(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_SOURCE_OLDVALUE, CSV_TARGET_FILE.getAbsolutePath(), true);

//        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath(), true);


        refreshResourceSchema(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
        basicPage.listResources()
                .table()
                    .clickByName("CSV (target with groups) authoritative")
                        .selectAccountsPanel()
                        .tasks()
                            .clickCreateNew()
                            .liveSynchronizationTask()
                                .configuration()
                                    .name("LiveSyncTest")
                .next()
                .nextToSchedule()
                .interval("5")
                .next()
                .saveAndRun()
                                    .feedback()
                                    .assertSuccess();
    }


    @Test (priority = 1, dependsOnMethods = {RESOURCE_AND_SYNC_TASK_SETUP_DEPENDENCY})
    public void test0020newResourceAccountUserCreated() throws IOException {
        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                    .updateSearch()
                .and()
                .assertCurrentTableContains(ScenariosCommons.TEST_USER_DON_NAME);
    }

    @Test (priority = 2, dependsOnMethods = {NEW_USER_AND_ACCOUNT_CREATED_DEPENDENCY})
    public void test0030protectedAccountAdded(){

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_PROTECTED_NAME)
                    .updateSearch()
                .and()
                .assertCurrentTableDoesntContain(ScenariosCommons.TEST_USER_PROTECTED_NAME);
        ListResourcesPage resourcesPage = basicPage.listResources();

        resourcesPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .selectResourceObjectsPanel()
                            .table()
                            .assertCurrentTableContains(ScenariosCommons.TEST_USER_PROTECTED_NAME);

    }


    @Test (priority = 3, dependsOnMethods = {NEW_USER_AND_ACCOUNT_CREATED_DEPENDENCY})
    public void test0040newResourceAccountCreatedLinked() throws IOException {

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                    .updateSearch()
                .and()
                    .clickByName(ScenariosCommons.TEST_USER_DON_NAME)
                        .selectProjectionsPanel()
                            .table()
                                    .selectCheckboxByName(ScenariosCommons.TEST_USER_DON_NAME)
                        .and()
                            .clickHeaderActionDropDown()
                                .delete()
                                .clickYes()
                        .and()
                    .and()
                        .clickSave()
                            .feedback()
                            .isSuccess();

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        ListUsersPage usersListPage = basicPage.listUsers();
        ProjectionsPanel projectionsTab = usersListPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                    .updateSearch()
                .and()
                .clickByName(ScenariosCommons.TEST_USER_DON_NAME)
                      .selectProjectionsPanel();
        Selenide.screenshot("SynchronizationTests_projectionTab_" + System.currentTimeMillis());
        projectionsTab
                        .table()
                        .assertTableContainsText(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
    }

    @Test (priority = 4, dependsOnMethods = {NEW_USER_ACCOUNT_CREATED_LINKED_DEPENDENCY})
    public void test0050alreadyLinkedResourceAccountModified() throws IOException {

        FileUtils.copyFile(CSV_UPDATED_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(ScenariosCommons.TEST_USER_DON_NAME)
                        .assertGivenName("Donato");
    }

    @Test (priority = 5, dependsOnMethods = {LINKED_USER_ACCOUNT_MODIFIED})
    public void test0060alreadyLinkedResourceAccountDeleted() throws IOException {

        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                        .updateSearch()
                    .and()
                    .assertCurrentTableDoesntContain(ScenariosCommons.TEST_USER_DON_NAME);
    }

    @Test (priority = 6, dependsOnMethods = {RESOURCE_AND_SYNC_TASK_SETUP_DEPENDENCY})
    public void test0070resourceAccountDeleted(){

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                        .search()
                            .byName()
                            .inputValue("raphael")
                        .updateSearch()
                    .and()
                        .clickByName("raphael")
                            .selectProjectionsPanel()
                                .table()
                                .assertCurrentTableDoesntContain(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);

        ListResourcesPage resourcesPage = basicPage.listResources();
        resourcesPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .selectResourceObjectsPanel()
                            .table()
                            .selectCheckboxByName("raphael")
                                .delete()
                            .clickYes()
                        .and()
                            .table()
                            .assertCurrentTableDoesntContain("raphael");

        usersPage = basicPage.listUsers();
        usersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue("raphael")
                        .updateSearch()
                    .and()
                        .clickByName("raphael")
                            .selectProjectionsPanel()
                                .table()
                                .assertCurrentTableDoesntContain(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
    }

    @Test(priority = 7, dependsOnMethods = {LINKED_USER_ACCOUNT_DELETED})
    public void test0080resourceAccountCreatedWhenResourceUnreachable() throws IOException {

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME,  ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath()+"err", false);

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                        .updateSearch()
                    .and()
                    .assertCurrentTableDoesntContain(ScenariosCommons.TEST_USER_DON_NAME);

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath(), true);

        ListTasksPage  tasksPage = basicPage.listTasks();
        tasksPage
                .table()
                .search()
                .byName()
                .inputValue("LiveSyncTest")
                .updateSearch()
                .and()
                .clickByName("LiveSyncTest")
                .clickResume()
                .resumeStopRefreshing();

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        usersPage = basicPage.listUsers();
        usersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                        .updateSearch()
                    .and()
                    .assertCurrentTableContains(ScenariosCommons.TEST_USER_DON_NAME);
    }

    @Test (priority = 8, dependsOnMethods = {RESOURCE_ACCOUNT_CREATED_WHEN_UNREACHABLE})
    public void test0090resourceAccountCreatedWhenResourceUnreachableToBeLinked() throws IOException {
        ListUsersPage listUsersPage= basicPage.listUsers();
        listUsersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                        .updateSearch()
                    .and()
                        .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                            .selectProjectionsPanel()
                                .table()
                                .selectCheckboxByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                            .and()
                                .clickHeaderActionDropDown()
                                    .delete()
                                    .clickYes()
                            .and()
                        .and()
                        .clickSave()
                            .feedback()
                    .assertSuccess();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME , ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath()+"err",false);

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME , ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath(),true);

        ListTasksPage  tasksPage = basicPage.listTasks();
        tasksPage
                .table()
                .search()
                .byName()
                .inputValue("LiveSyncTest")
                .updateSearch()
                .and()
                .clickByName("LiveSyncTest")
                .clickResume()
                .resumeStopRefreshing();

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.toMillis());

        listUsersPage = basicPage.listUsers();
        listUsersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                            .selectProjectionsPanel()
                                .table()
                        .assertCurrentTableContains(ScenariosCommons.TEST_USER_RAPHAEL_NAME);
    }
}
