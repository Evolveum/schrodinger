package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class TaskResourceObjectsStep  extends PrismFormWizardStepPanel<TaskWizardPage> {

    public TaskResourceObjectsStep(TaskWizardPage parent) {
        super(parent);
    }

    //todo fill in the form attributes

    public TaskDistributionStep next() {
        clickNext();
        return new TaskDistributionStep(getParent());
    }
}
