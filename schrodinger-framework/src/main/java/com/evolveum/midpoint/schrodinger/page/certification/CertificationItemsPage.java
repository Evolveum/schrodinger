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
package com.evolveum.midpoint.schrodinger.page.certification;

import com.evolveum.midpoint.schrodinger.component.assignmentholder.ObjectListPageWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.cases.WorkitemsTable;
import com.evolveum.midpoint.schrodinger.page.cases.WorkitemPage;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;

/**
 * Created by Viliam Repan (lazyman).
 */
public class CertificationItemsPage extends ObjectListPageWithPageRedirect<WorkitemsTable, WorkitemPage> {

    @Override
    public WorkitemsTable table() {
        return new WorkitemsTable(this, getTableBoxElement());
    }

    @Override
    public WorkitemPage getObjectDetailsPage() {
        return new WorkitemPage();
    }

    @Override
    protected String getTableAdditionalClass(){
        return ConstantsUtil.OBJECT_USER_BOX_COLOR;
    }
}
