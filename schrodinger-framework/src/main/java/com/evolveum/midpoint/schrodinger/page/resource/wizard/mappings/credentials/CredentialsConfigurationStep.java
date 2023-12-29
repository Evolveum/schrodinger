/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.credentials;

import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.TiledMappingConfigurationStep;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class CredentialsConfigurationStep<T> extends TiledMappingConfigurationStep<T, CredentialsConfigurationStep<T>> {

    public CredentialsConfigurationStep(T parent) {
        super(parent);
    }

    @Override
    public CredentialsInboundPanel<CredentialsConfigurationStep<T>> getInboundMappingPanel() {
        return new CredentialsInboundPanel<>(this, $(Schrodinger.byDataId("tilesContainer")));
    }

    @Override
    public CredentialsOutboundPanel<CredentialsConfigurationStep<T>> getOutboundMappingPanel() {
        return new CredentialsOutboundPanel<>(this, $(Schrodinger.byDataId("tilesContainer")));
    }
}
