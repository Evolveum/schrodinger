package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class TaskDistributionStep  extends PrismFormWizardStepPanel<TaskWizardPage> {

    public TaskDistributionStep(TaskWizardPage parent) {
        super(parent);
    }

    //todo fill in the form attributes

    public BasicPage saveAndRun() {
        $x(".//a[@data-s-id='submit']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return new BasicPage();
    }
}
