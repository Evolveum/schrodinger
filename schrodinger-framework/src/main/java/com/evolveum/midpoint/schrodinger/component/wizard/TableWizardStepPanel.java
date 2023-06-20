package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class TableWizardStepPanel<T> extends WizardStepPanel<T> {

    public TableWizardStepPanel(T parent) {
        super(parent);
    }

    protected Table<TableWizardStepPanel<T>> table() {
        return new Table<>(TableWizardStepPanel.this,
                $(Schrodinger.byDataId("table")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

}
