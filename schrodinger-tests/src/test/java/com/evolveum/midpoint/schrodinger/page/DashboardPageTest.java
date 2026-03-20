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
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DashboardPageTest extends AbstractSchrodingerTest {

    private static final File RESOURCE_FILE = new File(
            "src/test/resources/objects/resources/resource-csv-username.xml");
    private static final File OBJECT_COLLECTION_FILE = new File(
            "src/test/resources/objects/objectcollections/object-collection-shadow-count-notfound.xml");
    private static final File DASHBOARD_FILE = new File(
            "src/test/resources/objects/dashboards/shadow-count-dashboard.xml");
    private static final File SYSTEM_CONFIGURATION_FILE = new File(
            "src/test/resources/objects/systemconfiguration/system-configuration-shadow-count-dashboard.xml");
    private static final File CSV_SOURCE_FILE = new File(
            "src/test/resources/sources/midpoint-username.csv");

    private static final String RESOURCE_NAME = "Test CSV: username";
    private static final String DASHBOARD_LABEL = "Shadow count dashboard";
    private static final String WIDGET_LABEL = "Broken shadows";
    private static final String SHADOW_NAME = "user01";

    @Override
    protected List<File> getObjectListToImport() {
        return List.of(OBJECT_COLLECTION_FILE, SYSTEM_CONFIGURATION_FILE);
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        addCsvResourceFromFileAndTestConnection(RESOURCE_FILE, RESOURCE_NAME, CSV_SOURCE_FILE.getAbsolutePath());

        basicPage
                .listResources()
                .table()
                .clickByName(RESOURCE_NAME)
                .selectAccountsPanel()
                .reload()
                .table()
                .selectRowByName(SHADOW_NAME);

        importObject(DASHBOARD_FILE, true);
        reloginAsAdministrator();
    }

    @Test
    public void test100DashboardShowsShadowCountForBrokenResource() {
        DashboardPage dashboardPage = basicPage.dashboard(DASHBOARD_LABEL);

        dashboardPage
                .assertWidgetValueNotEquals(WIDGET_LABEL, "UNKNOWN")
                .assertWidgetValueEquals(WIDGET_LABEL, "4");
    }
}
