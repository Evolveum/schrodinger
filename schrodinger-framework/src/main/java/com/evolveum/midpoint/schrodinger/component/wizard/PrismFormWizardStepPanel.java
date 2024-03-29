/*
 * Copyright (c) 2023 Evolveum
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

package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class PrismFormWizardStepPanel<T> extends WizardStepPanel<T> {

    public PrismFormWizardStepPanel(T parent) {
        super(parent, $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    protected PrismForm<PrismFormWizardStepPanel<T>> getFormPanel() {
        SelenideElement formElement = $(Schrodinger.byDataId("div", getFormElementId()))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new PrismForm<>(PrismFormWizardStepPanel.this, formElement);
    }

    protected String getFormElementId() {
        return "form";
    }

}
