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

package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.SelectableRowTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import java.util.Arrays;

public class SelectMarkModal<T> extends ModalBox<T> {

    public SelectMarkModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelectMarkModal<T> selectMarks(String... marks) {
        SelectableRowTable<?, ?> table = new SelectableRowTable<>(SelectMarkModal.this,
                getParentElement().$(Schrodinger.byDataId("div", "table")));
        Arrays.stream(marks).forEach(mark -> table.findRowByColumnLabelAndRowValue("Name", mark).clickCheckBox());
        return this;
    }

    public T clickConfirmationButton() {
        getParentElement().$(Schrodinger.byDataId("a", "addButton")).click();
        Utils.waitForAjaxCallFinish();
        if (Utils.isModalWindowSelenideElementVisible()) {
            Selenide.sleep(3000);
        }
        return getParent();
    }
}
