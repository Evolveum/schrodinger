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
package com.evolveum.midpoint.schrodinger.component.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.configuration.InternalsConfigurationPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.setCheckFormGroupOptionCheckedById;

/**
 * Created by Viliam Repan (lazyman).
 */
public class InternalConfigurationPanel extends Component<InternalsConfigurationPage> {

    public InternalConfigurationPanel(InternalsConfigurationPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public InternalConfigurationPanel selectCheckConsistency() {
        setCheckFormGroupOptionCheckedById("consistencyChecks", true);
        return this;
    }

    public InternalConfigurationPanel deselectCheckConsistency() {
        setCheckFormGroupOptionCheckedById("consistencyChecks", false);
        return this;
    }

    public InternalConfigurationPanel selectCheckEncryption() {
        setCheckFormGroupOptionCheckedById("encryptionChecks", true);
        return this;
    }

    public InternalConfigurationPanel deselectCheckEncryption() {
        setCheckFormGroupOptionCheckedById("encryptionChecks", false);
        return this;
    }

    public InternalConfigurationPanel selectCheckReadEncryption() {
        setCheckFormGroupOptionCheckedById("readEncryptionChecks", true);
        return this;
    }

    public InternalConfigurationPanel deselectCheckReadEncryption() {
        setCheckFormGroupOptionCheckedById("readEncryptionChecks", false);
        return this;
    }

    public InternalConfigurationPanel selectModelProfiling() {
        setCheckFormGroupOptionCheckedById("modelProfiling", true);
        return this;
    }

    public InternalConfigurationPanel deselectModelProfiling() {
        setCheckFormGroupOptionCheckedById("modelProfiling", false);
        return this;
    }

    public InternalConfigurationPanel selectTolerateUndeclaredPrefixes() {
        setCheckFormGroupOptionCheckedById("tolerateUndeclaredPrefixes", true);
        return this;
    }

    public InternalConfigurationPanel deselectTolerateUndeclaredPrefixes() {
        setCheckFormGroupOptionCheckedById("tolerateUndeclaredPrefixes", false);
        return this;
    }

    public InternalConfigurationPanel clickUpdate() {
        $(Schrodinger.byDataId("updateInternalsConfig"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }
}

