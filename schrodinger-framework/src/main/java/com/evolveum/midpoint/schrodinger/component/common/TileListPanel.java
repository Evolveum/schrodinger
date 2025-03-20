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

}
