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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by honchar
 */
public class DropDownSearchItemPanel<T> extends Component<T, DropDownSearchItemPanel<T>> {
    public DropDownSearchItemPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T inputDropDownValue(String value) {
        if (getParentElement() == null){
            return getParent();
        }
        SelenideElement inputField = getParentElement().find(Schrodinger.byDataId("select", "input"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.selectOptionContainingText(value);
        return getParent();
    }

    public DropDownSearchItemPanel<T> assertDropDownOptionExist(String option) {
        boolean exists = getDropDownOptionsList().contains(option);
        assertion.assertTrue(exists, "Drop down option '" + option + "' doesn't exist.");

        return DropDownSearchItemPanel.this;
    }

    public DropDownSearchItemPanel<T> assertDropDownOptionUnique(String option) {
        boolean isUnique = Collections.frequency(getDropDownOptionsList(), option) == 1;
        assertion.assertTrue(isUnique, "Drop down option '" + option + "' is not unique.");

        return DropDownSearchItemPanel.this;
    }

    private @NotNull List<String> getDropDownOptionsList() {
        SelenideElement inputField = getParentElement().find(Schrodinger.byDataId("select", "input"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        if (!inputField.exists()) {
            return new ArrayList<>();
        }
        inputField.click();
        return inputField
                .$$x("option")
                .texts();
    }
}
