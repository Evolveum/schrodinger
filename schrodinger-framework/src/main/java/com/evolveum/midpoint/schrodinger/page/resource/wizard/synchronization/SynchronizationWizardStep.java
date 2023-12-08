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
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SynchronizationWizardStep<T> extends TableWizardStepPanel<T> {

    public SynchronizationWizardStep(T parent) {
        super(parent);
    }

    public ListOfReactionsTable<SynchronizationWizardStep<T>> addReaction() {
        table()
                .getToolbarButtonByTitleKey("SynchronizationReactionTable.newObject")
                .click();
        Utils.waitForAjaxCallFinish();
        return new ListOfReactionsTable<>(SynchronizationWizardStep.this, $(Schrodinger.byDataId("table")));
    }

    public ListOfReactionsTable<SynchronizationWizardStep<T>> addSimpleReaction() {
        table()
                .getToolbarButtonByTitleKey("SynchronizationReactionTable.newObject.simple")
                .click();
        Utils.waitForAjaxCallFinish();
        return new ListOfReactionsTable<>(SynchronizationWizardStep.this, $(Schrodinger.byDataId("table")));
    }

    public T saveSynchronizationSettings() {
        return clickButtonByTitleKeyAndRedirectToParent("SynchronizationReactionTableWizardPanel.saveButton");
    }

    public T exitWizard() {
        return clickButtonByTitleKeyAndRedirectToParent("WizardPanel.exit");
    }

    private T clickButtonByTitleKeyAndRedirectToParent(String titleKey) {
        String titleTranslated = Utils.translate(titleKey);
        $x(".//a[@title=\"" + titleTranslated + "\"]").click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public SynchronizationWizardStep<T> assertLifecycleStateValueEquals(int rowIndex,
                                                                        String lifecycleStateExpectedValue) {
        TableRow<?, ?> tableRow = table().getTableRow(rowIndex);
        SelenideElement cell = tableRow.getColumnCellElementByColumnName("Lifecycle state");
        SelenideElement select = cell.$(By.tagName("select"));
        String actualValue = select.getText();
        assertion.assertEquals(actualValue, lifecycleStateExpectedValue, "Lifecycle state value should be equal " +
                "to: " + lifecycleStateExpectedValue + "; actual value: " + actualValue);
        return this;
    }

    public SynchronizationWizardStep<T> assertAllLifecycleStateValuesEqual(String lifecycleStateExpectedValue) {
        int rowsCount = table().rowsCount();
        for (int i = 1; i <= rowsCount; i++) {
            assertLifecycleStateValueEquals(i, lifecycleStateExpectedValue);
        }
        return this;
    }

    public SynchronizationWizardStep<T> assertActionValueForSituationEquals(String situationValue,
                                                                            String actionExpectedValue) {
        TableRow<?, ?> tableRow = table().findRowByColumnLabel("Situation *", situationValue);
        String actionActualValue = tableRow.getColumnCellTextByColumnName("Action");
        assertion.assertEquals(actionActualValue, actionExpectedValue, "Action value for " + situationValue +
                " situation should be equal to: " + actionExpectedValue + "; actual value: " + actionActualValue);
        return this;
    }

}
