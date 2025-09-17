/*
 * Copyright (c) 2023. Evolveum
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.page.self.accessrequest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RoleCatalogStepPanel extends TileListWizardStepPanel<RequestAccessPage>
        implements NextStepAction<ShoppingCartStepPanel> {

    private static final String TABLE_VIEW_ICON_CLASS = "fa-table-list";
    private static final String TILES_VIEW_ICON_CLASS = "fa-table-cells";

    public RoleCatalogStepPanel(RequestAccessPage parent) {
        super(parent, $(Schrodinger.byDataId("contentBody")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public RoleCatalogStepPanel selectAllRolesMenu() {
        return selectMenuByLabel("All roles");
    }

    public RoleCatalogStepPanel selectAllOrganizationsMenu() {
        return selectMenuByLabel("All organizations");
    }

    public RoleCatalogStepPanel selectAllServicesMenu() {
        return selectMenuByLabel("All services");
    }

    public RoleCatalogStepPanel selectRolesOfTeammateMenu(String teammateName) {
        selectMenuByLabel("Roles of teammate");
        RolesOfTeammatePanel panel = new RolesOfTeammatePanel(getParent(), getParentElement().$x(".//ul[@data-s-id='container']"));
        panel.clickManualButton()
                .table()
                .clickByName(teammateName);
        return RoleCatalogStepPanel.this;
    }

    public Search<RoleCatalogStepPanel> search() {
        return new Search<>(RoleCatalogStepPanel.this, getParentElement().$(By.cssSelector(".search-panel-form")));
    }

    public boolean assertAccessesPanelContainsItems(String... itemLabels) {
        if (itemLabels == null) {
            return false;
        }
        return Arrays.stream(itemLabels).allMatch(item -> findTileByLabel(item) != null);
    }

    public RoleCatalogStepPanel selectMenuByLabel(String label) {
        $x(".//div[@data-s-id='link' " +
                "and descendant-or-self::*[contains(., '" + label + "')]]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
//                "and contains(text(), '" + label + "')]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        Utils.waitForAjaxCallFinish();
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel addItemToCart(String itemLabel) {
        Selenide.screenshot("addItemToCart_1");
        search().byName().inputValue(itemLabel).updateSearch();
        Selenide.screenshot("addItemToCart_2");
        SelenideElement item = findTileByLabel(itemLabel);
        if (item != null) {
            Selenide.screenshot("addItemToCart_3");
            item.$x(".//a[@data-s-id='add']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            Utils.waitForAjaxCallFinish();
        }
        Selenide.screenshot("addItemToCart_4");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.toMillis());
        return RoleCatalogStepPanel.this;
    }

    public <RCT extends RoleCatalogItemsTable<RoleCatalogStepPanel, RCT>> RoleCatalogItemsTable<RoleCatalogStepPanel, RCT> table() {
        return new RoleCatalogItemsTable<>(RoleCatalogStepPanel.this);
    }

    public RoleCatalogStepPanel selectTableView() {
        clickHeaderToggleButton(TABLE_VIEW_ICON_CLASS);
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel selectTilesView() {
        clickHeaderToggleButton(TILES_VIEW_ICON_CLASS);
        return RoleCatalogStepPanel.this;
    }

    private void clickHeaderToggleButton(String iconClass) {
        SelenideElement iconElement = getViewToggleButtonByIconClass(iconClass);
        iconElement.click();
        iconElement.parent().shouldHave(Condition.attribute("aria-pressed", "true"),
                MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    private SelenideElement getViewToggleButtonByIconClass(String iconClass) {
        SelenideElement item = $(Schrodinger.byDataId("div", "viewToggle"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return item.$x(".//i[contains(@class, '" + iconClass + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public RoleCatalogStepPanel assertShoppingCartIsEmpty() {
        SelenideElement shoppingCart = $(Schrodinger.byDataId("mainHeader"))
                .$x(".//span[@data-s-id='cartCount']");
        assertion.assertTrue(!shoppingCart.exists() || !shoppingCart.isDisplayed() ||
                StringUtils.isEmpty(shoppingCart.getText()), "Shopping cart should be empty.");
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel assertShoppingCartIsNotEmpty() {
        SelenideElement shoppingCart = $(Schrodinger.byDataId("mainHeader"))
                .$x(".//span[@data-s-id='cartCount']");
        assertion.assertTrue(shoppingCart.exists() && shoppingCart.isDisplayed() &&
                StringUtils.isNotEmpty(shoppingCart.getText()), "Shopping cart should not be empty.");
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel assertShoppingCartCountEquals(String expectedValue) {
        SelenideElement shoppingCart = $(Schrodinger.byDataId("mainHeader"))
                .$x(".//span[@data-s-id='cartCount']");
        assertion.assertTrue(shoppingCart.exists() && shoppingCart.isDisplayed() &&
                StringUtils.equals(expectedValue, shoppingCart.getText()),
                "Shopping cart items count doesn't equal to expected value (" + expectedValue + ")");
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel assertTableViewIsSelected() {
        SelenideElement iconElement = getViewToggleButtonByIconClass(TABLE_VIEW_ICON_CLASS);
        String ariaPressedAttribute = iconElement.parent().getAttribute("aria-pressed");
        assertion.assertTrue("true".equals(ariaPressedAttribute), "Table view is not selected.");
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel assertTilesViewIsSelected() {
        SelenideElement iconElement = getViewToggleButtonByIconClass(TILES_VIEW_ICON_CLASS);
        String ariaPressedAttribute = iconElement.parent().getAttribute("aria-pressed");
        assertion.assertTrue("true".equals(ariaPressedAttribute), "Tiles view is not selected.");
        return RoleCatalogStepPanel.this;
    }

    @Override
    public ShoppingCartStepPanel next() {
        $x(".//a[@data-s-id='nextLink' and contains(@class, 'btn-success')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        return new ShoppingCartStepPanel(getParent());
    }

    private static class RolesOfTeammatePanel extends Component<RequestAccessPage, RolesOfTeammatePanel> {

        public RolesOfTeammatePanel(RequestAccessPage parent, SelenideElement parentElement) {
            super(parent, parentElement);
        }

        public ObjectBrowserModal clickManualButton() {
            getParentElement().$x(".//a[@data-s-id='manual']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
            Utils.waitForAjaxCallFinish();
            return new ObjectBrowserModal(RolesOfTeammatePanel.this, Utils.getModalWindowSelenideElement());
        }
    }

}
