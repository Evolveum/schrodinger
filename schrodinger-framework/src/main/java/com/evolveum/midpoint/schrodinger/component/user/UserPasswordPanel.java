/*
 * Copyright (c) 2021 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;

public class UserPasswordPanel<T> extends PanelWithContainerWrapper<T> {

    public UserPasswordPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public UserPasswordPanel setPasswordValue(String value) {
//        PrismForm passwordContainer = form()
//                .expandContainerPropertiesPanel("Password")
//                .getPrismContainerPanel("Password")
//                .getContainerFormFragment();

        boolean existValue = getParentElement().$x("//a[@data-s-id='changePasswordLink']").exists();
        if (existValue) {
            getParentElement().$x("//a[@data-s-id='changePasswordLink']").click();
        }
        getParentElement().$x("//input[@data-s-id='password1']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        getParentElement().$x("//input[@data-s-id='password2']").waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(value);
        getParentElement().$x("//input[@data-s-id='password2']").waitUntil(Condition.value(value), MidPoint.TIMEOUT_SHORT_4_S);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

}
