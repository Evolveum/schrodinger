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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.self.AssignmentDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by honchar
 */
public class RequestRoleItemsPanel extends Component<RequestRoleTab> {

    public RequestRoleItemsPanel(RequestRoleTab parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public RequestRoleItemsPanel addItemToCart(String itemName) {
        $(Schrodinger.byElementValue("div", "class", "inner-label", itemName))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent()
                .parent()
                .$(Schrodinger.byElementAttributeValue("span", "class", "shopping-cart-item-button-add"))
                .click();
        return RequestRoleItemsPanel.this;
    }

    public AssignmentDetailsPage clickPropertiesLink(String itemName) {
        $(Schrodinger.byElementValue("div", "class", "inner-label", itemName))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent()
                .parent()
                .$(Schrodinger.byElementAttributeValue("span", "class", "shopping-cart-item-button-details"))
                .click();
        $(byText("Assignment details")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new AssignmentDetailsPage();

    }


    public  RequestRoleItemsPanel assertItemsCountEqual(int expectedCount) {
        ElementsCollection col = $$(Schrodinger.byDataId("itemButtonContainer"));
        int realCount = col != null ? col.size() : 0;
        assertion.assertEquals(expectedCount, realCount, "The count of shopping cart items doesn't match, ");
        return this;
    }
}
