/*
 * Copyright (c) 2023. Evolveum
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.page.self.accessrequest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ShoppingCartStepPanel extends WizardStepPanel<RequestAccessPage> {

    public ShoppingCartStepPanel(RequestAccessPage parent) {
        super(parent, $(Schrodinger.byDataId(ID_CONTENT_BODY)).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public RequestAccessPage clickSubmitButton() {
        getParentElement().$(Schrodinger.byDataId("submit")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public ShoppingCartStepPanel comment(String comment) {
        SelenideElement commentElement = getParentElement().$x(".//textarea[@data-s-id='comment']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        commentElement.setValue(comment);
        return this;
    }

    public ShoppingCartStepPanel selectValidityOption(String validityLabel) {
        SelenideElement validityDropDown = getValidityDropDown();
        validityDropDown.selectOption(validityLabel);
        validityDropDown.should(Condition.text(validityLabel), MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public ShoppingCartStepPanel assertValidityOptionPresent(String validityLabel) {
        SelenideElement validityDropDown = getValidityDropDown();
        SelenideElement validityOptionElement = validityDropDown.$x(".//option[contains(text(), '" + validityLabel + "')]");
        assertion.assertTrue(validityOptionElement.exists(),
                "Validity option '" + validityLabel + "' doesn't exist in the validity dropdown.");
        return this;
    }

    public ShoppingCartStepPanel assertValidityOptionSelected(String validityLabel) {
        SelenideElement validityDropDown = getValidityDropDown();
        SelenideElement validityOptionElement = validityDropDown.$x(".//option[contains(text(), '" + validityLabel + "')]");
        assertion.assertTrue(validityOptionElement.exists() && validityOptionElement.isSelected(),
                "Validity option '" + validityLabel + "' isn't selected though it should be.");
        return this;
    }

    private SelenideElement getValidityDropDown() {
        return getParentElement().$x(".//select[@data-s-id='validity']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public ShoppingCartItemsTable getShoppingCartItemsTable() {
        return new ShoppingCartItemsTable(ShoppingCartStepPanel.this);
    }

    public ShoppingCartStepPanel assertOpenConflictExist() {
        SelenideElement openConflict = getParentElement().$(Schrodinger.byDataId("openConflict")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        assertion.assertTrue(openConflict.exists() && openConflict.isDisplayed(),
                "Open conflict solver button should exist, but it doesn't.");
        return this;
    }

    public ShoppingCartStepPanel assertOpenConflictNotExist() {
        SelenideElement openConflict = getParentElement().$(Schrodinger.byDataId("openConflict"));
        assertion.assertTrue(!openConflict.exists(),
                "Open conflict solver button should not exist, but it does.");
        return this;
    }

    public ShoppingCartStepPanel assertWarningBudgeExist() {
        SelenideElement warningBudge = $x(".//span[contains(@class,\"badge badge-warning\")]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        assertion.assertTrue(warningBudge.exists() && warningBudge.isDisplayed(),
                "Warning budge should exist, but it doesn't.");
        return this;
    }

    public ShoppingCartStepPanel assertWarningBudgeNotExist() {
        SelenideElement warningBudge = $x(".//span[contains(@class,\"badge badge-warning\")]");
        assertion.assertTrue(!warningBudge.exists(),
                "Warning budge should not exist, but it does.");
        return this;
    }
}
