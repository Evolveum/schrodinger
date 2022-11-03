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

package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.*;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.InputBox;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Search<T> extends Component<T> {

    public Search(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TextInputSearchItemPanel<Search<T>> byName() {
        Utils.waitForAjaxCallFinish();
        SelenideElement nameElement = getItemSearchElement("Name", true);
        SelenideElement nameInput = nameElement.find(Schrodinger.byDataId("input", "input"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new TextInputSearchItemPanel(this, nameInput);
    }

    public TextInputSearchItemPanel<Search<T>> textInputPanelByItemName(String itemName) {
        return textInputPanelByItemName(itemName, true);
    }

    public TextInputSearchItemPanel<Search<T>> textInputPanelByItemName(String itemName, boolean addIfAbsent) {
        return new TextInputSearchItemPanel(this, getItemSearchElement(itemName, addIfAbsent));
    }

    public DropDownSearchItemPanel<Search<T>> dropDownPanelByItemName(String itemName) {
        return dropDownPanelByItemName(itemName, true);
    }

    public DropDownSearchItemPanel<Search<T>> dropDownPanelByItemName(String itemName, boolean addIfAbsent) {
        return new DropDownSearchItemPanel(this, getItemSearchElement(itemName, addIfAbsent));
    }

    public ReferenceSearchItemPanel<Search<T>> referencePanelByItemName(String itemName) {
        return referencePanelByItemName(itemName, true);
    }

    public ReferenceSearchItemPanel<Search<T>> referencePanelByItemName(String itemName, boolean addIfAbsent) {
        return new ReferenceSearchItemPanel(this, getItemSearchElement(itemName, addIfAbsent));
    }

    public DateIntervalSearchItemPanel<Search<T>> dateIntervalPanelByItemName(String itemName) {
        return dateIntervalPanelByItemName(itemName, true);
    }

    public DateIntervalSearchItemPanel<Search<T>> dateIntervalPanelByItemName(String itemName, boolean addIfAbsent) {
        return new DateIntervalSearchItemPanel<>(this, getItemSearchElement(itemName, addIfAbsent));
    }

    public ItemPathSearchItemPanel<Search<T>> itemPathPanelByItemName(String itemName) {
        return itemPathPanelByItemName(itemName, true);
    }

    public ItemPathSearchItemPanel<Search<T>> itemPathPanelByItemName(String itemName, boolean addIfAbsent) {
        return new ItemPathSearchItemPanel<>(this, getItemSearchElement(itemName, addIfAbsent));
    }

    private SelenideElement getItemSearchElement(String itemName, boolean addIfAbsent) {
        choiceBasicSearch();

        SelenideElement itemElement = getItemByName(itemName);
        if (itemElement == null && addIfAbsent){
            addSearchItemByNameLinkClick(itemName);
            Utils.waitForAjaxCallFinish();
            itemElement = getItemByName(itemName);
        }
        return itemElement;
    }

    public Search<T> updateSearch(){
        SelenideElement simpleSearchButton = getParentElement().$x(".//button[@" + Schrodinger.DATA_S_ID + "='searchButton']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        Actions builder = new Actions(WebDriverRunner.getWebDriver());
        builder.moveToElement(simpleSearchButton, 5, 5).click().build().perform();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
        return this;
    }

    private void choiceBasicSearch() {
        if (isBasicSearch()) {
            sleep(1000);
            return;
        }
        selectSearchType("SearchBoxModeType.BASIC");
    }

    private boolean isBasicSearch() {
        SelenideElement searchButton = getParentElement().find(Schrodinger.byDataId("span", "searchButtonLabel"));
        return searchButton.getText() != null && searchButton.getText().equals("Basic");
    }

    private void clickDroDownForSearchMode() {
        Utils.waitForAjaxCallFinish();
        SelenideElement dropDownButton = getParentElement()
                .$x(".//div[@"+Schrodinger.DATA_S_ID+"='searchButtonPanel']")
                .$x(".//button[@data-toggle='dropdown']");
        dropDownButton.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        dropDownButton.shouldBe(Condition.attribute("aria-expanded", "true"), MidPoint.TIMEOUT_LONG_20_S);
    }

    public InputBox<Search<T>> byFullText() {
        selectSearchType("SearchBoxModeType.FULLTEXT");

        // we assume fulltext is enabled in systemconfig, else error is thrown here:
        SelenideElement fullTextField = getParentElement().$(Schrodinger.byDataId("input", "fullTextField")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new InputBox<> (this, fullTextField);
    }

    private void selectSearchType(String resourceKey) {
        clickDroDownForSearchMode();
        try {
            SelenideElement searchTypeSelection = getParentElement().find(Schrodinger.bySchrodingerDataResourceKey(resourceKey)).parent();
            searchTypeSelection.shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
            searchTypeSelection.shouldBe(Condition.disappear, MidPoint.TIMEOUT_MEDIUM_6_S);
            sleep(1000);
        } catch (Throwable t) {
            //TODO how to properly handle?
            t.printStackTrace();
        }

    }

    public Search<T> addSearchItemByNameLinkClick(String name) {
        SelenideElement popover = getMorePopover();
        popover.$(Schrodinger.byElementValue("a", name))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public Search<T> addSearchItemByAddButtonClick(String name) {
        SelenideElement popover = getMorePopover();
        popover.$(Schrodinger.byElementValue("a", name))            //click checkbox next to search attribute
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent().$(By.tagName("input")).click();
        Utils.waitForAjaxCallFinish();
        popover.$x(".//a[contains(@class, 'btn-success')]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click(); //click Add button
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        Utils.waitForAjaxCallFinish();
        popover.shouldBe(Condition.hidden, MidPoint.TIMEOUT_MEDIUM_6_S);
        return this;
    }

    private SelenideElement getMorePopover() {
        choiceBasicSearch();
        getParentElement().find(Schrodinger.byDataId("button", "more"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return getDisplayedPopover();
    }

    public SelenideElement getItemByName(String name) {
        ElementsCollection items = getParentElement().findAll(Schrodinger.byDataId("searchItemContainer"));
        for (SelenideElement item : items) {
            if (item.$(Schrodinger.byElementValue("div", name)).exists()) {
                return item;
            }
        }
        return null;
    }

    private SelenideElement getDisplayedPopover() {
        return $(By.className("search-popover")).shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
    }



    public Search<T> resetBasicSearch() {
        choiceBasicSearch();
        ElementsCollection collection = getParentElement().findAll(Schrodinger.byDataId("removeButton"));
        for (SelenideElement searchPropertyPanel : collection) {
            if (searchPropertyPanel.exists()) {
                searchPropertyPanel.shouldBe(Condition.enabled, MidPoint.TIMEOUT_MEDIUM_6_S);
                searchPropertyPanel.click();
                Utils.waitForAjaxCallFinish();
            }
        }
//        while ($x(".//a[@data-s-id='removeButton']").exists()
//                && $x(".//a[@data-s-id='removeButton']").isDisplayed()) {
//            SelenideElement deleteButton = $x(".//a[@data-s-id='removeButton']");
//            deleteButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
//        }
        return this;
    }

    public Search<T> clearTextSearchItemByNameAndUpdate(String itemName) {
        choiceBasicSearch();
        SelenideElement itemElement = getItemByName(itemName);
        if (itemElement == null){
            return this;
        }
        TextInputSearchItemPanel searchField = new TextInputSearchItemPanel(this, itemElement);
        searchField.inputValue("");
        updateSearch();
        return this;
    }

    public Search<T> assertExistSearchItem(String name) {
        assertion.assertNotNull(getItemByName(name), "Search item with name '" + name + "' don't exists.");
        return this;
    }

    public Search<T> assertDoesntExistSearchItem(String name) {
        assertion.assertNull(getItemByName(name), "Search item with name '" + name + "' exists.");
        return this;
    }

    public Search<T> assertHelpTextOfSearchItem(String name, String expectedHelpText) {
        assertion.assertTrue(getItemByName(name).$x("./i[@"+ Schrodinger.DATA_S_ID +"='help']")
                        .has(Condition.attribute("title", expectedHelpText)),
                "Search item with name '" + name + "' don't contains help text '" + expectedHelpText + "'");
        return this;
    }

    public Search<T> assertActualOptionOfSelectSearchItem(String name, String expectedOption) {
        assertion.assertTrue(getItemByName(name).$x("./div[@" + Schrodinger.DATA_S_ID + "='searchItemField']").$x("./select[@" + Schrodinger.DATA_S_ID + "='input']")
                .$x("./option[@selected='selected']").has(Condition.text(expectedOption)), "Search item with name '" + name + "' don't contains option '" + expectedOption + "'");
        return this;
    }
}

