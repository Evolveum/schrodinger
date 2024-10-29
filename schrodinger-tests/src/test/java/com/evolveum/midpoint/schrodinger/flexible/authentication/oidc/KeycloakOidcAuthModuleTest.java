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

import com.evolveum.midpoint.schrodinger.flexible.authentication.util.KeycloakUtils;

import java.io.File;

public class KeycloakOidcAuthModuleTest extends AbstractOidcAuthModuleTest {

    protected static final File SECURITY_POLICY_PKCE_FILE =
            new File (BASE_DIR_FOR_SECURITY_FILES + "using-pkce-keycloak.xml");

    protected String getServerPrefix() {
        return KeycloakUtils.SERVER_PREFIX;
    }

    @Override
    protected void login(String username, String password) {
        KeycloakUtils.login(username, password);
    }

    @Override
    protected void logoutAndCheckIt(String username) {
        KeycloakUtils.logoutAndCheckIt();
    }

    @Override
    protected File getSecurityPolicyPKCEFile() {
        return SECURITY_POLICY_PKCE_FILE;
    }
}
