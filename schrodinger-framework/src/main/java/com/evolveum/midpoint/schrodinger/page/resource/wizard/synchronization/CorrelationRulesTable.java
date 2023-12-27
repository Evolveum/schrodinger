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
import com.evolveum.midpoint.schrodinger.component.wizard.EditableTableWithRedirectToWizardStep;

public class CorrelationRulesTable<P>
        extends EditableTableWithRedirectToWizardStep<P, CorrelationItemsConfigurationStep<P>, CorrelationRulesTable<P>> {

    public CorrelationRulesTable(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public CorrelationItemsConfigurationStep<P> getWizardStepPanelOnEditAction() {
        return new CorrelationItemsConfigurationStep<>(getParent());
    }

    @Override
    public String getObjectNameColumnLabel() {
        return "Rule name";
    }

    public CorrelationRulesTable<P> setEnabled(String name, String lifecycleState) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue(getObjectNameColumnLabel(), name, true);
        setDropdownValue("Enabled", lifecycleState, row);
        return this;
    }
}
