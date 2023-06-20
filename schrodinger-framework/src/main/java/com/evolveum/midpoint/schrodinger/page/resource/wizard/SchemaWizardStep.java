package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.wizard.PreviousStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SchemaWizardStep extends TableWizardStepPanel<ResourceWizardPage>
        implements PreviousStepAction<DiscoveryWizardStep> {
    public SchemaWizardStep(ResourceWizardPage parent) {
        super(parent);
    }

    public SchemaWizardStep selectObjectClassByName(String name) {
        $(Schrodinger.byFollowingSiblingEnclosedValue("td", "class", "check", "data-s-id", "2", name))
                .$(By.tagName("input"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public ResourceWizardResultStep createResource() {
        $x(".//a[@data-s-id='submit']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new ResourceWizardResultStep(getParent());
    }
    @Override
    public DiscoveryWizardStep back() {
        clickBack();
        return new DiscoveryWizardStep(getParent());
    }
}
