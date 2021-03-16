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
package com.evolveum.midpoint.schrodinger.page.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.SchrodingerException;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class FormSubmittablePage {

    SelenideElement element;

    public FormSubmittablePage() {

        this.element = $(Schrodinger.byDataId("dynamicForm")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);

    }

    public PrismForm<FormSubmittablePage> form() {

        if (!element.exists()) {
            throw new SchrodingerException("Dynamic form not present");
        }

        return new PrismForm<>(this, element);
    }

}
