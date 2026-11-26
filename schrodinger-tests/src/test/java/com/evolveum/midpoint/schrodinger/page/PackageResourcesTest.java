/*
 * Copyright (c) 2010-2026 Evolveum
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
package com.evolveum.midpoint.schrodinger.page;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;

/**
 * Pages with components of UI libraries that load their scripts through Wicket package resource URLs
 * (wicket/resource/...). MidPoint serves these only from selected locations, so a library missing there
 * shows up as a failed request and non-working component.
 */
public class PackageResourcesTest extends AbstractSchrodingerTest {

    private static final File TASK_WITH_STATISTICS_FILE =
            new File("./src/test/resources/objects/tasks/operation-statistics-clean-up.xml");
    private static final String TASK_WITH_STATISTICS_NAME = "OperationStatisticsCleanupTest";

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        resetToDefault();
        FormLoginPage login = midPoint.formLogin();
        basicPage = login.loginIfUserIsNotLog(username, password);
    }

    /**
     * Chart panels need chart.js.
     */
    @Test
    public void test0010chartLibraryIsLoaded() {
        importObject(TASK_WITH_STATISTICS_FILE, true, true);
        TaskPage taskPage = basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                        .inputValue(TASK_WITH_STATISTICS_NAME)
                        .updateSearch()
                    .and()
                    .clickByName(TASK_WITH_STATISTICS_NAME);
        taskPage.selectOperationStatisticsPanel()
                .assertResultsChartIsRendered();
        basicPage.assertNoFailedPackageResources();
    }

    /**
     * Selection of users in request access wizard is a select2 component, which loads its language file.
     */
    @Test
    public void test0020select2ResourcesAreLoaded() {
        basicPage.requestAccess()
                .selectGroupTile();
        basicPage.assertNoFailedPackageResources();
    }

    /**
     * Password field loads the password strength script by plain URL (static/...), which must not be turned
     * into a package resource URL.
     */
    @Test
    public void test0030passwordStrengthScriptIsLoaded() {
        basicPage.newUser()
                .selectPasswordPanel()
                .assertPasswordStrengthScriptLoaded();
        basicPage.assertNoFailedPackageResources();
    }
}
