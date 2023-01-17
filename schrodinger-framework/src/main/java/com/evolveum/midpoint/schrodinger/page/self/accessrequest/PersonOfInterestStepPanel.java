/*
 * Copyright (c) 2023. Evolveum
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.page.self.accessrequest;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModalTable;
import com.evolveum.midpoint.schrodinger.component.self.RequestRoleTab;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import jdk.jshell.execution.Util;

import static com.codeborne.selenide.Selenide.$;

public class PersonOfInterestStepPanel extends TileListWizardStepPanel<RequestAccessPage> {

    private static final int MYSELF_TILE_INDEX = 1;
    private static final int GROUP_TILE_INDEX = 2;

    public PersonOfInterestStepPanel(RequestAccessPage parent) {
        super(parent);
    }

    public RelationStepPanel selectMyself() {
        selectTileByNumber(MYSELF_TILE_INDEX);
        return new RelationStepPanel(getParent());
    }

    public RelationStepPanel selectGroup(String... userNames) {
        selectTileByNumber(GROUP_TILE_INDEX, false);
        new DefineGroupOfUsersPanel(getParent())
                .selectUserGroupByButtonClick(userNames)
                .clickNextButton();
        return new RelationStepPanel(getParent());
    }

    public class DefineGroupOfUsersPanel extends TileListWizardStepPanel<RequestAccessPage> {

        public DefineGroupOfUsersPanel(RequestAccessPage parent) {
            super(parent);
        }

        public DefineGroupOfUsersPanel selectUserGroupByButtonClick(String... userNames) {
            if (userNames == null) {
                return this;
            }
            getParentElement().$(Schrodinger.byDataId("selectManually")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
            Utils.waitForAjaxCallFinish();

            ObjectBrowserModal<DefineGroupOfUsersPanel> userSelectionModal = new ObjectBrowserModal(this, Utils.getModalWindowSelenideElement());
            ObjectBrowserModalTable<DefineGroupOfUsersPanel, ObjectBrowserModal<DefineGroupOfUsersPanel>> table = userSelectionModal.table();
            for (String userName : userNames) {
                table.search()
                        .byName()
                        .inputValue(userName)
                        .updateSearch()
                        .and()
                        .selectCheckboxByName(userName);
            }
            userSelectionModal.clickAddButton();
            return this;
        }
    }
}
