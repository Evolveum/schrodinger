package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class TaskScheduleStep extends PrismFormWizardStepPanel<TaskWizardPage> implements NextStepAction {

    public TaskScheduleStep(TaskWizardPage parent) {
        super(parent);
    }

    public TaskScheduleStep interval(String interval) {
        getFormPanel().addAttributeValue("Interval", interval);
        return this;
    }

    @Override
    public TaskDistributionStep next() {
        clickNext();
        return new TaskDistributionStep(getParent());
    }
}
