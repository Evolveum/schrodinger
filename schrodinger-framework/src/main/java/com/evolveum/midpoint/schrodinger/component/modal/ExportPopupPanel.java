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
package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class ExportPopupPanel<T> extends ModalBox<T> {

    public ExportPopupPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public Table<ExportPopupPanel<T>> table() {
        return new Table<>(this,
                getParentElement().$(Schrodinger.bySelfOrDescendantElementAttributeValue("div", "data-s-id", "table",
                        "style", "float: left; padding-bottom: 5px;"))
                        .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public ExportPopupPanel<T> setReportName(String reportName) {
        getParentElement().$x(".//div[@data-s-id='name']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(reportName);
        return this;
    }

    public T exportSelectedColumns() {
        $(Schrodinger.byDataId("export")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return getParent();
    }

    public T cancel() {
        $(Schrodinger.byDataId("cancelButton")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return getParent();
    }

}
