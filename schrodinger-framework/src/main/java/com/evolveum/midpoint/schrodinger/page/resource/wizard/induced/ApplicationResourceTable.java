/*
 * Copyright (c) 2024  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.induced;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.table.SelectableRowTable;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ApplicationResourceTable<P> extends SelectableRowTable<ApplicationResourceStep<P>, ApplicationResourceTable<P>> {

    public ApplicationResourceTable(ApplicationResourceStep<P> parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public ApplicationResourceTable<P> selectRowByName(String name) {
        findRowByColumnLabelAndRowValue("Name", name).getParentElement().click();
        Utils.waitForAjaxCallFinish();
        return ApplicationResourceTable.this;
    }
}
