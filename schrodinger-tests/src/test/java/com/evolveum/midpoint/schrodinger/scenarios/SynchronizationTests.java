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

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.ProjectionsTab;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.task.ListTasksPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String RESOURCE_AND_SYNC_TASK_SETUP_DEPENDENCY = "setUpResourceAndSynchronizationTask";
    private static final String NEW_USER_AND_ACCOUNT_CREATED_DEPENDENCY = "newResourceAccountUserCreated";
    private static final String NEW_USER_ACCOUNT_CREATED_LINKED_DEPENDENCY = "newResourceAccountCreatedLinked";
    private static final String LINKED_USER_ACCOUNT_MODIFIED = "alreadyLinkedResourceAccountModified";
    private static final String LINKED_USER_ACCOUNT_DELETED = "alreadyLinkedResourceAccountDeleted";
    private static final String RESOURCE_ACCOUNT_CREATED_WHEN_UNREACHABLE = "resourceAccountCreatedWhenResourceUnreachable";

    private static final String FILE_RESOUCE_NAME = "midpoint-advanced-sync.csv";
    private static final String DIRECTORY_CURRENT_TEST = "synchronizationTests";

    @Test(priority = 0)
    public void setUpResourceAndSynchronizationTask() throws IOException {

        initTestDirectory(DIRECTORY_CURRENT_TEST);

        csvTargetFile = new File(testTargetDir, FILE_RESOUCE_NAME);

        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);

        addResourceFromFileAndTestConnection(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_FILE, ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, csvTargetFile.getAbsolutePath());
        addObjectFromFile(ScenariosCommons.USER_TEST_RAPHAEL_FILE);

        //changeResourceFilePath(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_SOURCE_OLDVALUE, CSV_TARGET_FILE.getAbsolutePath(), true);

//        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath(), true);


        refreshResourceSchema(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
        ListResourcesPage listResourcesPage = basicPage.listResources();
        ((TaskPage)listResourcesPage
                .table()
                    .clickByName(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .clickAccountsTab()
                        .liveSyncTask()
                            .clickCreateNew()
                                .selectTabBasic()
                                    .form()
                                        .addAttributeValue("name","LiveSyncTest")
                                        .selectOption("recurrence","Recurring")
                                        .and()
                                    .and())
                                .selectScheduleTab()
                                    .form()
                                        .addAttributeValue("interval", "5")
                                        .and()
                            .and()
                                .clickSaveAndRun()
                                    .feedback()
                                    .isInfo();
    }


    @Test (priority = 1, dependsOnMethods = {RESOURCE_AND_SYNC_TASK_SETUP_DEPENDENCY})
    public void newResourceAccountUserCreated() throws IOException {
        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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
    public void protectedAccountAdded(){

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
                        .clickAccountsTab()
                        .clickSearchInResource()
                            .table()
                            .assertCurrentTableContains(ScenariosCommons.TEST_USER_PROTECTED_NAME);

    }


    @Test (priority = 3, dependsOnMethods = {NEW_USER_AND_ACCOUNT_CREATED_DEPENDENCY})
    public void newResourceAccountCreatedLinked() throws IOException {

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                    .updateSearch()
                .and()
                    .clickByName(ScenariosCommons.TEST_USER_DON_NAME)
                        .selectTabProjections()
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

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M);

        ListUsersPage usersListPage = basicPage.listUsers();
        ProjectionsTab projectionsTab = usersListPage
                .table()
                    .search()
                        .byName()
                        .inputValue(ScenariosCommons.TEST_USER_DON_NAME)
                    .updateSearch()
                .and()
                .clickByName(ScenariosCommons.TEST_USER_DON_NAME)
                      .selectTabProjections();
        Selenide.screenshot("SynchronizationTests_projectionTab");
        projectionsTab
                        .table()
                        .assertTableContainsText(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
    }

    @Test (priority = 4, dependsOnMethods = {NEW_USER_ACCOUNT_CREATED_LINKED_DEPENDENCY})
    public void alreadyLinkedResourceAccountModified() throws IOException {

        FileUtils.copyFile(CSV_UPDATED_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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
    public void alreadyLinkedResourceAccountDeleted() throws IOException {

        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_20_S);

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
    public void resourceAccountDeleted(){

        ListUsersPage usersPage = basicPage.listUsers();
        usersPage
                .table()
                        .search()
                            .byName()
                            .inputValue("raphael")
                        .updateSearch()
                    .and()
                        .clickByName("raphael")
                            .selectTabProjections()
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
                        .clickAccountsTab()
                        .clickSearchInResource()
                            .table()
                            .selectCheckboxByName("raphael")
                                .clickDelete()
                            .clickYes()
                        .and()
                        .clickSearchInResource()
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
                            .selectTabProjections()
                                .table()
                                .assertCurrentTableDoesntContain(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME);
    }

    @Test(priority = 7, dependsOnMethods = {LINKED_USER_ACCOUNT_DELETED})
    public void resourceAccountCreatedWhenResourceUnreachable() throws IOException {

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME,  ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath()+"err", false);

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);

        Selenide.sleep(MidPoint.TIMEOUT_LONG_20_S);

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

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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
    public void resourceAccountCreatedWhenResourceUnreachableToBeLinked() throws IOException {
        ListUsersPage listUsersPage= basicPage.listUsers();
        listUsersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                        .updateSearch()
                    .and()
                        .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                            .selectTabProjections()
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
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME , ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath()+"err",false);

        FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

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

        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);

        listUsersPage = basicPage.listUsers();
        listUsersPage
                    .table()
                        .search()
                            .byName()
                            .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                            .selectTabProjections()
                                .table()
                        .assertCurrentTableContains(ScenariosCommons.TEST_USER_RAPHAEL_NAME);
    }
}