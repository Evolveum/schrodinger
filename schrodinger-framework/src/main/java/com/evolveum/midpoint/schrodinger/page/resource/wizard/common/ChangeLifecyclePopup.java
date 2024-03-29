/*
 * Copyright (c) 2024  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.common;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.modal.ModalBox;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

public class ChangeLifecyclePopup<T> extends ModalBox<T> {

    public ChangeLifecyclePopup(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ChangeLifecyclePopup<T> selectLifecycleValue(String lifecycle) {
        $(Schrodinger.byDataId("select", "panel")).selectOption(lifecycle);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public T clickApplyChanges() {
        getParentElement().$(Schrodinger.byDataId("applyChanges")).click();
        Utils.waitForAjaxCallFinish();
        return this.getParent();
    }

    public T clickClose() {
        getParentElement().$(Schrodinger.byDataId("close")).click();
        Utils.waitForAjaxCallFinish();
        return this.getParent();
    }
}
