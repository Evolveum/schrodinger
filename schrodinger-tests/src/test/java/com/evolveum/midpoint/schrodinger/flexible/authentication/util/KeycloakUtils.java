/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.flexible.authentication.util;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.util.AssertionWithScreenshot;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class KeycloakUtils {

    public static final String SERVER_PREFIX = "keycloak";
    protected static AssertionWithScreenshot assertion = new AssertionWithScreenshot();

    public static void login(String username, String password) {
        $(By.name("username")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(username);

        $(By.name("password")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(password);
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
    }

    public static void logoutAndCheckIt() {
        String name = $x(".//div[@class='login-pf-page-header']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .text();

        assertion.assertEquals(
                name,
                "KEYCLOAK IDP FOR SCHRODINGER TESTS",
                "Wrong name of keycloak realm after logout.");

        $(By.name("username")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }
}
