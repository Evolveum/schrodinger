package com.evolveum.midpoint.schrodinger.component.task.wizard;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TaskTypeSelectionStep extends TileListWizardStepPanel<TaskWizardPage> {

    public TaskTypeSelectionStep(TaskWizardPage page) {
        super(page, $(Schrodinger.byDataId("templateChoicePanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public TaskWizardPage reconciliationTask() {
        selectTileByLabel("Reconciliation task");
        Utils.waitForAjaxCallFinish();
        clickCreateNewTaskButton();
        return getParent();
    }

    public TaskWizardPage importTask() {
        selectTileByLabel("Import task");
        Utils.waitForAjaxCallFinish();
        clickCreateNewTaskButton();
        return getParent();
    }

    public TaskWizardPage liveSynchronizationTask() {
        selectTileByLabel("Live synchronization task");
        Utils.waitForAjaxCallFinish();
        clickCreateNewTaskButton();
        return getParent();
    }

    private void clickCreateNewTaskButton() {
        $x(".//button[@data-s-id='createNewTask']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
    }

}
