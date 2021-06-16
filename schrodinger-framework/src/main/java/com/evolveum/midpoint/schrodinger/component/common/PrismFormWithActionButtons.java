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
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/17/2018.
 */
public class PrismFormWithActionButtons<T> extends PrismForm<T> {
    public PrismFormWithActionButtons(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T clickDone() {

        $(Schrodinger.byDataResourceKey("div", "MultivalueContainerListPanel.doneButton"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this.getParent();
    }

    public T clickCancel() {

        $(Schrodinger.byDataResourceKey("div", "MultivalueContainerListPanel.cancelButton"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().waitUntil(Condition.disappears, MidPoint.TIMEOUT_MEDIUM_6_S);
        return this.getParent();
    }

    public PrismFormWithActionButtons<T> selectFormTabByName(String tabName) {
        if (getFormTabbedPanelId() != null) {
            TabPanel<PrismFormWithActionButtons<T>> tabPanel = new TabPanel<PrismFormWithActionButtons<T>>(this,
                    $(Schrodinger.byDataId(getFormTabbedPanelId())).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
            tabPanel.clickTabWithName(tabName);
        }
        return this;
    }

    protected String getFormTabbedPanelId() {
        return null;
    }
}
