package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

public class TaskResourceObjectsStep<WSP extends WizardStepPanel>  extends PrismFormWizardStepPanel<TaskWizardPage>
        implements NextStepAction<WSP> {

    public TaskResourceObjectsStep(TaskWizardPage parent) {
        super(parent);
    }

    //todo fill in the form attributes

    public TaskScheduleStep nextToSchedule() {
        clickNext();
        return new TaskScheduleStep(getParent());
    }

    public TaskExecutionStep nextToExecution() {
        clickNext();
        return new TaskExecutionStep(getParent());
    }

    public TaskDistributionStep nextToDistribution() {
        clickNext();
        return new TaskDistributionStep(getParent());
    }

    @Override
    public WSP next() {
        return null;
    }
}
