/*
 * Copyright (c) 2023  Evolveum
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

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import org.testng.annotations.Test;

public class PasswordPanelTest extends AbstractSchrodingerTest {

    /**
     * Covers MID-9333
     */
    @Test
    public void test00100setPasswordValueWithLastSpace() {
        basicPage.newUser()
                .selectBasicPanel()
                .form()
                .addAttributeValue("Name", "PasswordPanelTest")
                .and()
                .and()
                .selectAssignmentsPanel()
                .clickAddAllAssignment()
                .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ROLE)
                .table()
                .selectRowByName("End user")
                .and()
                .clickAdd()
                .and()
                .selectPasswordPanel()
                .setPasswordValue("Password123 ")
                .and()
                .clickSave()
                .feedback()
                .isSuccess();

        basicPage.loggedUser().logout();

        FormLoginPage login = new FormLoginPage();
        login.login("PasswordPanelTest", "Password123")
                .assertUserMenuDoesntExist()
                        .feedback()
                                .assertError();

        login.login("PasswordPanelTest", "Password123 ")
                .assertUserMenuExist();
    }

    /**
     * Covers MID-11072
     */
    @Test
    public void test00101MultipleProtectedStringTypeFieldsInSamePanel() {
        reloginAsAdministrator();

        basicPage.newPerson()
                .selectBasicPanel()
                .form()
                .addAttributeValue("Name", "ProtectedStringTypeTest")
                .and()
                .formContainer("Passwords")
                .addProtectedAttributeValue("secretPassword", "Password123")
                .addProtectedAttributeValue("Value", "Password123*")
                .and()
                .and()
                .selectAssignmentsPanel()
                .clickAddAllAssignment()
                .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ROLE)
                .table()
                .selectRowByName("End user")
                .and()
                .clickAdd()
                .and()
                .clickSave()
                .feedback()
                .isSuccess();

        basicPage.loggedUser().logout();

        FormLoginPage login = new FormLoginPage();
        login.login("ProtectedStringTypeTest", "Password123*")
                .assertUserMenuExist();
    }
}
