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

import com.evolveum.midpoint.schrodinger.component.common.table.EditableRowTable;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SynchronizationWizardStep<T> extends TableWizardStepPanel<T> {

    public SynchronizationWizardStep(T parent) {
        super(parent);
    }

    public MainConfigurationWizardStep<SynchronizationWizardStep<T>> addReaction() {
        table()
                .getToolbarButtonByTitle("SynchronizationReactionTable.newObject")
                .click();
        return new MainConfigurationWizardStep<>(SynchronizationWizardStep.this);
    }

    public ListOfReactionsTable<SynchronizationWizardStep<T>> addSimpleReaction() {
        table()
                .getToolbarButtonByTitle("SynchronizationReactionTable.newObject.simple")
                .click();
        Utils.waitForAjaxCallFinish();
        return new ListOfReactionsTable<>(SynchronizationWizardStep.this, $(Schrodinger.byDataId("table")));
    }

    public T saveSynchronizationSettings() {
        String titleTranslated = Utils.getPropertyString("SynchronizationReactionTableWizardPanel.saveButton");
        $x(".//a[@title=\"" + titleTranslated + "\"]").click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }
}
