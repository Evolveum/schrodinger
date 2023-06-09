package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

import static com.codeborne.selenide.Selenide.$x;

public class BasicInformationWizardStep extends WizardStepPanel<ResourceWizardPage> {
    public BasicInformationWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    public BasicInformationWizardStep name(String name) {
        getFormPanel().addAttributeValue("Name", name);
        return BasicInformationWizardStep.this;
    }

    public BasicInformationWizardStep description(String description) {
        getFormPanel().addAttributeValue("Description", description);
        return BasicInformationWizardStep.this;
    }

    private PrismForm<BasicInformationWizardStep> getFormPanel() {
        SelenideElement formElement = $x(".//div[@data-s-id='form']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new PrismForm<>(BasicInformationWizardStep.this, formElement);
    }

    private SelenideElement getContentBodyElement() {
        return $x(".//div[@data-s-id='contentBody']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
