/*
 * Copyright (c) 2026 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
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
