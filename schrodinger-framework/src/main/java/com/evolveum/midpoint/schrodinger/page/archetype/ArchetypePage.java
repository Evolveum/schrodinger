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
package com.evolveum.midpoint.schrodinger.page.archetype;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.ProjectionsPanel;
import com.evolveum.midpoint.schrodinger.component.archetype.ArchetypePolicyPanel;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ArchetypePage extends AbstractRolePage<ArchetypePage> {

    @Override
    public ProjectionsPanel<ArchetypePage> selectProjectionsPanel() {
        return super.selectProjectionsPanel();
    }

    @Override
    public AssignmentsPanel<ArchetypePage> selectAssignmentsPanel() {
        return super.selectAssignmentsPanel();
    }

    public ArchetypePolicyPanel selectArchetypePolicyPanel() {
        SelenideElement element = getNavigationPanelSelenideElement("Archetype policy")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ArchetypePolicyPanel(this, element);
    }

    public ArchetypePage assertName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("name", expectedValue);
        return this;
    }

    public ArchetypePage assertDisplayName(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("displayName", expectedValue);
        return this;
    }

    public ArchetypePage assertIdentifier(String expectedValue) {
        selectBasicPanel().form().assertPropertyInputValue("identifier", expectedValue);
        return this;
    }

}
