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
import com.evolveum.midpoint.schrodinger.component.wizard.NextStepAction;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

public class RelationStepPanel extends TileListWizardStepPanel<RequestAccessPage>
        implements NextStepAction<RoleCatalogStepPanel> {

    public static final int DEFAULT_RELATION_INDEX = 1;
    public static final int MANAGER_RELATION_INDEX = 2;
    public static final int APPROVER_RELATION_INDEX = 3;
    public static final int OWNER_RELATION_INDEX = 4;


    public RelationStepPanel(RequestAccessPage parent) {
        super(parent, $(Schrodinger.byDataId("contentBody")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public RoleCatalogStepPanel selectDefaultRelation() {
        if (!isRelationSelected(DEFAULT_RELATION_INDEX)) {
            selectTileByNumber(DEFAULT_RELATION_INDEX);
            return new RoleCatalogStepPanel(getParent());
        }
        return next();
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
        selectTileByLabelAndMoveToNext(label);
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

    @Override
    public RoleCatalogStepPanel next() {
        clickNext();
        return new RoleCatalogStepPanel(getParent());
    }

    //Request access can be configured in such a way, that relation step would be skipped
    //may be this suits for other wizards as well? implement in the parent class?
    public RoleCatalogStepPanel next(boolean skipRelationStep) {
        if (!skipRelationStep) {
            clickNext();
        }
        return new RoleCatalogStepPanel(getParent());
    }
}
