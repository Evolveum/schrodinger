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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourceWizardPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.service.ServicePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.openqa.selenium.By;

import javax.management.relation.Role;

import static com.codeborne.selenide.Selectors.byText;

public class ChooseFocusTypeAndRelationModal<T> extends Component<T, ChooseFocusTypeAndRelationModal<T>> {

    public ChooseFocusTypeAndRelationModal(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ChooseFocusTypeAndRelationModal<T> setType(String type) {
        getParentElement().$x(".//div[@data-s-id='type']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='select']")
                .selectOption(type);
        return this;
    }

    public String getType() {
        return getParentElement().$x(".//div[@data-s-id='type']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='select']")
                .getSelectedOption().getText();
    }

    public ChooseFocusTypeAndRelationModal<T> setRelation(String relation) {
        getParentElement().$x(".//div[@data-s-id='relation']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='select']")
                .selectOption(relation);
        return this;
    }

    public ChooseFocusTypeAndRelationModal<T> setArchetype(String archetype) {
        getParentElement().$x(".//div[@data-s-id='archetype']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$x(".//select[@" + Schrodinger.DATA_S_ID + "='select']")
                .selectOption(archetype);
        return this;
    }

    public BasicPage clickOk() {
        String selectedType = getType();
        getParentElement().$(Schrodinger.byDataId("okButton"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());
        if ("User".equals(selectedType)) {
            return new UserPage();
        } else if ("Organization".equals(selectedType)) {
            return new OrgPage();
        } else if ("Role".equals(selectedType)) {
            return new RolePage();
        } else if ("Service".equals(selectedType)) {
            return new ServicePage();
        } else if ("Resource".equals(selectedType)) {
            return new ResourceWizardPage();
        }
        return null;
    }

    public T clickCancel() {
        getParentElement().$(Schrodinger.byDataId("cancel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }
}
