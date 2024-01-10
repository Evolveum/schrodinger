/*
 * Copyright (c) 2024  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.induced;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.MappingsTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class MappingsStep<P> extends TableWizardStepPanel<P, MappingsTable<MappingsStep<P>, MappingsTable>>
        implements PreviousStepAction<EntitlementsStep<P>> {

    public MappingsStep(P parent) {
        super(parent);
    }

    @Override
    public MappingsTable<MappingsStep<P>, MappingsTable> table() {
        return new MappingsTable<>(this, getParentElement().$(Schrodinger.byDataId("itemsTable"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public P saveSettings() {
        $x(".//a[contains(text(), 'Save settings')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    @Override
    public EntitlementsStep<P> back() {
        return new EntitlementsStep<>(getParent());
    }
}
