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
package com.evolveum.midpoint.schrodinger.page.service;

import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicPanel;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.ClassificationsPanel;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;

/**
 * Created by honchar
 */
public class ServicePage extends AbstractRolePage {

    @Override
    public ProjectionsPanel<ServicePage> selectProjectionsPanel() {
        return super.selectProjectionsPanel();
    }

    @Override
    public AssignmentHolderBasicPanel<ServicePage> selectBasicPanel() {
        return super.selectBasicPanel();
    }

    @Override
    public AssignmentsPanel<ServicePage> selectAssignmentsPanel() {
        return super.selectAssignmentsPanel();
    }

    public ClassificationsPanel selectClassificationsPanel() {
        return new ClassificationsPanel(ServicePage.this, getNavigationPanelSelenideElement("Classifications"));
    }


    public ServicePage assertName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("name", expectedValue);
        return this;
    }

    public ServicePage assertDisplayName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("displayName", expectedValue);
        return this;
    }

    public ServicePage assertIdentifier(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("identifier", expectedValue);
        return this;
    }

}
