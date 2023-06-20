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
