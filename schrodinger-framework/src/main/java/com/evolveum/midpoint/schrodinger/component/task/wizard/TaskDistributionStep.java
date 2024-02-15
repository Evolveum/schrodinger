package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.PrismFormWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

public class TaskDistributionStep  extends PrismFormWizardStepPanel<TaskWizardPage> {

    public TaskDistributionStep(TaskWizardPage parent) {
        super(parent);
    }

    //todo fill in the form attributes

    public BasicPage saveAndRun() {
        return saveAndRun(false);
    }

    public BasicPage saveAndRun(boolean waitTaskToFinish) {
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(2000);
        SelenideElement submitButton = getParentElement().$(Schrodinger.byDataResourceKey("PageBase.button.saveAndRun"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        submitButton.click();
        Selenide.screenshot("debugScreenshotForSaveAndRunTaskButton");
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(2000);
        if (submitButton.exists() && submitButton.isDisplayed()) {
            Selenide.screenshot("syncTest_secondClick");
            submitButton.click();
            Utils.waitForAjaxCallFinish();
        }
        if (waitTaskToFinish) {
            Selenide.screenshot("syncTest_waitForTaskFinish");
            Selenide.sleep(30000);
        }
        return new BasicPage();
    }
}
