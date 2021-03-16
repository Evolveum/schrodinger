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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.configuration.InternalsConfigurationPage;

import static com.evolveum.midpoint.schrodinger.util.Utils.setCheckFormGroupOptionCheckedByTitleResourceKey;
import static com.evolveum.midpoint.schrodinger.util.Utils.setOptionCheckedById;

/**
 * Created by Viliam Repan (lazyman).
 */
public class TracesTab extends Component<InternalsConfigurationPage> {

    public TracesTab(InternalsConfigurationPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TracesTab selectResourceSchemaOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.resourceSchemaOperations", true);
        return this;
    }

    public TracesTab deselectResourceSchemaOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.resourceSchemaOperations", false);
        return this;
    }

    public TracesTab selectConnectorOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.connectorOperations", true);
        return this;
    }

    public TracesTab deselectConnectorOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.connectorOperations", false);
        return this;
    }

    public TracesTab selectShadowFetchOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.shadowFetchOperations", true);
        return this;
    }

    public TracesTab deselectShadowFetchOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.shadowFetchOperations", false);
        return this;
    }

    public TracesTab selectRepositoryOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.repositoryOperations", true);
        return this;
    }

    public TracesTab deselectRepositoryOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.repositoryOperations", false);
        return this;
    }

    public TracesTab selectRoleEvaluations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.roleEvaluations", true);
        return this;
    }

    public TracesTab deselectRoleEvaluations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.roleEvaluations", false);
        return this;
    }

    public TracesTab selectPrismOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.prismOperations", true);
        return this;
    }

    public TracesTab deselectPrismOperations() {
        setCheckFormGroupOptionCheckedByTitleResourceKey("InternalOperationClasses.prismOperations", false);
        return this;
    }

}

