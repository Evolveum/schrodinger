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
package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardPage;

import static com.codeborne.selenide.Selenide.$x;

public class ResourceDataPreviewPanel<W extends WizardPage, T extends TileListWizardStepPanel<W>>
        extends Component<T> {

    public ResourceDataPreviewPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ResourceDataPreviewPanel<W, T> assertTableContainsObjects(int objectCount) {
        getResourceObjectsTable().assertTableObjectsCountEquals(objectCount);
        return ResourceDataPreviewPanel.this;
    }

    public ResourceDataPreviewPanel<W, T> assertTableColumnContainsValue(String columnName, String value) {
        getResourceObjectsTable().assertTableContainsColumnWithValue(columnName, value);
        return ResourceDataPreviewPanel.this;
    }

    private Table<ResourceDataPreviewPanel<W, T>> getResourceObjectsTable() {
        return new Table<>(ResourceDataPreviewPanel.this,
                $x(".//div[@data-s-id='table']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public T exitWizard() {
        $x(".//a[contains(text(), 'Exit wizard')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }
}
