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

package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.induced.ApplicationResourceStep;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class InducementsPanel<P extends AbstractRolePage> extends AssignmentsPanel<P> {

    public InducementsPanel(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public FocusSetAssignmentsModal<InducementsPanel<P>> clickAddInducement() {
        return super.clickAddAssignment();
    }

    public ApplicationResourceStep<InducementsPanel<P>> clickAddApplicationResource() {
        super.clickAddAssignment();
        return new ApplicationResourceStep<>(this);
    }

    public boolean inducementExists(String inducementName){
        return super.assignmentExists(inducementName);
    }

    public InducementsPanel<P> selectTypeAll() {
        selectType("All");
        return this;
    }

    public InducementsPanel<P> selectTypeRole() {
        selectType("Role");
        return this;
    }

    public InducementsPanel<P> selectTypeOrg() {
        selectType("Organization");
        return this;
    }

    public InducementsPanel<P> selectTypeService() {
        selectType("Service");
        return this;
    }

    public InducementsPanel<P> selectTypeResource() {
        selectType("Resource");
        return this;
    }

}
