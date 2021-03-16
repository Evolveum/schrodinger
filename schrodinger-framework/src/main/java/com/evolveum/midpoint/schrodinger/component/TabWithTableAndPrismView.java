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
package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;

import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

abstract public class TabWithTableAndPrismView<P> extends Component<P> {

    public TabWithTableAndPrismView(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public <T extends TabWithTableAndPrismView<P>> AbstractTableWithPrismView<T> table() {

        SelenideElement tableBox = $(By.cssSelector(".box.boxed-table")).waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new AbstractTableWithPrismView<T>((T) this, tableBox) {
            @Override
            public PrismFormWithActionButtons<AbstractTableWithPrismView<T>> clickByName(String name) {
                $(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

                SelenideElement prismElement = $(Schrodinger.byDataId("div", getPrismViewPanelId()))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

                return new PrismFormWithActionButtons<>(this, prismElement);
            }

            @Override
            public AbstractTableWithPrismView<T> selectCheckboxByName(String name) {
                $(Schrodinger.byFollowingSiblingEnclosedValue("td", "class", "check", "data-s-id", "3", name))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

                return this;
            }

            @Override
            public AbstractTableWithPrismView<T> selectHeaderCheckbox() {
                $(Schrodinger.byFollowingSiblingEnclosedValue("th", "class", "check", "data-s-id", "3", ""))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
                return this;
            }

            @Override
            public AbstractTableWithPrismView<T> removeByName(String name) {
                rowByColumnLabel("Name", name).getInlineMenu().clickInlineMenuButtonByIconClass(".fa.fa-minus");
                return this;
            }

            @Override
            public AbstractTableWithPrismView<T> clickHeaderActionButton(String actionButtonStyle) {
                $(Schrodinger.byDescendantElementAttributeValue("th", "class", actionButtonStyle))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
                return this;
            }
        };
    }

    abstract protected String getPrismViewPanelId();
}
