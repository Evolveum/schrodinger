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
package com.evolveum.midpoint.schrodinger.component.configuration;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;
import com.evolveum.midpoint.schrodinger.page.configuration.ListRepositoryObjectsPage;
import com.evolveum.midpoint.schrodinger.page.configuration.RepositoryObjectPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ListRepositoryObjectsTable extends TableWithPageRedirect<ListRepositoryObjectsPage> {

    public ListRepositoryObjectsTable(ListRepositoryObjectsPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public RepositoryObjectPage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new RepositoryObjectPage();
    }

    @Override
    public ListRepositoryObjectsTable selectCheckboxByName(String name) {
        return null;
    }

    @Override
    protected TableHeaderDropDownMenu<ListRepositoryObjectsTable> clickHeaderActionDropDown() {
        $(Schrodinger.bySelfOrAncestorElementAttributeValue("button", "data-toggle", "dropdown", "class", "sortableLabel"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelenideElement dropDown = $(Schrodinger.byDataId("ul", "dropDownMenu"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<ListRepositoryObjectsTable>(this, dropDown);
    }

    public ListRepositoryObjectsTable exportObject(String type, String name) {
        showObjectInTableByTypeAndName(type, name)
                .clickExportButton();
        return this;
    }

    public ListRepositoryObjectsTable deleteObject(String type, String name) {
        showObjectInTableByTypeAndName(type, name)
                .clickDeleteButton()
                    .clickYes();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        and()
                .feedback()
                    .assertSuccess();
        return this;
    }

    public ListRepositoryObjectsTable showObjectInTableByTypeAndName(String type, String name) {
        search()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue(type)
                .updateSearch()
                .byName()
                .inputValue(name)
                .updateSearch();
        return this;
    }

    public ConfirmationModal<ListRepositoryObjectsTable>  clickDeleteButton() {
        $x(".//a[@data-s-id='delete']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new ConfirmationModal<>(this, Utils.getModalWindowSelenideElement());
    }

    public ListRepositoryObjectsTable  clickExportButton() {
        $x(".//a[@data-s-id='export']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public ListRepositoryObjectsTable setUseZipOptionChecked(boolean value) {
        Utils.setOptionCheckedById("zipCheck", value);
        return this;
    }

    public ListRepositoryObjectsTable setShowAllItemsOptionChecked(boolean value) {
        Utils.setOptionCheckedById("showAllItemsCheck", value);
        return this;
    }
}
