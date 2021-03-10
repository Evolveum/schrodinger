/*
 * Copyright (c) 2010-2020 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
 */
package com.evolveum.midpoint.schrodinger.component.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.self.RequestRolePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class UserAssignmentsViewTab extends RequestRoleTab {
    public UserAssignmentsViewTab(RequestRolePage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public UserAssignmentsViewTab selectAvailableRelation(String relationName) {
        $(Schrodinger.byDataId("sourceUserRelations")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(byText(relationName)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }
}
