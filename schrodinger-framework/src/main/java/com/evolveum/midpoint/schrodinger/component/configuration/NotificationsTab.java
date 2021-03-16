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

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class NotificationsTab extends Component<SystemPage> {

    public NotificationsTab(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public NotificationsTab setRedirectToFile(String value) {
        SelenideElement property = $(Schrodinger.byDataId("redirectToFile"));
        ElementsCollection values = property.$$(By.className("prism-property-value"));
        if (values.size() == 1) {
            values.first().$(By.className("form-control")).setValue(value);
        }
        return this;
    }
}
