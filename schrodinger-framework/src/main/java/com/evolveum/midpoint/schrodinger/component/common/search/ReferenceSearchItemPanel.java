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
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class ReferenceSearchItemPanel<T> extends Component<T> {
    public ReferenceSearchItemPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T inputRefOid(String oid) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='configure']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='configure']")
//                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$x(".//input[@" + Schrodinger.DATA_S_ID + "='oid']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        if(!oid.equals(inputField.getValue())) {
            inputField.setValue(oid);
        }
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.shouldBe(Condition.disappear, MidPoint.TIMEOUT_MEDIUM_6_S);
        return getParent();
    }

    public T inputRefType(String type) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Type:")).parent()
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.selectOptionContainingText(type);
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.shouldBe(Condition.disappear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public T inputRefRelation(String relation) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Relation:")).parent()
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.selectOption(relation);
        inputField.shouldBe(Condition.text(relation), MidPoint.TIMEOUT_MEDIUM_6_S);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.shouldBe(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public T inputRefName(String referenceObjNamePartial, String referenceObjNameFull) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Name:")).parent()
                .$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.setValue(referenceObjNameFull);
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
//        $(By.className("wicket-aa-container")).shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
//                .$(byText(referenceObjNameFull)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.shouldBe(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public boolean matchRefSearchFieldValue(String value) {
        if (getParentElement() == null || value == null){
            return false;
        }
        SelenideElement inputField = getParentElement().parent().$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return value.equals(inputField.getValue());

    }

    public ReferenceSearchItemPanel<T> assertRefSearchFieldValueMatch(String value) {
        assertion.assertTrue(matchRefSearchFieldValue(value));
        return this;

    }


}
