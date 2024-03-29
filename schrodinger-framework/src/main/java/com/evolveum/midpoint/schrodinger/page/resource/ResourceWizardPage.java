/*
 * Copyright (c) 2010-2021 Evolveum
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
package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * This is old resource wizard page. It's not present in mp since 4.6
 */
@Deprecated
public class ResourceWizardPage extends BasicPage {

    public void clickOnWizardTab(String tabName){
        $(By.linkText(tabName))
                .shouldBe(Condition.visible)
                .click();
    }

    public ConfigurationWizardStep<ResourceWizardPage> selectConfigurationStep() {
        clickOnWizardTab("Configuration");
        SelenideElement tabElement = $(Schrodinger.byDataId("configuration")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ConfigurationWizardStep<>(this, tabElement);
    }

    public SchemaWizardStep selectSchemaStep() {
        clickOnWizardTab("Schema");
        SelenideElement tabElement = $(Schrodinger.byDataId("tabPanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new SchemaWizardStep(this, tabElement);
    }

    public boolean isReadonlyMode() {
        return $(By.className("wizard"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S)
                .exists();
    }

    public ResourceWizardPage assertReadonlyMode() {
        assertion.assertTrue(isReadonlyMode());
        return this;
    }
}
