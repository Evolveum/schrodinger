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
import com.evolveum.midpoint.schrodinger.SchrodingerException;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class WizardStepPanel<T> extends Component<T, WizardStepPanel<T>> {
    protected static final String ID_CONTENT_BODY = "contentBody";

    public WizardStepPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    protected SelenideElement getContentPanelElement() {
        return $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    protected void clickNext() {
        if (!NextStepAction.class.isAssignableFrom(this.getClass())) {
            throw new SchrodingerException("Current wizard step doesn't support next step action.");
        }
        $(Schrodinger.bySelfOrDescendantElementAttributeValue("a", "data-s-id", "next",
                        "data-s-id", "nextLabel"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();
    }

    public <WSP extends WizardStepPanel<T>> WSP clickBack() {
        if (!PreviousStepAction.class.isAssignableFrom(this.getClass())) {
            throw new SchrodingerException("Current wizard step doesn't support back action.");
        }
        $(Schrodinger.byDataId("back")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Utils.waitForAjaxCallFinish();

        PreviousStepAction<WSP> currentStep = (PreviousStepAction<WSP>) this;
        return currentStep.back();
    }

}
