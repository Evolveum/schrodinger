package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.WizardPage;

public class TaskWizardPage extends WizardPage {

    public TaskConfigurationStep configuration() {
        return new TaskConfigurationStep(TaskWizardPage.this);
    }
}
