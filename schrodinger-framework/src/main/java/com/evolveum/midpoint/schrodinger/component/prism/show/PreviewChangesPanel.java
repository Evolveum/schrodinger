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
package com.evolveum.midpoint.schrodinger.component.prism.show;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.PreviewPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class PreviewChangesPanel extends Component<PreviewPage> {

    public PreviewChangesPanel(PreviewPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public VisualizationPanel<PreviewChangesPanel> primaryDeltas() {
        SelenideElement primaryDeltas = $(Schrodinger.byDataId("primary"));
        return new VisualizationPanel<>(this, primaryDeltas);
    }

    public VisualizationPanel<PreviewChangesPanel> secondaryDeltas() {
        SelenideElement secondaryDeltas = $(Schrodinger.byDataId("secondaryDeltas"));
        return new VisualizationPanel<>(this, secondaryDeltas);
    }
}
