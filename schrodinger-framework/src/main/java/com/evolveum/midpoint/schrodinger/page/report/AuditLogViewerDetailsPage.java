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
package com.evolveum.midpoint.schrodinger.page.report;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.prism.show.VisualizationPanel;
import com.evolveum.midpoint.schrodinger.page.BasicPage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AuditLogViewerDetailsPage extends BasicPage {

    public VisualizationPanel<AuditLogViewerDetailsPage> deltaPanel() {
        SelenideElement el = $x(".//div[@data-s-id='deltaPanel']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new VisualizationPanel<>(this, el);
    }

    public AuditLogViewerDetailsPage assertAuditLogViewerDetailsPageIsOpened() {
        assertion.assertTrue($(byText("Audit Log Details")).exists());
        return this;
    }


}
