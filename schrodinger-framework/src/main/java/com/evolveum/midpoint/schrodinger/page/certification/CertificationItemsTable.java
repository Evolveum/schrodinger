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
package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ConfigurableActionConfirmationModal;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.ObjectDetailsPage;
import com.evolveum.midpoint.schrodinger.page.cases.WorkitemPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

/**
 * Created by honchar
 */
public class CertificationItemsTable<T> extends TableWithPageRedirect<T, ObjectDetailsPage, CertificationItemsTable<T>> {

    public CertificationItemsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public ObjectDetailsPage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new ObjectDetailsPage();
    }


    public CertificationItemsTable<T> approveWorkitemByName(String itemName) {
        findRowByColumnLabelAndRowValue("Object", itemName)
                .getParentElement().$x(".//i[contains(@class, 'fa-check')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
        return this;
    }

    public ConfigurableActionConfirmationModal<CertificationItemsTable<T>> approveWorkitemByNameWithConfirmation(String itemName) {
        findRowByColumnLabelAndRowValue("Object", itemName)
                .getParentElement().$x(".//i[contains(@class, 'fa-check')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
        return new ConfigurableActionConfirmationModal<>(CertificationItemsTable.this, Utils.getModalWindowSelenideElement());
    }

    @Override
    public ObjectDetailsPage getObjectDetailsPage() {
        return new ObjectDetailsPage();
    }
}
