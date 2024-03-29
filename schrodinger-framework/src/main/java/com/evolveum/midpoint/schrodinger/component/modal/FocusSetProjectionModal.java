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
import com.evolveum.midpoint.schrodinger.component.FocusTableWithChoosableElements;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class FocusSetProjectionModal<T> extends ModalBox<T> {

    public FocusSetProjectionModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public FocusTableWithChoosableElements<FocusSetProjectionModal<T>> table() {
        SelenideElement resourcesBox = getParentElement().find(Schrodinger.byDataId("itemsTable"));
        return new FocusTableWithChoosableElements<>(this, resourcesBox);
    }

    public T clickAdd() {

        $(Schrodinger.byDataResourceKey("userBrowserDialog.button.addButton"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this.getParent();
    }
}
