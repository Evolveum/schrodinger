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
package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TextInputSearchItemPanel<T extends Search> extends Component<T> {

    public TextInputSearchItemPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T inputValue(String input) {
        Utils.waitForAjaxCallFinish();
        if (getParentElement() == null){
            return getParent();
        }
        SelenideElement inputField = getParentElement().parent().$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.editable, MidPoint.TIMEOUT_MEDIUM_6_S);
        if(!input.equals(inputField.getValue())) {
            inputField.setValue(input);
            Utils.waitForAjaxCallFinish();
        }
        return getParent();
    }
}
