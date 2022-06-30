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
package com.evolveum.midpoint.schrodinger.component.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class ChangePasswordPanel<T> extends Component<T> {

    public ChangePasswordPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ChangePasswordPanel<T> setOldPasswordValue(String value) {
        getParentElement().$(Schrodinger.byDataId("oldPassword"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(value);
        return this;
    }

    public ChangePasswordPanel<T> setNewPasswordValue(String value) {
        getParentElement().$(Schrodinger.byDataId("password1"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(value);
        return this;
    }

    public ChangePasswordPanel<T> setRepeatPasswordValue(String value) {
        getParentElement().$(Schrodinger.byDataId("password2"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(value);
        return this;
    }

    public ChangePasswordPanel<T> expandPasswordPropagationPanel() {
        SelenideElement contentPanelElement = $(Schrodinger.byElementAttributeValue("div", "class", "box-body"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String displayValue = contentPanelElement.getAttribute("style");
        if (displayValue != null && displayValue.contains("display: none;")) {
            $(Schrodinger.byElementAttributeValue("button", "data-widget", "collapse"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            contentPanelElement.shouldBe(Condition.attribute("style", null), MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        return this;
    }

    public Table<ChangePasswordPanel<T>> accountsTable() {
        return new Table<>(this, $(Schrodinger.byDataId("accounts")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public ChangePasswordPanel<T> assertPasswordPropagationResultSuccess(String userName) {
        assertion.assertTrue(accountsTable()
                .rowByColumnLabel("Name", userName)
                    .getColumnCellElementByColumnName("Propagation result")
                        .$x(".//i[@data-s-id='basicIcon'][contains(@class,\"fa-check-circle\") and contains(@class, \"text-success\")]")
                        .isDisplayed(), "Propagation result is not successfull, ");
        return this;
    }

    public ChangePasswordPanel clickAccountCheckboxIconByResourceValue(String resourceValue) {
        Table<ChangePasswordPanel> accountsTable = new Table<>(this, $(Schrodinger.byDataId("accounts"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        TableRow row = accountsTable.rowByColumnResourceKey("ChangePasswordPanel.resourceName", resourceValue);
        row.clickColumnByName("Name", "i");
        return this;
    }
}
