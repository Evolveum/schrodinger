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
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/9/2018.
 */
public class FocusTableWithChoosableElements<T> extends AbstractTable<T, FocusTableWithChoosableElements<T>> {
    public FocusTableWithChoosableElements(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public PrismForm<AbstractTable<T, FocusTableWithChoosableElements<T>>> clickByName(String name) {
        return null;
    }

    @Override
    public AbstractTable<T, FocusTableWithChoosableElements<T>> selectCheckboxByName(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement checkbox = getParentElement()
                .$(Schrodinger.byElementValue("td", "data-s-id", "3", "div", name))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .find(Schrodinger.byDataId("input", "check"));
        checkbox.click();
        checkbox.shouldBe(Condition.checked, MidPoint.TIMEOUT_MEDIUM_6_S);
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
        SelenideElement searchElement = getParentElement().find(By.cssSelector(".search-panel-form"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new Search<>(this, searchElement);
    }
}
