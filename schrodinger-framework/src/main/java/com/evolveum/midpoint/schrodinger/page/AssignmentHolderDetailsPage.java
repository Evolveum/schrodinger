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

import static com.codeborne.selenide.Selenide.$x;
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
import com.evolveum.midpoint.schrodinger.component.user.UserPasswordPanel;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

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

    public SelenideElement getButtonPanelElement() {
        return $(Schrodinger.byElementValue("legend", "Operations"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).parent();
    }

    public void clickOperationButton(String className, String elementId) {
        if (isUseTabbedPanel()) {
            $(Schrodinger.byDataId(elementId))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
        } else {
            getButtonByIconClass(className).click();
        }
    }

    public SelenideElement getButtonByIconClass(String className) {
        return getButtonPanelElement().$x(".//i[contains(@class,\"" + className + "\")]")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public BasicPage clickBack() {
        clickOperationButton("fa fa-arrow-left", "back");
        return new BasicPage();
    }

    public ProgressPage clickSave() {
        clickOperationButton("fa fa-save", "save");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ProgressPage();
    }

    public PreviewPage clickPreview() {
        clickOperationButton("fa fa-eye", "previewChanges");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        feedback().isSuccess();
        return new PreviewPage();
    }

    public AssignmentHolderDetailsPage assertPreviewButtonIsNotVisible() {
        assertion.assertFalse(getPreviewButton().is(Condition.visible), "Preview button shouldn't be visible.");
        return this;
    }

    private SelenideElement getPreviewButton() {
        return getButtonByIconClass("fa fa-eye");
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
        return new AssignmentsPanel<>((P) this, getNavigationPanelSelenideElement("Assignments", "All"));
    }

    public AssignmentsPanel<P> selectIndirectAssignmentsPanel() {
        return new AssignmentsPanel<>((P) this, getNavigationPanelSelenideElement("Assignments", "All direct/indirect assignments"));
    }

//
//    public AssignmentHolderBasicPanel<P> selectBasicPanel() {
//        return new AssignmentHolderBasicPanel<>((P) this, getNavigationPanelSelenideElement("Basic"));
//    }

    public SelenideElement getNavigationPanelSelenideElement(String... panelTitle) {
        Utils.waitForAjaxCallFinish();
        if (isUseTabbedPanel()) {
            return getTabPanel().clickTab(panelTitle[0])
                    .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        } else {
            return getNavigationPanel().selectPanelByName(panelTitle);
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

    public boolean isUseTabbedPanel() {
        Utils.waitForMainPanelOnDetailsPage();
        return !($x(".//div[contains(@class,\"details-panel-navigation\")]").isDisplayed());
    }
}
