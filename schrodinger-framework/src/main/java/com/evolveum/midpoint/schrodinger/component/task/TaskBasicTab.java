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

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicTab;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;

import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * @author lskublik
 */
public class TaskBasicTab extends AssignmentHolderBasicTab<TaskPage> {
    public TaskBasicTab(TaskPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public String utility() {
        return form().findProperty("category").$(By.tagName("input")).getValue();
    }

    public TaskBasicTab assertUtilityValueEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, form().findProperty("category").$(By.tagName("input")).getValue(), "Utility value doesn't match");
        return this;
    }
}