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
package com.evolveum.midpoint.schrodinger.component.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/2/2018.
 */
public class ProjectionsDropDown<T> extends TableHeaderDropDownMenu<T> {

    public ProjectionsDropDown(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T enable() {
        return clickByDataResourceKey("pageAdminFocus.button.enable");
    }

    public T disable() {
        return clickByDataResourceKey("pageAdminFocus.button.disable");
    }

    public T unlink() {
        return clickByDataResourceKey("pageAdminFocus.button.unlink");
    }

    public T unlock() {
        return clickByDataResourceKey("pageAdminFocus.button.unlock");
    }

    public T edit() {
        return clickByDataResourceKey("pageAdminFocus.button.unlock");
    }

//    public FocusSetProjectionModal<T> addProjection() {
//        $(Schrodinger.byElementValue("a", "data-s-id", "menuItemLink", "\n" +
//                "        Add projection")).waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//
//        SelenideElement actualModal = $(Schrodinger.byElementAttributeValue("div", "aria-labelledby", "Choose object"));
//
//        return new FocusSetProjectionModal<>(this.getParent(), actualModal);
//    }

    public ConfirmationModal<ProjectionsDropDown<T>> delete() {
        $(Schrodinger.byElementValue("a", "data-s-id", "menuItemLink", "\n" +
                "        Delete")).waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        return new ConfirmationModal<>(this, Utils.getModalWindowSelenideElement());
    }
}
