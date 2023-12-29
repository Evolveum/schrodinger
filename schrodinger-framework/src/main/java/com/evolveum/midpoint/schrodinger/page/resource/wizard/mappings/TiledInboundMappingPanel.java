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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.activation.ActivationInboundPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class TiledInboundMappingPanel<T, MP extends TiledInboundMappingPanel<T, MP>> extends TileListWizardStepPanel<T> {

    public TiledInboundMappingPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public MP addInbound(String activationRuleTitle) {
        getParentElement().$(Schrodinger.byDataId("addInbound")).click();
        Utils.waitForAjaxCallFinish();
        SelenideElement modalBox = Utils.getModalWindowSelenideElement();
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.BASIC", modalBox);
        tile.click();
        Utils.waitForAjaxCallFinish();
        return getThis();
    }

    public MP assertTilesNumberEquals(int expected) {
        assertion.assertEquals(expected, getParentElement().$$(Schrodinger.byDataId("tile")).size(),
                "Wrong number of tiles");
        return getThis();
    }

    private MP getThis() {
        return (MP) this;
    }

}
