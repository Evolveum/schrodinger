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

package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.Toast;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class LoggedUser {

    public FormLoginPage logout() {
        //sometimes Toast component covers logout button; therefore, we try to close all toast message panels at first
        ElementsCollection toasts = $$x(".//div[contains(@class, \"toast\") and contains(@class, \"bg-\")]");
        if (!toasts.isEmpty()) {
            toasts.asFixedIterable().forEach(toast -> toast.$x(".//button[contains(@class, 'close')]").click());
        }
        $(Schrodinger.byDataId("logoutForm"))
                .find(By.cssSelector(".fas.fa-power-off"))
                .parent()
                .click();

        //todo implement

        return new FormLoginPage();
    }

    public FormLoginPage logoutIfUserIsLogin() {
        if($(Schrodinger.byDataId("logoutForm")).exists()) {
            $(Schrodinger.byDataId("logoutForm")).click();
            Utils.waitForAjaxCallFinish();
            $(Schrodinger.byDataId("logoutForm")).shouldBe(Condition.hidden, MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return new FormLoginPage();
    }
}
