/*
 * Copyright (c) 2021 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class DetailsNavigationPanel<T extends AssignmentHolderDetailsPage> extends Component<T> {

    public DetailsNavigationPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelenideElement selectPanelByName(String... name) {
        for (String navigationItemName : name) {
            SelenideElement nav = $(Schrodinger.byElementAttributeValue("span", "data-s-resource-key", navigationItemName))
                    .waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
            nav.click();
            nav.parent().parent().waitUntil(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return $(Schrodinger.byDataId("mainPanel")).waitUntil(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }
}
