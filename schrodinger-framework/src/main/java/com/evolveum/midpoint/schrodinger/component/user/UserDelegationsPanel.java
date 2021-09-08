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
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.DelegationDetailsPanel;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UserDelegationsPanel extends Component<UserPage> {

    public UserDelegationsPanel(UserPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ObjectBrowserModal<UserDelegationsPanel> clickAddDelegation() {
        SelenideElement button = $(Schrodinger.byDataId("assignmentsMenu")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(Schrodinger.byElementAttributeValue("button", "data-toggle", "dropdown"));
        button.click();
        button.$(Schrodinger.bySelfOrDescendantElementAttributeValue("a", "data-s-id", "menuItemLink",
                "data-s-resource-key", "AssignmentTablePanel.menu.addDelegation"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ObjectBrowserModal<>(this, Utils.getModalWindowSelenideElement());
    }

    public ConfirmationModal<UserDelegationsPanel> clickDeleteDelegation() {
        SelenideElement button = $(Schrodinger.byDataId("assignmentsMenu")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(Schrodinger.byElementAttributeValue("button", "data-toggle", "dropdown"));
        button.click();
        button.$(Schrodinger.bySelfOrDescendantElementAttributeValue("a", "data-s-id", "menuItemLink",
                "data-s-resource-key", "AssignmentTablePanel.menu.unassign"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ConfirmationModal<>(this, Utils.getModalWindowSelenideElement());
    }

    public UserDelegationsPanel clickAllDelegationsCheckBox() {
        SelenideElement checkbox = $(Schrodinger.byDataId("assignmentsCheckAll")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        checkbox.click();
        return this;
    }

    public DelegationDetailsPanel<UserDelegationsPanel> getDelegationDetailsPanel(String delegateToUser) {
        return new DelegationDetailsPanel<>(this,
                $(By.linkText(delegateToUser))
                        .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }
}
