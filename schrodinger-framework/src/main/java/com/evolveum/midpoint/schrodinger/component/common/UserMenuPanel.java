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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.login.LoginPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;
/**
 * Created by honchar
 */
public class UserMenuPanel<BP extends BasicPage> extends Component<BP> {

    public UserMenuPanel(BP parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public LoginPage clickLogout() {
        getParentElement()
                .$(Schrodinger.byDataId("logoutForm"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(Schrodinger.byElementAttributeValue("input", "type", "submit"))
                .click();
        $(Schrodinger.byElementAttributeValue("input", "name", "username")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new LoginPage();
    }
}
