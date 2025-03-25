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
package com.evolveum.midpoint.schrodinger.flexible.authentication.oidc;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.flexible.authentication.util.AzureUtils;

import static com.codeborne.selenide.Selenide.$x;

public class AzureOidcAuthModuleTest extends AbstractOidcAuthModuleTest {

    protected String getServerPrefix() {
        return AzureUtils.SERVER_PREFIX;
    }

    @Override
    protected void login(String username, String password) {
        AzureUtils.login(username, password);
    }

    @Override
    protected void logoutAndCheckIt(String username) {
        $x(".//div[@class='table' and @role='button' and @data-test-id='" + username + "']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();

        $x(".//div[@id='login_workload_logo_text']").shouldBe(Condition.exist, MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    @Override
    protected String getEnabledUserName() {
        return AzureUtils.getUsernameWithSuffix(super.getEnabledUserName());
    }

    @Override
    protected String getDisabledUsername() {
        return AzureUtils.getUsernameWithSuffix(super.getDisabledUsername());
    }

    @Override
    protected String getNonExistUsername() {
        return AzureUtils.getUsernameWithSuffix(super.getNonExistUsername());
    }

    protected String getDisabledUserFilePath() {
        return USER_FILE_PREFIX + getServerPrefix() + "-" + DISABLED_USER_FILE_SUFFIX;
    }

    protected String getEnabledUserFilePath() {
        return USER_FILE_PREFIX + getServerPrefix() + "-" + ENABLED_USER_FILE_SUFFIX;
    }
}
