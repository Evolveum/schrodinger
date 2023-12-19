/*
 * Copyright (c) 2023 Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.wizard.TableWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class InboundMappingsPanel<T> extends TableWizardStepPanel<T> {

    public InboundMappingsPanel(T parent) {
        super(parent);
    }

    public InboundMappingsTable<InboundMappingsPanel<T>> addInbound() {
        table()
                .getToolbarButtonByTitleKey("InboundAttributeMappingsTable.newObject")
                .click();
        return new InboundMappingsTable<>(InboundMappingsPanel.this, $(Schrodinger.byDataId("table")));
    }

    public InboundMappingsPanel<T> assertMappingExist(String mappingName) {
        table().assertTableContainsColumnWithValue("Name", mappingName);
        return this;
    }

    public InboundMappingsPanel<T> assertMappingLifecycleStateEquals(String mappingName, String lifecycleStateExpectedValue) {
        TableRow<?, ?> row = table().findRowByColumnLabel("Name", mappingName);
        String stateRealValue = row.getColumnCellTextByColumnName("Lifecycle state");
        assertion.assertEquals(stateRealValue, lifecycleStateExpectedValue, "Lifecycle state value should be equal " +
                "to: " + lifecycleStateExpectedValue + "; actual value: " + stateRealValue);
        return this;
    }

    public InboundMappingsPanel<T> assertMappingIconTitleEquals(String mappingName, String iconTitle) {
        TableRow<?, ?> row = table().findRowByColumnLabel("Name", mappingName);
        SelenideElement iconElement = row.getParentElement().$x(".//i[@data-s-id='image' and @title='" + iconTitle + "']");
        assertion.assertTrue(iconElement.exists() && iconElement.isDisplayed(), "Icon with title " +
                iconTitle + "should exist for mapping: " + mappingName);
        return this;
    }
}
