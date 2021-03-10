/*
 * Copyright (c) 2010-2020 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0
 * and European Union Public License. See LICENSE file for details.
 */
package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

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
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$x(".//input[@" + Schrodinger.DATA_S_ID + "='oid']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        if(!oid.equals(inputField.getValue())) {
            inputField.setValue(oid);
        }
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.waitUntil(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public T inputRefType(String type) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Type:")).parent()
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.selectOptionContainingText(type);
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.waitUntil(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public T inputRefRelation(String relation) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Relation:")).parent()
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='input']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.selectOptionContainingText(relation);
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.waitUntil(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public T inputRefName(String referenceObjNamePartial, String referenceObjNameFull) {
        if (getParentElement() == null){
            return getParent();
        }
        getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        getParentElement().parent().$x(".//a[@" + Schrodinger.DATA_S_ID + "='editButton']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        SelenideElement inputField = getParentElement().parent().$(Schrodinger.byElementValue("label", "Name:")).parent()
                .$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        inputField.setValue(referenceObjNamePartial);
        $(By.className("wicket-aa-container")).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .$(byText(referenceObjNameFull)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        SelenideElement confirmButton = getParentElement().$x(".//a[@" + Schrodinger.DATA_S_ID + "='confirmButton']");
        confirmButton.waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        confirmButton.waitUntil(Condition.hidden, MidPoint.TIMEOUT_DEFAULT_2_S);
        return getParent();
    }

    public boolean matchRefSearchFieldValue(String value) {
        if (getParentElement() == null || value == null){
            return false;
        }
        SelenideElement inputField = getParentElement().parent().$x(".//input[@" + Schrodinger.DATA_S_ID + "='input']")
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);
        return value.equals(inputField.getValue());

    }

    public ReferenceSearchItemPanel<T> assertRefSearchFieldValueMatch(String value) {
        assertion.assertTrue(matchRefSearchFieldValue(value));
        return this;

    }


}
