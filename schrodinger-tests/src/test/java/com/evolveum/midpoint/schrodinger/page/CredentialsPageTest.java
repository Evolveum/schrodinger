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
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by honchar
 */
public class CredentialsPageTest extends AbstractSchrodingerTest {
    private static final File CHANGE_USER_PASSWORD_TEST_FILE = new File("./src/test/resources/objects/users/change-user-password-test-user.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(CHANGE_USER_PASSWORD_TEST_FILE);
    }

    @Test (priority = 1)
    public void test0010changeUserPasswordSuccessfully() {
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password");
        basicPage.credentials()
                .passwordTab()
                    .changePasswordPanel()
                        .setOldPasswordValue("password")
                        .setNewPasswordValue("Password1")
                        .setRepeatPasswordValue("Password1")
                        .changePassword()
                            .assertPasswordPropagationResultSuccess("CredentialsPageTestUser")
                            .and()
                        .and()
                .toast()
                .assertSuccess();
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password1")
                .assertUserMenuExist();
    }

    @Test(priority = 2, dependsOnMethods = {"test0010changeUserPasswordSuccessfully"})
    public void test0020changeUserPasswordWrongOldPassword() {
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password1");
        basicPage.credentials()
                .passwordTab()
                    .changePasswordPanel()
                        .setOldPasswordValue("wrongPassword")
                        .setNewPasswordValue("passwordNew")
                        .setRepeatPasswordValue("passwordNew")
                .changePassword()
                .and()
                .and()
                .toast()
                .assertError();
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "passwordNew");
        basicPage.assertUserMenuDoesntExist();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password1")
                .assertUserMenuExist();
    }

    @Test(priority = 3, dependsOnMethods = {"test0010changeUserPasswordSuccessfully"})
    public void test0030changeUserPasswordNewPasswordDoesntMatch() {
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password1");
        basicPage.credentials()
                .passwordTab()
                    .changePasswordPanel()
                        .setOldPasswordValue("password1")
                        .setNewPasswordValue("passwordNew1")
                        .setRepeatPasswordValue("passwordNew2")
                .changePassword()
                .and()
                .and()
                .toast()
                .assertError();
        basicPage.loggedUser().logout();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "passwordNew1")
                .assertUserMenuDoesntExist();
        midPoint.formLogin()
                .loginWithReloadLoginPage("CredentialsPageTestUser", "password1")
                .assertUserMenuExist();
    }

}
