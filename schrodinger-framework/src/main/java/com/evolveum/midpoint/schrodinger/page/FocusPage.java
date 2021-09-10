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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.common.SummaryPanel;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.evolveum.midpoint.schrodinger.util.Utils.*;

/**
 * @author skublik
 */

public class FocusPage<F extends FocusPage> extends AssignmentHolderDetailsPage<F> {

    public FocusPage checkForce() {
        activateExecuteOption("Force");
        return this;
    }

    public FocusPage checkReconcile() {
        activateExecuteOption("Reconcile");
        return this;
    }

    public FocusPage checkExecuteAfterAllApprovals() {
        activateExecuteOption("Execute after all approvals");
        return this;
    }

    public FocusPage checkKeepDisplayingResults() {
        activateExecuteOption("Keep displaying results");
        return this;
    }

    public FocusPage uncheckForce() {
        disactivateExecuteOption("Force");
        return this;
    }

    public FocusPage uncheckReconcile() {
        disactivateExecuteOption("Reconcile");
        return this;
    }

    public FocusPage uncheckExecuteAfterAllApprovals() {
        disactivateExecuteOption("Execute after all approvals");
        return this;
    }

    public FocusPage uncheckKeepDisplayingResults() {
        disactivateExecuteOption("Keep displaying results");
        return this;
    }

    public void activateExecuteOption(String option) {
        if (isUseTabbedPanel()) {
            setCheckFormGroupOptionCheckedByValue(option, true);
        } else {
            SelenideElement optionButton =
                    $x(".//a[contains(text(), '" + option + "')]").waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
            if (optionButton.getAttribute("class").contains("active")) {
                return;
            }
            optionButton.click();
            optionButton.waitUntil(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
    }

    public void disactivateExecuteOption(String option) {
        if (isUseTabbedPanel()) {
            setCheckFormGroupOptionCheckedByValue(option, false);
        } else {
            SelenideElement optionButton =
                    $x(".//a[contains(text(), " + option + ")]").waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
            if (!optionButton.getAttribute("class").contains("active")) {
                return;
            }
            optionButton.click();
            optionButton.waitUntil(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
    }

    public ProjectionsPanel<F> selectProjectionsPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Projections");
        Selenide.sleep(2000);
        return new ProjectionsPanel<F>(this, element);
    }

    public ProjectionsPanel<F> selectCasesPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Cases");
        Selenide.sleep(2000);
        return new ProjectionsPanel<F>(this, element);
    }


    public SummaryPanel<F> summary() {

        SelenideElement summaryPanel = $(By.cssSelector("div.info-box-content"));

        return new SummaryPanel(this, summaryPanel);
    }

    public boolean isActivationState(String state) {

        SelenideElement summaryPanel = $(Schrodinger.byDataId("span", "summaryTagLabel")).waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        if (state != null || !(state.isEmpty())) {
            return state.equals(summaryPanel.getText());
        } else {
            return "".equals(summaryPanel.getText());
        }
    }

    public FocusPage<F> assertActivationStateEquals(String state) {
        assertion.assertTrue(isActivationState(state), "Activation state doesn't equal to " + state);
        return this;
    }
}
