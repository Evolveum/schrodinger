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
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.task.TaskBasicPanel;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


/**
 * Created by matus on 3/20/2018.
 */
public class FeedbackBox<T> extends Component<T> {

    public FeedbackBox(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelenideElement getChildElement(String id){
        return getParentElement().$(Schrodinger.byDataId("div", id)).shouldBe(Condition.appear, MidPoint.TIMEOUT_LONG_1_M);
    }

    public SelenideElement getChildElement(){
        return getParentElement().$(Schrodinger.byDataId("div", "0")).shouldBe(Condition.appear, MidPoint.TIMEOUT_LONG_1_M);
    }

    public Boolean isSuccess() {
        return getParentElement().getAttribute("class") != null
                && getParentElement().getAttribute("class").contains("card-success");
    }

    public Boolean isWarning(String idOfChild) {

        return getChildElement(idOfChild).$(By.cssSelector("div.feedback-message.box.box-solid.box-warning")).exists();

    }

    public Boolean isWarning() {
        return getParentElement().getAttribute("class") != null
                && getParentElement().getAttribute("class").contains("card-warning");
    }

    public Boolean isError(String idOfChild) {
        return getChildElement(idOfChild).$(By.cssSelector("div.feedback-message.box.box-solid.box-danger")).exists();
    }

    public Boolean isError() {
        return getParentElement().getAttribute("class") != null
                && getParentElement().getAttribute("class").contains("card-danger");
    }

    public Boolean isInfo(String idOfChild) {
        return getChildElement(idOfChild).$(By.cssSelector("div.feedback-message.box.box-solid.box-info")).exists();
    }

    public Boolean isInfo() {
        return  isInfo("0");
    }

    public FeedbackBox<T> clickShowAll() {

        $(Schrodinger.byDataId("showAll")).click();

        return this;
    }

    public FeedbackBox<T> clickClose() {

        $(Schrodinger.byDataId("close")).click();

        return this;
    }

    public TaskBasicPanel clickShowTask() {

        $(Schrodinger.byDataId("backgroundTask")).scrollIntoView(false).click();
        Utils.waitForMainPanelOnDetailsPage();
        TaskPage taskPage = new TaskPage();
        SelenideElement taskBasicTab = taskPage.getNavigationPanelSelenideElement("Basic");
        return new TaskBasicPanel(new TaskPage(), taskBasicTab);
    }

    public CasePage clickShowCase() {
        getParentElement().$x(".//a[@data-s-id='case']").shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S).click();
        $(Schrodinger.byDataId("summaryBox")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        return new CasePage();
    }

    public Boolean isFeedbackBoxPresent() {
        return getParentElement().isDisplayed();
    }

    public FeedbackBox<T> assertSuccess() {
        assertion.assertTrue(isSuccess(), "Feedback panel status is not success.");
        return this;
    }

    public FeedbackBox<T> assertError() {
        assertion.assertTrue(isError(), "Feedback panel status is not error.");
        return this;
    }

    public FeedbackBox<T> assertWarning() {
        assertion.assertTrue(isWarning(), "Feedback panel status is not warning.");
        return this;
    }

    public FeedbackBox<T> assertInfo() {
        assertion.assertTrue(isInfo("1"), "Feedback panel status is not info.");
        return this;
    }

    @Deprecated
    public FeedbackBox<T> assertSuccess(String idOfChild) {
        assertion.assertTrue(isSuccess(), "Feedback panel status is not success.");
        return this;
    }

    /**
     * should be reviewed
     * @param idOfChild
     * @return
     */
    @Deprecated
    public FeedbackBox<T> assertError(String idOfChild) {
        assertion.assertTrue(isError(idOfChild), "Feedback panel status is not error.");
        return this;
    }

    /**
     * should be reviewed
     * @param idOfChild
     * @return
     */
    @Deprecated
    public FeedbackBox<T> assertInfo(String idOfChild) {
        assertion.assertTrue(isInfo(idOfChild), "Feedback panel status is not info.");
        return this;
    }

    public FeedbackBox<T> assertMessageExists(String messageText) {
        assertion.assertTrue($(By.linkText(messageText)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).exists());
        return this;
    }
}
