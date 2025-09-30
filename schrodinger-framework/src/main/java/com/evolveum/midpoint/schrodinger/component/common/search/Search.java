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
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Search<T> extends Component<T, Search<T>> {

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
        return new ReferenceSearchItemPanel<>(this, getItemSearchElement(itemName, addIfAbsent));
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
        Utils.scrollToElement(simpleSearchButton);
        simpleSearchButton.click();
        Utils.waitForAjaxCallFinish();
//        Actions builder = new Actions(WebDriverRunner.getWebDriver());
//        builder.moveToElement(simpleSearchButton, 5, 5).click().build().perform();
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
        String basicKey = "SearchBoxModeType.BASIC";
        return searchButton.getText() != null && searchButton.getText().equals(Utils.translate(basicKey));
    }

    private boolean isAdvancedSearch() {
        SelenideElement searchButton = getParentElement().find(Schrodinger.byDataId("span", "searchButtonLabel"));
        String basicKey = "SearchBoxModeType.AXIOM_QUERY";
        SelenideElement axiomInput = getParentElement()
                .$(Schrodinger.byDataId("input", "axiomQueryField"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return searchButton.getText().equals(Utils.translate(basicKey)) && axiomInput.exists() && axiomInput.isDisplayed();
    }

    public Search<T> assertAdvancedSearchIsSelected() {
        assertion.assertTrue(isAdvancedSearch(), "Advanced search should be selected.");
        return Search.this;
    }

    private Search<T> selectSearchMode(String searchMode) {
        getParentElement()
                .$x(".//span[@data-s-id='searchButtonLabel' and contains(text(), '" + searchMode + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return Search.this;
    }

    private void clickDroDownForSearchMode() {
        Utils.waitForAjaxCallFinish();
        SelenideElement dropDownButton = getParentElement()
                .$x(".//div[@"+Schrodinger.DATA_S_ID+"='searchButtonPanel']")
                .$x(".//button[@data-toggle='dropdown']");
        dropDownButton.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        dropDownButton.shouldBe(Condition.attribute("aria-expanded", "true"), MidPoint.TIMEOUT_LONG_20_S);
    }

    public Search<T> fullText() {
        return fullText("");
    }

    public Search<T> basic() {
        choiceBasicSearch();
        return this;
    }

    public Search<T> fullText(String valueToSearch) {
        selectSearchType("SearchBoxModeType.FULLTEXT");

        // we assume fulltext is enabled in systemconfig, else error is thrown here:
        SelenideElement fullTextField = getParentElement().$(Schrodinger.byDataId("input", "fullTextField")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        if (StringUtils.isNotEmpty(valueToSearch)) {
            fullTextField.setValue(valueToSearch);
        }
        return Search.this;
    }

    public Search<T> advanced() {
        return advanced("");
    }

    public Search<T> advanced(String valueToSearch) {
        selectSearchType("SearchBoxModeType.AXIOM_QUERY");

        if (StringUtils.isNotEmpty(valueToSearch)) {
            SelenideElement fullTextField = getParentElement().$(Schrodinger.byDataId("input", "axiomQueryField")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            fullTextField.setValue(valueToSearch);
        }
        return Search.this;
    }


    private void selectSearchType(String resourceKey) {
        clickDroDownForSearchMode();
        try {
            SelenideElement searchTypeSelection = getParentElement().$x(".//*[@data-s-resource-key='" + resourceKey + "']");
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
        SelenideElement link = findSearchItemByNameLinkInMorePopover(popover, name);
        Utils.scrollToElement(link);
        link
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public Search<T> addSearchItemByAddButtonClick(String name) {
        SelenideElement popover = getMorePopover();
        SelenideElement link = findSearchItemByNameLinkInMorePopover(popover, name);
        if (link == null) {
            return this;
        }
        link
                .parent()
                .parent()
                .$(By.tagName("input"))
                .click();
        Utils.waitForAjaxCallFinish();
        popover.$x(".//a[contains(@class, 'btn-success')]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click(); //click Add button
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        Utils.waitForAjaxCallFinish();
        popover.shouldBe(Condition.hidden, MidPoint.TIMEOUT_MEDIUM_6_S);
        return this;
    }

    private SelenideElement findSearchItemByNameLinkInMorePopover(SelenideElement popover, String name) {
        ElementsCollection links = popover.$$x(".//span[@data-s-id='itemName' and contains(text(), '" + name + "')]");
        SelenideElement link = links
                .asFixedIterable()
                .stream()
                .filter(l -> l.getText().equals(name))
                .findFirst()
                .orElse(null);
        if (link != null) {
            Utils.scrollToElement(link);
        }
        return link;
    }

    private SelenideElement getMorePopover() {
        choiceBasicSearch();
        getMoreDropdownButtonElement()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return getDisplayedPopover();
    }

    public SelenideElement getItemByName(String name) {
        ElementsCollection items = getParentElement()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .findAll(Schrodinger.byDataId("searchItemContainer"));
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

    public Search<T> selectCustomFiler(String filterName) {
        SelenideElement searchItemElement = getItemSearchElement(filterName, false);
        SelenideElement checkBox = searchItemElement.$(Schrodinger.byDataId("checkDisable")).$(By.tagName("input"));
        if (checkBox.isDisplayed() && checkBox.is(Condition.checked)) {
            return this;
        }
        checkBox.click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public Search<T> resetBasicSearch() {
        choiceBasicSearch();
        Utils.waitForAjaxCallFinish();
        while (!getParentElement().findAll(Schrodinger.byDataId("removeButton")).isEmpty()) {
            getParentElement().$(Schrodinger.byDataId("removeButton"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                    .click();
            Utils.waitForAjaxCallFinish();
        }
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
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

    private SelenideElement getMoreDropdownButtonElement() {
        Utils.waitForAjaxCallFinish();
        return getParentElement().find(Schrodinger.byDataId("button", "more"));
    }

    public Search<T> assertMoreDropDownButtonVisible() {
        assertion.assertTrue(getMoreDropdownButtonElement().isDisplayed(),
                "More dropdown button should be visible.");
        return this;
    }

    public Search<T> assertMoreDropDownButtonNotVisible() {
        assertion.assertFalse(getMoreDropdownButtonElement().isDisplayed(),
                "More dropdown button should not be visible.");
        return this;
    }

    public Search<T> assertSearchItemExists(String name) {
        assertion.assertNotNull(getItemByName(name), "Search item with name '" + name + "' don't exists.");
        return this;
    }

    public Search<T> assertSearchItemDoesntExist(String name) {
        assertion.assertNull(getItemByName(name), "Search item with name '" + name + "' exists.");
        return this;
    }

    public Search<T> assertHelpTextOfSearchItem(String name, String expectedHelpText) {
        assertion.assertTrue(getItemByName(name).$x(".//i[@"+ Schrodinger.DATA_S_ID +"='help']")
                        .has(Condition.attribute("title", expectedHelpText)),
                "Search item with name '" + name + "' don't contains help text '" + expectedHelpText + "'");
        return this;
    }

    public Search<T> assertActualOptionOfSelectSearchItem(String name, String expectedOption) {
        assertion.assertTrue(getItemByName(name).$x("./div[@" + Schrodinger.DATA_S_ID + "='searchItemField']").$x("./select[@" + Schrodinger.DATA_S_ID + "='input']")
                .$x("./option[@selected='selected']").has(Condition.text(expectedOption)), "Search item with name '" + name + "' don't contains option '" + expectedOption + "'");
        return this;
    }

    public Search<T> assertFulltextSearchIsDisplayed() {
        SelenideElement fullTextField = getParentElement().$(Schrodinger.byDataId("input", "fullTextField")).shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        assertion.assertTrue(fullTextField.exists() && fullTextField.isDisplayed(), "Fulltext search is not displayed.");
        return Search.this;
    }
}

