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
package com.evolveum.midpoint.schrodinger.component.task;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.service.ServicesPageTable;
import com.evolveum.midpoint.schrodinger.page.task.ListTasksPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 6/25/2018.
 */
public class TasksPageTable extends AssignmentHolderObjectListTable<ListTasksPage, TaskPage> {
    public TasksPageTable(ListTasksPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<TasksPageTable> clickHeaderActionDropDown() {
        //todo looks like the same code for all tables
        $(Schrodinger.bySelfOrAncestorElementAttributeValue("button", "data-toggle", "dropdown", "class", "sortableLabel"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelenideElement dropDown = $(Schrodinger.byDataId("ul", "dropDownMenu"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<TasksPageTable>(this, dropDown);
    }

    @Override
    public TaskPage getObjectDetailsPage(){
        return new TaskPage();
    }

    @Override
    public Duration getDetailsPageLoadingTimeToWait() {
        return MidPoint.TIMEOUT_MEDIUM_6_S;
    }

    public ConfirmationModal<TasksPageTable> suspendTask() {
        return suspendTask(null, null);
    }

    public ConfirmationModal<TasksPageTable> suspendTaskByName(String nameValue) {
        return suspendTask("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> suspendTask(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.suspendTask");
    }

    public ConfirmationModal<TasksPageTable> resumeTask() {
        return resumeTask(null, null);
    }

    public ConfirmationModal<TasksPageTable> resumeTaskByName(String nameValue) {
        return resumeTask("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> resumeTask(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.resumeTask");
    }

    public ConfirmationModal<TasksPageTable> runNowTask() {
        return runNowTask(null, null);
    }

    public ConfirmationModal<TasksPageTable> runNowTaskByName(String nameValue) {
        return runNowTask("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> runNowTask(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.scheduleTask");
    }

    public ConfirmationModal<TasksPageTable> deleteTask() {
        return deleteTask(null, null);
    }

    public ConfirmationModal<TasksPageTable> deleteTaskByName(String nameValue) {
        return deleteTask("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> deleteTask(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.deleteTask");
    }

    public ConfirmationModal<TasksPageTable> reconcileWorkers() {
        return reconcileWorkers(null, null);
    }

    public ConfirmationModal<TasksPageTable> reconcileWorkersByName(String nameValue) {
        return reconcileWorkers("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> reconcileWorkers(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.reconcileWorkers");
    }

    public ConfirmationModal<TasksPageTable> suspendRootOnly() {
        return suspendRootOnly(null, null);
    }

    public ConfirmationModal<TasksPageTable> suspendRootOnlyByName(String nameValue) {
        return suspendRootOnly("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> suspendRootOnly(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.suspendRootOnly");
    }

    public ConfirmationModal<TasksPageTable> resumeRootOnly() {
        return resumeRootOnly(null, null);
    }

    public ConfirmationModal<TasksPageTable> resumeRootOnlyByName(String nameValue) {
        return resumeRootOnly("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> resumeRootOnly(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.resumeRootOnly");
    }

    public ConfirmationModal<TasksPageTable> deleteWorkersAndWorkState() {
        return deleteWorkersAndWorkState(null, null);
    }

    public ConfirmationModal<TasksPageTable> deleteWorkersAndWorkStateByName(String nameValue) {
        return deleteWorkersAndWorkState("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> deleteWorkersAndWorkState(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.deleteWorkersAndWorkState");
    }

    public ConfirmationModal<TasksPageTable> deleteWorkState() {
        return deleteWorkState(null, null);
    }

    public ConfirmationModal<TasksPageTable> deleteWorkStateByName(String nameValue) {
        return deleteWorkState("ObjectType.name", nameValue);
    }

    public ConfirmationModal<TasksPageTable> deleteWorkState(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.deleteWorkState");
    }

    public ConfirmationModal<TasksPageTable> deleteAllClosedTasks(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageTasks.button.deleteAllClosedTasks");
    }

}
