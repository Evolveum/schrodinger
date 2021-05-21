package com.evolveum.midpoint.schrodinger.component.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithComponentRedirect;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class WorkitemsTabTable extends TableWithComponentRedirect<WorkitemsTab, WorkitemDetailsPanel> {

    public WorkitemsTabTable(WorkitemsTab parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public WorkitemDetailsPanel<WorkitemsTab> clickByName(String name) {
        getParentElement()
                .$(Schrodinger.byDataId("tableContainer"))
                .$(By.partialLinkText(name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsTab>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    /**
     *
     * @param state open, closed
     * @return
     */
    public WorkitemDetailsPanel<WorkitemsTab> clickNameByState(String state) {
        rowByColumnLabel("State", state)
                .clickColumnByName("Name");
//                    getParentElement()
//                            .$(Schrodinger.byDataId("tableContainer"))
//                            .$(By.partialLinkText(name))
//                            .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsTab>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    public WorkitemDetailsPanel<WorkitemsTab> clickNameByActor(String actor) {
        rowByColumnLabel("Actor(s)", actor)
                .clickColumnByName("Name");
//                    getParentElement()
//                            .$(Schrodinger.byDataId("tableContainer"))
//                            .$(By.partialLinkText(name))
//                            .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement detailsPanel = $(Schrodinger.byDataId("div", "details"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new WorkitemDetailsPanel<WorkitemsTab>(WorkitemsTabTable.this.getParent(), detailsPanel);
    }

    @Override
    public TableWithComponentRedirect<WorkitemsTab, WorkitemDetailsPanel> selectCheckboxByName(String name) {
        //do nothing as there is no checkbox column in the table
        return this;
    }

}
