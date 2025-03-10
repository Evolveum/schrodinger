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
import com.evolveum.midpoint.schrodinger.MidPoint;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AzureUtils {

    public static final String SERVER_PREFIX = "azure";

    private static final String USER_NAME_SUFFIX = "@evolveumsandbox.onmicrosoft.com";

    public static String getUsernameWithSuffix(String username) {
        return username + USER_NAME_SUFFIX;
    }

    public static void login(String username, String password) {
        $(By.name("loginfmt")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).setValue(username);
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();

        $(By.name("passwd")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).setValue(password);
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();

        $x(".//input[@type='button']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
    }
}
