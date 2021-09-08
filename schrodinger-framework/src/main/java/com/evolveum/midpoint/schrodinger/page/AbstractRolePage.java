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
package com.evolveum.midpoint.schrodinger.page;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.ApplicablePoliciesPanel;
import com.evolveum.midpoint.schrodinger.component.GovernancePanel;
import com.evolveum.midpoint.schrodinger.component.InducementsPanel;
import com.evolveum.midpoint.schrodinger.component.MembersPanel;

/**
 * @author skublik
 */

public class AbstractRolePage<A extends AbstractRolePage> extends FocusPage<A> {

    public InducementsPanel<A> selectTabInducements() {
        SelenideElement element = getTabPanel().clickTab("FocusType.inducement");

        return new InducementsPanel<A>((A) this, element);
    }

    public MembersPanel<A> selectTabMembers() {
        SelenideElement element = getTabPanel().clickTab("pageRole.members");
        return new MembersPanel<A>((A) this, element);

    }

    public GovernancePanel<A> selectTabGovernance() {
        SelenideElement element = getTabPanel().clickTab("pageRole.governance");
        return new GovernancePanel<A>((A) this, element);

    }

    public ApplicablePoliciesPanel<A> selectTabApplicablePolicies() {
        SelenideElement element = getTabPanel().clickTab("pageAdminFocus.applicablePolicies");
        return new ApplicablePoliciesPanel<A>((A) this, element);

    }
}
