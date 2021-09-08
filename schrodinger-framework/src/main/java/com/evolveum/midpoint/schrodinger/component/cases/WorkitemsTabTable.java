package com.evolveum.midpoint.schrodinger.component.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithComponentRedirect;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class WorkitemsTabTable extends TableWithComponentRedirect<WorkitemsPanel, WorkitemDetailsPanel> {

    public WorkitemsTabTable(WorkitemsPanel parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public WorkitemDetailsPanel<WorkitemsPanel> clickByName(String name) {
        getParentElement()
                .$(Schrodinger.byDataId("tableContainer"))
                .$(By.partialLinkText(name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsPanel>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    /**
     *
     * @param state open, closed
     * @return
     */
    public WorkitemDetailsPanel<WorkitemsPanel> clickNameByState(String state) {
        TableRow row = rowByColumnLabel("State", state);
        if (row == null) {
            assertion.fail("Unable to find row with the the value " + state + "in State column");
        }
        row
                .clickColumnByName("Name");
//                    getParentElement()
//                            .$(Schrodinger.byDataId("tableContainer"))
//                            .$(By.partialLinkText(name))
//                            .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsPanel>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    public WorkitemDetailsPanel<WorkitemsPanel> clickNameByActor(String actor) {
        rowByColumnLabel("Actor(s)", actor)
                .clickColumnByName("Name");
//                    getParentElement()
//                            .$(Schrodinger.byDataId("tableContainer"))
//                            .$(By.partialLinkText(name))
//                            .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsPanel>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    @Override
    public TableWithComponentRedirect<WorkitemsPanel, WorkitemDetailsPanel> selectCheckboxByName(String name) {
        //do nothing as there is no checkbox column in the table
        return this;
    }

}
