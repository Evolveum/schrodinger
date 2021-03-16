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
import com.evolveum.midpoint.schrodinger.component.TabWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar.
 */
public class SchemaStepSchemaTab extends TabWithContainerWrapper<SchemaWizardStep> {

    public SchemaStepSchemaTab(SchemaWizardStep parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SchemaStepSchemaTab clickObjectClass(String objectClassName) {
        $(Schrodinger.bySelfOrDescendantElementAttributeValue("div", "class", "box box-solid box-primary",
                "class", "box-title"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.linkText(objectClassName))
                .click();
        $(Schrodinger.byDataId("objectClassInfoContainer")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public boolean isObjectClassPresent(String objectClassName) {
        return $(Schrodinger.bySelfOrDescendantElementAttributeValue("div", "class", "box box-solid box-primary",
                "class", "box-title"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.linkText(objectClassName))
                .exists();
    }

    public Table<SchemaStepSchemaTab> getAttributesTable() {
        return new Table<SchemaStepSchemaTab>(this,
                $(Schrodinger.byDataId("attributeTable")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public SchemaStepSchemaTab assertObjectClassPresent(String objectClassName) {
        assertion.assertTrue(isObjectClassPresent(objectClassName), "Object class component isn't visible.");
        return this;
    }

}
