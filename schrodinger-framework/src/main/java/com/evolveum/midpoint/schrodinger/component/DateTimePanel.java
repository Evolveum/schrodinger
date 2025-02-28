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
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

/**
 * Created by honchar
 */
public class DateTimePanel<T> extends Component<T, DateTimePanel<T>> {

    public void checkShowingOfPopover() {
        SelenideElement button = findButton();
        button.click();
        SelenideElement widget = getWidget();
        button.click();
        widget.shouldNotBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public enum AmOrPmChoice {
        AM, PM
    }

    public DateTimePanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public DateTimePanel<T> setDateTimeValue(String date) {
        return setDateTimeValue(date, null, null, null);
    }

    public DateTimePanel<T> setDateTimeValue(String date, String hours, String minutes, AmOrPmChoice amOrPmChoice) {

        String dateAsString = date;
        if (StringUtils.isNotEmpty(hours)) {
            dateAsString += " " + hours + ":" + minutes + " " + amOrPmChoice.name();
        }

        SelenideElement inputEle = findInput();
        inputEle.click();
        inputEle.clear();
        inputEle.setValue(dateAsString);

        return this;
    }

    public DateTimePanel<T> setDateTimeValueByPicker(int month, int day, int year, int hours, int minutes) {
        return setDateTimeValueByPicker(month, day, year, hours, minutes, null);
    }

    public DateTimePanel<T> setDateTimeValueByPicker(int month, int day, int year, int hours, int minutes, AmOrPmChoice amOrPmChoice) {
        findButton().click();

        SelenideElement widget = getWidget();

        clickOnChangeCalendarView(widget);
        clickOnChangeCalendarView(widget);

        widget.$(By.cssSelector("div[data-action='selectYear'][data-value='" + year + "']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        widget.$(By.cssSelector("div[data-action='selectMonth'][data-value='" + (month - 1) + "']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        widget.$(By.cssSelector("div[data-action='selectDay'][data-day='" + day + "']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();

        widget.$(By.cssSelector("div[data-action='togglePicker']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();

        widget.$(By.cssSelector("div[data-action='showHours'][data-time-component='hours']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        widget.$x(".//div[@class='time-container-hour']").shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);

        widget.$(By.cssSelector("div[data-action='selectHour'][data-value='" + hours + "']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();

        widget.$(By.cssSelector("div[data-action='showMinutes'][data-time-component='minutes']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        widget.$(By.cssSelector("div[data-action='selectMinute'][data-value='" + minutes + "']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();

        if (amOrPmChoice != null) {
            SelenideElement meridianButton = widget.$(By.cssSelector("button[data-action='toggleMeridiem']"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            String currentMeridian = meridianButton.getText();
            if (!amOrPmChoice.name().equals(currentMeridian)) {
                meridianButton.click();
                Utils.waitForAjaxCallFinish();
            }
        }

        findButton().click();
        Utils.waitForAjaxCallFinish();

        return this;
    }

    private SelenideElement getWidget() {
        return Selenide.$(By.className("tempus-dominus-widget"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    private void clickOnChangeCalendarView(SelenideElement widget) {
        widget.$(By.cssSelector("div[data-action='changeCalendarView']"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
    }

    public String dateTime() {
        return findInput().getValue();
    }

    public SelenideElement findInput() {
        return getParentElement().$(Schrodinger.byDataId("container")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(Schrodinger.byDataId("input")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public SelenideElement findButton() {
        return getParentElement().$(Schrodinger.byDataId("iconContainer")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public DateTimePanel<T> assertDateTimeValueEquals(String expectedValue) {
        String dataValue = dateTime();
        dataValue = dataValue.replace((char) 8239, ' ');
        assertion.assertEquals(dataValue, expectedValue, "Date Time value doesn't match.");
        return this;
    }
}
