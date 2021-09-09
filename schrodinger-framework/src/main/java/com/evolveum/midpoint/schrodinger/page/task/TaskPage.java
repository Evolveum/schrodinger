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
package com.evolveum.midpoint.schrodinger.page.task;

import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.getModalWindowSelenideElement;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.task.*;

import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.SummaryPanel;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.PreviewPage;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by Viliam Repan (lazyman).
 */
public class TaskPage extends AssignmentHolderDetailsPage<TaskPage> {

    public PreviewPage clickPreviewChanges() {
        $(Schrodinger.byDataId("previewChanges")).click();
        return new PreviewPage();
    }

    public SummaryPanel<TaskPage> summary() {

        SelenideElement summaryBox = $(By.cssSelector("div.info-box-content"));

        return new SummaryPanel(this, summaryBox);
    }

    public ProgressPage clickSaveAndRun() {
        $(Schrodinger.byDataId("saveAndRun")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new ProgressPage();
    }

    public TaskPage clickResume() {

        $(Schrodinger.byDataResourceKey("span", "pageTaskEdit.button.resume")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public TaskPage resumeStopRefreshing() {

        $(Schrodinger.byElementAttributeValue("span", "title", "Resume refreshing")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public TaskPage clickRunNow() {
        $(Schrodinger.byDataResourceKey("span", "pageTaskEdit.button.runNow")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return this;
    }

    public TaskPage clickSuspend() {
        $(Schrodinger.byDataResourceKey("span", "pageTaskEdit.button.suspend")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public boolean isRunNowVisible(){
        return $(Schrodinger.byDataResourceKey("span", "pageTaskEdit.button.runNow")).is(Condition.visible);
    }

    public TaskPage downloadReport() {
        $(Schrodinger.byDataResourceKey("PageTask.download.report"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public ConfirmationModal<TaskPage> cleanupEnvironmentalPerformanceInfo() {
        $(By.cssSelector(".fa.fa-area-chart"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new ConfirmationModal<TaskPage>(this, getModalWindowSelenideElement());
    }

    public ConfirmationModal<TaskPage> cleanupResults() {
        $(By.cssSelector(".fa.fa-list-alt"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new ConfirmationModal<TaskPage>(this, getModalWindowSelenideElement());
    }

    public TaskPage refreshNow() {
        $(Schrodinger.byDataResourceKey("autoRefreshPanel.refreshNow"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public TaskPage resumeRefreshing() {
        $(Schrodinger.byDataResourceKey("autoRefreshPanel.resumeRefreshing"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    @Override
    public AssignmentHolderBasicPanel<TaskPage> selectBasicPanel() {
        return new TaskBasicPanel(this, getNavigationPanelSelenideElement(("Basic")));
    }

    @Override
    public AssignmentsPanel<TaskPage> selectAssignmentsPanel() {
        return null;
    }

    public AssignmentHolderBasicPanel<TaskPage> selectSchedulePanel(){
        return new AssignmentHolderBasicPanel<TaskPage>(this, getNavigationPanelSelenideElement(("Schedule")));
    }

    public OperationStatisticsPanel selectOperationStatisticsPanel() {
        return new OperationStatisticsPanel(this, getNavigationPanelSelenideElement(("Operation statistics")));
    }

    public EnvironmentalPerformancePanel selectEnvironmentalPerformancePanel() {
        return new EnvironmentalPerformancePanel(this, getNavigationPanelSelenideElement(("Environmental performance")));
    }

    public InternalPerformancePanel selectInternalPerformanceTab() {
        return new InternalPerformancePanel(this, getNavigationPanelSelenideElement(("Internal performance")));
    }

    public ResultPanel selectResultPanel() {
        return new ResultPanel(this, getNavigationPanelSelenideElement(("Result")));
    }

     public ErrorsPanel selectErrorsPanel() {
        return new ErrorsPanel(this, getNavigationPanelSelenideElement(("Title")));
    }

    public TaskPage setHandlerUriForNewTask(String handler) {
        SelenideElement handlerElement = $(Schrodinger.byDataResourceKey("a", "TaskHandlerSelectorPanel.selector.header"));
        selectBasicPanel().form().addAttributeValue("handlerUri", handler);
//        $(Schrodinger.byElementAttributeValue("li", "textvalue", handler)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        handlerElement.waitWhile(Condition.exist, MidPoint.TIMEOUT_MEDIUM_6_S);
        return this;
    }
}
