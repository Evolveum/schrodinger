package com.evolveum.midpoint.schrodinger.component.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PrismFormWizardStepPanel<T> extends WizardStepPanel<T> {

    public PrismFormWizardStepPanel(T parent) {
        super(parent);
    }

    protected PrismForm<PrismFormWizardStepPanel> getFormPanel() {
        SelenideElement formElement = $(Schrodinger.byDataId("div", "valueForm"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new PrismForm<>(PrismFormWizardStepPanel.this, formElement);
    }

}
