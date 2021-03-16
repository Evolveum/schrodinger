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

import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicTab;
import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.component.ProjectionsTab;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;

/**
 * Created by Viliam Repan (lazyman).
 */
public class RolePage extends AbstractRolePage {

    @Override
    public ProjectionsTab<RolePage> selectTabProjections() {
        return super.selectTabProjections();
    }

    @Override
    public AssignmentHolderBasicTab<RolePage> selectTabBasic() {
        return super.selectTabBasic();
    }

    @Override
    public AssignmentsTab<RolePage> selectTabAssignments() {
        return super.selectTabAssignments();
    }

    public RolePage assertName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("name", expectedValue);
        return this;
    }

    public RolePage assertDisplayName(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("displayName", expectedValue);
        return this;
    }

    public RolePage assertIdentifier(String expectedValue) {
        selectTabBasic().form().assertPropertyInputValue("identifier", expectedValue);
        return this;
    }
}
