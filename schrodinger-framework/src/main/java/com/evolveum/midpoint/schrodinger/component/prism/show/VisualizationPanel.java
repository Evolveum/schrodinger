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
package com.evolveum.midpoint.schrodinger.component.prism.show;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class VisualizationPanel<T> extends Component<T> {

    public VisualizationPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public VisualizationPanel<T> assertExpanded() {
        SelenideElement minimizeButton = getParentElement().$(Schrodinger.byDataId("a", "minimize")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement icon = minimizeButton.$(By.tagName("i"));
        icon.shouldHave(Condition.cssClass("fa-chevron-down"), MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public VisualizationPanel<T> expandVisualizationPanel() {
        SelenideElement minimizeButton = getParentElement().$x(".//a[@data-s-id='minimize']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement icon = minimizeButton.$(By.tagName("i"));
        if (!icon.has(Condition.cssClass("fa-chevron-down"))) {
            minimizeButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        }
        return this;
    }

    public VisualizationItemsPanel<VisualizationPanel<T>> getObjectItemsDeltaPanel() {
        return new VisualizationItemsPanel<>(VisualizationPanel.this, getParentElement().$(Schrodinger.byDataId("items")));
    }

    @Deprecated
    public List<VisualizationPanel> objectDeltas() {
        ElementsCollection collection = $(Schrodinger.byDataId("body")).$$(Schrodinger.byDataId("partialScene"));
        List<VisualizationPanel> partialPanel = new ArrayList<>(collection.size());
        collection.forEach(element -> partialPanel.add(new VisualizationPanel<>(this, element)));
        return partialPanel;
    }

    public PartialVisualizationHeader header() {
        SelenideElement element = $(Schrodinger.byDataId("headerPanel"));
        return new PartialVisualizationHeader(this, element);
    }

    public VisualizationPanel<T> assertItemsDeltasSizeEquals(int expectedSize) {
        assertion.assertEquals(expectedSize, getObjectItemsDeltaPanel().getVisualizationItemsCount());
        return this;
    }


}
