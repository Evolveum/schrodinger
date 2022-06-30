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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.DateTimePanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class DateIntervalSearchItemPanel<T> extends Component<T> {

    public DateIntervalSearchItemPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public DateTimePanel<DateIntervalSearchItemPanel<T>> getFromDateTimeFieldPanel() {
        return new DateTimePanel<>(this, getPopupPanel().$(Schrodinger.byDataId("dateFromValue")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public DateTimePanel<DateIntervalSearchItemPanel<T>> getToDateTimeFieldPanel() {
        return new DateTimePanel<>(this, getPopupPanel().$(Schrodinger.byDataId("dateToValue")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public T confirm() {
        getParentElement().$x(".//a[@data-s-id='confirmButton']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return getParent();
    }

    private SelenideElement getPopupPanel() {
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return getParentElement().$x(".//div[@" + Schrodinger.DATA_S_ID + "='popoverPanel']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

}
