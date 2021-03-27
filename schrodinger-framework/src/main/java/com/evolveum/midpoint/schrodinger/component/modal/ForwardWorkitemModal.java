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

import static com.codeborne.selenide.Selenide.$;

public class ForwardWorkitemModal<T> extends ModalBox<T> {

    public ForwardWorkitemModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ObjectBrowserModalTable<T, ForwardWorkitemModal<T>> table(){
        SelenideElement box = $(Schrodinger.byElementAttributeValue("div", "class","box boxed-table"))
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ObjectBrowserModalTable<T, ForwardWorkitemModal<T>>(this, box){
            public T clickByName(String name){
                getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

//                box.waitUntil(Condition.disappears, MidPoint.TIMEOUT_DEFAULT_2_S);

                return getParent().getParent();
            }
        };
    }

}