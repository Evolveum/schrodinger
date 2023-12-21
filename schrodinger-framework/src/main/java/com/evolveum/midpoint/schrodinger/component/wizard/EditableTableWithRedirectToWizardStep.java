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

package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.EditableRowTable;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.util.Utils;

public abstract class EditableTableWithRedirectToWizardStep<T, WS extends WizardStepPanel<T>, P  extends EditableTableWithRedirectToWizardStep<T, WS, P>>
        extends EditableRowTable<T, P> {

    public EditableTableWithRedirectToWizardStep(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public WS clickEditButton(String objectName) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue(getObjectNameColumnLabel(), objectName);
        row.getInlineMenu().clickInlineMenuButtonByIconClass("fa fa-edit");
        Utils.waitForAjaxCallFinish();
        return getWizardStepPanelOnEditAction();
    }

    protected abstract WS getWizardStepPanelOnEditAction();
    protected abstract String getObjectNameColumnLabel();
}
