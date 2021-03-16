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
package com.evolveum.midpoint.schrodinger.component.org;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

/**
 * @author skublik
 */

public class OrgRootTab extends Component<OrgTreePage> {

    public OrgRootTab(OrgTreePage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public OrgHierarchyPanel<OrgRootTab> getOrgHierarchyPanel() {
        SelenideElement treePanel = getParentElement().$(Schrodinger.byDataId("div", "treePanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new OrgHierarchyPanel<>(this, treePanel);
    }

    public MemberPanel<OrgRootTab> getMemberPanel() {
        SelenideElement memberPanel = getParentElement().$(Schrodinger.byDataId("div", "memberPanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new MemberPanel<>(this, memberPanel);
    }

    public ManagerPanel<OrgRootTab> getManagerPanel() {
        SelenideElement memberPanel = getParentElement().$(Schrodinger.byDataId("div", "managerContainer"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ManagerPanel<>(this, memberPanel);
    }
}
