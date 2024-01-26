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
package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.wizard.TileListWizardStepPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class NewResourcePanel extends TileListWizardStepPanel<ResourceWizardPage> {

    public NewResourcePanel(ResourceWizardPage parentPage) {
        super(parentPage);
    }

    public ConnectorSelectionStep fromScratch() {
        selectTileByLabel("From scratch");
        return new ConnectorSelectionStep(getParent());
    }

    public BasicInformationWizardStep copyFromTemplate(String templateTitle) {
        selectTileByLabel("Copy from template");
        selectTileByLabel(templateTitle);
        return new BasicInformationWizardStep(getParent());
    }
}
