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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.page.resource.EditResourceConfigurationPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

/**
 * Created by matus on 3/28/2018.
 */
public class ResourceConfigurationPanel extends PanelWithContainerWrapper<EditResourceConfigurationPage> {
    public ResourceConfigurationPanel(EditResourceConfigurationPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

//    public PrismForm<ResourceConfigurationPanel> form() {
//        SelenideElement element = getParentElement().$(Schrodinger.byElementAttributeValue("div", "class", "tab-content"))
//                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
//        return new PrismForm<>(this, element);
//    }
}
