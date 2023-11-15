/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.simulation;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

public class ProcessedObjectsTable<T> extends TableWithPageRedirect<T> {

    public ProcessedObjectsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public SimulationResultDetailsPage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "title", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new SimulationResultDetailsPage().waitForDetailsPanelToBeDisplayed();
    }

    @Override
    public ProcessedObjectsTable<T> selectCheckboxByName(String name) {
        return ProcessedObjectsTable.this;
    }

    @Override
    protected TableHeaderDropDownMenu<ProcessedObjectsTable<T>> clickHeaderActionDropDown() {
        return null;
    }

    @Override
    public Search<ProcessedObjectsTable<T>> search() {
        SelenideElement searchElement = this.getParentElement().$(By.cssSelector(".search-panel-form"));
        return new Search<>(this, searchElement);
    }

    @Override
    public ProcessedObjectsTable<T> assertTableContainsText (String text) {
        super.assertTableContainsText(text);
        return ProcessedObjectsTable.this;
    }

    @Override
    public ProcessedObjectsTable<T> assertTableContainsColumnWithValue(String columnResourceKey, String value) {
        super.assertTableContainsColumnWithValue(columnResourceKey, value);
        return ProcessedObjectsTable.this;
    }
}
