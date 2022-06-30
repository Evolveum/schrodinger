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
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.Assert;

/**
 * Created by honchar
 */
public class DateTimePanel<T> extends Component<T> {

    public enum AmOrPmChoice {
        AM, PM
    }

    public DateTimePanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public DateTimePanel<T> setDateTimeValue(String date, String hours, String minutes, AmOrPmChoice amOrPmChoice) {
        SelenideElement dateEle = findDate();
        dateEle.click();
        dateEle.clear();
        dateEle.setValue(date);

        SelenideElement hoursEle = findHours();
        hoursEle.doubleClick();
        hoursEle.doubleClick();
        hoursEle.sendKeys(hours);

        SelenideElement minutesEle = findMinutes();
        minutesEle.doubleClick();
        minutesEle.doubleClick();
        minutesEle.sendKeys(minutes);

        SelenideElement amOrPmChoiceEle = findAmOrPmChoice();
        amOrPmChoiceEle.click();
        amOrPmChoiceEle.selectOption(amOrPmChoice.name());

        return this;
    }

    public String date() {
        return findDate().getValue();
    }

    public String hours() {
        return findHours().getValue();
    }

    public String minutes() {
        return findMinutes().getValue();
    }

    public String amOrPmChoice() {
        return findAmOrPmChoice().getSelectedText();
    }

    public SelenideElement findDate() {
        return getParentElement().$(Schrodinger.byDataId("date")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public SelenideElement findHours() {
        return getParentElement().$(Schrodinger.byDataId("hours")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public SelenideElement findMinutes() {
        return getParentElement().$(Schrodinger.byDataId("minutes")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public SelenideElement findAmOrPmChoice() {
        return getParentElement().$(Schrodinger.byDataId("amOrPmChoice")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public DateTimePanel<T> assertDateValueEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, date(), "Date value doesn't match.");
        return this;
    }
    public DateTimePanel<T> assertHoursValueEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, hours(), "Hours value doesn't match.");
        return this;
    }
    public DateTimePanel<T> assertMinutesValueEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, minutes(), "Minutes value doesn't match.");
        return this;
    }
    public DateTimePanel<T> assertAmPmValueEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, amOrPmChoice(), "Am/Pm value doesn't match.");
        return this;
    }
}
