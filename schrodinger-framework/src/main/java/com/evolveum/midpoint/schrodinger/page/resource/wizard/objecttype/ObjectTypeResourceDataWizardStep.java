/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype;

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class ObjectTypeResourceDataWizardStep extends PrismFormWizardStepPanel<ObjectTypeWizardPage>
        implements NextStepAction<ObjectTypeMidpointDataWizardStep>,
        PreviousStepAction<ObjectTypeBasicInformationWizardStep> {

    public ObjectTypeResourceDataWizardStep(ObjectTypeWizardPage parent) {
        super(parent);
    }

    public ObjectTypeResourceDataWizardStep objectClass(String value) {
        getFormPanel().selectOption("Object class", value);
        return ObjectTypeResourceDataWizardStep.this;
    }

    public ObjectTypeResourceDataWizardStep auxiliaryObjectClass(String value) {
        getFormPanel().selectOption("Auxiliary object class", value);
        return ObjectTypeResourceDataWizardStep.this;
    }

    public ObjectTypeResourceDataWizardStep subtreeSearchHierarchyScope() {
        return searchHierarchyScope("Subtree search");
    }

    public ObjectTypeResourceDataWizardStep oneLevelSearchHierarchyScope() {
        return searchHierarchyScope("One-level search");
    }

    public ObjectTypeResourceDataWizardStep searchHierarchyScope(String value) {
        getFormPanel().selectOption("Search hierarchy scope", value);
        return ObjectTypeResourceDataWizardStep.this;
    }

    public ObjectTypeResourceDataWizardStep filter(String value) {
        getFormPanel().addAttributeValue("Filter", value);
        return ObjectTypeResourceDataWizardStep.this;
    }

    public ObjectTypeResourceDataWizardStep classificationCondition (String value) {
        getFormPanel().addAttributeValue("Classification condition", value);
        return ObjectTypeResourceDataWizardStep.this;
    }

    @Override
    public ObjectTypeMidpointDataWizardStep next() {
        clickNext();
        return new ObjectTypeMidpointDataWizardStep(getParent());
    }

    @Override
    public ObjectTypeBasicInformationWizardStep back() {
        clickBack();
        return new ObjectTypeBasicInformationWizardStep(getParent());
    }
}
