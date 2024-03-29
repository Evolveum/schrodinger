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
package com.evolveum.midpoint.schrodinger.component.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.ForwardWorkitemModal;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.cases.DeltasToBeApprovedTable;
import com.evolveum.midpoint.schrodinger.page.cases.WorkitemPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Kate Honchar
 */
public class WorkitemDetailsPanel<P> extends Component<P, WorkitemDetailsPanel<P>> {

    public WorkitemDetailsPanel(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public WorkitemDetailsPanel<P> approveButtonClick(){
        getParentElement()
                .$(Schrodinger.byDataId("workItemApproveButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        $x(".//div[@data-s-id='detailsBox' and contains(@class, \"feedback-message\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        return this;
    }

    public WorkitemDetailsPanel<P> rejectButtonClick(){
        getParentElement()
                .$(Schrodinger.byDataId("workItemRejectButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        $x(".//div[@data-s-id='detailsBox' and contains(@class, \"feedback-message\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        return this;
    }

    public ForwardWorkitemModal forwardButtonClick(){
        getParentElement()
                .$(Schrodinger.byDataId("workItemForwardButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        ForwardWorkitemModal<P> forwardWorkitemModal = new ForwardWorkitemModal<P>(getParent(), Utils.getModalWindowSelenideElement());
        return forwardWorkitemModal;
    }

    public ConfirmationModal<P> forwardOperationUserSelectionPerformed(){
        return new ConfirmationModal<P>(getParent(), Utils.getModalWindowSelenideElement());
    }

    public void claimButtonClick(){
        getParentElement()
                .$(Schrodinger.byDataId("workItemClaimButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
    }

    public WorkitemDetailsPanel<P> assertApproverElementValueMatches(String approver){
        assertion.assertTrue(getParentElement()
                .$(Schrodinger.byDataId("approver"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(byText(approver))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .is(Condition.visible));
        return this;
    }

    public WorkitemDetailsPanel<P> setComment(String comment) {
        $(By.tagName("textarea")).shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .setValue(comment);
        return this;
    }

    public DeltasToBeApprovedTable<WorkitemDetailsPanel<P>> deltasToBeApprovedTable() {
        return new DeltasToBeApprovedTable<>(WorkitemDetailsPanel.this);
    }
}
