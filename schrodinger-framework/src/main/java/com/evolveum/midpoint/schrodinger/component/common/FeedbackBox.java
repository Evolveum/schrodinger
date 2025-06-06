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

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


/**
 * Created by matus on 3/20/2018.
 */
public class FeedbackBox<T> extends Component<T, FeedbackBox<T>> {

    public FeedbackBox(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelenideElement getChildElement(String id){
        return getParentElement().$(Schrodinger.byDataId("div", id)).shouldBe(Condition.appear, MidPoint.TIMEOUT_LONG_1_M);
    }

    public Boolean isSuccess() {
        return containsClass("success");
    }

    public Boolean isWarning() {
        return containsClass("warning");
    }

    public Boolean isError(String idOfChild) {
        return getChildElement(idOfChild).$(By.cssSelector("div.feedback-message.box.box-solid.box-danger")).exists();
    }

    public Boolean isError() {
        return containsClass("danger");
    }

    public Boolean isInfo() {
        return containsClass("info");
    }

    private Boolean containsClass(String cssClass) {
        return getParentElement().getAttribute("class") != null
                && getParentElement().getAttribute("class").contains("card-" + cssClass);
    }

    public FeedbackBox<T> clickShowAll() {

        $(Schrodinger.byDataId("showAll")).click();

        return this;
    }

    public TaskBasicPanel clickShowTask() {

        $(Schrodinger.byDataId("backgroundTaskLink")).scrollIntoView(false).click();
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

    public FeedbackBox<T> assertSuccess() {
        assertion.assertTrue(isSuccess(), "Feedback panel status is not success.");
        return this;
    }

    public FeedbackBox<T> assertError() {
        assertion.assertTrue(isError(), "Feedback panel status is not error.");
        return this;
    }

    public FeedbackBox<T> assertInfo() {
        assertion.assertTrue(isInfo(), "Feedback panel status is not info.");
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

    public FeedbackBox<T> assertMessageExists(String messageText) {
        SelenideElement detailsBox = $(Schrodinger.byDataId("detailsBox")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        assertion.assertTrue(detailsBox
                .$(byText(messageText)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).exists());
        return this;
    }

    public String getFeedbackMessage() {
        try {
            SelenideElement messageElement = getParentElement().$x(".//div[@data-s-id='message']")
                    .shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);
            if (messageElement.exists()) {
                return messageElement.getText();
            }
        } catch (Exception e) {
            //nothing to do
        }
        return "";
    }
}
