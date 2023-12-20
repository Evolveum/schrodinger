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

package com.evolveum.midpoint.schrodinger.component.prism.show;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;

public class VisualizationItemsPanel<T> extends Component<T, VisualizationItemsPanel<T>> {

    public VisualizationItemsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public int getVisualizationItemsCount() {
        return getVisualizationItemsCount(getParentElement());
    }

    public int getVisualizationItemsCount(SelenideElement parentElement) {
        ElementsCollection col = getItemsElementList(parentElement);
        return CollectionUtils.isEmpty(col) ? 0 : col.size();
    }

    public boolean itemLineExists(String itemName, String itemValue) {
        return itemLineExists(getParentElement(), itemName, itemValue);
    }

    public boolean itemLineExists(SelenideElement parentElement, String itemName, String itemValue) {
        ElementsCollection items = getItemsElementList(parentElement);
        for (SelenideElement item : items) {
            SelenideElement itemNameEl = item.$(Schrodinger.byDataId("name"));
            SelenideElement itemValueEl = item.$(Schrodinger.byDataId("newValueContainer"))
                    .$(Schrodinger.byDataId("label"));
            if (!itemValueEl.isDisplayed()) {
                itemValueEl = item.$(Schrodinger.byDataId("newValueContainer"))
                        .$(Schrodinger.byDataId("link"));
            }
            String itemNameRealValue = itemNameEl.exists() ? itemNameEl.getText() : null;
            String itemValueRealValue = itemValueEl.exists() ? itemValueEl.getText() : null;
            boolean match = StringUtils.equals(itemName, itemNameRealValue)
                    && StringUtils.equals(itemValue, itemValueRealValue);
            if (match) {
                return true;
            }
        }
        return false;
    }

    public boolean newItemValueExists(String itemValue) {
        return newItemValueExists(getParentElement(), itemValue);
    }

    public boolean newItemValueExists(SelenideElement parentElement, String itemValue) {
        ElementsCollection items = parentElement.$$(Schrodinger.byDataId("newValueContainer"));
        for (SelenideElement item : items) {
            if (item.$(byText(itemValue)).isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    public boolean itemExists(String itemName) {
        return itemExists(getParentElement(), itemName);
    }

    public boolean itemExists(SelenideElement parentElement, String itemName) {
        ElementsCollection items = parentElement.$$(Schrodinger.byDataId("nameContainer"));
        for (SelenideElement item : items) {
            if (item.$(byText(itemName)).isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    public boolean itemLineDoesntExist(String itemName) {
        return itemLineDoesntExist(getParentElement(), itemName);
    }

    public boolean itemLineDoesntExist(SelenideElement parentElement, String itemName) {
        ElementsCollection items = getItemsElementList(parentElement);
        for (SelenideElement item : items) {
            SelenideElement itemNameEl = item.$(Schrodinger.byDataId("name"));
            String text = itemNameEl.getText();
            if (itemName.equals(text)) {
                return false;
            }
        }
        return true;
    }

    private ElementsCollection getItemsElementList(SelenideElement parentElement) {
        return parentElement.$$(Schrodinger.byDataId("itemLine"));
    }
}
