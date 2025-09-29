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
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;

public class ShoppingCartSummaryPanel extends PrismForm<ShoppingCartItemsTable> {

    public ShoppingCartSummaryPanel(ShoppingCartItemsTable parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ShoppingCartItemsTable clickSaveButton() {
        getParentElement()
                .$x(".//a[@data-s-id='save']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }

    public ShoppingCartItemsTable clickCloseButton() {
        getParentElement()
                .$x(".//a[@data-s-id='close']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }

    @Override
    public ShoppingCartSummaryPanel addAttributeValue(String name, String value) {
        super.addAttributeValue(name, value);
        return this;
    }

    @Override
    public ShoppingCartSummaryPanel assertPropertyInputValue(String attributeName, String expectedValue) {
        super.assertPropertyInputValue(attributeName, expectedValue);
        return this;
    }
}
