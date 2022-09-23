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
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Created by matus on 5/9/2018.
 */
public class ConfirmationModal<T> extends ModalBox<T> {
    public ConfirmationModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T clickYes() {
        SelenideElement yesButton = $(Schrodinger.byDataResourceKey("a", "confirmationDialog.yes"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        sleep(500); //wait a bit, so we are sure that the popup is loaded properly
        yesButton.click();

        $(Schrodinger.byDataResourceKey("a", "confirmationDialog.yes"))
                .shouldBe(Condition.disappear, MidPoint.TIMEOUT_MEDIUM_LONG_3_M);

        return this.getParent();
    }

    public T clickNo() {
        $(Schrodinger.byDataResourceKey("a", "confirmationDialog.no"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        $(Schrodinger.byDataResourceKey("a", "confirmationDialog.yes"))
                .shouldBe(Condition.disappear, MidPoint.TIMEOUT_LONG_1_M);

        return this.getParent();
    }

    public T close() {
        $(By.className("w_close"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        $(Schrodinger.byDataResourceKey("a", "confirmationDialog.yes"))
                .shouldBe(Condition.disappear, MidPoint.TIMEOUT_LONG_1_M);

        return this.getParent();
    }

}
