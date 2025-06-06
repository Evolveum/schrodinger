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
package com.evolveum.midpoint.schrodinger.page.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.PanelWithTableAndPrismView;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismContainerPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.user.*;
import com.evolveum.midpoint.schrodinger.page.FocusPage;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UserPage extends FocusPage<UserPage> {

    public UserPersonasPanel selectPersonasPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Personas");
        return new UserPersonasPanel(this, element);
    }

    public UserTasksPanel selectTasksPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Cases");
        return new UserTasksPanel(this, element);
    }

    public UserHistoryPanel selectHistoryPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("History");
        return new UserHistoryPanel(this, element);
    }

    public UserDelegationsPanel<UserPage> selectDelegationsPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Delegations");
        return new UserDelegationsPanel<>(this, element);
    }

    public UserDelegatedToMePanel selectDelegatedToMePanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Delegated to me");
        return new UserDelegatedToMePanel(this, element);
    }

    @Override
    public ProjectionsPanel<UserPage> selectProjectionsPanel() {
        return super.selectProjectionsPanel();
    }

    public UserPage assertName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("name", expectedValue);
        return this;
    }

    public UserPage assertGivenName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("givenName", expectedValue);
        return this;
    }

    public UserPage assertFamilyName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("familyName", expectedValue);
        return this;
    }

    public UserPage assertFullName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("fullName", expectedValue);
        return this;
    }

    public UserPage assertDescription(String expectedValue) {
        selectBasicPanel().form().assertPropertyTextareaValueContainsText("description", expectedValue);
        return this;
    }

    public UserPasswordPanel<UserPage> selectPasswordPanel() {
        return new UserPasswordPanel(this, getNavigationPanelSelenideElement("Password"));
    }

    public UserPage addPasswordAttributeValue(String value) {
        selectPasswordPanel().setPasswordValue(value);
        return this;
    }

    public UserApplicationsPanel selectApplicationsPanel() {
        return new UserApplicationsPanel(this, getNavigationPanelSelenideElement("Applications"));
    }

}
