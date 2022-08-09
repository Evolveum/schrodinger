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
package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar.
 */
public class SchemaStepSchemaPanel extends PanelWithContainerWrapper<ResourcePage> {

    public SchemaStepSchemaPanel(ResourcePage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SchemaStepSchemaPanel clickObjectClass(String objectClassName) {
        $(Schrodinger.bySelfOrDescendantElementAttributeValue("div", "class", "box box-solid box-primary",
                "class", "box-title"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.linkText(objectClassName))
                .click();
        $(Schrodinger.byDataId("objectClassInfoContainer")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public boolean isObjectClassPresent(String objectClassName) {
        return $(Schrodinger.bySelfOrDescendantElementAttributeValue("div", "class", "box box-solid box-primary",
                "class", "box-title"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.linkText(objectClassName))
                .exists();
    }

    public Table<SchemaStepSchemaPanel> getAttributesTable() {
        return new Table<SchemaStepSchemaPanel>(this,
                $(Schrodinger.byDataId("attributeTable")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public SchemaStepSchemaPanel assertObjectClassPresent(String objectClassName) {
        assertion.assertTrue(isObjectClassPresent(objectClassName), "Object class component isn't visible.");
        return this;
    }

}
