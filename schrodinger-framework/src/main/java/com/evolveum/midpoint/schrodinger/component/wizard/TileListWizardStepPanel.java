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
import com.evolveum.midpoint.schrodinger.component.common.TileListPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TileListWizardStepPanel<T> extends WizardStepPanel<T> {

    protected static final String ID_CONTENT_BODY = "choicePanel";

    public TileListWizardStepPanel(T parent) {
        super(parent, $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

     public TileListWizardStepPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public WizardStepPanel<T> selectTileByNumber(int tileNumber) {
        return selectTileByNumber(tileNumber, true);
    }

    public WizardStepPanel<T> selectTileByNumber(int tileNumber, boolean clickNextButton) {
        getTileListPanel().selectTileByNumber(tileNumber);
        if (clickNextButton) {
            clickNext();
        }
        return this;
    }

    public WizardStepPanel<T> selectTileByLabelAndMoveToNext(String tileLabel) {
        selectTileByLabel(tileLabel);
        clickNext();
        return this;
    }

    public WizardStepPanel<T> selectTileByLabel(String tileLabel) {
        getTileListPanel().selectTileByLabel(tileLabel);
        return this;
    }

    public SelenideElement findTileByNumber(int tileNumber) {
        getTileListPanel().findTileByNumber(tileNumber);
        return null;
    }

    public SelenideElement findTileByCssClass(int cssClass) {
        return getTileListPanel().findTileByCssClass(cssClass);
    }

    public SelenideElement findTileByKey(String tileLabelKey) {
        return getTileListPanel().findTileByLabel(Utils.translate(tileLabelKey));
    }

    public SelenideElement findTileByLabel(String tileLabel) {
        return getTileListPanel().findTileByLabel(tileLabel);
    }

    private TileListPanel<TileListWizardStepPanel<T>> getTileListPanel() {
        return new TileListPanel<>(this, getContentPanelElement());
    }

    @Override
    protected SelenideElement getContentPanelElement() {
        return $x(".//div[contains(@class, 'tiles-wizard')]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }
}
