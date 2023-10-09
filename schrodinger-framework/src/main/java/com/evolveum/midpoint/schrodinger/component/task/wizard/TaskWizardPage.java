package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.WizardPage;

public class TaskWizardPage extends WizardPage {

    public enum TaskType {
        IMPORT, LIVE_SYNCHRONIZATION, RECONCILIATION;
    }

    private TaskType taskType;

    public TaskConfigurationStep configuration() {
        return new TaskConfigurationStep(TaskWizardPage.this);
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}
