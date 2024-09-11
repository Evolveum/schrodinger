/*
 * Copyright (c) 2024 Evolveum
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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithTableAndPrismView;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ObjectCollectionViewsPanel extends PanelWithTableAndPrismView<SystemPage> {

    public ObjectCollectionViewsPanel(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ObjectCollectionViewDetailsPanel clickAddButton() {
        table().getButtonToolbar().$x(".//i[contains(@class, \"fa fa-plus\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new ObjectCollectionViewDetailsPanel(ObjectCollectionViewsPanel.this, getParentElement());
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return getParentElement().$x(".//div[@class='card-body']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
