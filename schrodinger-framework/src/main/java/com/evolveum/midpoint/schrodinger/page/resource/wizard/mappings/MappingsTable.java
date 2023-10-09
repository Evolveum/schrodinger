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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.EditableRowTable;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization.ListOfReactionsTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

public class MappingsTable<T> extends EditableRowTable<T> {

    public MappingsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public MappingsTable<T> name(String value) {
        return name(value, rowsCount());
    }

    public MappingsTable<T> name(String value, int rowIndex) {
        setInputValue("Name", value, rowIndex);
        return MappingsTable.this;
    }

    public MappingsTable<T> ref(String value) {
        return ref(value, rowsCount());
    }

    public MappingsTable<T> ref(String value, int rowIndex) {
        setInputValue("Ref", value, rowIndex);
        return MappingsTable.this;
    }

    public MappingsTable<T> expression(String value) {
        return expression(value, rowsCount());
    }

    public MappingsTable<T> expression(String value, int rowIndex) {
        setDropdownValue("Expression", value, rowIndex);
        return MappingsTable.this;
    }

    public MappingsTable<T>  enabled(String value) {
        return enabled(value, rowsCount());
    }

    public MappingsTable<T> enabled(String value, int rowIndex) {
        setDropdownValue("Enabled", value, rowIndex);
        return MappingsTable.this;
    }

    public MappingsTable<T> switchToDevelopment() {
        return switchToDevelopment(rowsCount());
    }

    public MappingsTable<T> switchToDevelopment(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToDev"))
                .click();
        return MappingsTable.this;
    }

    public MappingsTable<T> switchToProduction() {
        return switchToProduction(rowsCount());
    }

    public MappingsTable<T> switchToProduction(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToProd"))
                .click();
        return MappingsTable.this;
    }
}