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

import com.codeborne.selenide.*;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.DetailsNavigationPanel;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.ElementClickInterceptedException;

/**
 * Created by honchar
 */
public abstract class AssignmentHolderDetailsPage<P extends AssignmentHolderDetailsPage> extends BasicPage {

    public AssignmentHolderDetailsPage() {
    }

    public SelenideElement getButtonPanelElement() {
        return $x(".//fieldset[contains(@class, 'objectButtons')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).parent();
    }

    public void clickOperationButtonByClassName(String className) {
        SelenideElement button = getButtonByIconClass(className);
        try {
            button.scrollIntoView(false);
            button.click();
        } catch (ElementClickInterceptedException e) {
            button.parent().click();
        }
    }

    public void clickOperationButtonByTitleKey(String titleKey) {
        SelenideElement button = getButtonPanelElement()
                .$(Schrodinger.byDataResourceKey(titleKey));
        try {
            button.scrollIntoView(false);
            button.click();
        } catch (ElementClickInterceptedException e) {
            button.parent().click();
        }
    }

    public SelenideElement getButtonByIconClass(String className) {
        return getButtonPanelElement().$x(".//i[contains(@class,\"" + className + "\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public BasicPage clickBack() {
        clickOperationButtonByClassName("fa fa-arrow-left");
        return new BasicPage();
    }

    public ProgressPage clickSave() {
        clickOperationButtonByClassName("fa fa-save");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new ProgressPage();
    }

    public PreviewPage clickPreview() {
        clickOperationButtonByClassName("fa fa-eye");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        feedback().isSuccess();
        return new PreviewPage();
    }

    public AssignmentHolderDetailsPage assertPreviewButtonIsNotVisible() {
        boolean buttonExists = getButtonPanelElement().$x(".//i[contains(@class,\"fa fa-eye\")]").exists();
        boolean buttonVisible = buttonExists && getButtonPanelElement().$x(".//i[contains(@class,\"fa fa-eye\")]").isDisplayed();
        assertion.assertFalse(buttonVisible, "Preview button shouldn't be visible.");
        return this;
    }

    private SelenideElement getPreviewButton() {
        return getButtonByIconClass("fa fa-eye");
    }

    public DetailsNavigationPanel<AssignmentHolderDetailsPage<P>> getNavigationPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "navigation"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        tabPanelElement.shouldBe(Condition.appear, MidPoint.TIMEOUT_LONG_20_S);
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
        Utils.waitForMainPanelOnDetailsPage();
        Utils.waitForAjaxCallFinish();
        return getNavigationPanel().selectPanelByName(panelTitle);
    }

    public ObjectBrowserModal<AssignmentHolderDetailsPage<P>> changeArchetype() {
        if ($(Schrodinger.byDataResourceKey("PageAdminObjectDetails.button.changeArchetype")).exists()) {
            $(Schrodinger.byDataResourceKey("PageAdminObjectDetails.button.changeArchetype"))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            return new ObjectBrowserModal<AssignmentHolderDetailsPage<P>>(this, getModalWindowSelenideElement());
        }
        return null;
    }
}
