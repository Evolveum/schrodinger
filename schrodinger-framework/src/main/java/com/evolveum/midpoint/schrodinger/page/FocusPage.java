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
import com.evolveum.midpoint.schrodinger.component.ProjectionsTab;
import com.evolveum.midpoint.schrodinger.component.common.SummaryPanel;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.setOptionCheckedByName;

/**
 * @author skublik
 */

public class FocusPage<F extends FocusPage> extends AssignmentHolderDetailsPage<F> {

    public FocusPage checkForce() {
        setOptionCheckedByName("executeOptions:force", true);
        return this;
    }

    public FocusPage checkReconcile() {
        setOptionCheckedByName("executeOptions:reconcileContainer:container:check", true);
        return this;
    }

    public FocusPage checkExecuteAfterAllApprovals() {
        setOptionCheckedByName("executeOptions:executeAfterAllApprovals", true);
        return this;
    }

    public FocusPage checkKeepDisplayingResults() {
        setOptionCheckedByName("executeOptions:keepDisplayingResultsContainer:container:check", true);
        return this;
    }

    public FocusPage uncheckForce() {
        setOptionCheckedByName("executeOptions:force", false);
        return this;
    }

    public FocusPage uncheckReconcile() {
        setOptionCheckedByName("executeOptions:reconcileLabel:reconcile", false);
        return this;
    }

    public FocusPage uncheckExecuteAfterAllApprovals() {
        setOptionCheckedByName("executeOptions:executeAfterAllApprovals", false);
        return this;
    }

    public FocusPage uncheckKeepDisplayingResults() {
        setOptionCheckedByName("executeOptions:keepDisplayingResultsContainer:keepDisplayingResults", false);
        return this;
    }

    public ProjectionsTab<F> selectTabProjections() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.projections");
        Selenide.sleep(2000);
        return new ProjectionsTab<F>(this, element);
    }

    public ProjectionsTab<F> selectTabCases() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.cases");
        Selenide.sleep(2000);
        return new ProjectionsTab<F>(this, element);
    }


    public SummaryPanel<UserPage> summary() {

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
