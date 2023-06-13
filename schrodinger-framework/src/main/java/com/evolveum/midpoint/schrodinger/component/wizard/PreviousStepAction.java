package com.evolveum.midpoint.schrodinger.component.wizard;

public interface PreviousStepAction<WSP extends WizardStepPanel> {

    WSP back();

}
