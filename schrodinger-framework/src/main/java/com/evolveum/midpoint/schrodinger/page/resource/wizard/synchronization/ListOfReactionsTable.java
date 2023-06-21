/*
 * Copyright (c) 2023 Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.EditableRowTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

public class ListOfReactionsTable<T> extends EditableRowTable<T> {

    public ListOfReactionsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ListOfReactionsTable<T> name(String value) {
        return name(value, rowsCount());
    }

    public ListOfReactionsTable<T> name(String value, int rowIndex) {
        setInputValue("Name", value, rowIndex);
        return ListOfReactionsTable.this;
    }

    public ListOfReactionsTable<T> situation(String value) {
        return situation(value, rowsCount());
    }

    public ListOfReactionsTable<T> situation(String value, int rowIndex) {
        setDropdownValue("Situation", value, rowIndex);
        return ListOfReactionsTable.this;
    }

    public ListOfReactionsTable<T> action(String value) {
        return action(value, rowsCount());
    }

    public ListOfReactionsTable<T> action(String value, int rowIndex) {
        setDropdownValue("Action", value, rowIndex);
        return ListOfReactionsTable.this;
    }

    public ListOfReactionsTable<T> switchToDevelopment() {
        return switchToDevelopment(rowsCount());
    }

    public ListOfReactionsTable<T> switchToDevelopment(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToDev"))
                .click();
        return ListOfReactionsTable.this;
    }

    public ListOfReactionsTable<T> switchToProduction() {
        return switchToProduction(rowsCount());
    }

    public ListOfReactionsTable<T> switchToProduction(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToProd"))
                .click();
        return ListOfReactionsTable.this;
    }

}
