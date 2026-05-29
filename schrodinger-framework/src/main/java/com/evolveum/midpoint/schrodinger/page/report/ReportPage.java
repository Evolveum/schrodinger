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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.report.ReportEnginePanel;
import com.evolveum.midpoint.schrodinger.component.report.ReportExportPanel;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

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

    public ReportExportPanel selectCollectionReportTabExport() {
        return new ReportExportPanel(this, getNavigationPanelSelenideElement("pageReport.export.title"));
    }

    public ReportEnginePanel selectCollectionReportTabEngine() {
        return new ReportEnginePanel(this, getNavigationPanelSelenideElement("pageReport.engine.title"));
    }

    public Table<ReportPage, Table> clickShowReportReviewButton() {
        String buttonTitleTranslated = Utils.translate("pageCreateCollectionReport.button.showPreview.false");
        getButtonPanelElement()
                .$x(".//a[@title='" + buttonTitleTranslated + "']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .click();
        Utils.waitForAjaxCallFinish();
        String reportPreviewTranslated = Utils.translate("PageReport.reportPreview");
        SelenideElement tableEl = $x(".//div[@data-s-id='tableContainer']" +
                "[.//h3[contains(text(), '" + reportPreviewTranslated + "')]]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new Table<>(ReportPage.this, tableEl);
    }

}
