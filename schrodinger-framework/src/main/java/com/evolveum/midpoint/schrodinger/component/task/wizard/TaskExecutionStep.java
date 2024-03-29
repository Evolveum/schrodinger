/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class TaskExecutionStep extends PrismFormWizardStepPanel<TaskWizardPage> implements NextStepAction {

    public TaskExecutionStep(TaskWizardPage parent) {
        super(parent);
    }

    public TaskExecutionStep mode(String value) {
        getFormPanel().setDropDownAttributeValue("Mode", value);
        return TaskExecutionStep.this;
    }

    public TaskExecutionStep predefined(String value) {
        getFormPanel().setDropDownAttributeValue("Predefined", value);
        return TaskExecutionStep.this;
    }


    @Override
    public TaskScheduleStep next() {
        clickNext();
        return new TaskScheduleStep(getParent());
    }
}
