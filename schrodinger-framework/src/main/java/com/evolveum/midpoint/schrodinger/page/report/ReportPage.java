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
package com.evolveum.midpoint.schrodinger.page.report;

import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.report.ReportEnginePanel;
import com.evolveum.midpoint.schrodinger.component.report.ReportExportPanel;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
/**
 * Created by honchar
 */
public class ReportPage extends AssignmentHolderDetailsPage<ReportPage> {

    public ReportPage() {
    }

    @Override
    public AssignmentsPanel<ReportPage> selectAssignmentsPanel() {
        return null;
    }

    @Override
    public AssignmentHolderBasicPanel<ReportPage> selectBasicPanel() {
        return new AssignmentHolderBasicPanel<>(this, getNavigationPanelSelenideElement("pageReport.basic.title"));
    }

    public ReportExportPanel selectCollectionReportTabExport() {
        return new ReportExportPanel(this, getNavigationPanelSelenideElement("pageReport.export.title"));
    }

    public ReportEnginePanel selectCollectionReportTabEngine() {
        return new ReportEnginePanel(this, getNavigationPanelSelenideElement("pageReport.engine.title"));
    }
}
