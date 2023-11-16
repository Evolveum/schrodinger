package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TaskTypeSelectionStep extends TileListWizardStepPanel<TaskWizardPage> {

    public TaskTypeSelectionStep(TaskWizardPage page) {
        super(page, $(Schrodinger.byDataId("templateChoicePanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public TaskTypeSelectionStep reconciliationTask() {
        selectTileByLabel("Reconciliation task");
        Utils.waitForAjaxCallFinish();
        getParent().setTaskType(TaskWizardPage.TaskType.RECONCILIATION);
        return this;
    }

    public TaskTypeSelectionStep importTask() {
        selectTileByLabel("Import task");
        Utils.waitForAjaxCallFinish();
        getParent().setTaskType(TaskWizardPage.TaskType.IMPORT);
        return this;
    }

    public TaskTypeSelectionStep liveSynchronizationTask() {
        selectTileByLabel("Live synchronization task");
        Utils.waitForAjaxCallFinish();
        getParent().setTaskType(TaskWizardPage.TaskType.LIVE_SYNCHRONIZATION);
        return this;
    }

    public TaskWizardPage clickCreateTaskButton() {
        $x(".//button[@data-s-id='createNewTask']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public TaskTypeSelectionStep turnOffTaskSimulating() {
        SelenideElement simulateContainer = $(Schrodinger.byDataId("simulate"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement switchElement = simulateContainer.$x(".//div[contains(@class, 'bootstrap-switch-on')]");
        if (switchElement.exists() && switchElement.isDisplayed()) {
            switchElement.click();
            Utils.waitForAjaxCallFinish();
        }
        return this;
    }

    public TaskTypeSelectionStep turnOnTaskSimulating() {
        SelenideElement simulateContainer = $(Schrodinger.byDataId("simulate"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement switchElement = simulateContainer.$x(".//div[contains(@class, 'bootstrap-switch-off')]");
        if (switchElement.exists() && switchElement.isDisplayed()) {
            switchElement.click();
            Utils.waitForAjaxCallFinish();
        }
        return this;
    }

}
