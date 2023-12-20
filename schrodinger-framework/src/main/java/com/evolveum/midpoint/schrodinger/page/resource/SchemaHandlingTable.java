/*
 * Copyright (c) 2023  Evolveum
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
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithComponentRedirect;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeWizardChoiceStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeWizardPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SchemaHandlingTable<T> extends
        TableWithComponentRedirect<T, ObjectTypeWizardChoiceStep<SchemaHandlingTable<T>>, SchemaHandlingTable<T>> {

    public SchemaHandlingTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SchemaHandlingTable<T> setLifecycleStateValue(String displayNameValue, String lifecycleStateExpectedValue) {
        TableRow<?, ?> tableRow = findRowByColumnLabelAndRowValue("Display name", displayNameValue);
        SelenideElement cell = tableRow.getColumnCellElementByColumnName("Lifecycle state");
        SelenideElement select = cell.$(By.tagName("select"));
        select.selectOption(lifecycleStateExpectedValue);
        return this;
    }


    @Override
    public ObjectTypeWizardChoiceStep<SchemaHandlingTable<T>> clickByName(String name) {
        return new ObjectTypeWizardChoiceStep<>(this);
    }

    public ObjectTypeBasicInformationWizardStep addNewObjectType() {
        String addNewObjectTypeKey = "ResourceSchemaHandlingPanel.newObject1";
        String titleTranslated = Utils.translate(addNewObjectTypeKey);
        $(Schrodinger.byElementAttributeValue("a", "title", titleTranslated))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        ObjectTypeWizardPage objectTypeWizardPage = new ObjectTypeWizardPage();
        return new ObjectTypeBasicInformationWizardStep(objectTypeWizardPage);
    }

    public ResourcePage saveMappings() {
        return new ResourcePage();
    }


}
