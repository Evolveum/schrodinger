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

import com.codeborne.selenide.*;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class SecurityQuestionsPage extends FormLoginPage {

    public SecurityQuestionsPage setUsername(String username) {
        $(Schrodinger.byDataId("username")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(username);
        $(Schrodinger.byDataId("showQuestions")).click();
        Utils.waitForAjaxCallFinish();
        return  this;
    }

    public SecurityQuestionsPage setAnswer(int index, String value) {
        Utils.waitForAjaxCallFinish();
        ElementsCollection col = $$(Schrodinger.byDataId("questionAnswer"));
        Utils.waitForAjaxCallFinish();
        col.get(index).sendKeys(value);
        return this;
    }

    public BasicPage submit() {
        $(Schrodinger.byDataId("insideForm")).click();
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return new BasicPage();
    }

    public MyPasswordQuestionsPanel<SecurityQuestionsPage> getPasswordQuestionsPanel() {
        return new MyPasswordQuestionsPanel<>(this,
                $(Schrodinger.byDataId("questionAnswerPanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

//    public SecurityQuestionsPage register() {
//        // todo implement
//        return this;
//    }
//
//    public BasicPage loginWithReloadLoginPage(String username, String password) {
//        return loginWithReloadLoginPage(username, password, null);
//    }
//
//    public BasicPage loginWithReloadLoginPage(String username, String password, String locale) {
//        open("/login");
//        Selenide.sleep(5000);
//
//        return login(username, password, locale);
//    }
//
//    public BasicPage login(String username, String password) {
//        return login(username, password, null);
//    }
//
//    public BasicPage login(String username, String password, String locale) {
//        if (StringUtils.isNotEmpty(locale)){
//            changeLanguage(locale);
//        }
//        $(By.name("username")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(username);
//        $(By.name("password")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).setValue(password);
//        $x(".//input[@type='submit']").click();
//
//        return new BasicPage();
//    }
//
//
//    public FeedbackBox<? extends SecurityQuestionsPage> feedback() {
//        SelenideElement feedback = $(By.cssSelector("div.feedbackContainer")).shouldBe(Condition.appear, MidPoint.TIMEOUT_LONG_1_M);
//        return new FeedbackBox<>(this, feedback);
//    }

    public static String getBasePath() {
        return "/securityquestions";
    }

    public FormLoginPage waitForSubmitButtonToBeVisible() {
        $x(".//a[@data-s-id='showQuestions']").shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
        return this;
    }

}
