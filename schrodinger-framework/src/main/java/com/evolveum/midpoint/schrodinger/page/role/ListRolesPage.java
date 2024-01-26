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
package com.evolveum.midpoint.schrodinger.page.role;

import com.evolveum.midpoint.schrodinger.component.assignmentholder.ObjectListPageWithPageRedirect;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ListRolesPage extends ObjectListPageWithPageRedirect<RolesPageTable, RolePage> {

    @Override
    public RolesPageTable table() {
        return new RolesPageTable(this, getTableBoxElement());
    }

    @Override
    protected String getTableAdditionalClass(){
        return ConstantsUtil.OBJECT_ROLE_BOX_COLOR;
    }

    @Override
    public RolePage getObjectDetailsPage() {
        return new RolePage();
    }
}
