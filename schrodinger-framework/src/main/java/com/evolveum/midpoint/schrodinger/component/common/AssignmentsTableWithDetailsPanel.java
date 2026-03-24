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
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class AssignmentsTableWithDetailsPanel<T, P extends AssignmentsTableWithDetailsPanel> extends AbstractTableWithPrismView<T, P> {
    public AssignmentsTableWithDetailsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public AssignmentDetailsPanel<AssignmentsTableWithDetailsPanel<T, P>> clickByName(String name) {
        $(Schrodinger.byElementValue("span", "data-s-id", "title", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        SelenideElement prismElement = $(Schrodinger.byDataId("div", "itemDetails"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new AssignmentDetailsPanel<>(this, prismElement);
    }


    @Override
    public AssignmentsTableWithDetailsPanel<T, P> selectCheckboxByName(String name) {
        $(Schrodinger.byFollowingSiblingEnclosedValue("td", "class", "align-middle check", "data-s-id", "3", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return this;
    }


    @Override
    public AssignmentsTableWithDetailsPanel<T, P> selectHeaderCheckbox() {
        $(By.tagName("thead")).$x(".//input[@data-s-id='check']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    @Override
    public AssignmentsTableWithDetailsPanel<T, P> removeByName(String name) {
        findRowByColumnLabelAndRowValue("Name", name).getInlineMenu().clickInlineMenuButtonByIconClass(".fa.fa-minus");
        return this;
    }

    @Override
    public AssignmentsTableWithDetailsPanel<T, P> clickHeaderActionButton(String actionButtonStyle) {
        $(By.tagName("thead")).$x(".//i[@class='" + actionButtonStyle + "']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }
}
