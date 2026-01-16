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
import com.evolveum.midpoint.schrodinger.component.modal.ReportConfigurationModal;
import com.evolveum.midpoint.schrodinger.component.report.ReportTable;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import com.evolveum.midpoint.schrodinger.page.report.ListReportsPage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ReportTests extends AbstractSchrodingerTest {

    public static final File ENDUSER_ROLE_WITH_REPORT_RESTRICTIONS = new File("./src/test/resources/objects/roles/role-enduser-with-restricted-reports.xml");
    public static final File ENDUSER_STEPHAN = new File("./src/test/resources/objects/users/enduser-stephan.xml");
    public static final File TEST_CAMPAIGN_WITH_STEPHAN_REVIEWER = new File("./src/test/resources/objects/accessCertification/campaign/test-campaign-with-stephan-reviewer.xml");
    public static final File AUDIT_REPORT_DATA = new File("./src/test/resources/objects/reportdata/audit-report-data.xml");
    public static final File EXPORT_TASK_FOR_AUDIT_RECORDS = new File("./src/test/resources/objects/tasks/export-task-for-audit-records.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ENDUSER_ROLE_WITH_REPORT_RESTRICTIONS, ENDUSER_STEPHAN,
                AUDIT_REPORT_DATA, EXPORT_TASK_FOR_AUDIT_RECORDS);
    }

    @Test
    public void test00100createReport() {
        basicPage.listReports()
        .newObjectButtonClick("New Collection report")
            .selectBasicPanel()
                .form()
                    .addAttributeValue("Name", "TestReport")
                    .and()
                .and()
            .clickSave()
            .feedback()
            .assertSuccess();
        basicPage.listReports()
            .table()
                .search()
                    .byName()
                    .inputValue("TestReport")
                    .updateSearch()
                    .and()
                .assertVisibleObjectsCountEquals(1);
        basicPage.listReports().table().assertTableContainsText("TestReport");
    }

    @Test
    public void test00200runUsersReport() {
        ReportTable reportTable = basicPage.listReports().table();
        reportTable
                .search()
                    .resetBasicSearch();
        reportTable
                .runReport("All audit records report") //model is shown
                .runReport(); //confirm report run
        basicPage.listReports("Collection reports")
            .table()
                .search()
                    .byName()
                    .inputValue("All audit records report")
                    .updateSearch()
                    .and()
                .assertVisibleObjectsCountEquals(1);
    }

    /**
     * covers MID-10436, run Reconciliation report with Situation and Kind parameters
     */
    @Test
    public void test00300defineSituationAndKindParameters() {
        ReportConfigurationModal<ListReportsPage> runReportModal = basicPage
                .listReports()
                .table()
                .runReport("Reconciliation report");
        runReportModal
                .table()
                .search()
                .textInputPanelByItemName("Situation")
                .inputValue("Linked")
                .textInputPanelByItemName("Kind")
                .inputValue("account")
                .updateSearch();
        runReportModal.runReport();

        Selenide.sleep(5000);

        basicPage.createdReports()
                .table()
                .search()
                .byName()
                .inputValue("Reconciliation report")
                .updateSearch()
                .and()
                .assertTableContainsText("Reconciliation report")
                .assertAllObjectsCountEquals(1);
    }

    /**
     * Tests if the user doesn't see 'Csv export' button in the object list table toolbar.
     * Also, user shouldn't see Download report button on the task details page
     */
    @Test
    public void test00300denyCsvExport() {
        loginAsUser("stephan", "Test5ecr3t");
        final String csvExportButtonIcon = "fa fa-download";
        basicPage
                .listUsers()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon)
                .and()
                .listRoles()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon)
                .and()
                .auditLogViewer()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon)
                .and()
                .createdReports()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon)
                .and()
                .listServices()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon)
                .and()
                .listArchetypes()
                .table()
                .assertToolbarButtonNotExist(csvExportButtonIcon);
        //todo implement in gui
//        basicPage
//                .listTasks()
//                .table()
//                .clickByName("Export task for All audit records report")
//                .assertDownloadReportButtonNotVisible();
    }

    /**
     * Tests if the user doesn't see 'Create report' button in the object list table toolbar.
     * Also, checks if the user doesn't see 'Create report' button on the campaigns details page.
     */
    @Test
    public void test00400denyCreateReport() {
        reloginAsAdministrator();
        importObject(TEST_CAMPAIGN_WITH_STEPHAN_REVIEWER);
        loginAsUser("stephan", "Test5ecr3t");
        final String createReportButtonIcon = "fa fa-chart-pie";
        basicPage
                .listUsers()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .listRoles()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .auditLogViewer()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .listServices()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .createdReports()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .listArchetypes()
                .table()
                .assertToolbarButtonNotExist(createReportButtonIcon)
                .and()
                .campaigns()
                .selectTableView()
                .clickByName("Certification of critical roles 1")
                .assertCreateReportButtonNotVisible();
        basicPage
                .listReports()
                .table()
                .runReport("All audit records report")
                .runReport()
                .assertFeedbackExists()
                .feedback()
                .assertError();
    }
}
