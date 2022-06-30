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
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class ScenePanel<T> extends Component<T> {

    public ScenePanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ScenePanel<T> assertExpanded() {
        SelenideElement minimizeButton = getParentElement().$x(".//a[@data-s-id='minimizeButton']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement icon = minimizeButton.$(By.tagName("i"));
        assertion.assertTrue(icon.has(Condition.cssClass("fa-chevron-down")), "Primary deltas should be expanded.");
        return this;
    }

    public ScenePanel<T> expandScenePanel() {
        SelenideElement minimizeButton = getParentElement().$x(".//a[@data-s-id='minimizeButton']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement icon = minimizeButton.$(By.tagName("i"));
        if (!icon.has(Condition.cssClass("fa-chevron-down"))) {
            minimizeButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        }
        return this;
    }

    public List<ScenePanel> objectDeltas() {
        ElementsCollection collection = $(Schrodinger.byDataId("body")).$$(Schrodinger.byDataId("partialScene"));
        List<ScenePanel> partialScenes = new ArrayList<>(collection.size());
        collection.forEach(element -> partialScenes.add(new ScenePanel<>(this, element)));
        return partialScenes;
    }

    public PartialSceneHeader header() {
        SelenideElement element = $(Schrodinger.byDataId("headerPanel"));
        return new PartialSceneHeader(this, element);
    }

    public ScenePanel<T> assertDeltasSizeEquals(int expectedSize) {
        assertion.assertEquals(expectedSize, objectDeltas().size());
        return this;
    }


}
