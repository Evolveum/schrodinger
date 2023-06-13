package com.evolveum.midpoint.schrodinger.component.wizard;

public interface NextStepAction<WSP extends WizardStepPanel> {

    WSP next();
}
