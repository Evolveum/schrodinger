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
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.modal.ObjectBrowserModal;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Utils;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$x;

public class RoleCatalogStepPanel extends TileListWizardStepPanel<RequestAccessPage> {

    public RoleCatalogStepPanel(RequestAccessPage parent) {
        super(parent);
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

    public boolean assertAccessesPanelContainsItems(String... itemLabels) {
        if (itemLabels == null) {
            return false;
        }
        return Arrays.stream(itemLabels).allMatch(item -> findTileByLabel(item) != null);
    }

    private RoleCatalogStepPanel selectMenuByLabel(String label) {
        $x(".//div[@data-s-id='link' " +
                "and descendant-or-self::*[contains(., '" + label + "')]]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
//                "and contains(text(), '" + label + "')]").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        Utils.waitForAjaxCallFinish();
        return RoleCatalogStepPanel.this;
    }

    public RoleCatalogStepPanel addItemToCart(String itemLabel) {
        SelenideElement item = findTileByLabel(itemLabel);
        if (item != null) {
            item.$x(".//a[@data-s-id='add']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
            Utils.waitForAjaxCallFinish();
        }
        return RoleCatalogStepPanel.this;
    }

    public ShoppingCartStepPanel navigateToShoppingCartPanel() {
        clickNextButton();
        return new ShoppingCartStepPanel(getParent());
    }

    private static class RolesOfTeammatePanel extends Component<RequestAccessPage> {

        public RolesOfTeammatePanel(RequestAccessPage parent, SelenideElement parentElement) {
            super(parent, parentElement);
        }

        public ObjectBrowserModal clickManualButton() {
            getParentElement().$x(".//a[data-s-id='manual']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
            Utils.waitForAjaxCallFinish();
            return new ObjectBrowserModal(RolesOfTeammatePanel.this, Utils.getModalWindowSelenideElement());
        }
    }

}
