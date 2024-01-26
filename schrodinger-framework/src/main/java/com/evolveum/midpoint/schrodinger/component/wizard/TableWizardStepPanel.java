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

package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public abstract class TableWizardStepPanel<P, T extends Table> extends WizardStepPanel<P> {

    public TableWizardStepPanel(P parent) {
        super(parent, $x(".//div[contains(@class, \"card\")]").shouldBe(Condition.visible,
                MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public abstract T table();

    protected SelenideElement getTableElement() {
        return getParentElement().$(Schrodinger.byDataId("table"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

}
