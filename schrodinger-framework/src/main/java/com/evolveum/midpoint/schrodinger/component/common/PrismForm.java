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

package com.evolveum.midpoint.schrodinger.component.common;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;

import com.codeborne.selenide.*;

import com.evolveum.midpoint.schrodinger.component.DateTimePanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by Viliam Repan (lazyman).
 */
public class PrismForm<T> extends Component<T, PrismForm<T>> {

    private static final String CARET_DOWN_ICON_STYLE = "fa-caret-down";

    public PrismForm(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public PrismForm<T> addAttributeValue(String name, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);

//        getParentElement().$(By.className("prism-properties")).shouldBe(Condition.appear,MidPoint.TIMEOUT_MEDIUM_6_S);

        ElementsCollection values = property.$$x(".//div[contains(@class, \"prism-property-value\")]");
        if (values.size() >= 1) {
            values.first().$x(".//*[@data-s-id='input' and contains(@class,\"form-control\")]").setValue(value);
        }

        // todo implement
        return this;
    }

    public PrismForm<T> removeAttributeValue(String name, String value) {
        SelenideElement property = findProperty(name);
        if (property != null && property.$(Schrodinger.byDataResourceKey("removeButton")).exists()) {
            property.$(Schrodinger.byDataResourceKey("removeButton"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        }
        return this;
    }

    public PrismForm<T> changeAttributeValue(String name, String oldValue, String newValue) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = getParentElement().$(Schrodinger.byDataResourceKey(name));

        getParentElement().$(By.className("prism-properties")).shouldBe(Condition.appear,MidPoint.TIMEOUT_MEDIUM_6_S);

        ElementsCollection values = property.$$x(".//div[contains(@class, \"prism-property-value\")]");
        if (values.size() >= 1) {
            values.first().$x(".//input[contains(@class,\"form-control\")]").setValue(newValue);
        }

        // todo implement
        return this;
    }


    public PrismForm<T> setFileForUploadAsAttributeValue(String name, File file) {
        getParentElement().$(By.cssSelector("input.form-object-value-binary-file-input")).uploadFile(file);

        return this;
    }

    public PrismForm<T> removeFileAsAttributeValue(String name) {
        SelenideElement property = findProperty(name);
        property.$(Schrodinger.byElementAttributeValue("a", "title", "Remove file")).click();

        return this;
    }

    public boolean propertyWithTitleTextExists(String propertyName, String text) {
        return findProperty(propertyName).$x(".//i[contains(@title, '" + text + "')]").exists();
    }

    public PrismForm<T> showEmptyAttributes(String containerName) {
        Utils.waitForAjaxCallFinish();
        SelenideElement showEmptyLink = getPrismContainerPanel(containerName).getContainerFormFragment().getParentElement().$x(".//div[@data-s-id='showEmptyButton']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        if ("Show empty fields".equals(showEmptyLink.getText())) {
            showEmptyLink.click();
            showEmptyLink.shouldBe(Condition.text("Hide empty fields"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return this;
    }

    public Boolean inputAttributeValueEquals(String name, String expectedValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.$(By.xpath(".//input[contains(@class,\"form-control\")]"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        String valueElement = value.getValue();

        if (!valueElement.isEmpty()) {
            return valueElement.equals(expectedValue);
        } else {
            return expectedValue.isEmpty();
        }
    }

    public Boolean inputAttributeValueEmpty(String name) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.$(By.xpath(".//input[contains(@class,\"form-control\")]"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        String valueElement = value.getValue();

        return valueElement == null || valueElement.isEmpty();
    }

    public Boolean inputAttributeValueContains(String name, String expectedPartialValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.parent().$x(".//input[@data-s-id='input']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String valueElement = value.getValue();

        if (!valueElement.isEmpty()) {
            return valueElement.contains(expectedPartialValue);
        } else {
            return expectedPartialValue.isEmpty();
        }
    }

    public Boolean textareaAttributeValueEquals(String name, String expectedValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.parent().$x(".//textarea[@data-s-id='input']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String valueElement = value.getValue();

        if (!valueElement.isEmpty()) {
            return valueElement.equals(expectedValue);
        } else {
            return expectedValue.isEmpty();
        }
    }

    public Boolean textareaAttributeValueContains(String name, String expectedPartialValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.parent().$x(".//textarea[@data-s-id='input']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String valueElement = value.getValue();

        if (!valueElement.isEmpty()) {
            return valueElement.contains(expectedPartialValue);
        } else {
            return expectedPartialValue.isEmpty();
        }
    }

    //seems that the property fields in new container are wrapped to extra parent, that is why we need one extra parent() call
    //needs to be checked
    public Boolean compareInputAttributeValueInNewContainer(String name, String expectedValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.parent().parent().$(By.xpath(".//input[contains(@class,\"form-control\")]"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String valueElement = value.getValue();

        if (!valueElement.isEmpty()) {
            return valueElement.equals(expectedValue);
        } else {
            return expectedValue.isEmpty();
        }

    }

    public Boolean isPropertyEnabled(String name) {
        SelenideElement property = findProperty(name);
        SelenideElement valueElement = property.parent().$(org.openqa.selenium.By.xpath(".//*[contains(@class,\"form-control\")]"));
        return (valueElement.exists() && valueElement.isEnabled());
    }

    public Boolean compareInputAttributeValues(String name, String... expectedValues) {
        return compareInputAttributeValues(name, Arrays.asList(expectedValues));
    }

    public Boolean containsInputAttributeValues(String name, String... expectedValues) {
        return containsInputAttributeValues(name, Arrays.asList(expectedValues));
    }

    public Boolean compareInputAttributeValues(String name, List<String> expectedValues) {
        return compareInputAttributeValues(name, expectedValues, true);
    }

    public Boolean containsInputAttributeValues(String name, List<String> expectedValues) {
        return compareInputAttributeValues(name, expectedValues, false);
    }

    private Boolean compareInputAttributeValues(String name, List<String> expectedValues, boolean strictSameValues) {
        SelenideElement property = findProperty(name);
        ElementsCollection valuesElements = property.parent().$$(By.xpath(".//input[contains(@class,\"form-control\")]"));
        List<String> values = new ArrayList<String>();
        for (SelenideElement valueElement : valuesElements) {
            String value = valueElement.getValue();
            if (!value.isEmpty()) {
                return values.add(value);
            }
        }
        if (!values.isEmpty()) {
            if (strictSameValues) {
                return values.equals(expectedValues);
            }
            return values.containsAll(expectedValues);
        } else {
            return expectedValues.isEmpty();
        }

    }

    public Boolean compareSelectAttributeValue(String name, String expectedValue) {
        SelenideElement property = findProperty(name);
        SelenideElement value = property.$(By.xpath(".//select[contains(@class,\"form-control\")]"));
        String selectedOptionText = value.getSelectedText();

        if (!selectedOptionText.isEmpty()) {
            return selectedOptionText.equals(expectedValue);
        } else {
            return expectedValue.isEmpty();
        }

    }

    public PrismForm<T> assertPropertySelectValue(String attributeName, String expectedValue) {
        assertion.assertTrue(compareSelectAttributeValue(attributeName, expectedValue),"The value of the select attribute " + attributeName
                    + " doesn't match to expected value '" + expectedValue + "'.");
        return this;
    }

    public PrismForm<T> addAttributeValue(QName name, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);

        property.$x(".//*[contains(@class, \"form-control\")]").setValue(value);
        return this;
    }

    public PrismForm<T> setAceEditorAreaValue(String name, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);

        property.$(Schrodinger.byElementAttributeValue("textarea", "class", "ace_text-input"))
                .shouldBe(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S)
                .sendKeys(value);
        return this;
    }

    public PrismForm<T> setPolyStringLocalizedValue(QName name, String locale, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);

        property
            .$(By.className("fa-language"))
            .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
            .click();
        SelenideElement localeDropDown =
            property
            .$(Schrodinger.byDataId("languagesList"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
            .$(By.tagName("select"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        if (localeDropDown != null){
            localeDropDown.selectOption(locale);

            property
                .$(Schrodinger.byDataId("languageEditor"))
                .$(By.className("fa-plus-circle"))
                .shouldBe(Condition.visible)
                .click();
        }

        property.$(Schrodinger.byDataId("translation"))
        .shouldBe(Condition.visible)
        .$(By.className("form-control"))
        .setValue(value);

        return this;
    }

    public PrismForm<T> setDropDownAttributeValue(String name, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);
        return setDropDownAttributeValue(property, value);
    }

    public PrismForm<T> setDropDownAttributeValue(QName name, String value) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);
        return setDropDownAttributeValue(property, value);
    }

    private PrismForm<T> setDropDownAttributeValue(SelenideElement property, String value) {
        Utils.waitForAjaxCallFinish();
        ElementsCollection values = property.$$(By.className("prism-property-value"));
        if (values.size() > 0) {
            SelenideElement dropDown = values.first().$(By.tagName("select"));
            if (dropDown != null){
                dropDown.selectOptionContainingText(value);
            }
        }
        return this;
    }

    public PrismForm<T> setAttributeValue(QName name, String value) {
        // todo implement
        return this;
    }

    public PrismForm<T> removeAttributeValue(QName name, String value) {
        // todo implement
        return this;
    }

    public PrismForm<T> changeAttributeValue(QName name, String oldValue, String newValue) {
        // todo implement
        return this;
    }

    public PrismForm<T> showEmptyAttributes(QName containerName, String value) {
        // todo implement
        return this;
    }

    public PrismForm<T> setFileForUploadAsAttributeValue(QName containerName, File file) {
        // todo implement
        return this;
    }

    public PrismForm<T> removeFileAsAttributeValue(QName containerName) {
        // todo implement
        return this;
    }

    private SelenideElement findPropertyValueInput(String name) {
        Selenide.sleep(5000);

        return  getParentElement().$(Schrodinger.byElementAttributeValue("div", "contains",
                Schrodinger.DATA_S_QNAME, "#" + name)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

    }

    public SelenideElement findProperty(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement element = null;
        try {
            ElementsCollection properties = getParentElement().$$x(".//*[@data-s-id='property']");
            element = properties.stream().filter(e ->
                    e.getAttribute("data-s-qname") != null && e.getAttribute("data-s-qname").contains("#" + name))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            element = getParentElement().$x(".//div[@data-s-resource-key='" + name + "']");
        } finally {
            if (element == null || !element.exists()) {
                element = getParentElement().$(byText(name)).parent().parent();
            }
        }
        return element;
    }

    private SelenideElement findProperty(QName qname) {
        String name = Schrodinger.qnameToString(qname);
        return getParentElement().$(Schrodinger.byDataQName(name));
    }

    public PrismForm<T> selectOption(String attributeName, String option) {
        Utils.waitForAjaxCallFinish();

        SelenideElement property = findProperty(attributeName);

        property.$(By.xpath(".//select[contains(@class,\"form-control\")]"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).selectOption(option);

        return this;
    }

    public PrismForm<T> expandContainerPropertiesPanel(String containerHeaderKey){
        SelenideElement panelHeader = getParentElement().$(Schrodinger.byElementAttributeValue("a", "data-s-resource-key", containerHeaderKey))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent()
                .parent();

        SelenideElement headerChevron = panelHeader.$(By.tagName("i"));
        if (headerChevron.getAttribute("class") != null && !headerChevron.getAttribute("class").contains(CARET_DOWN_ICON_STYLE)) {
            headerChevron.click();
            panelHeader
                    .$(Schrodinger.byElementAttributeValue("i", "class","fa fa-caret-down fa-lg"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        panelHeader
                .parent()
                .$(By.className("prism-properties"))
                .shouldBe(Condition.visible);
        return this;
    }

    public PrismForm<T> addNewContainerValue(String containerHeaderKey, String newContainerHeaderKey){
        SelenideElement panelHeader = getParentElement().$(By.linkText(containerHeaderKey))
                .parent()
                .parent();
        panelHeader.scrollIntoView(false);
        panelHeader.find(By.className("fa-plus-circle"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();

        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());

        SelenideElement newContainerElement = panelHeader
                .parent()
                .parent()
                .$(Schrodinger.byElementValue("a", newContainerHeaderKey));

        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());

        newContainerElement.scrollTo();

        return this;
    }

    public SelenideElement getPrismPropertiesPanel(String containerHeaderKey){
        expandContainerPropertiesPanel(containerHeaderKey);

        SelenideElement containerHeaderPanel = getParentElement().$(Schrodinger.byDataResourceKey("a", containerHeaderKey));
        return containerHeaderPanel
                .parent()
                .parent()
                .parent()
                .find(By.className("prism-properties"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

    }

    public PrismForm<T> collapseAllChildrenContainers(String parentContainerHeraderKey){
        SelenideElement parentContainerPanel = null;
        if  (getParentElement().$(Schrodinger.byElementAttributeValue("a", "data-s-resource-key", parentContainerHeraderKey))
                .is(Condition.exist)) {
            parentContainerPanel = getParentElement().$(Schrodinger.byElementAttributeValue("a", "data-s-resource-key", parentContainerHeraderKey))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        } else {
            parentContainerPanel = getParentElement().$(By.linkText(parentContainerHeraderKey))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        if (parentContainerPanel == null){
            return this;
        }
        parentContainerPanel = parentContainerPanel
                .parent()
                .parent()
                .parent()
                .parent()
                .$(By.className("container-wrapper"))
                .shouldBe(Condition.visible);

        while (parentContainerPanel.findAll(By.className(CARET_DOWN_ICON_STYLE)) != null &&
                        parentContainerPanel.findAll(By.className(CARET_DOWN_ICON_STYLE)).size() > 0){
            SelenideElement childContainerHeaderIcon = parentContainerPanel.find(By.className(CARET_DOWN_ICON_STYLE));
            childContainerHeaderIcon
                    .shouldBe(Condition.visible)
                    .click();
            childContainerHeaderIcon
                    .is(Condition.not(Condition.exist));
        }
        return this;
    }

    public ObjectBrowserModal<PrismForm<T>> editRefValue(String attributeName) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(attributeName);
        property.$x(".//button[@" + Schrodinger.DATA_S_ID + "='edit']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        ObjectBrowserModal objectBrowserModal = new ObjectBrowserModal<>(this, Utils.getModalWindowSelenideElement());

        return objectBrowserModal;
    }

    public PrismContainerPanel<PrismForm<T>> getPrismContainerPanel(String containerName) {
        SelenideElement panel = null;
        panel = getParentElement().$x(".//a[@class='prism-title'][@data-s-resource-key='" +
                        containerName + "' or @data-s-resource-key='" + Utils.translate(containerName) + "']")
                .should(Condition.exist, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement containerPanel = panel
                .parent()
                .parent()
                .parent();
        containerPanel.scrollTo();
        return new PrismContainerPanel<PrismForm<T>>(this, containerPanel);
    }

    public PrismContainerPanel<PrismForm<T>> getPrismContainerPanelByResourceKey(String resourceKey) {
        SelenideElement containerPanel = getParentElement().$(Schrodinger.byDataResourceKey(resourceKey))
                .parent()
                .parent()
                .parent();
        containerPanel.scrollTo();
        return new PrismContainerPanel<PrismForm<T>>(this, containerPanel);
    }

    public PrismForm<T> assertPropertyWithTitleTextExist(String propertyName, String text) {
        assertion.assertTrue(propertyWithTitleTextExists(propertyName, text),
                "Property " + propertyName + " with title text '" + text + "' doesn't exist.");
        return this;
    }

    public PrismForm<T> assertPropertyWithTitleTextDoesntExist(String propertyName, String text) {
        assertion.assertFalse(propertyWithTitleTextExists(propertyName, text),
                "Property " + propertyName + " with title text '" + text + "' shouldn't exist.");
        return this;
    }

    public PrismForm<T> assertPropertyEnabled(String propertyName) {
        assertion.assertTrue(isPropertyEnabled(propertyName), "Property " + propertyName + " should be enabled.");
        return this;
    }

    public PrismForm<T> assertPropertyDisabled(String propertyName) {
        assertion.assertFalse(isPropertyEnabled(propertyName), "Property " + propertyName + " should be disabled.");
        return this;
    }

    public PrismForm<T> assertPropertyInputValue(String attributeName, String expectedValue) {
        assertion.assertTrue(inputAttributeValueEquals(attributeName, expectedValue), "The value of the input attribute " + attributeName
                + " doesn't match to expected value '" + expectedValue + "'.");
        return this;
    }

    public PrismForm<T> assertPropertyIsNotEmpty(String attributeName) {
        assertion.assertTrue(!inputAttributeValueEmpty(attributeName), "The value of the input attribute " + attributeName
                + " should not be empty but it is.");
        return this;
    }

    public PrismForm<T> assertPropertyTextareaValueEquals(String attributeName, String expectedValue) {
        assertion.assertTrue(textareaAttributeValueEquals(attributeName, expectedValue), "The value of the input attribute " + attributeName
                + " doesn't match to expected value '" + expectedValue + "'.");
        return this;
    }

    public PrismForm<T> assertPropertyInputValueContainsText(String attributeName, String partialValue) {
        assertion.assertTrue(inputAttributeValueContains(attributeName, partialValue), "The value of the input attribute " + attributeName
                + " doesn't contain text '" + partialValue + "'.");
        return this;
    }

    public PrismForm<T> assertPropertyTextareaValueContainsText(String attributeName, String partialValue) {
        assertion.assertTrue(textareaAttributeValueContains(attributeName, partialValue), "The value of the input attribute " + attributeName
                + " doesn't contain text '" + partialValue + "'.");
        return this;
    }

    public PrismForm<T> assertPropertyInputValues(String attributeName, String... expectedValues) {
        assertion.assertTrue(compareInputAttributeValues(attributeName, expectedValues), "The values of the input attribute " + attributeName
                + " doesn't match to expected values.");
        return this;
    }

    public PrismForm<T> assertPropertyInputValues(String attributeName, List<String> expectedValues) {
        assertion.assertTrue(compareInputAttributeValues(attributeName, expectedValues), "The values of the input attribute " + attributeName
                + " doesn't match to expected values.");
        return this;
    }

    public PrismForm<T> assertPropertyDropdownValue(String attributeName, String expectedValue) {
        assertion.assertTrue(compareSelectAttributeValue(attributeName, expectedValue), "The value of the dropdown attribute " + attributeName
                + " doesn't match to expected value '" + expectedValue + "'.");
        return this;
    }

    public DateTimePanel<PrismForm<T>> getDatePanel(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement property = findProperty(name);
        return new DateTimePanel<>(this, property);
    }
}
