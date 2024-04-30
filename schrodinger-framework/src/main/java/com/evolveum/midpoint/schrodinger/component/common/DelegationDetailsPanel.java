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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.DateTimePanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
/**
 * Created by honchar
 */
public class DelegationDetailsPanel<T> extends Component<T, DelegationDetailsPanel<T>> {

    public DelegationDetailsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public DelegationDetailsPanel<T> expandDetailsPanel() {
        $(By.linkText("Current user")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        $(Schrodinger.byDataId("delegationDescription")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public DelegationDetailsPanel<T> clickHeaderCheckbox(){
        SelenideElement checkBox = $(Schrodinger.byDataId("headerRow")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .find(Schrodinger.byDataId("selected"));
        String checkedAttr = checkBox.getAttribute("checked");
        checkBox.click();
        if (checkedAttr == null || !checkedAttr.equals("checked")) {
            checkBox.shouldBe(Condition.attribute("checked", "checked"), MidPoint.TIMEOUT_DEFAULT_2_S);
        } else {
            checkBox.shouldBe(Condition.attribute("checked", null), MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        return this;
    }

    public String getDescriptionValue() {
        return $(Schrodinger.byDataId("delegationDescription")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .getText();
    }

    public DelegationDetailsPanel<T> setDescriptionValue(String descriptionValue) {
        $(Schrodinger.byDataId("delegationDescription")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(descriptionValue);
        return this;
    }

    public boolean isDescriptionEnabled() {
        SelenideElement description = $(Schrodinger.byDataId("delegationDescription"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        String readonlyAttr = description.getAttribute("readonly");
        boolean notReadonly = readonlyAttr == null || (!readonlyAttr.equals("readonly") && !readonlyAttr.equals("true"));
        String disabledAttr = description.getAttribute("disabled");
        boolean notDisabled = disabledAttr == null || (!disabledAttr.equals("disabled") && !disabledAttr.equals("true"));
        return notReadonly && notDisabled;
    }

    public DateTimePanel<DelegationDetailsPanel<T>> getValidFromPanel() {
        return new DateTimePanel<>(this,
                $(Schrodinger.byDataId("delegationValidFrom")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public boolean isValidFromPanelEnabled () {
        if (!getValidFromPanel().findInput().isEnabled()) {
            return false;
        }

        String buttonClasses = getValidFromPanel().findButton().getAttribute("class");
        if (buttonClasses != null && buttonClasses.contains("disabled")) {
            return false;
        }

        return true;
    }

    public DelegationDetailsPanel<T> setValidFromValue(String date, String hours, String minutes, DateTimePanel.AmOrPmChoice amOrPmChoice) {
        DateTimePanel<DelegationDetailsPanel<T>> validFromPanel = new DateTimePanel<>(this,
                $(Schrodinger.byDataId("delegationValidFrom")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        validFromPanel.setDateTimeValue(date, hours, minutes, amOrPmChoice);
        return this;
    }

    public DateTimePanel<DelegationDetailsPanel<T>> getValidToPanel() {
        return new DateTimePanel<>(this,
                $(Schrodinger.byDataId("delegationValidTo")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public DelegationDetailsPanel<T> setValidToValue(String date, String hours, String minutes, DateTimePanel.AmOrPmChoice amOrPmChoice) {
        DateTimePanel<DelegationDetailsPanel<T>> validFromPanel = new DateTimePanel<>(this,
                $(Schrodinger.byDataId("delegationValidTo")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        validFromPanel.setDateTimeValue(date, hours, minutes, amOrPmChoice);
        return this;
    }

    public DelegationDetailsPanel<T> setAssignmentPrivilegesCheckboxValue(boolean value) {
        Utils.setOptionCheckedById("assignmentPrivilegesCheckbox", value);
        return this;
    }

    public boolean isAssignmentPrivilegesSelected() {
        if ($(Schrodinger.byDataId("assignmentPrivilegesCheckbox")).exists()) {
            return $(Schrodinger.byDataId("assignmentPrivilegesCheckbox")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .isSelected();
        }
        return false;
    }

    public DelegationDetailsPanel<T> clickAssignmentLimitationsCheckbox(boolean value) {
        Utils.setOptionCheckedById("allowTransitive", value);
        return this;
    }

    public boolean isAssignmentLimitationsSelected() {
        return $(Schrodinger.byDataId("allowTransitive")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .isSelected();
    }

    public DelegationDetailsPanel<T> clickApprovalWorkItemsCheckbox(boolean value) {
        Utils.setOptionCheckedById("approvalWorkItems", value);
        return this;
    }

    public boolean isApprovalWorkItemsSelected() {
        return $(Schrodinger.byDataId("approvalWorkItems")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .isSelected();
    }

    public DelegationDetailsPanel<T> clickCertificationWorkItemsCheckbox(boolean value) {
        Utils.setOptionCheckedById("certificationWorkItems", value);
        return this;
    }

    public boolean isCertificationWorkItemsSelected() {
        return $(Schrodinger.byDataId("certificationWorkItems")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .isSelected();
    }

    public DelegationDetailsPanel<T> assertApprovalWorkItemsSelected() {
        assertion.assertTrue(isApprovalWorkItemsSelected(), "Workflow approvals (for approval work items) checkbox is not selected but should be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertApprovalWorkItemsNotSelected() {
        assertion.assertTrue(isApprovalWorkItemsSelected(), "Workflow approvals (for approval work items) checkbox is selected but shouldn't be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertAssignmentLimitationsSelected() {
        assertion.assertTrue(isAssignmentLimitationsSelected(), "Assignment limitations checkbox is not selected but should be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertAssignmentLimitationsNotSelected() {
        assertion.assertFalse(isAssignmentLimitationsSelected(), "Assignment limitations checkbox is selected but shouldn't be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertAssignmentPrivilegesSelected() {
        assertion.assertTrue(isAssignmentPrivilegesSelected(),"Assignment privileges checkbox is not selected but should be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertAssignmentPrivilegesNotSelected() {
        assertion.assertFalse(isAssignmentPrivilegesSelected(),"Assignment privileges checkbox is selected but shouldn't be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertCertificationWorkItemsSelected() {
        assertion.assertTrue(isCertificationWorkItemsSelected(), "Workflow approvals (for certification work items) checkbox is not selected but should be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertCertificationWorkItemsNotSelected() {
        assertion.assertFalse(isCertificationWorkItemsSelected(), "Workflow approvals (for certification work items) checkbox is selected but shouldn't be.");
        return this;
    }

    public DelegationDetailsPanel<T> assertDescriptionEnabled() {
        assertion.assertTrue(isDescriptionEnabled());
        return this;
    }

    public DelegationDetailsPanel<T> assertDescriptionDisabled() {
        assertion.assertFalse(isDescriptionEnabled());
        return this;
    }

    public DelegationDetailsPanel<T> assertValidFromPanelEnabled() {
        assertion.assertTrue(isValidFromPanelEnabled());
        return this;
    }

    public DelegationDetailsPanel<T> assertValidFromPanelDisabled() {
        assertion.assertFalse(isValidFromPanelEnabled());
        return this;
    }
}
