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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ActivationInboundPanel<T> extends Component<T, ActivationInboundPanel<T>> {

    public ActivationInboundPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ActivationInboundPanel<T> addInbound(String activationRuleTitle) {
        getParentElement().$(Schrodinger.byDataId("addInbound")).click();
        Utils.waitForAjaxCallFinish();
        SelenideElement modalBox = Utils.getModalWindowSelenideElement();
        SelenideElement tile = Utils.findTileElementByTitle("ResourceObjectTypePreviewTileType.BASIC", modalBox);
        tile.click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public ActivationInboundPanel<T> assertTilesNumberEquals(int expected) {
        assertion.assertEquals(expected, getParentElement().$$(Schrodinger.byDataId("tile")).size(),
                "Wrong number of tiles");
        return this;
    }
}
