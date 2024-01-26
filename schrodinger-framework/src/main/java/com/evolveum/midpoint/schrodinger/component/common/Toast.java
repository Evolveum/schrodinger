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

public class Toast<T> extends Component<T, Toast<T>> {

    public Toast(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public Boolean isSuccess() {
        return containsClass("success");
    }

    public Boolean isWarning() {
        return containsClass("warning");
    }

    public Boolean isError() {
        return containsClass("danger");
    }

    public Boolean isInfo() {
        return containsClass("info");
    }

    private Boolean containsClass(String cssClass) {
        return getParentElement().getAttribute("class") != null
                && getParentElement().getAttribute("class").contains("bg-" + cssClass);
    }


    public Toast<T> clickClose() {

        getParentElement().$x(".//button[contains(@class, 'close')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public Toast<T> assertSuccess() {
        assertion.assertTrue(isSuccess(), "Feedback panel status is not success.");
        return this;
    }

    public Toast<T> assertError() {
        assertion.assertTrue(isError(), "Feedback panel status is not error.");
        return this;
    }

    public Toast<T> assertWarning() {
        assertion.assertTrue(isWarning(), "Feedback panel status is not warning.");
        return this;
    }

    public Toast<T> assertInfo() {
        assertion.assertTrue(isInfo(), "Feedback panel status is not info.");
        return this;
    }

    public Toast<T> assertMessageExists(String messageText) {
        assertion.assertTrue($(By.linkText(messageText)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).exists());
        return this;
    }

}
