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
package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class ReferenceSearchItemPanel<T> extends Component<T> {
    public ReferenceSearchItemPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ReferenceSearchItemPopup propertySettings() {
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='configure']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        SelenideElement popover = getParentElement().$x(".//div[@data-s-id='popover']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new ReferenceSearchItemPopup(popover);
    }

    public boolean matchRefSearchFieldValue(String value) {
        if (getParentElement() == null || value == null){
            return false;
        }
        SelenideElement inputField = getParentElement().$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return value.equals(inputField.getValue());

    }

    public ReferenceSearchItemPanel<T> assertRefSearchFieldValueMatch(String value) {
        assertion.assertTrue(matchRefSearchFieldValue(value));
        return this;

    }

    public class ReferenceSearchItemPopup extends Component<ReferenceSearchItemPanel<T>> {

        public ReferenceSearchItemPopup(SelenideElement popupElement) {
            super(ReferenceSearchItemPanel.this, popupElement);
        }

        public ReferenceSearchItemPopup inputRefOid(String oid) {
            SelenideElement inputField = getParentElement().$x(".//input[@" + Schrodinger.DATA_S_ID + "='oid']")
                    .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            if(!oid.equals(inputField.getValue())) {
                inputField.setValue(oid);
            }
            Utils.waitForAjaxCallFinish();
            return ReferenceSearchItemPopup.this;
        }

        public ReferenceSearchItemPopup inputRefType(String type) {
            SelenideElement inputField = getParentElement().$(Schrodinger.byElementValue("label", "Type:")).parent()
                    .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                    .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            inputField.selectOptionContainingText(type);
            return ReferenceSearchItemPopup.this;
        }

        public ReferenceSearchItemPopup inputRefRelation(String relation) {
            SelenideElement inputField = getParentElement().$(Schrodinger.byElementValue("label", "Relation:")).parent()
                    .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                    .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            inputField.selectOption(relation);
            inputField.shouldBe(Condition.text(relation), MidPoint.TIMEOUT_MEDIUM_6_S);
            Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
            return ReferenceSearchItemPopup.this;
        }

        public ReferenceSearchItemPopup inputRefName(String referenceObjName) {
            SelenideElement inputField = getParentElement().$(Schrodinger.byElementValue("label", "Name:")).parent()
                    .$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                    .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
            inputField.setValue(referenceObjName);
            Utils.waitForAjaxCallFinish();
            Selenide.sleep(4000);
            getParentElement().$x(".//h3").click();
            inputField.pressEscape();
            return ReferenceSearchItemPopup.this;
        }

        public ReferenceSearchItemPanel<T> applyButtonClick() {
            SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
            confirmButton.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            Utils.waitForAjaxCallFinish();
            return getParent();
        }
    }

}
