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

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.user.*;
import com.evolveum.midpoint.schrodinger.page.FocusPage;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UserPage extends FocusPage<UserPage> {

    public UserPersonasPanel selectTabPersonas() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.personas");

        return new UserPersonasPanel(this, element);
    }

    public UserTasksPanel selectTabTasks() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.tasks");

        return new UserTasksPanel(this, element);
    }

    public UserHistoryPanel selectTabHistory() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.objectHistory");

        return new UserHistoryPanel(this, element);
    }

    public UserDelegationsPanel selectTabDelegations() {
        SelenideElement element = getTabPanel().clickTab("FocusType.delegations");

        return new UserDelegationsPanel(this, element);
    }

    public UserDelegatedToMePanel selectTabDelegatedToMe() {
        SelenideElement element = getTabPanel().clickTab("FocusType.delegatedToMe");

        return new UserDelegatedToMePanel(this, element);
    }

    @Override
    public ProjectionsPanel<UserPage> selectTabProjections() {
        return super.selectTabProjections();
    }

    public UserPage assertName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("name", expectedValue);
        return this;
    }

    public UserPage assertGivenName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("givenName", expectedValue);
        return this;
    }

    public UserPage assertFamilyName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("familyName", expectedValue);
        return this;
    }

    public UserPage assertFullName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("fullName", expectedValue);
        return this;
    }
}
