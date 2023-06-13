package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

public class SchemaWizardStep extends WizardStepPanel<ResourceWizardPage>
        implements PreviousStepAction<DiscoveryWizardStep> {
    public SchemaWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    @Override
    public DiscoveryWizardStep back() {
        return new DiscoveryWizardStep(getParent());
    }
}
