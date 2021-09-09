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

import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.getModalWindowSelenideElement;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.DetailsNavigationPanel;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by honchar
 */
public abstract class AssignmentHolderDetailsPage<P extends AssignmentHolderDetailsPage> extends BasicPage {

    private boolean useTabbedPanel = false; //if gui uses old TabbedPanel, set to true

    public AssignmentHolderDetailsPage() {
        this(false);
    }

    public AssignmentHolderDetailsPage(boolean useTabbedPanel) {
        this.useTabbedPanel = useTabbedPanel;
    }

    public BasicPage clickBack() {
        $(Schrodinger.byDataResourceKey("pageAdminFocus.button.back"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new BasicPage();
    }

    public ProgressPage clickSave() {
        $(Schrodinger.byDataId("save")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ProgressPage();
    }

    public PreviewPage clickPreview() {
        getPreviewButton().waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        feedback().isSuccess();
        return new PreviewPage();
    }

    public AssignmentHolderDetailsPage assertPreviewButtonIsNotVisible() {
        assertion.assertFalse(getPreviewButton().is(Condition.visible), "Preview button shouldn't be visible.");
        return this;
    }

    private SelenideElement getPreviewButton() {
        return $(Schrodinger.byDataId("previewChanges"));
    }

    public TabPanel getTabPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "tabPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        tabPanelElement.waitUntil(Condition.appear, MidPoint.TIMEOUT_LONG_20_S);
        return new TabPanel<>(this, tabPanelElement);
    }

    public DetailsNavigationPanel<AssignmentHolderDetailsPage<P>> getNavigationPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "navigation"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        tabPanelElement.waitUntil(Condition.appear, MidPoint.TIMEOUT_LONG_20_S);
        return new DetailsNavigationPanel<>(this, tabPanelElement);
    }

    public AssignmentHolderBasicPanel<P> selectBasicPanel() {
        return new AssignmentHolderBasicPanel<>((P) this, getNavigationPanelSelenideElement("Basic"));
    }

    public AssignmentsPanel<P> selectAssignmentsPanel() {
        return new AssignmentsPanel<>((P) this, getNavigationPanelSelenideElement("Assignments"));
    }

    protected SelenideElement getNavigationPanelSelenideElement(String tabTitle) {
        if (useTabbedPanel) {
            return getTabPanel().clickTab(tabTitle)
                    .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        } else {
            return getNavigationPanel().selectPanelByName(tabTitle);
        }
    }

    public ObjectBrowserModal<AssignmentHolderDetailsPage<P>> changeArchetype() {
        if ($(Schrodinger.byDataResourceKey("PageAdminObjectDetails.button.changeArchetype")).exists()) {
            $(Schrodinger.byDataResourceKey("PageAdminObjectDetails.button.changeArchetype"))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            return new ObjectBrowserModal<AssignmentHolderDetailsPage<P>>(this, getModalWindowSelenideElement());
        }
        return null;
    }
}
