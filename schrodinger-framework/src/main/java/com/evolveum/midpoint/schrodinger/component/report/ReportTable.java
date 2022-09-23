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
package com.evolveum.midpoint.schrodinger.component.report;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.ReportConfigurationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.report.CreatedReportsPage;
import com.evolveum.midpoint.schrodinger.page.report.ListReportsPage;
import com.evolveum.midpoint.schrodinger.page.report.ReportPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

/**
 * Created by honchar
 */
public class ReportTable extends AssignmentHolderObjectListTable<ListReportsPage, ReportPage> {

    public ReportTable(ListReportsPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<ReportTable> clickHeaderActionDropDown() {
       return null;
    }

    @Override
    public ReportPage getObjectDetailsPage() {
        return new ReportPage();
    }
    
    public ReportConfigurationModal runReport(String report) {
        clickMenuItemButton("ObjectType.name", report, ".fa.fa-play");
        return new ReportConfigurationModal<>(getParent(), Utils.getModalWindowSelenideElement());
    }

    public ReportPage editReport(String report) {
        clickMenuItemButton("ObjectType.name", report, ".fa.fa-edit");
        return new ReportPage();
    }

    public CreatedReportsPage showReportOutput(String report) {
        clickMenuItemButton("ObjectType.name", report, ".fa.fa-files-o");
        return new CreatedReportsPage();
    }


}

