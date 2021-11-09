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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.DropDown;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

/**
 * @author skublik
 */

public class OrgTreeNodeDropDown<T> extends DropDown<T> {

    public OrgTreeNodeDropDown(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public OrgPage edit(){
        Utils.waitForAjaxCallFinish();
        getParentElement().$x(".//schrodinger[@"+ Schrodinger.DATA_S_RESOURCE_KEY +"='TreeTablePanel.edit']").parent()
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForMainPanelOnDetailsPage();
        return new OrgPage();
    }

    public OrgHierarchyPanel<OrgRootTab> expandAll(){
        Utils.waitForAjaxCallFinish();
        getParentElement().$x(".//schrodinger[@"+ Schrodinger.DATA_S_RESOURCE_KEY +"='TreeTablePanel.expandAll']").parent()
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        return (OrgHierarchyPanel<OrgRootTab>) getParent();
    }

    public T collapseAll(){
        Utils.waitForAjaxCallFinish();
        getParentElement().$x(".//schrodinger[@"+ Schrodinger.DATA_S_RESOURCE_KEY +"='TreeTablePanel.collapseAll']").parent()
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        return getParent();
    }
}
