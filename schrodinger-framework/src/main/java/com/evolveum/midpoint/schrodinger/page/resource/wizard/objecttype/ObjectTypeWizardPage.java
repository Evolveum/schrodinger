package com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class ObjectTypeWizardPage extends WizardPage {

    public ObjectTypeWizardChoiceStep objectTypeWizardChoicePanel() {
        return new ObjectTypeWizardChoiceStep(ObjectTypeWizardPage.this);
    }

    public SelenideElement getChoicePanelElement() {
        return $(Schrodinger.byDataId("choicePanel"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }


}
