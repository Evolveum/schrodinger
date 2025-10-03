/*
 * Copyright (c) 2025. Evolveum
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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class ShoppingCartItemsTable extends Table<ShoppingCartStepPanel, ShoppingCartItemsTable> {

    public ShoppingCartItemsTable(ShoppingCartStepPanel parent) {
        super(parent, $(Schrodinger.byDataId("table", "table")));
    }

    public ShoppingCartSummaryPanel editShoppingCartItem(String nameColumnValue) {
        clickEditButton(nameColumnValue);
        Selenide.screenshot("shoppingCartDetails" + System.currentTimeMillis());
        return new ShoppingCartSummaryPanel(ShoppingCartItemsTable.this, getPrismFormElement());
    }

    private void clickEditButton(String nameColumnValue) {
        Utils.waitForAjaxCallFinish();
        $(Schrodinger.byDataId("tableContainer")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        findRowByColumnLabelAndRowValue("Access name", nameColumnValue, true)
                .getInlineMenu().clickInlineMenuButtonByIconClass(".fas.fa-pen-to-square");
    }

    private SelenideElement getPrismFormElement() {
        return Utils.getModalWindowSelenideElement()
                .$x(".//div[contains(@class, 'modal-content')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
