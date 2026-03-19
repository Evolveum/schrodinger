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
package com.evolveum.midpoint.schrodinger.page;

import static com.codeborne.selenide.Selenide.$$x;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;

/**
 * Created by Viliam Repan (lazyman).
 */
public class DashboardPage extends BasicPage {

    public SelenideElement widgetByLabel(String label) {
        return $$x("//div[contains(@class,'small-box')]")
                .findBy(Condition.text(label))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public String getWidgetValue(String label) {
        return widgetByLabel(label)
                .$x(".//h3")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .getText()
                .trim();
    }

    public DashboardPage assertWidgetValueEquals(String label, String expectedValue) {
        assertion.assertEquals(getWidgetValue(label), expectedValue,
                "Dashboard widget value doesn't equal expected value '" + expectedValue + "'.");
        return this;
    }

    public DashboardPage assertWidgetValueNotEquals(String label, String unexpectedValue) {
        assertion.assertNotEquals(getWidgetValue(label), unexpectedValue,
                "Dashboard widget value shouldn't equal '" + unexpectedValue + "'.");
        return this;
    }

}
