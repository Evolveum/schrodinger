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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class WizardStepPanel<W extends WizardPage> extends Component<W> {
    public static final String ID_CONTENT_BODY = "contentBody";

    public WizardStepPanel(W parent) {
        super(parent, $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public SelenideElement getStepPanelContentElement() {
        return $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    protected void clickNextButton() {
        if (isLastStep()) {
            return;
        }
        $(Schrodinger.byDataId("next")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();
    }

    protected void clickBackButton() {
        $(Schrodinger.byDataId("back")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();
    }

    protected boolean isLastStep() {
        return false;
    }

    protected boolean isFirstStep() {
        return false;
    }

}
