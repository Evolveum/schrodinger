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

package com.evolveum.midpoint.schrodinger.page.archetype;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.cases.CasesListTable;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ArchetypeListTable extends AssignmentHolderObjectListTable<ListArchetypesPage, ArchetypePage, ArchetypeListTable> {

    public ArchetypeListTable(ListArchetypesPage parent, SelenideElement parentElement){
        super(parent, parentElement);
    }

    @Override
    protected TableHeaderDropDownMenu<CasesListTable> clickHeaderActionDropDown() {
        return null;
    }

    @Override
    public ArchetypePage getObjectDetailsPage(){
        Utils.waitForMainPanelOnDetailsPage();
        return new ArchetypePage();
    }

}
