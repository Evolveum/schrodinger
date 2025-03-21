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

package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by Viliam Repan (lazyman).
 */
public class TabPanel<T> extends Component<T, TabPanel<T>> {

    public TabPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelenideElement clickTab(String resourceKey) {
        String translatedLabel = Utils.translate(resourceKey);
        SelenideElement link = getParentElement()
                .$x(".//a[@data-s-id='link' and contains(text(), '" + translatedLabel + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);

        return verifyAndFetchActiveTab(link);
    }

    public SelenideElement clickTabWithName(String tabName) {
        SelenideElement link = getParentElement().$(By.linkText(tabName));

        return verifyAndFetchActiveTab(link);
    }

    public String getTabBadgeText(String resourceKey) {
        SelenideElement element = getParentElement().$(Schrodinger.bySchrodingerDataResourceKey(resourceKey));
        element.shouldBe(Condition.visible);

        SelenideElement badge = element.$(Schrodinger.byDataId("small", "count"));
        badge.shouldBe(Condition.visible);

        return badge.getValue();
    }

    private SelenideElement verifyAndFetchActiveTab(SelenideElement link) {
        link.shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement li = link.parent();
        if (li.getAttribute("class").contains("active")) {
            return li.parent().parent().parent().$(By.cssSelector(".tab-pane.active"));
        }
        link.click();
        Utils.waitForAjaxCallFinish();
        link.shouldBe(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);

        return li.parent().parent().parent().$(By.cssSelector(".tab-pane.active"));
    }

    public SelenideElement getActiveTab() {
        return getParentElement().$(By.cssSelector(".tab-pane.active"));
    }

    public TabPanel<T> assertTabExists(String resourceKey) {
        assertion.assertTrue(getParentElement().$(Schrodinger.bySchrodingerDataResourceKey(resourceKey)).exists()
                        && getParentElement().$(Schrodinger.bySchrodingerDataResourceKey(resourceKey)).isDisplayed(),
                "Tab with resource key '" + resourceKey + "' should exist.");
        return this;
    }

    public TabPanel<T> assertTabDoesntExist(String resourceKey) {
        assertion.assertFalse(getParentElement().$(Schrodinger.bySchrodingerDataResourceKey(resourceKey)).exists(),
                "Tab with resource key '" + resourceKey + "' should exist.");
        return this;
    }
}
