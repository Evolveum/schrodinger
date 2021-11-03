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
package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/9/2018.
 */
public class FocusTableWithChoosableElements<T> extends AbstractTable<T> {
    public FocusTableWithChoosableElements(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public PrismForm<AbstractTable<T>> clickByName(String name) {
        return null;
    }

    @Override
    public AbstractTable<T> selectCheckboxByName(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement checkbox = getParentElement()
                .$(Schrodinger.byElementValue("td", "data-s-id", "3", "div", name))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .$x(".//input[@data-s-id='check']");
        checkbox.click();
        checkbox.waitUntil(Condition.checked, MidPoint.TIMEOUT_MEDIUM_6_S);
        Utils.waitForAjaxCallFinish();
        return this;
    }


    private String constructCheckBoxIdBasedOnRow(String row) {
        StringBuilder constructCheckboxName = new StringBuilder("table:box:tableContainer:table:body:rows:")
                .append(row).append(":cells:1:cell:check");

        return constructCheckboxName.toString();
    }

    @Override
    public Search<FocusTableWithChoosableElements<T>> search() {
        SelenideElement searchElement = getParentElement().$x(".//div[contains(@class, \"form-inline\") "
                + "and contains(@class, \"pull-right\") and contains(@class, \"search-form\")]")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new Search<FocusTableWithChoosableElements<T>>(this, searchElement);
    }
}
