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
package com.evolveum.midpoint.schrodinger.component.common.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/22/2018.
 */
public class EditableRowTable<T, P extends EditableRowTable> extends Table<T, P>{
    public EditableRowTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public EditableRowTable<T, P> setInputValue(String columnResourceKey, String attributeValue, int rowIndex){
        SelenideElement element = getTableCellElement(columnResourceKey, rowIndex);
        if (element != null && element.exists()) {
            element.$x(".//input").setValue(attributeValue);
        }
        return EditableRowTable.this;
    }

    public EditableRowTable<T, P> setDropdownValue(String columnResourceKey, String attributeValue, TableRow<?, ?> tableRow){
        int i = -1;
        try {
            i = Integer.parseInt(Objects.requireNonNull(tableRow.getParentElement().getAttribute("data-s-id")));
        } catch (NumberFormatException e) {
            screenshot("setDropdownValueFailed");
        }
        if (i < 0) {
            return null;
        }
        return setDropdownValue(columnResourceKey, attributeValue, i);
    }

    public EditableRowTable<T, P> setDropdownValue(String columnResourceKey, String attributeValue, int rowIndex){
        SelenideElement element = getTableCellElement(columnResourceKey, rowIndex);
        if (element != null && element.exists()) {
            element.$x(".//select").selectOption(attributeValue);
        }
        return EditableRowTable.this;
    }

    public EditableRowTable<T, P> addAttributeValue(String columnName, String attributeValue){
        SelenideElement element = $(Schrodinger.byAncestorPrecedingSiblingDescendantOrSelfElementEnclosedValue("input","type","text",null,null, columnName))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(attributeValue);
        if (element.exists()) {
            element.setValue(attributeValue);
        }
        return this;
    }


    public EditableRowTable<T, P> clickCheckBox(String attributeName){

    $(Schrodinger.byAncestorPrecedingSiblingDescendantOrSelfElementEnclosedValue("input","type","checkbox",null,null,attributeName))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }


}
