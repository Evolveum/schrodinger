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
package com.evolveum.midpoint.schrodinger.component.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Kate Honchar
 */
public class OperationRequestPanel extends Component<CasePage> {

    public OperationRequestPanel(CasePage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public boolean changesAreApplied(){
        return $(Schrodinger.byDataId("operationRequestCasePanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.className("box-success"))
                .$(byText("Changes applied (successfully or not)"))
                .is(Condition.visible);
    }

    public boolean changesAreRejected(){
        return $(Schrodinger.byDataId("operationRequestCasePanel"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.className("box-danger"))
                .$(byText("Changes rejected"))
                .is(Condition.visible);
    }

    public OperationRequestPanel assertChangesAreApplied() {
        assertion.assertTrue(changesAreApplied(), "Changes are not applied");
        return this;
    }

    public OperationRequestPanel assertChangesAreRejected() {
        assertion.assertTrue(changesAreRejected(), "Changes are not rejected");
        return this;
    }
}
