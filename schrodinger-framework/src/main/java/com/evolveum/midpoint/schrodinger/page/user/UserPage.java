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

import com.evolveum.midpoint.schrodinger.component.ProjectionsTab;
import com.evolveum.midpoint.schrodinger.component.user.*;
import com.evolveum.midpoint.schrodinger.page.FocusPage;

import org.testng.Assert;

/**
 * Created by Viliam Repan (lazyman).
 */
public class UserPage extends FocusPage<UserPage> {

    public UserPersonasTab selectTabPersonas() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.personas");

        return new UserPersonasTab(this, element);
    }

    public UserTasksTab selectTabTasks() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.tasks");

        return new UserTasksTab(this, element);
    }

    public UserHistoryTab selectTabHistory() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.objectHistory");

        return new UserHistoryTab(this, element);
    }

    public UserDelegationsTab selectTabDelegations() {
        SelenideElement element = getTabPanel().clickTab("FocusType.delegations");

        return new UserDelegationsTab(this, element);
    }

    public UserDelegatedToMeTab selectTabDelegatedToMe() {
        SelenideElement element = getTabPanel().clickTab("FocusType.delegatedToMe");

        return new UserDelegatedToMeTab(this, element);
    }

    @Override
    public ProjectionsTab<UserPage> selectTabProjections() {
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
