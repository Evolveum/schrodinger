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

import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.component.wizard.WizardStepPanel;

public class PersonOfInterestStepPanel extends TileListWizardStepPanel<RequestAccessPage> {

    private static final int MYSELF_TILE_INDEX = 1;
    private static final int GROUP_TILE_INDEX = 2;

    public PersonOfInterestStepPanel(RequestAccessPage parent) {
        super(parent);
    }

    public RelationStepPanel selectMyself() {
        selectTileByNumber(MYSELF_TILE_INDEX);
        return new RelationStepPanel(getParent());
    }

    public WizardStepPanel<RequestAccessPage> selectGroup() {
        selectTileByNumber(GROUP_TILE_INDEX);
        return new RelationStepPanel(getParent());
    }
}
