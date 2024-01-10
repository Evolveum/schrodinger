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
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.modal.SelectMarkModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.simulation.ProcessedObjectsTable;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/25/2018.
 */
public class ResourceShadowTable<T> extends TableWithPageRedirect<T, AccountPage, ResourceShadowTable<T>> {
    public ResourceShadowTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public AccountPage clickByName(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement link = getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "title", name));
        link.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        Utils.waitForAjaxCallFinish();
        $(By.className("page-title")).shouldBe(Condition.textCaseSensitive("Account"), MidPoint.TIMEOUT_LONG_1_M);

        return new AccountPage();
    }

    @Override
    public ResourceShadowTable<T> selectRowByName(String name) {
        Utils.waitForAjaxCallFinish();
        SelenideElement check = $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("input", "type", "checkbox", "data-s-id", "3", name));
        check.shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        check.shouldBe(Condition.selected, MidPoint.TIMEOUT_MEDIUM_6_S);

        return this;
    }

    public UserPage clickOnOwnerByName(String name) {
        return clickOnOwnerByName(name, "");
    }

    public UserPage clickOnOwnerByName(String name, String expectedUserPageTitle) {

        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        if (StringUtils.isNotEmpty(expectedUserPageTitle)) {
            $(Schrodinger.byDataId("span", "pageTitle"))
                    .shouldBe(Condition.text(expectedUserPageTitle), MidPoint.TIMEOUT_MEDIUM_6_S);
        } else {
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        }
        return new UserPage();
    }

    @Override
    protected TableHeaderDropDownMenu<ResourceShadowTable<T>> clickHeaderActionDropDown() {
        $(Schrodinger.byElementAttributeValue("button", "data-toggle", "dropdown"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        SelenideElement cog = $(Schrodinger.byElementAttributeValue("ul","role","menu"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new TableHeaderDropDownMenu<>(this, cog);
    }

    @Override
    public Search<ResourceShadowTable<T>> search() {
        return (Search<ResourceShadowTable<T>>) super.search();
    }

    public ResourceShadowTable<T> enable() {
        return enable(null, null);
    }

    public ResourceShadowTable<T> enable(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.enableAccount");
        return this;
    }

    public ResourceShadowTable<T> disable() {
        return disable(null, null);
    }

    public ResourceShadowTable<T> disable(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.disableAccount");
        return this;
    }

    public ConfirmationModal<ResourceShadowTable<T>> delete() {
        return delete(null, null);
    }

    public ConfirmationModal<ResourceShadowTable<T>> delete(String columnTitleKey, String rowValue) {
        return clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.deleteAccount");
    }

    public ResourceShadowTable<T> importAccount() {
        return importAccount(null, null);
    }

    public ResourceShadowTable<T> importAccount(String columnTitleKey, String rowValue) {
        clickMenu(columnTitleKey, rowValue, "pageContentAccounts.menu.importAccount");
        return this;
    }

    public TaskExecutionModePopup importPreview() {
        return importPreview(null, null);
    }

    public TaskExecutionModePopup importPreview(String columnTitleKey, String rowValue) {
        clickMenu(columnTitleKey, rowValue, "ShadowTablePanel.menu.importPreviewAccount");
        SelenideElement popup = Utils.getModalWindowSelenideElement();
        return new TaskExecutionModePopup(ResourceShadowTable.this, popup);
    }

    public ResourceShadowTable<T> removeMarks() {
        return removeMarks(null, null);
    }

    public ResourceShadowTable<T> removeMarks(String columnTitleKey, String rowValue, String... marks) {
        clickMenu(columnTitleKey, rowValue, "pageContentAccounts.menu.mark.remove");
        SelectMarkModal<ResourceShadowTable<T>> modal =
                new SelectMarkModal<>(this, Utils.getModalWindowSelenideElement());
        modal.selectMarks(marks);
        modal.clickConfirmationButton();
        return this;
    }

    public ResourceShadowTable<T> removeOwner() {
        return removeOwner(null, null);
    }

    public ResourceShadowTable<T> removeOwner(String columnTitleKey, String rowValue) {
        clickMenuItemWithConfirmation(columnTitleKey, rowValue, "pageContentAccounts.menu.removeOwner");
        return this;
    }

    public ResourceShadowTable<T> assertRealObjectIsMarked(String objectName, String markName) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", objectName, true);
        SelenideElement mark = row.getParentElement()
                .$x(".//span[@data-s-id='realMarks' and contains(text(), '" + markName + "')]");
        assertion.assertTrue(mark.isDisplayed(), "Mark " + markName + " is not present for object " + objectName);
        return this;
    }

    public ResourceShadowTable<T> assertProcessedObjectIsMarked(String objectName, String markName) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", objectName, true);
        SelenideElement mark = row.getParentElement()
                .$x(".//span[@data-s-id='processedMarks' and contains(text(), '" + markName + "')]");
        assertion.assertTrue(mark.isDisplayed(), "Mark " + markName + " is not present for object " + objectName);
        return this;
    }

    public ResourceShadowTable<T> assertSituationEquals(String objectName, String situation) {
        TableRow<?, ?> row = findRowByColumnLabelAndRowValue("Name", objectName, true);
        SelenideElement cell = row.getParentElement()
                .$x(".//div[@data-s-id='cell' and contains(text(), '" + situation + "')]");
        assertion.assertTrue(cell.isDisplayed(), "Situation " + situation + " is not present for object " + objectName);
        return this;
    }


    @Override
    public AccountPage getObjectDetailsPage(){
        return new AccountPage();
    }

}
