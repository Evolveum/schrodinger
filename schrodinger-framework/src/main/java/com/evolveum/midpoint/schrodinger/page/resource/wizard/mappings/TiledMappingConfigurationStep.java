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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public abstract class TiledMappingConfigurationStep<T, M extends TiledMappingConfigurationStep<T, M>> extends WizardStepPanel<T> {

    public TiledMappingConfigurationStep(T parent) {
        super(parent, $(Schrodinger.byDataId("tabTable")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public <IM extends TiledInboundMappingPanel<TiledMappingConfigurationStep<T, M>, ? extends TiledInboundMappingPanel>> IM inbound() {
        getParentElement().$(Schrodinger.byDataId("li", "0"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getInboundMappingPanel();
    }

    public <OM extends TiledOutboundMappingPanel<TiledMappingConfigurationStep<T, M>,
            ? extends TiledOutboundMappingPanel>> OM outbound() {
        getParentElement().$(Schrodinger.byDataId("li", "1"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getOutboundMappingPanel();
    }

    public T exitWizard() {
        $x(".//a[contains(text(), 'Exit wizard')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public abstract <IM extends TiledInboundMappingPanel<TiledMappingConfigurationStep<T, M>,
            ? extends TiledInboundMappingPanel>> IM getInboundMappingPanel();

    public abstract <OM extends TiledOutboundMappingPanel<TiledMappingConfigurationStep<T, M>,
            ? extends TiledOutboundMappingPanel>> OM getOutboundMappingPanel();
}
