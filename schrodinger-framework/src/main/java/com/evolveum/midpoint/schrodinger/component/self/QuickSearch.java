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
package com.evolveum.midpoint.schrodinger.component.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/10/2018.
 */
public class QuickSearch<T> extends Component<T> {
    public QuickSearch(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public QuickSearch<T> inputValue(String name) {
        $(Schrodinger.byElementAttributeValue("input", "name", "searchInput")).setValue(name);

        return this;
    }

    //TODO rethink
    public Table clickSearch() {
        $(Schrodinger.byElementAttributeValue("button", "data-s-id", "searchButton"))
                .click();

        return new Table("null", null);
    }

    public QuickSearchDropDown<QuickSearch<T>> clickSearchFor() {
        $(Schrodinger.bySelfOrDescendantElementAttributeValue("button", "data-toggle", "dropdown", "class", "sr-only"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement dropDown = $(Schrodinger.byElementAttributeValue("ul", "role", "menu"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new QuickSearchDropDown<>(this, dropDown);
    }
}
