/*
 * Copyright (c) 2026 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.AbstractRolePage;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.service.ServicePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import com.evolveum.midpoint.xml.ns._public.common.common_3.*;

public class AssignmentDetailsPanel<T> extends PrismFormWithActionButtons<T> {

    public AssignmentDetailsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public AssignmentHolderDetailsPage navigateToTargetRefDetailsPage(Class<? extends AssignmentHolderType> targetRefType) {
        getParentElement().$(Schrodinger.byDataId("displayName"))
                .$x(".//a[contains(@class, 'summary-panel-navigation-button')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(2000);
        if (RoleType.class.equals(targetRefType)) {
            return new RolePage();
        } else if (OrgType.class.equals(targetRefType)) {
            return new OrgPage();
        } else if (ServiceType.class.equals(targetRefType)) {
            return new ServicePage();
        }
        //todo add all the possible variants here
        return new AbstractRolePage();
    }
}
