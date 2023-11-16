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
package com.evolveum.midpoint.schrodinger.component.configuration;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithTableAndPrismView;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * @author skublik
 */

public class ObjectPolicyPanel extends PanelWithTableAndPrismView<SystemPage> {

    public ObjectPolicyPanel(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public PrismFormWithActionButtons<ObjectPolicyPanel> clickAddObjectPolicy() {
        SelenideElement plusButton = $(Schrodinger.byElementAttributeValue("i", "class", "fa fa-plus"));
        plusButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        plusButton.shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);
        SelenideElement prismElement = $(Schrodinger.byDataId("div", "itemDetails"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new PrismFormWithActionButtons(this, prismElement);
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return $(Schrodinger.byDataId("div", "itemDetails"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
