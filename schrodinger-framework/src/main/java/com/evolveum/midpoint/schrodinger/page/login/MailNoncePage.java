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
package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.self.ChangePasswordPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class MailNoncePage extends FormLoginPage {

    public MailNoncePage setMail(String mail) {
        $(Schrodinger.byDataId("email")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(mail);
        $(Schrodinger.byDataId("submitIdentifier")).click();
        return  this;
    }

    public static String getBasePath() {
        return "/emailNonce";
    }

    public ChangePasswordPanel<MailNoncePage> getChangePasswordPanel() {
        return new ChangePasswordPanel<>(this, $(Schrodinger.byDataId("mainForm")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }
}
