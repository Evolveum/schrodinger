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
package com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import jdk.jshell.execution.Util;

import static com.codeborne.selenide.Selenide.$;

public class OptionalSettingsWizardStep<T> extends PrismFormWizardStepPanel<T> {

    public OptionalSettingsWizardStep(T parent) {
        super(parent);
    }

    public OptionalSettingsWizardStep<T> condition(String value) {
        getFormPanel().addAttributeValue("Condition", value);
        return OptionalSettingsWizardStep.this;
    }

    public OptionalSettingsWizardStep<T> channel(String value) {
        getFormPanel().addAttributeValue("Channel", value);
        return OptionalSettingsWizardStep.this;
    }

    public OptionalSettingsWizardStep<T> order(String order) {
        getFormPanel().addAttributeValue("Order", order);
        return OptionalSettingsWizardStep.this;
    }

    public T done() {
        $(Schrodinger.byDataId("a", "submit"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }
}
