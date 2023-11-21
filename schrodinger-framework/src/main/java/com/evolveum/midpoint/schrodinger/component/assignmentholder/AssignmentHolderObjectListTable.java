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
package com.evolveum.midpoint.schrodinger.component.assignmentholder;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.modal.ExportPopupPanel;

import com.evolveum.midpoint.schrodinger.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by honchar
 */
public abstract class AssignmentHolderObjectListTable<P, PD extends AssignmentHolderDetailsPage> extends TableWithPageRedirect<P> {

    public AssignmentHolderObjectListTable(P parent, SelenideElement parentElement){
        super(parent, parentElement);
    }


    @Override
    public AssignmentHolderObjectListTable<P, PD> selectCheckboxByName(String name) {
        findRowByColumnLabel(getNameColumnLabel(), name).clickCheckBox();
        return this;
    }

    @Override
    public PD clickByName(String name) {
        Utils.waitForAjaxCallFinish();
        getParentElement().$x(".//span[@data-s-id='label' and contains(text(), '" + name + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        PD detailsPage = getObjectDetailsPage();
        Utils.waitForMainPanelOnDetailsPage();
        return detailsPage;
    }

    public Duration getDetailsPageLoadingTimeToWait() {
        return MidPoint.TIMEOUT_DEFAULT_2_S;
    }

    public PD clickByPartialName(String name) {

        getParentElement()
                .$(Schrodinger.byDataId("tableContainer"))
                .$(By.partialLinkText(name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds()); //todo remove after mid-7275 fix

        return getObjectDetailsPage();
    }

    @Override
    public Search<? extends AssignmentHolderObjectListTable<P, PD>> search() {
        SelenideElement searchElement = findSearchElement();

        return new Search<>(this, searchElement);
    }

    protected SelenideElement findSearchElement() {
        return getParentElement().$(By.cssSelector(".search-panel-form"));
    }

    @Override
    public AssignmentHolderObjectListTable<P, PD> selectAll() {
        $(Schrodinger.bySelfOrAncestorElementAttributeValue("input", "type", "checkbox", "data-s-id", "topToolbars"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public PD newObjectButtonByCssClick(String iconCssClass){
        if (!getToolbarButtonByCss(iconCssClass).isDisplayed()) {
            getToolbarButtonByCss("fa fa-plus")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
//            Selenide.sleep(2000);
        } else {
            getToolbarButtonByCss(iconCssClass).click();
        }
        Utils.waitForAjaxCallFinish();
        try {
            Utils.getModalWindowSelenideElement();
        } catch (Error e) {
            //nothing to do here; the window appears depending on configuration
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//i[contains(@class, \"" + iconCssClass + "\")]").click();
            Utils.waitForAjaxCallFinish();
        }
        Utils.waitForMainPanelOnDetailsPage();
        return getObjectDetailsPage();
    }

    public PD newObjectButtonByTitleClick(String buttonTitle){
        if (!getToolbarButtonByTitleKey(buttonTitle).isDisplayed()) {
            getToolbarButtonByCss("fa fa-plus")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
            Selenide.sleep(2000);
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//button[@title=\"" + buttonTitle + "\"]").click();
        } else {
            getToolbarButtonByTitleKey(buttonTitle).click();
        }
        Utils.waitForMainPanelOnDetailsPage();
        return getObjectDetailsPage();
    }

    public ExportPopupPanel<P> clickExportButton() {
        getToolbarButtonByCss("fa fa-download")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(2000);
        return new ExportPopupPanel<>(getParent(), Utils.getModalWindowSelenideElement());
    }

    public AssignmentHolderObjectListTable<P, PD> clickRefreshButton() {
        getToolbarButtonByCss("fa fa-sync-alt")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(2000);
        return this;
    }

    public PD newObjectCollectionButtonClickPerformed(String mainButtonIconCssClass, String objCollectionButtonIconCssClass){
        SelenideElement mainButtonElement = getButtonToolbar()
                .$(Schrodinger.bySelfOrDescendantElementAttributeValue("button", "data-s-id", "mainButton",
                        "class", mainButtonIconCssClass))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        if (!mainButtonElement.parent().$x(".//div[@data-s-id='additionalButton']").exists() ||
                "false".equals(mainButtonElement.getAttribute("aria-expanded"))) {
            mainButtonElement.click();
            Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
            mainButtonElement
                    .shouldBe(Condition.attribute("aria-expanded", "true"), MidPoint.TIMEOUT_SHORT_4_S);
        }
        if (StringUtils.isNotEmpty(objCollectionButtonIconCssClass)
                && mainButtonElement.parent().parent().$x(".//div[@data-s-id='additionalButton']").exists()) {
            mainButtonElement.parent()
                    .$(By.cssSelector(objCollectionButtonIconCssClass))
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                    .click();
            Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
        }
        return getObjectDetailsPage();
    }

    public int countNewObjectButtonsInPopup(String mainButtonIconCssClass) {
        SelenideElement mainButtonElement = getToolbarButtonByCss(mainButtonIconCssClass)
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        mainButtonElement.click();
        try {
            Utils.getModalWindowSelenideElement();
        } catch (Error e) {
            //nothing to do here, the popup can appear or not; depends on configuration;
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            SelenideElement modal = Utils.getModalWindowSelenideElement();
            ElementsCollection childrenButtonCollection = modal.$$x(".//div[@data-s-id='additionalButton']");
            int count = childrenButtonCollection != null ? childrenButtonCollection.size() : 0;
            modal.$x(".//a[@data-s-id='cancelButton']").click();
            Utils.waitForAjaxCallFinish();
            modal.shouldBe(Condition.disappear, MidPoint.TIMEOUT_LONG_20_S);
            return count;
        }
        return 0;
    }

    public abstract PD getObjectDetailsPage();

    protected String getNameColumnLabel() {
        return "Name";
    }

    public AssignmentHolderObjectListTable<P, PD> assertTemplatePanelButtonsCountEquals(int expectedButtonsCount) {
        int realButtonsCount = countNewObjectButtonsInPopup("fa fa-plus");
        assertion.assertTrue(realButtonsCount == expectedButtonsCount,
                "Template panel doesn't contain an expected buttons count, expected: " + expectedButtonsCount +
                        ", real count: " + realButtonsCount);
        return this;
    }

    public AssignmentHolderObjectListTable<P, PD> assertTemplatePanelVisible() {
        SelenideElement templatePanel = $(Schrodinger.byDataId("newObjectSelectionButtonPanel"));
        assertion.assertTrue(templatePanel.exists(),  "Template panel doesn't exist.");
        return this;
    }
}
