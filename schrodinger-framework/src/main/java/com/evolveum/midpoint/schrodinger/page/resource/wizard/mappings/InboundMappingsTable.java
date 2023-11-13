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
import com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization.ListOfReactionsTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

public class InboundMappingsTable<T> extends MappingsTable<T> {

    public InboundMappingsTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public InboundMappingsTable<T> target(String value) {
        return target(value, rowsCount());
    }

    public InboundMappingsTable<T> target(String value, int rowIndex) {
        setInputValue("Target", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> name(String value) {
        return name(value, rowsCount());
    }

    public InboundMappingsTable<T> name(String value, int rowIndex) {
        setInputValue("Name", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> fromResourceAttribute(String value) {
        return fromResourceAttribute(value, rowsCount());
    }

    public InboundMappingsTable<T> fromResourceAttribute(String value, int rowIndex) {
        setInputValue("From resource attribute", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> expression(String value) {
        return expression(value, rowsCount());
    }

    public InboundMappingsTable<T> expression(String value, int rowIndex) {
        setDropdownValue("Expression", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T>  enabled(String value) {
        return enabled(value, rowsCount());
    }

    public InboundMappingsTable<T> enabled(String value, int rowIndex) {
        setDropdownValue("Enabled", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> lifecycleState(String value) {
        return lifecycleState(value, rowsCount());
    }

    public InboundMappingsTable<T> lifecycleState(String value, int rowIndex) {
        setDropdownValue("Lifecycle state", value, rowIndex);
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> switchToDevelopment() {
        return switchToDevelopment(rowsCount());
    }

    public InboundMappingsTable<T> switchToDevelopment(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToDev"))
                .click();
        return InboundMappingsTable.this;
    }

    public InboundMappingsTable<T> switchToProduction() {
        return switchToProduction(rowsCount());
    }

    public InboundMappingsTable<T> switchToProduction(int rowIndex) {
        getTableRow(rowIndex)
                .getParentElement()
                .$(Schrodinger.byDataId("switchToProd"))
                .click();
        return InboundMappingsTable.this;
    }
}
