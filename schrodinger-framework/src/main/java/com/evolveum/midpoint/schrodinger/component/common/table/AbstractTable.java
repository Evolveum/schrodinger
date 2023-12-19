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
package com.evolveum.midpoint.schrodinger.component.common.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/9/2018.
 */
public abstract class AbstractTable<T, P extends AbstractTable<T, P>> extends SelectableRowTable<T, P> {

    public AbstractTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public abstract PrismForm<AbstractTable<T, P>> clickByName(String name);

    public AbstractTable<T, P> selectHeaderCheckBox(){
        $(By.tagName("thead"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S)
        .$(Schrodinger.byElementAttributeValue("input", "type", "checkbox"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }
}
