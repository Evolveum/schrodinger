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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.DropDown;
import com.evolveum.midpoint.schrodinger.component.task.wizard.TaskTypeSelectionStep;
import com.evolveum.midpoint.schrodinger.component.task.wizard.TaskWizardPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by matus on 5/22/2018.
 */
public class ResourceTaskQuickAccessDropDown<T> extends DropDown<T> {
    public ResourceTaskQuickAccessDropDown(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T clickShowExisting() {
        $(Schrodinger.byDataResourceKey("schrodinger", "ResourceContentResourcePanel.showExisting")).parent()
                .click();

        return this.getParent();
    }

    public TaskTypeSelectionStep clickCreateTask() {
        String newTaskKey = "ResourceObjectsPanel.button.createTask";
        getParentElement()
                .$(Schrodinger.byDataResourceKey("ResourceObjectsPanel.button.createTask"))
                .parent()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
        Utils.waitForAjaxCallFinish();
        Utils.waitForMainPanelOnDetailsPage();
        return new TaskTypeSelectionStep(new TaskWizardPage());
    }

}
