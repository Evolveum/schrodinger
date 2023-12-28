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
import org.openqa.selenium.By;

public class ActivationOutboundPanel<T> extends Component<T, ActivationOutboundPanel<T>> {

    public ActivationOutboundPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ActivationOutboundPanel<T> setLifecycleStateValue(String activationRuleTitle, String lifecycleStateValue) {
        SelenideElement select = getLifecycleStateElement(activationRuleTitle);
        select.selectOption(lifecycleStateValue);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public ActivationOutboundPanel<T> remove(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement remove = tile.$(Schrodinger.byDataId("removeButton"));
        remove.click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public ActivationRuleMainConfigurationStep<ActivationOutboundPanel<T>> settings(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement remove = tile.$(Schrodinger.byDataId("configureButton"));
        remove.click();
        Utils.waitForAjaxCallFinish();
        return new ActivationRuleMainConfigurationStep<>(this);
    }

    //todo how to distinguish scenario configuration tiles?
    public ScenarioConfigurationStep<ActivationOutboundPanel<T>> scenarioConfigurationSettings(String activationRuleTitle) {
        settings(activationRuleTitle);
        return new ScenarioConfigurationStep<>(this);
    }

    public ActivationOutboundPanel<T> assertTilesNumberEquals(int expected) {
        assertion.assertEquals(expected, getParentElement().$$(Schrodinger.byDataId("tile")).size(),
                "Wrong number of tiles");
        return this;
    }

    public ActivationOutboundPanel<T> assertTilesLifecycleStateValueEquals(String activationRuleTitle,
                                                                           String expectedLifecycleStateValue) {
        SelenideElement select = getLifecycleStateElement(activationRuleTitle);
        assertion.assertEquals(expectedLifecycleStateValue, select.getSelectedOption().getText(),
                "Wrong lifecycle state value");
        return this;
    }

    private SelenideElement getLifecycleStateElement(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement lifecycleState = tile.$(Schrodinger.byDataId("lifecycleState"));
        return lifecycleState.$(By.tagName("select"));
    }
}
