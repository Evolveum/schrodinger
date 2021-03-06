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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.user.ProjectionsDropDown;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by matus on 5/22/2018.
 */
public class ResourceAccountsTab<T> extends Component<T> {
    public ResourceAccountsTab(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ResourceTaskQuickAccessDropDown<ResourceAccountsTab<T>> importTask() {
        SelenideElement importDiv = $(Schrodinger.byDataId("div", "import"));
        importDiv.waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement dropDownElement = importDiv.lastChild().lastChild()
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceTaskQuickAccessDropDown<>(this, dropDownElement);
    }

    public ResourceTaskQuickAccessDropDown<ResourceAccountsTab<T>> reconciliationTask() {
        SelenideElement reconcileDiv = $(Schrodinger.byDataId("div", "reconciliation"));
        reconcileDiv.waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement dropDownElement = reconcileDiv.lastChild().lastChild()
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceTaskQuickAccessDropDown<>(this, dropDownElement);
    }

    public ResourceTaskQuickAccessDropDown<ResourceAccountsTab<T>> liveSyncTask() {
        $(Schrodinger.byElementValue("label", "data-s-id", "label", "Live Sync"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        ElementsCollection dropDownElement = $$(By.cssSelector(".dropdown-menu.pull-right"));

        SelenideElement concretElement = null;

        for (SelenideElement element : dropDownElement) {
            if (element.isDisplayed()) {
                concretElement = element;
                break;
            }
        }
        return new ResourceTaskQuickAccessDropDown<>(this, concretElement);
    }

    public ResourceAccountsTab<T> clickSearchInRepository() {

        $(Schrodinger.byDataId("a", "repositorySearch"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        $(Schrodinger.byDataId("a", "repositorySearch"))
                .waitUntil(Condition.cssClass("active"), MidPoint.TIMEOUT_LONG_1_M);

        return this;
    }

    public ResourceAccountsTab<T> clickSearchInResource() {
        $(Schrodinger.byDataId("a", "resourceSearch"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        $(Schrodinger.byDataId("a", "resourceSearch"))
                .waitUntil(Condition.cssClass("active"), MidPoint.TIMEOUT_LONG_1_M);
        return this;
    }

    public ResourceShadowTable<ResourceAccountsTab<T>> table() {

        SelenideElement element = $(By.cssSelector(".box.boxed-table"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceShadowTable<>(this, element);
    }

    public void setIntent(String intent) {
        $(Schrodinger.byDataId("div", "intent")).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .$(Schrodinger.byDataId("input", "input"))
                .setValue(intent).sendKeys(Keys.ENTER);
    }

    public ProjectionsDropDown<ResourceAccountsTab<T>> clickHeaderActionDropDown() {

        $(By.tagName("thead"))
                .$(Schrodinger.byDataId("inlineMenuPanel"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();

        SelenideElement dropDownMenu = $(Schrodinger.byElementAttributeValue("ul", "class", "dropdown-menu pull-right"));

        return new ProjectionsDropDown<ResourceAccountsTab<T>>(this, dropDownMenu);
    }

}
