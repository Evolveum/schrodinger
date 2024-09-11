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
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;

public class ObjectCollectionViewDetailsPanel extends TabPanel<ObjectCollectionViewsPanel> {

    public ObjectCollectionViewDetailsPanel(ObjectCollectionViewsPanel parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectBasicPanel() {
        clickTab("Basic");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectDisplayPanel() {
        clickTab("UserInterfaceFeatureType.display");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectSearchBoxConfigurationPanel() {
        clickTab("GuiObjectListViewType.searchBoxConfiguration");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectColumnPanel() {
        clickTab("GuiObjectListViewType.column");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectCollectionPanel() {
        clickTab("GuiObjectListViewType.collection");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectActionPanel() {
        clickTab("GuiObjectListViewType.action");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    public PrismForm<ObjectCollectionViewDetailsPanel> selectAdditionalPanelsPanel() {
        clickTab("GuiObjectListViewType.additionalPanels");
        return new PrismForm<>(this, getPrismViewPanel());
    }

    protected SelenideElement getPrismViewPanel() {
        return getParentElement().$x(".//div[@class='card-body']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

}
