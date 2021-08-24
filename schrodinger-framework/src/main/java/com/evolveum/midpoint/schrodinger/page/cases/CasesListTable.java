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
package com.evolveum.midpoint.schrodinger.page.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Kate Honchar.
 */
public class CasesListTable extends AssignmentHolderObjectListTable<CasesPage, CasePage> {

    public CasesListTable(CasesPage parent, SelenideElement parentElement){
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<CasesListTable> clickHeaderActionDropDown() {
        return null;
    }

    @Override
    public CasePage getObjectDetailsPage(){
        $(Schrodinger.byDataId("mainPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new CasePage();
    }

    public ConfirmationModal<CasesListTable> stopCase() {
        return stopCase(null, null);
    }

    public ConfirmationModal<CasesListTable> stopCaseByName(String nameValue) {
        return stopCase("ObjectType.name", nameValue);
    }

    public ConfirmationModal<CasesListTable> stopCase(String columnTitleKey, String rowValue) {
        return clickButtonMenuItemWithConfirmation(columnTitleKey, rowValue, "fa.fa-stop");
    }

    public ConfirmationModal<CasesListTable> deleteCase() {
        return stopCase(null, null);
    }

    public ConfirmationModal<CasesListTable> deleteCaseByName(String nameValue) {
        return stopCase("ObjectType.name", nameValue);
    }

    public ConfirmationModal<CasesListTable> deleteCase(String columnTitleKey, String rowValue) {
        return clickButtonMenuItemWithConfirmation(columnTitleKey, rowValue, "fa.fa-minus");
    }
}
