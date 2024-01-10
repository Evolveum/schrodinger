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
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

public class EntitlementsStep<P> extends TableWizardStepPanel<P, EntitlementsTable<EntitlementsStep<P>>>
        implements NextStepAction<MappingsStep<P>>, PreviousStepAction<ResourceObjectTypeStep<P>> {

    public EntitlementsStep(P parent) {
        super(parent);
    }

    @Override
    public EntitlementsTable<EntitlementsStep<P>> table() {
        return new EntitlementsTable<>(this, getParentElement().$(Schrodinger.byDataId("table"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    @Override
    public MappingsStep<P> next() {
        return new MappingsStep<>(getParent());
    }

    @Override
    public ResourceObjectTypeStep<P> back() {
        return new ResourceObjectTypeStep<>(getParent());
    }
}
