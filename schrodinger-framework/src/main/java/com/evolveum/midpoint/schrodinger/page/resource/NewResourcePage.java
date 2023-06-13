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

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.BasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceWizardPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by Viliam Repan (lazyman).
 */
public class NewResourcePage extends BasicPage {

    public BasicInformationWizardStep createResourceFromScratch(String resourceTitle) {
        $x(".//div[@data-s-id='panelHeader']")
                .$x(".//div[@data-s-id='type']")
                .$x(".//select[@data-s-id='input']")
                .selectOption("From scratch");
        Utils.waitForAjaxCallFinish();
        $x(".//div[@data-s-id='tile' and contains(text(), '" + resourceTitle + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        ResourceWizardPage page = new ResourceWizardPage();
        page.assertBasicStep();
        return new BasicInformationWizardStep(page);
    }

    public BasicInformationWizardStep createResourceFromTemplate(String templateTitle) {
        $x(".//div[@data-s-id='panelHeader']")
                .$x(".//div[@data-s-id='type']")
                .$x(".//select[@data-s-id='input']")
                .selectOption("From template");
        Utils.waitForAjaxCallFinish();
        $x(".//div[@data-s-id='tile' and contains(text(), '" + templateTitle + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        ResourceWizardPage page = new ResourceWizardPage();
        page.assertBasicStep();
        return new BasicInformationWizardStep(page);
    }
}
