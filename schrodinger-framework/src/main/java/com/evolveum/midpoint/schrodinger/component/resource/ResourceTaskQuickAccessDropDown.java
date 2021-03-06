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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.DropDown;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

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

    public TaskPage clickCreateNew() {

        ElementsCollection elements = $$(Schrodinger.byElementValue("a", "data-s-id", "menuItemLink", "Create new"));
        for (SelenideElement element : elements) {

            if (element.isDisplayed()) {
                element.click();
                break;
            }
        }
        $(Schrodinger.byDataResourceKey("pageTask.basic.title")).waitUntil(Condition.exist, MidPoint.TIMEOUT_LONG_1_M);
        return new TaskPage();
    }

}
