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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class CorrelationWizardStep <T> extends TableWizardStepPanel<T> {

    public CorrelationWizardStep(T parent) {
        super(parent);
    }

    public T saveSynchronizationSettings() {
        return clickButtonByTitleKeyAndRedirectToParent("CorrelationWizardPanelWizardPanel.saveButton");
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

    public CorrelationWizardStep<T> assertCorrelationRuleExist(String correlationRuleName) {
        table().assertTableContainsColumnWithValue("Rule name", correlationRuleName);
        return this;
    }

    public CorrelationWizardStep<T> assertCorrelationRuleEnabled(String correlationRuleName) {
        TableRow<?, ?> tableRow = table().rowByColumnResourceKey("Rule name", correlationRuleName);
        SelenideElement cell = tableRow.getColumnCellElementByColumnName("Enabled");
        String enabledRealValue = cell != null ? cell.getText() : null;
        assertion.assertEquals(enabledRealValue, "True", "Enabled value for " + correlationRuleName
                + " correlation rule should be True");
        return this;
    }

    public CorrelationWizardStep<T> assertCorrelationRuleDisabled(String correlationRuleName) {
        TableRow<?, ?> tableRow = table().rowByColumnResourceKey("AbstractCorrelatorType.name", correlationRuleName);
        SelenideElement cell = tableRow.getColumnCellElementByColumnName("Enabled");
        String enabledRealValue = cell != null ? cell.getText() : null;
        assertion.assertEquals(enabledRealValue, "False", "Enabled value for " + correlationRuleName
                + " correlation rule should be False");
        return this;
    }
}
