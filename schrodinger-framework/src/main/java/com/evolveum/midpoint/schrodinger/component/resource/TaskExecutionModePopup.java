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

package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ModalBox;
import com.evolveum.midpoint.schrodinger.simulation.ProcessedObjectsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class TaskExecutionModePopup extends ModalBox<ResourceShadowTable> {

    public TaskExecutionModePopup(ResourceShadowTable<?> parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TaskExecutionModePopup selectTaskExecutionMode(String value) {
        getParentElement().$(Schrodinger.byDataId("dropdown")).selectOption(value);
        return TaskExecutionModePopup.this;
    }

    public ResourceShadowTable<?> cancel() {
        getParentElement().$(Schrodinger.byDataId("cancel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public ProcessedObjectsPage select() {
        getParentElement().$(Schrodinger.byDataId("select")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ProcessedObjectsPage();
    }

}
