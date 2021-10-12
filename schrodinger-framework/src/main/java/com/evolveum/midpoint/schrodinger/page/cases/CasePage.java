/*
 * Copyright (c) 2010-2021 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.page.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.cases.OperationRequestPanel;
import com.evolveum.midpoint.schrodinger.component.cases.WorkitemsPanel;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Kate Honchar.
 */
public class CasePage extends AssignmentHolderDetailsPage {

    public ChildrenCasesPanel selectTabChildren(){
        return new ChildrenCasesPanel(this, getNavigationPanelSelenideElement("Child cases"));
    }

    public OperationRequestPanel selectTabOperationRequest(){
        return new OperationRequestPanel(this, getNavigationPanelSelenideElement("Operation request"));
    }

    public WorkitemsPanel selectTabWorkitems(){
        return new WorkitemsPanel(this, getNavigationPanelSelenideElement("Workitems"));
    }

    public TaskPage navigateToTask() {
        if ($(Schrodinger.byDataResourceKey("PageCase.navigateToTask")).exists()) {
            $(Schrodinger.byDataResourceKey("PageCase.navigateToTask"))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            $(By.cssSelector(".info-box-icon.summary-panel-task")).waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            return new TaskPage();
        }
        return null;
    }
}
