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
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar.
 */
public class ObjectBrowserModal<T> extends ModalBox<T> {
    public ObjectBrowserModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ObjectBrowserModal<T> selectType(String type) {
        SelenideElement typeDropDown =
                $(Schrodinger.byElementAttributeValue("select", "data-s-id", "type"));
        typeDropDown.selectOption(type);
        return this;
    }

    public ObjectBrowserModalTable<T, ObjectBrowserModal<T>> table(){
        SelenideElement box = $(Schrodinger.byDataId("div", "itemsTable"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ObjectBrowserModalTable<>(this, box);
    }

    public T clickAddButton() {
        getParentElement().$x(".//*[@data-s-id='addButton']").shouldBe(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }


}
