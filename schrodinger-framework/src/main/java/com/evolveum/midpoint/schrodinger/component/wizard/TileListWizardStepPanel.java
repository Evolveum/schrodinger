/*
 * Copyright (c) 2023. Evolveum
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

package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class TileListWizardStepPanel<W extends WizardPage> extends WizardStepPanel<W> {

    public TileListWizardStepPanel(W parent) {
        super(parent);
    }

    public WizardStepPanel selectTileByNumber(int tileNumber) {
        Utils.waitForAjaxCallFinish();
        findTileByNumber(tileNumber).click();
        clickNextButton();
        return this;
    }

    public WizardStepPanel selectTileByLabel(String tileLabel) {
        findTileByLabel(tileLabel).click();
        Utils.waitForAjaxCallFinish();
        clickNextButton();
        return this;
    }

    public SelenideElement findTileByNumber(int tileNumber) {
        ElementsCollection collection = getStepPanelContentElement().$$x(".//div[@data-s-id='tile']");
        if (collection.size() >= tileNumber) {
            return collection.get(tileNumber - 1);
        }
        return null;
    }

    public SelenideElement findTileByLabel(String tileLabel) {
        return $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue(
                "div", "data-s-id", "tile", "data-s-id", "title", tileLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }
}
