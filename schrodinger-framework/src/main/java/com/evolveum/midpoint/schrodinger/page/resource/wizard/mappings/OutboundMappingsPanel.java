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

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.EditableRowTable;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class OutboundMappingsPanel<T> extends TableWizardStepPanel<T> {

    public OutboundMappingsPanel(T parent) {
        super(parent);
    }

    public OutboundMappingsTable<OutboundMappingsPanel<T>> addOutbound() {
        table()
                .getToolbarButtonByTitleKey("OutboundAttributeMappingsTable.newObject")
                .click();
        return new OutboundMappingsTable<>(OutboundMappingsPanel.this, $(Schrodinger.byDataId("table")));
    }

    protected EditableRowTable<TableWizardStepPanel<T>> table() {
        return new EditableRowTable<>(OutboundMappingsPanel.this,
                $(Schrodinger.byDataId("table")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }
}
