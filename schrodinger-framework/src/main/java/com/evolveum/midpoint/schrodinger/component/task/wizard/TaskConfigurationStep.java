package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;

public class TaskConfigurationStep extends PrismFormWizardStepPanel<TaskWizardPage> {

    public TaskConfigurationStep(TaskWizardPage parent) {
        super(parent);
    }

    public TaskConfigurationStep name(String name) {
        getFormPanel().addAttributeValue("Name", name);
        return TaskConfigurationStep.this;
    }

    public TaskConfigurationStep description(String description) {
        getFormPanel().addAttributeValue("Description", description);
        return TaskConfigurationStep.this;
    }

    public TaskConfigurationStep documentation(String documentation) {
        getFormPanel().addAttributeValue("Documentation", documentation);
        return TaskConfigurationStep.this;
    }

    public TaskConfigurationStep category(String category) {
        getFormPanel().addAttributeValue("Category", category);
        return TaskConfigurationStep.this;
    }

    public TaskResourceObjectsStep next() {
        clickNext();
        return new TaskResourceObjectsStep(getParent());
    }
}
