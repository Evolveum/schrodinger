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
package com.evolveum.midpoint.schrodinger.component.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.configuration.InternalsConfigurationPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.setCheckFormGroupOptionCheckedById;

/**
 * Created by Viliam Repan (lazyman).
 */
public class DebugUtilPanel extends Component<InternalsConfigurationPage> {

    public DebugUtilPanel(InternalsConfigurationPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public DebugUtilPanel selectDetailedDebugDump() {
        setCheckFormGroupOptionCheckedById("detailedDebugDump", true);
        return this;
    }

    public DebugUtilPanel deselectDetailedDebugDump() {
        setCheckFormGroupOptionCheckedById("detailedDebugDump", false);
        return this;
    }

    public boolean isDetailedDebugDumpSelected() {
        String checked = getDetailedDebugDumpElement().getAttribute("checked");
        return checked != null && "checked".equals(checked);
    }

    private SelenideElement getDetailedDebugDumpElement() {
        return $(Schrodinger.byDataId("detailedDebugDump")).$x("//input[@data-s-id='check']")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public DebugUtilPanel clickUpdate() {
        $(Schrodinger.byDataId("saveDebugUtil"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }
}

