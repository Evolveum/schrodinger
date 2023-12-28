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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.activation;

import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class ActivationRuleOptionalConfigurationStep<T> extends PrismFormWizardStepPanel<T>
        implements PreviousStepAction<ActivationRuleMainConfigurationStep<T>> {

    public ActivationRuleOptionalConfigurationStep(T parent) {
        super(parent);
    }

    @Override
    public ActivationRuleMainConfigurationStep<T> back() {
        clickNext();
        return new ActivationRuleMainConfigurationStep<>(getParent());
    }
}
