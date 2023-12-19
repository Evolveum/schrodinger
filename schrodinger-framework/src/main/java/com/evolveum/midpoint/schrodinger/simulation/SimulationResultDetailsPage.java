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

package com.evolveum.midpoint.schrodinger.simulation;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.prism.show.VisualizationPanel;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;

public class SimulationResultDetailsPage extends BasicPage {

    public SimulationResultDetailsPage waitForDetailsPanelToBeDisplayed() {
        try {
            $x(".//div[@data-s-id='details' and contains(@class, 'card')]")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        } catch (Exception e) {

        }
        return SimulationResultDetailsPage.this;
    }

    public VisualizationPanel<SimulationResultDetailsPage> changes() {
        SelenideElement changesElement = $(Schrodinger.byDataId("div", "changes"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new VisualizationPanel<>(SimulationResultDetailsPage.this, changesElement);
    }

    public SimulationTaskDetailsTable simulationTaskDetails() {
        SelenideElement taskDetailsElement = $(Schrodinger.byDataId("div", "details"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new SimulationTaskDetailsTable(this, taskDetailsElement);
    }

    public SimulationResultDetailsPage assertMarkValueEquals(String markName, int expectedCount) {
        SelenideElement markTile = getMarkTileElement(markName);
        String markValue = markTile.$(Schrodinger.byDataId("h3", "value"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .getText();
        assertion.assertEquals(markValue, String.valueOf(expectedCount), "Mark value doesn't match");
        return this;
    }

    public ProcessedObjectsPage selectMark(String markName) {
        SelenideElement markTile = getMarkTileElement(markName);
        markTile.$(Schrodinger.byDataId("a", "moreInfo"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new ProcessedObjectsPage();
    }

    private SelenideElement getMarkTileElement(String markName) {
        ElementsCollection marks = $$(Schrodinger.byDataId("div", "markName"));
        return marks.asFixedIterable().stream()
                .filter(mark -> mark.$x(".//span[@title='" + markName + "']").isDisplayed())
                .findFirst()
                .orElse(null);
    }

    public ProcessedObjectsPage back() {
        $(Schrodinger.byDataId("a", "back")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ProcessedObjectsPage();
    }


}
