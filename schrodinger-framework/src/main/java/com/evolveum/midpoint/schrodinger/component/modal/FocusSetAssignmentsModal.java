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
package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.FocusTableWithChoosableElements;
import com.evolveum.midpoint.schrodinger.component.MultiCompositedButtonPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/11/2018.
 */
public class FocusSetAssignmentsModal<T> extends ModalBox<T> {
    public FocusSetAssignmentsModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public FocusSetAssignmentsModal<T> selectType(String option) {
        SelenideElement tabElement = $(Schrodinger.byElementValue("a", "class", "tab-label", option))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        String classActive = tabElement.attr("class");

        tabElement.click();
        if (!classActive.contains("active")) {
            tabElement.shouldBe(Condition.attribute("class", classActive + " active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return this;
    }

    public FocusSetAssignmentsModal<T> selectKind(String option) {
        $(By.name("mainPopup:content:popupBody:kindContainer:kind:input"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).selectOption(option);

        return this;
    }

    public FocusSetAssignmentsModal<T> selectIntent(String option) {
        $(By.name("mainPopup:content:popupBody:intentContainer:intent:input"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).selectOption(option);

        return this;
    }

    public FocusTableWithChoosableElements<FocusSetAssignmentsModal<T>> table() {
        SelenideElement resourcesBox = getParentElement().$x(".//div[@class='box boxed-table']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new FocusTableWithChoosableElements<FocusSetAssignmentsModal<T>>(this, resourcesBox);
    }

    public FocusSetAssignmentsModal<T> setRelation(String relation) {
        getParentElement().$x(".//select[@data-s-id='select']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).selectOption(relation);
        return this;
    }

    public MultiCompositedButtonPanel<FocusSetAssignmentsModal<T>> getCompositedButtonsPanel() {
        SelenideElement buttonsPanel = $(Schrodinger.byDataId("compositedButtons")).shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        return new MultiCompositedButtonPanel<>(this, buttonsPanel);
    }

    public FocusSetAssignmentsModal<T> clickCompositedButtonByTypeAndRelation(String type, String relation) {
        String buttonTitle = "New " + type + " type assignment with " + relation + " relation";
        clickCompositedButtonByTitle(buttonTitle);
        return this;
    }

    public FocusSetAssignmentsModal<T> clickCompositedButtonByTitle(String buttonTitle) {
        SelenideElement button = getCompositedButtonsPanel().findCompositedButtonByTitle(buttonTitle).getParentElement();
        button.click();
        button.shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        return this;
    }

    public T clickAdd() {
        Utils.waitForAjaxCallFinish();
        SelenideElement addButton = getParentElement().$x(".//a[@data-s-resource-key='userBrowserDialog.button.addButton']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        addButton.click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        if (addButton.isDisplayed()) {
            addButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        }
        return this.getParent();
    }

    public boolean compositedIconsExist() {
        return getParentElement().$(Schrodinger.byDataId("compositedButtons")).exists();
    }

}
