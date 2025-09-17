/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.self.accessrequest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.SelectableRowTable;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.Selenide.$;

public class RoleCatalogItemsTable<T, RCT extends RoleCatalogItemsTable<T, RCT>> extends SelectableRowTable<T, RCT> {

    public RoleCatalogItemsTable(T parent) {
        super(parent, $(Schrodinger.byDataId("tilesTable")));
    }

    public RoleCatalogItemsTable<T, RCT> addAll() {
        getButtonToolbar().$x(".//a[@data-s-id='addAll']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return RoleCatalogItemsTable.this;
    }

   public RoleCatalogItemsTable<T, RCT> addSelectedToCart() {
        getButtonToolbar().$x(".//a[@data-s-id='addSelected']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return RoleCatalogItemsTable.this;
    }

    public RoleCatalogItemsTable<T, RCT> assertShoppingCartCountEqualsTableItemsCount() {
        SelenideElement shoppingCart = $(Schrodinger.byDataId("mainHeader"))
                .$x(".//span[@data-s-id='cartCount']");
        String rowsCount = String.valueOf(rowsCount());
        assertion.assertTrue(shoppingCart.exists() && shoppingCart.isDisplayed() &&
                        StringUtils.equals(rowsCount, shoppingCart.getText()),
                "Shopping cart items count doesn't equal to expected value (" + rowsCount + ")");
        return RoleCatalogItemsTable.this;
    }

}
