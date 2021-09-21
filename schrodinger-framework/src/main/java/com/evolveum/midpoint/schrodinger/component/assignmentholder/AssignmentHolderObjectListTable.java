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

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.modal.ExportPopupPanel;

import com.evolveum.midpoint.schrodinger.util.Utils;

import jdk.jshell.execution.Util;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by honchar
 */
public abstract class AssignmentHolderObjectListTable<P, PD extends AssignmentHolderDetailsPage> extends TableWithPageRedirect<P> {

    public AssignmentHolderObjectListTable(P parent, SelenideElement parentElement){
        super(parent, parentElement);
    }


    @Override
    public AssignmentHolderObjectListTable<P, PD> selectCheckboxByName(String name) {
        rowByColumnLabel(getNameColumnLabel(), name).clickCheckBox();
        return this;
    }

    @Override
    public PD clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//        Selenide.sleep(getDetailsPageLoadingTimeToWait());
        PD detailsPage = getObjectDetailsPage();
        if (detailsPage.isUseTabbedPanel()) {
            detailsPage.getTabPanel();
        } else {
            detailsPage.getNavigationPanel();
        }
        return detailsPage;
    }

    public long getDetailsPageLoadingTimeToWait() {
        return MidPoint.TIMEOUT_DEFAULT_2_S;
    }

    public PD clickByPartialName(String name) {

        getParentElement()
                .$(Schrodinger.byDataId("tableContainer"))
                .$(By.partialLinkText(name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return getObjectDetailsPage();
    }

    @Override
    public Search<? extends AssignmentHolderObjectListTable<P, PD>> search() {
        SelenideElement searchElement = getParentElement().$(By.cssSelector(".form-inline.pull-right.search-form"));

        return new Search<>(this, searchElement);
    }

    @Override
    public AssignmentHolderObjectListTable<P, PD> selectAll() {

        $(Schrodinger.bySelfOrAncestorElementAttributeValue("input", "type", "checkbox", "data-s-id", "topToolbars"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public SelenideElement getToolbarButtonByCss(String iconCssClass){
        return getButtonToolbar().$(By.cssSelector(iconCssClass));
    }

    public SelenideElement getToolbarButtonByTitle(String buttonTitle){
        return getButtonToolbar().$x(".//a[@title='" + buttonTitle + "']");
    }

    public PD newObjectButtonByCssClick(String iconCssClass){
        if (!getToolbarButtonByTitle(iconCssClass).isDisplayed()) {
            getToolbarButtonByCss(".fa.fa-plus")
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
            Selenide.sleep(2000);
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//i[contains(@class, \"" + iconCssClass + "\")]").click();
            Selenide.sleep(2000);
        }
        return getObjectDetailsPage();
    }

    public PD newObjectButtonByTitleClick(String buttonTitle){
        if (!getToolbarButtonByTitle(buttonTitle).isDisplayed()) {
            getToolbarButtonByCss(".fa.fa-plus")
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
            Selenide.sleep(2000);
        }
        if (Utils.isModalWindowSelenideElementVisible()) {
            Utils.getModalWindowSelenideElement().$x(".//button[@title=\"" + buttonTitle + "\"]").click();
            Selenide.sleep(2000);
        }
        return getObjectDetailsPage();
    }

    public ExportPopupPanel<P> clickExportButton() {
        getToolbarButtonByCss(".fa.fa-download")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(2000);
        return new ExportPopupPanel<>(getParent(), Utils.getModalWindowSelenideElement());
    }

    public AssignmentHolderObjectListTable<P, PD> clickRefreshButton() {
        getToolbarButtonByCss(".fa.fa-refresh")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(2000);
        return this;
    }

    public PD newObjectCollectionButtonClickPerformed(String mainButtonIconCssClass, String objCollectionButtonIconCssClass){
        SelenideElement mainButtonElement = getButtonToolbar()
                .$(Schrodinger.bySelfOrDescendantElementAttributeValue("button", "data-s-id", "mainButton",
                        "class", mainButtonIconCssClass))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        if (!mainButtonElement.parent().$x(".//div[@data-s-id='additionalButton']").exists() ||
                "false".equals(mainButtonElement.getAttribute("aria-expanded"))) {
            mainButtonElement.click();
            Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
            mainButtonElement
                    .waitUntil(Condition.attribute("aria-expanded", "true"), MidPoint.TIMEOUT_SHORT_4_S);
        }
        if (StringUtils.isNotEmpty(objCollectionButtonIconCssClass)
                && mainButtonElement.parent().parent().$x(".//div[@data-s-id='additionalButton']").exists()) {
            mainButtonElement.parent()
                    .$(By.cssSelector(objCollectionButtonIconCssClass))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                    .click();
            Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        }
        return getObjectDetailsPage();
    }

    public int countDropdownButtonChildrenButtons(String mainButtonIconCssClass) {
        SelenideElement mainButtonElement = getToolbarButtonByCss(mainButtonIconCssClass)
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        mainButtonElement.click();
        if (mainButtonElement.exists()) {
            ElementsCollection childrenButtonCollection = mainButtonElement.parent().parent()
                    .$(By.cssSelector(".dropdown-menu.auto-width"))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .findAll(By.tagName("i"));
            return childrenButtonCollection != null ? childrenButtonCollection.size() : 0;
        }
        return 0;
    }

    public abstract PD getObjectDetailsPage();

    protected String getNameColumnLabel() {
        return "Name";
    }

    public AssignmentHolderObjectListTable<P, PD> assertNewObjectDropdownButtonsCountEquals(String mainButtonIconCssClass, int expectedButtonsCount) {
        assertion.assertEquals(expectedButtonsCount, countDropdownButtonChildrenButtons(mainButtonIconCssClass), "The number of the dropdown buttons "
                + "for the button with '" + mainButtonIconCssClass + "' css class doesn't match to " + expectedButtonsCount);
        return this;
    }

}
