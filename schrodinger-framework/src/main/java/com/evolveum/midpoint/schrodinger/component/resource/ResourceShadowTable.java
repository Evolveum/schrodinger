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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/25/2018.
 */
public class ResourceShadowTable<T> extends TableWithPageRedirect<T> {
    public ResourceShadowTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public AccountPage clickByName(String name) {
        SelenideElement link = getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name));
        link.waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        $(By.className("page-title")).waitUntil(Condition.textCaseSensitive("Account"), MidPoint.TIMEOUT_LONG_1_M);

        return new AccountPage();
    }

    @Override
    public ResourceShadowTable<T> selectCheckboxByName(String name) {

        SelenideElement check = $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("input", "type", "checkbox", "data-s-id", "3", name));
        check.waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        check.waitUntil(Condition.selected, MidPoint.TIMEOUT_MEDIUM_6_S);

        return this;
    }

    public UserPage clickOnOwnerByName(String name) {
        return clickOnOwnerByName(name, "");
    }

    public UserPage clickOnOwnerByName(String name, String expectedUserPageTitle) {

        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        if (StringUtils.isNotEmpty(expectedUserPageTitle)) {
            $(Schrodinger.byDataId("span", "pageTitle"))
                    .waitUntil(Condition.text(expectedUserPageTitle), MidPoint.TIMEOUT_MEDIUM_6_S);
        } else {
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        }
        return new UserPage();
    }

    @Override
    protected TableHeaderDropDownMenu<ResourceShadowTable<T>> clickHeaderActionDropDown() {
        $(Schrodinger.byElementAttributeValue("button", "data-toggle", "dropdown"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement cog = $(Schrodinger.byElementAttributeValue("ul","role","menu"))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<>(this, cog);
    }

    @Override
    public Search<? extends ResourceShadowTable<T>> search() {
        return (Search<? extends ResourceShadowTable<T>>) super.search();
    }

    public ResourceShadowTable<T> clickEnable() {
        return clickEnable(null, null);
    }

    public ResourceShadowTable<T> clickEnable(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.enableAccount");
        return this;
    }

    public ResourceShadowTable<T> clickDisable() {
        return clickDisable(null, null);
    }

    public ResourceShadowTable<T> clickDisable(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.disableAccount");
        return this;
    }

    public ConfirmationModal<ResourceShadowTable<T>> clickDelete() {
        return clickDelete(null, null);
    }

    public ConfirmationModal<ResourceShadowTable<T>> clickDelete(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.deleteAccount");
    }

    public ResourceShadowTable<T> clickImport() {
        return clickImport(null, null);
    }

    public ResourceShadowTable<T> clickImport(String columnTitleKey, String rowValue) {
        clickMenu(columnTitleKey, rowValue, "pageContentAccounts.menu.importAccount");
        return this;
    }

    public ResourceShadowTable<T> clickRemoveOwner() {
        return clickRemoveOwner(null, null);
    }

    public ResourceShadowTable<T> clickRemoveOwner(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.removeOwner");
        return this;
    }
}
