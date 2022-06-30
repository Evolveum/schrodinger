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
package com.evolveum.midpoint.schrodinger.page.self;

import com.codeborne.selenide.Condition;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModalTable;
import com.evolveum.midpoint.schrodinger.component.self.RequestRoleTab;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class AssignmentsListPage extends BasicPage {

    /**
     * if request runs successfully, the user is redirected to RequestRolePage
     * if some error occurs, the user stays on the same page
     * @return
     */
    public BasicPage clickRequestButton() {
        $(Schrodinger.byDataId("request")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        if (feedback().isSuccess()) {
            return new RequestRolePage();
        } else {
            return this;
        }
    }

    public RequestRolePage clickCancelButton() {
        $(Schrodinger.byDataId("back")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new RequestRolePage();
    }

    public AssignmentsListPage setTargetUser(String... userNames) {
        if (userNames == null) {
            return this;
        }
        $(Schrodinger.byDataId("userSelectionButton")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        ObjectBrowserModal<RequestRoleTab> userSelectionModal = new ObjectBrowserModal(this, Utils.getModalWindowSelenideElement());
        ObjectBrowserModalTable<RequestRoleTab, ObjectBrowserModal<RequestRoleTab>> table = userSelectionModal.table();
        if (userSelectionModal != null) {
            for (String userName : userNames) {
                table.search()
                        .byName()
                        .inputValue(userName)
                        .updateSearch()
                        .and()
                        .selectCheckboxByName(userName);
            }
            userSelectionModal.clickAddButton();
        }
        return this;
    }

    public AssignmentsListPage setRequestComment(String comment) {
        $(Schrodinger.byDataId("description")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(comment);
        return this;
    }

}
