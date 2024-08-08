/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class DefinedTasksTable<T> extends TableWithPageRedirect<T, TaskPage, DefinedTasksTable<T>> {

    public DefinedTasksTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public TaskPage clickByName(String name) {
        Utils.waitForAjaxCallFinish();
        getParentElement().$x(".//span[@data-s-id='title' and contains(text(), '" + name + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForSummaryDisplayNameOnDetailsPage(name);
        return new TaskPage();
    }

    @Override
    public TaskPage getObjectDetailsPage() {
        return new TaskPage();
    }
}
