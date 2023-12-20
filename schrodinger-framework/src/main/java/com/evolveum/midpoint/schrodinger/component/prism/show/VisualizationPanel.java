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

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class VisualizationPanel<T> extends Component<T, VisualizationPanel<T>> {

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

    private VisualizationItemsPanel<VisualizationPanel<T>> getObjectItemsDeltaPanel() {
        return getObjectItemsDeltaPanel(getParentElement());
    }

    private VisualizationItemsPanel<VisualizationPanel<T>> getObjectItemsDeltaPanel(String containerName) {
        SelenideElement container = getPartialVisualizationPanelForContainer(containerName);
        if (container == null) {
            return null;
        }
        return getObjectItemsDeltaPanel(container);
    }

    private VisualizationItemsPanel<VisualizationPanel<T>> getObjectItemsDeltaPanel(SelenideElement parentElement) {
        return new VisualizationItemsPanel<>(VisualizationPanel.this, parentElement.$(Schrodinger.byDataId("itemsTable")));
    }

    public PartialVisualizationHeader<VisualizationPanel<T>> header() {
        SelenideElement element = $(Schrodinger.byDataId("headerPanel"));
        return new PartialVisualizationHeader<>(this, element);
    }

    public VisualizationPanel<T> assertItemsDeltasSizeEquals(int expectedSize) {
        assertion.assertEquals(expectedSize, getObjectItemsDeltaPanel().getVisualizationItemsCount());
        return this;
    }

    public VisualizationPanel<T> assertItemsDeltasSizeEquals(String containerName, int expectedSize) {
        assertion.assertEquals(expectedSize, getObjectItemsDeltaPanel(containerName).getVisualizationItemsCount());
        return this;
    }

    public VisualizationPanel<T> assertObjectNameValueEquals(String objectNameValue) {
        SelenideElement el = $x(".//span[@data-s-id='objectName' and contains(., '" + objectNameValue + "')]");
        assertion.assertTrue(el.isDisplayed(), "Object name should be displayed, ");
        assertion.assertEquals(el.getText(), objectNameValue, "Object name should match with expected value " + objectNameValue + ", ");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertItemValueEquals(String itemName, String itemValue) {
        return assertItemValueEquals(null, itemName, itemValue);
    }

    public VisualizationPanel<T> assertItemValueEquals(String containerName, String itemName, String itemValue) {
        assertion.assertTrue(getObjectItemsDeltaPanel(containerName).itemLineExists(itemName, itemValue),
                "Item '" + itemName + "' value doesn't match, expected " + itemValue);
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertNewValueExists(String itemValue) {
        assertion.assertTrue(getObjectItemsDeltaPanel().newItemValueExists(itemValue),
                "New item value '" + itemValue + "' isn't present in the Changes panel.");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertNewValueExists(String containerName, String itemValue) {
        assertion.assertTrue(getObjectItemsDeltaPanel(containerName).newItemValueExists(itemValue),
                "New item value '" + itemValue + "' isn't present in the Changes panel.");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertItemExists(String itemName) {
        assertion.assertTrue(getObjectItemsDeltaPanel().itemExists(itemName),
                "Item '" + itemName + "' should be present in the Changes panel.");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertItemExists(String containerName, String itemName) {
        assertion.assertTrue(getObjectItemsDeltaPanel(containerName).itemExists(itemName),
                "Item '" + itemName + "' should be present in the Changes panel.");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> assertItemValueNotPresent(String itemName) {
        return assertItemValueNotPresent(getParentElement(), itemName);
    }

    public VisualizationPanel<T> assertItemValueNotPresent(String containerName, String itemName) {
        SelenideElement container = getPartialVisualizationPanelForContainer(containerName);
        return assertItemValueNotPresent(container, itemName);
    }

    private VisualizationPanel<T> assertItemValueNotPresent(SelenideElement parentElement, String itemName) {
        assertion.assertTrue(getObjectItemsDeltaPanel(parentElement).itemLineDoesntExist(itemName),
                "Item '" + itemName + "' value shouldn't exists.");
        return VisualizationPanel.this;
    }

    public VisualizationPanel<T> simpleView() {
        getParentElement().$x(".//div[contains(@class, 'card-header')]").$(byText("Simple"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public VisualizationPanel<T> advancedView() {
        getParentElement().$x(".//div[contains(@class, 'card-header')]").$(byText("Advanced"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public VisualizationPanel<T> showOperationalItems() {
        return showOperationalItems(null);
    }
    public VisualizationPanel<T> showOperationalItems(String containerName) {
        SelenideElement parentElement = containerName == null ?
                getParentElement() : getPartialVisualizationPanelForContainer(containerName);
        parentElement.$(Schrodinger.byDataId("a", "showOperationalItemsLink"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    private SelenideElement getPartialVisualizationPanelForContainer(String containerName) {
        ElementsCollection partialVisualizations = getParentElement()
                .$$x(".//div[@data-s-id='partialVisualization']");
        return partialVisualizations.asFixedIterable().stream()
                .filter(partialVisualization -> partialVisualization
                        .$x(".//span[@data-s-id='nameLink' and contains(text(), '" + containerName + "')]")
                        .isDisplayed())
                .findFirst()
                .orElse(null);
    }

}
