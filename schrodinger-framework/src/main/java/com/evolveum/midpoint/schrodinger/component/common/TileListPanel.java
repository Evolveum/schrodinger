/*
 * Copyright (c) 2025 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class TileListPanel<T> extends Component<T, TileListPanel<T>> {
    public TileListPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TileListPanel<T> selectTileByNumber(int tileNumber) {
        Utils.waitForAjaxCallFinish();
        SelenideElement tile = findTileByNumber(tileNumber);
        String tileCss = tile.getAttribute("class");
        if (tileCss == null || !tileCss.contains("active")) {
            tile.click();
            Utils.waitForAjaxCallFinish();
            if (tile.exists() && tile.isDisplayed()) {
                tile.shouldHave(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
            }
        }

        return this;
    }

    public TileListPanel<T> selectTileByLabelAndMoveToNext(String tileLabel) {
        selectTileByLabel(tileLabel);
        return this;
    }

    public TileListPanel<T> selectTileByLabel(String tileLabel) {
        SelenideElement tile = findTileByLabel(tileLabel);
        String tileCss = tile.getAttribute("class");
        if (tileCss != null && tileCss.contains("active")) {
            return this;
        }
        tile.click();
        Utils.waitForAjaxCallFinish();
        if (tile.exists() && tile.isDisplayed()) {
            tile.shouldHave(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return this;
    }

    public SelenideElement findTileByNumber(int tileNumber) {
        ElementsCollection collection = getParentElement().$$x(".//div[@data-s-id='tile']");
        if (collection.size() >= tileNumber) {
            return collection.get(tileNumber - 1);
        }
        return null;
    }

    protected int countTilesNumber() {
        ElementsCollection collection = getParentElement().$$x(".//div[@data-s-id='tile']");
        return collection.size();
    }


    public SelenideElement findTileByCssClass(int cssClass) {
        return $x(".//div[@data-s-id='tile' and contains(@class, '" + cssClass + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public SelenideElement findTileByKey(String tileLabelKey) {
        return findTileByLabel(Utils.translate(tileLabelKey));
    }

    public SelenideElement findTileByLabel(String tileLabel) {
        return Utils.findTileElementByTitle(tileLabel);
    }

    public TileListPanel<T> assertTilesCountEqual(int expectedValue) {
        int tilesCount = countTilesNumber();
        assertion.assertEquals(countTilesNumber(), expectedValue,
                "Tiles components number doesn't equal the expected value. Expected: "
                        + expectedValue + ", actual value: " + tilesCount);
        return TileListPanel.this;
    }

}
