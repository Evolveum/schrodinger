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

package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.component.table.DirectIndirectAssignmentTable;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class AssignmentsPanel<P extends AssignmentHolderDetailsPage> extends TabWithTableAndPrismView<P> {

    public AssignmentsPanel(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }


    public <A extends AssignmentsPanel<P>> FocusSetAssignmentsModal<A> clickAddAssignment() {
        return clickAddAssignment("");
    }

    public <A extends AssignmentsPanel<P>> FocusSetAssignmentsModal<A> clickAddAssignment(String title) {
        getParentElement().$x(".//i[contains(@class, \"fe fe-assignment\")]")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement modalElement = getNewAssignmentModal();
        FocusSetAssignmentsModal<A> modal = new FocusSetAssignmentsModal<A>((A) this, modalElement);
        if (modal.compositedIconsExist()) {
            modal.clickCompositedButtonByTitle(title);
        }
        modalElement.$x(".//div[@data-s-id='tabsPanel']").waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new FocusSetAssignmentsModal<A>((A) this, modalElement);
    }

    private SelenideElement getCompositedIconsPopupPanel(SelenideElement modalElement) {
        return modalElement.$x(".//div[@data-s-id='compositedButtons']").waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    private SelenideElement getNewAssignmentModal() {
        return Utils.getModalWindowSelenideElement();
    }


    public boolean assignmentExists(String assignmentName){
        SelenideElement assignmentSummaryDisplayName = table()
                .clickByName(assignmentName)
                    .getParentElement()
                        .$(Schrodinger.byDataId("displayName"));
        return assignmentName.equals(assignmentSummaryDisplayName.getText());
    }

    public <A extends AssignmentsPanel<P>> A selectTypeAll() {
        selectType("allAssignments");
        return (A) this;
    }

    public <A extends AssignmentsPanel<P>> A selectTypeRole() {
        selectType("roleTypeAssignments");
        return (A) this;
    }

    public <A extends AssignmentsPanel<P>> A selectTypeOrg() {
        selectType("orgTypeAssignments");
        return (A) this;
    }

    public <A extends AssignmentsPanel<P>> A selectTypeService() {
        selectType("serviceAssignments");
        return (A) this;
    }

    public <A extends AssignmentsPanel<P>> A selectTypeResource() {
        selectType("resourceAssignments");
        return (A) this;
    }

    public <A extends AssignmentsPanel<P>> DirectIndirectAssignmentTable<A> selectTypeAllDirectIndirect() {
        selectType("showIndirectAssignmentsButton");
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        SelenideElement table = $(Schrodinger.byDataId("table", "table"));

        return new DirectIndirectAssignmentTable<A>((A) this, table);
    }

    protected void selectType(String resourceKey) {
        $(Schrodinger.byDataId("div", resourceKey)).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public boolean containsAssignmentsWithRelation(String relation, String... expectedAssignments) {
        String relationString = relation.equals("Default") ? "" : ("Relation: " + relation);
        ElementsCollection labels = getParentElement()
                .$$(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("span", "data-s-id", "label",
                        "data-s-id", "5", relationString));
        List<String> indirectAssignments = new ArrayList<String>();
        for (SelenideElement label : labels) {
            if (!label.getText().isEmpty()) {
                indirectAssignments.add(label.getText());
            }
        }
        return indirectAssignments.containsAll(Arrays.asList(expectedAssignments));
    }

    @Override
    protected SelenideElement getPrismViewPanel() {
        return $(Schrodinger.byDataId("div", "valueForm"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public AssignmentsPanel<P> assertAssignmentsWithRelationExist(String relation, String... expectedAssignments) {
        assertion.assertTrue(containsAssignmentsWithRelation(relation, expectedAssignments), "Assignments doesn't exist.");
        return this;
    }

    public AssignmentsPanel<P> assertAssignmentsCountLabelEquals(String expectedValue) {
        SelenideElement el = $(By.partialLinkText("Assignments"));
        assertion.assertEquals(el.$x(".//small[@data-s-id='count']").getText(), expectedValue, "Assignments count label doesn't equal to expected value");
        return this;
    }
}
