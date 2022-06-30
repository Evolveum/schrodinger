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
package com.evolveum.midpoint.schrodinger.component.org;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class MemberTable<T> extends AssignmentHolderObjectListTable<T, AssignmentHolderDetailsPage> {

    public MemberTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public AssignmentHolderDetailsPage getObjectDetailsPage() {
        return new AssignmentHolderDetailsPage() {};
    }

    @Override
    protected TableHeaderDropDownMenu<MemberTable<T>> clickHeaderActionDropDown() {
        SelenideElement dropDownButton = $(Schrodinger.bySelfOrAncestorElementAttributeValue("button", "data-toggle", "dropdown",
                "class", "sortableLabel"));
        dropDownButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelenideElement dropDown = dropDownButton.parent().$x(".//ul[@"+Schrodinger.DATA_S_ID+"='dropDownMenu']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<MemberTable<T>>(this, dropDown);
    }

    public FocusSetAssignmentsModal<MemberTable<T>> assign(){
        return assign(null, null);
    }

    public FocusSetAssignmentsModal<MemberTable<T>> assign(String columnTitleKey, String rowValue){
        return clickMenuItemWithFocusSetAssignmentsModal(columnTitleKey, rowValue, "abstractRoleMemberPanel.menu.assign");
    }

    public ConfirmationModal<MemberTable<T>> recompute(){
        return recompute(null, null);
    }

    public ConfirmationModal<MemberTable<T>> recompute(String columnTitleKey, String rowValue){
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "abstractRoleMemberPanel.menu.recompute");
    }

}
