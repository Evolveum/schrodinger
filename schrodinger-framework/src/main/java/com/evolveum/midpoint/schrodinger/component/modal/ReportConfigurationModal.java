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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by honchar
 */
public class ReportConfigurationModal<T> extends ModalBox<T>{

    public ReportConfigurationModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T runReport() {
        SelenideElement runReportButton = getParentElement().$(Schrodinger.byDataId("a", "runReport")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        runReportButton.click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return getParent();
    }

    public Table<ReportConfigurationModal, Table> table() {
        return new Table<>(this, getTableBoxElement());
    }

    protected SelenideElement getTableBoxElement(){
        return getParentElement().$x(".//div[@data-s-id='table']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
