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

public class VisualizationItemsPanel<T> extends Component<T> {

    public VisualizationItemsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public int getVisualizationItemsCount() {
        ElementsCollection col = getItemsElementList();
        return CollectionUtils.isEmpty(col) ? 0 : col.size();
    }

    public boolean itemLineExists(String itemName, String itemValue) {
        ElementsCollection items = getItemsElementList();
        for (SelenideElement item : items) {
            SelenideElement itemNameEl = item.$(Schrodinger.byDataId("name"));
            SelenideElement itemValueEl = item.$(Schrodinger.byDataId("newValueContainer"))
                    .$(Schrodinger.byDataId("label"));
            String itemNameRealValue = itemNameEl.exists() ? itemNameEl.getValue() : null;
            String itemValueRealValue = itemValueEl.exists() ? itemValueEl.getValue() : null;
            boolean match = StringUtils.equals(itemName, itemNameRealValue)
                    && StringUtils.equals(itemValue, itemValueRealValue);
            if (match) {
                return true;
            }
        }
        return false;
    }

    public boolean itemLineDoesntExist(String itemName) {
        ElementsCollection items = getItemsElementList();
        for (SelenideElement item : items) {
            SelenideElement itemNameEl = item.$(Schrodinger.byDataId("name"));
            if (itemNameEl.exists() && itemNameEl.isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    private ElementsCollection getItemsElementList() {
        return getParentElement().$$(Schrodinger.byDataId("itemLine"));
    }
}
