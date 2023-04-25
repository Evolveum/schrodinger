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
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.self.ChangePasswordPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.checkerframework.checker.units.qual.C;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Created by honchar.
 */
public class ForgetPasswordSecurityQuestionsPage {


    public MyPasswordQuestionsPanel<ForgetPasswordSecurityQuestionsPage> getPasswordQuestionsPanel(){
        SelenideElement element = $(Schrodinger.byDataId("questionAnswer"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new MyPasswordQuestionsPanel<ForgetPasswordSecurityQuestionsPage>(this, element);
    }

    public void clickSendButton() {
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
    }

    public ChangePasswordPanel<ForgetPasswordSecurityQuestionsPage> getChangePasswordPanel() {
        return new ChangePasswordPanel<>(this, $(Schrodinger.byDataId("mainForm")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }
}
