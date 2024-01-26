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
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.activation.ScenarioConfigurationStep;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

public class TiledOutboundMappingPanel<T, MP extends TiledOutboundMappingPanel<T, MP>> extends TileListWizardStepPanel<T> {

    public TiledOutboundMappingPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public MP setLifecycleStateValue(String activationRuleTitle, String lifecycleStateValue) {
        SelenideElement select = getLifecycleStateElement(activationRuleTitle);
        select.selectOption(lifecycleStateValue);
        Utils.waitForAjaxCallFinish();
        return getThis();
    }

    public MP remove(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement remove = tile.$(Schrodinger.byDataId("removeButton"));
        remove.click();
        Utils.waitForAjaxCallFinish();
        return getThis();
    }

    public MP settings(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement remove = tile.$(Schrodinger.byDataId("configureButton"));
        remove.click();
        Utils.waitForAjaxCallFinish();
        return getThis();
    }

    //todo how to distinguish scenario configuration tiles?
    public ScenarioConfigurationStep<MP> scenarioConfigurationSettings(String activationRuleTitle) {
        settings(activationRuleTitle);
        return new ScenarioConfigurationStep<>(getThis());
    }

    public MP assertTilesNumberEquals(int expected) {
        assertion.assertEquals(expected, getParentElement().$$(Schrodinger.byDataId("tile")).size(),
                "Wrong number of tiles");
        return getThis();
    }

    public MP assertTilesLifecycleStateValueEquals(String tileTitle, String expectedLifecycleStateValue) {
        SelenideElement select = getLifecycleStateElement(tileTitle);
        assertion.assertEquals(expectedLifecycleStateValue, select.getSelectedOption().getText(),
                "Wrong lifecycle state value");
        return getThis();
    }

    private SelenideElement getLifecycleStateElement(String activationRuleTitle) {
        SelenideElement tile = Utils.findTileElementByTitle(activationRuleTitle);
        SelenideElement lifecycleState = tile.$(Schrodinger.byDataId("lifecycleState"));
        return lifecycleState.$(By.tagName("select"));
    }

    private MP getThis() {
        return (MP) this;
    }

}
