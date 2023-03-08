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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.prism.show.PreviewChangesPanel;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Viliam Repan (lazyman).
 */
public class PreviewPage extends BasicPage {

    public PreviewChangesPanel selectPanelForCurrentUser() {
        return selectPanelByName(null);
    }

    public PreviewChangesPanel selectPanelByName(String name) {
        SelenideElement element;
        if (isTabPanelDisplayed() && StringUtils.isNotEmpty(name)) {
            element = findTabPanelIfExists().clickTabWithName(name);
        } else {
            element = $x(".//form[@data-s-id='mainForm']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return new PreviewChangesPanel(this, element);
    }

    private TabPanel findTabPanelIfExists() {
        if (isTabPanelDisplayed()) {
            SelenideElement tabPanelElement = $(Schrodinger.byDataId("tabbedPanel"));
            return new TabPanel<>(this, tabPanelElement);
        } else {
            return null;
        }
    }

    /**
     * Starting from 4.7, preview changes page contains tabbed panel only in case, when more than one object is to be
     * changed (e.g. adding delegation to another user + some changes for currently opened user object). Therefore
     * it's necessary to check at first if tabbed panel is displayed before getting a preview changes panel
     * @return
     */
    public boolean isTabPanelDisplayed() {
        try {
            $(Schrodinger.byDataId("tabbedPanel"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ProgressPage clickSave() {
        $(Schrodinger.byDataId("save")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new ProgressPage();
    }




}
