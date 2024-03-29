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
package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
/**
 * Created by honchar.
 */
public class SchemaWizardStep extends Component<ResourceWizardPage, SchemaWizardStep> {
    public SchemaWizardStep(ResourceWizardPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TabPanel<SchemaWizardStep> getTabPanel() {
        return new TabPanel<>(this, getParentElement());
    }

//    public SchemaStepSchemaPanel selectSchemaTab() {
//        return new SchemaStepSchemaPanel(this, getTabPanel().clickTabWithName("Schema"));
//    }

    public SchemaStepXmlPanel selectXmlTab() {
        return new SchemaStepXmlPanel(this, getTabPanel().clickTabWithName("XML"));
    }
}
