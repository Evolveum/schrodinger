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
package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;

public class ConfigurableActionConfirmationModal<T> extends ModalBox<T> {

    public ConfigurableActionConfirmationModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ConfigurableActionConfirmationModal<T> assertValidationErrorExistsAfterConfirmation() {
        clickConfirmButton();
        SelenideElement feedback = getParentElement()
                .$x(".//div[@data-s-id='detailsBox' and contains(@class, \"feedback-message\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);
        Utils.scrollToElement(feedback);
        FeedbackBox<ConfigurableActionConfirmationModal<T>> feedbackBox = new FeedbackBox<>(this, feedback);
        feedbackBox.assertError();
        return this;
    }

    public T clickYes() {
        clickConfirmButton();
        getParentElement().$(Schrodinger.byDataId("a", "confirmButton"))
                .shouldBe(Condition.disappear, MidPoint.TIMEOUT_MEDIUM_LONG_3_M);

        return this.getParent();
    }

    private void clickConfirmButton() {
        SelenideElement yesButton = getParentElement().$(Schrodinger.byDataId("a", "confirmButton"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        sleep(500); //wait a bit, so we are sure that the popup is loaded properly
        yesButton.click();
    }

    public T clickNo() {
        getParentElement().$(Schrodinger.byDataId("a", "cancelButton"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        getParentElement().$(Schrodinger.byDataId("a", "cancelButton"))
                .shouldBe(Condition.disappear, MidPoint.TIMEOUT_LONG_1_M);

        return this.getParent();
    }

}
