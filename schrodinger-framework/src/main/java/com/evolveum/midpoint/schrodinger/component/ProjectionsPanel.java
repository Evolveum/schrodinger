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

package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.ProjectionFormPanelWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetProjectionModal;
import com.evolveum.midpoint.schrodinger.component.user.ProjectionsDropDown;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ProjectionsPanel<P extends AssignmentHolderDetailsPage> extends TabWithTableAndPrismView<FocusPage> {

    public ProjectionsPanel(FocusPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ProjectionsDropDown<ProjectionsPanel<P>> clickHeaderActionDropDown() {

        $(By.tagName("thead"))
                .$(Schrodinger.byDataId("inlineMenuPanel"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();

        SelenideElement dropDownMenu = $(Schrodinger.byElementAttributeValue("ul", "class", "dropdown-menu pull-right"));

        return new ProjectionsDropDown<ProjectionsPanel<P>>(this, dropDownMenu);
    }


    public AbstractTableWithPrismView<ProjectionsPanel<P>> table() {

        SelenideElement tableBox = $(Schrodinger.byDataId("div", "itemsTable"));

        return new AbstractTableWithPrismView<ProjectionsPanel<P>>(this, tableBox) {
            @Override
            public ProjectionFormPanelWithActionButtons<AbstractTableWithPrismView<ProjectionsPanel<P>>> clickByName(String name) {

                SelenideElement linkElement = $(Schrodinger.byElementValue("span", "data-s-id", "label", name));
                linkElement
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
                if (linkElement.isDisplayed() && linkElement.isEnabled()) {
                    linkElement.click();
                }

                SelenideElement prismElement = $(Schrodinger.byDataId("div", "itemDetails"))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_SHORT_4_S);

                return new ProjectionFormPanelWithActionButtons<>(this, prismElement);
            }

            @Override
            public AbstractTableWithPrismView<ProjectionsPanel<P>> selectCheckboxByName(String name) {

                $(Schrodinger.byFollowingSiblingEnclosedValue("td", "class", "check", "data-s-id", "3", name))
                        .$(By.tagName("input"))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

                return this;
            }

            @Override
            public AbstractTableWithPrismView<ProjectionsPanel<P>> selectHeaderCheckbox() {
                $(Schrodinger.byFollowingSiblingEnclosedValue("th", "class", "check", "data-s-id", "3", ""))
                        .$(By.tagName("input"))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
                return this;
            }

            @Override
            public AbstractTableWithPrismView<ProjectionsPanel<P>> removeByName(String name) {
                selectCheckboxByName(name);
                clickHeaderActionDropDown().delete();
                return this;
            }

            @Override
            public AbstractTableWithPrismView<ProjectionsPanel<P>> clickHeaderActionButton(String actionButtonStyle) {
                $(Schrodinger.byDescendantElementAttributeValue("th", "class", actionButtonStyle))
                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
                return this;
            }
        };
    }

//    public AbstractTable<ProjectionsTab> table() {
//
//        SelenideElement tableBox = $(By.cssSelector(".box.projection"));
//
//        return new AbstractTable<ProjectionsTab>(this, tableBox) {
//            @Override
//            public PrismForm<AbstractTable<ProjectionsTab>> clickByName(String name) {
//
//                $(Schrodinger.byElementValue("span", "data-s-id", "name", name))
//                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//
//                SelenideElement prismElement = $(By.cssSelector(".container-fluid.prism-object"))
//                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
//
//                return new PrismForm<>(this, prismElement);
//            }
//
//            @Override
//            public AbstractTable<ProjectionsTab> selectCheckboxByName(String name) {
//
//                $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("input", "type", "checkbox", "data-s-id", "3", name))
//                        .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//
//                return this;
//            }
//        };
//    }

    public FocusSetProjectionModal<ProjectionsPanel<P>> clickAddProjection() {
        $(Schrodinger.byElementAttributeValue("i", "class", "fa fa-plus"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return new FocusSetProjectionModal<ProjectionsPanel<P>>(this, Utils.getModalWindowSelenideElement());
    }

    public boolean projectionExists(String projectionName, String resourceName){
        table()
                .search()
                    .referencePanelByItemName("Resource")
                        .inputRefName(resourceName, resourceName)
                    .updateSearch();
        ProjectionFormPanelWithActionButtons form = table()
                .clickByName(projectionName);
        String assignmentActualName = form.getParentElement().$x(".//span[@data-s-id='displayName']")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).getText();
        form.clickCancel();
        return projectionName.equals(assignmentActualName);
    }

    public ProjectionFormPanelWithActionButtons<AbstractTableWithPrismView<ProjectionsPanel<P>>> viewProjectionDetails(String projectionName, String resourceName){
        Selenide.screenshot("beforeSearch");
        table()
                .search()
                    .referencePanelByItemName("Resource")
                        .inputRefName(resourceName, resourceName)
                    .updateSearch();
        Selenide.screenshot("afterSearch");
        ProjectionFormPanelWithActionButtons form = table()
                .clickByName(projectionName);
        ProjectionFormPanelWithActionButtons<AbstractTableWithPrismView<ProjectionsPanel<P>>> detailsPanel =
                new ProjectionFormPanelWithActionButtons<AbstractTableWithPrismView<ProjectionsPanel<P>>>(table(), $(Schrodinger.byDataId("valueForm")));
        return detailsPanel;
    }

    public ProjectionsPanel<P> assertProjectionExist(String projectionName, String resourceName) {
        assertion.assertTrue(projectionExists(projectionName, resourceName), "Projection " + projectionName + " should exist");
        return this;
    }

    public ProjectionsPanel<P> assertProjectionForResourceDoesntExist(String resourceName) {
        table()
                .search()
                 .referencePanelByItemName("Resource")
                    .inputRefName(resourceName, resourceName)
                    .updateSearch()
                    .and()
                .assertTableObjectsCountEquals(0);
        return this;
    }

    public ProjectionsPanel<P> assertProjectionEnabled(String projectionName, String resourceName) {
        table()
                .search()
                .referencePanelByItemName("Resource")
                .inputRefName(resourceName, resourceName)
                .updateSearch();
        ProjectionFormPanelWithActionButtons form = table()
                .clickByName(projectionName);
        form.assertPropertyDropdownValue("Administrative status", "Enabled");
        form.clickCancel();
        return this;
    }

    public ProjectionsPanel<P> assertProjectionDisabled(String projectionName, String resourceName) {
        table()
                .search()
                .referencePanelByItemName("Resource")
                .inputRefName(resourceName, resourceName)
                .updateSearch();
        ProjectionFormPanelWithActionButtons form = table()
                .clickByName(projectionName);
        form.assertPropertyDropdownValue("Administrative status", "Disabled");
        form.clickCancel();
        return this;
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return $(Schrodinger.byDataId("div", "itemDetails"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

}
