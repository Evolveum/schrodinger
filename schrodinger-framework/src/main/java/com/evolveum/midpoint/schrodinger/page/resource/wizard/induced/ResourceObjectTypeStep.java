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

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;

public class ResourceObjectTypeStep<P> extends TileListWizardStepPanel<P>
        implements NextStepAction<EntitlementsStep<P>>, PreviousStepAction<ApplicationResourceStep<P>> {

    public ResourceObjectTypeStep(P parent) {
        super(parent);
    }

    @Override
    public EntitlementsStep<P> next() {
        return new EntitlementsStep<>(getParent());
    }

    @Override
    public ApplicationResourceStep<P> back() {
        return new ApplicationResourceStep<>(getParent());
    }
}
