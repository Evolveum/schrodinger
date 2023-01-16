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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

import javax.management.relation.Role;
import java.util.Objects;

public class RelationStepPanel extends TileListWizardStepPanel<RequestAccessPage> {

    public static final int DEFAULT_RELATION_INDEX = 1;
    public static final int MANAGER_RELATION_INDEX = 2;
    public static final int APPROVER_RELATION_INDEX = 3;
    public static final int OWNER_RELATION_INDEX = 4;


    public RelationStepPanel(RequestAccessPage parent) {
        super(parent);
    }

    public RoleCatalogStepPanel selectDefaultRelation() {
        if (!isRelationSelected(DEFAULT_RELATION_INDEX)) {
            selectTileByNumber(DEFAULT_RELATION_INDEX);
        } else {
            clickNextButton();
        }
        return new RoleCatalogStepPanel(getParent());
    }

    public RoleCatalogStepPanel selectOwnerRelation() {
        if (!isRelationSelected(OWNER_RELATION_INDEX)) {
            selectTileByNumber(OWNER_RELATION_INDEX);
        }
        return new RoleCatalogStepPanel(getParent());
    }

    public RoleCatalogStepPanel selectApproverRelation() {
        if (!isRelationSelected(APPROVER_RELATION_INDEX)) {
            selectTileByNumber(APPROVER_RELATION_INDEX);
        }
        return new RoleCatalogStepPanel(getParent());
    }

    public RoleCatalogStepPanel selectManagerRelation() {
        if (!isRelationSelected(MANAGER_RELATION_INDEX)) {
            selectTileByNumber(MANAGER_RELATION_INDEX);
        }
        return new RoleCatalogStepPanel(getParent());
    }

    public RoleCatalogStepPanel selectRelationByLabel(String label) {
        selectTileByLabel(label);
        return new RoleCatalogStepPanel(getParent());
    }

    private boolean isRelationSelected(String label) {
        SelenideElement tile = findTileByLabel(label);
        return tile != null && Objects.requireNonNullElse(tile.getAttribute("class"), "").contains("active");
    }
   private boolean isRelationSelected(int number) {
       SelenideElement tile = findTileByNumber(number);
       return tile != null && Objects.requireNonNullElse(tile.getAttribute("class"), "").contains("active");
    }


}
