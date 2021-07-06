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
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.ChooseFocusTypeAndRelationModal;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourceWizardPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.service.ServicePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public class MemberPanel<T> extends Component<T> {

    public MemberPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ChooseFocusTypeAndRelationModal<MemberPanel<T>> newMember() {
        SelenideElement mainButton = $(By.xpath("//a[@data-s-id='button'][@title='New']"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).parent();
        if (!mainButton.$x(".//div[@data-s-id='additionalButton']").is(Condition.exist)) {
            mainButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
            return new ChooseFocusTypeAndRelationModal<>(this, Utils.getModalWindowSelenideElement());
        } else {
            mainButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
            mainButton.$x(".//div[@title='Create  member ']")
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
            return new ChooseFocusTypeAndRelationModal<>(this, Utils.getModalWindowSelenideElement());
        }
    }

    public BasicPage newMember(String title, String newMemberType) {
        SelenideElement mainButton = $(By.xpath(".//button[@type='button'][@title='Create  member ']"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).parent();
        if (!mainButton.$x(".//div[@data-s-id='additionalButton']").is(Condition.visible)) {
            mainButton.click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        mainButton.$x(".//div[@title='" + title + "']")
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        $(Schrodinger.byDataId("div", "mainPanel")).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        if ("User".equals(newMemberType)) {
            return new UserPage();
        } else if ("Organization".equals(newMemberType)) {
            return new OrgPage();
        } else if ("Role".equals(newMemberType)) {
            return new RolePage();
        } else if ("Service".equals(newMemberType)) {
            return new ServicePage();
        } else if ("Resource".equals(newMemberType)) {
            return new ResourceWizardPage();
        }
        return null;
    }

    public FocusSetAssignmentsModal<MemberPanel<T>> assignMember() {
        $(By.xpath("//button[@type='button'][@title='Assign  member ']")).waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return new FocusSetAssignmentsModal<MemberPanel<T>>(this,  Utils.getModalWindowSelenideElement());
    }

    public MemberPanel<T> selectType(String type) {
        getParentElement().$x(".//select[@name='memberContainer:memberTable:items:itemsTable:box:header:searchForm:search:form:typePanel:searchItemContainer:searchItemField:input']")
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).selectOption(type);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public MemberPanel<T> selectRelation(String relation) {
        table().search().dropDownPanelByItemName("Relation").inputDropDownValue(relation);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public MemberTable<MemberPanel<T>> table() {
        SelenideElement table = getParentElement().$x(".//div[@" + Schrodinger.DATA_S_ID + "='memberTable']");
        return new MemberTable<>(this, table);
    }
}
