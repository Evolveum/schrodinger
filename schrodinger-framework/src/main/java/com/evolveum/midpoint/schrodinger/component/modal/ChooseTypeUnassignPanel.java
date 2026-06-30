package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ChooseTypeUnassignPanel<T> extends ModalBox<T> {

    public ChooseTypeUnassignPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ChooseTypeUnassignPanel<T> selectType(String option) {
        //todo implement
        return this;
    }


    public ChooseTypeUnassignPanel<T> setRelation(String relation) {
        //todo implement
        return this;
    }

    public T clickOk() {
        Utils.waitForAjaxCallFinish();
        SelenideElement addButton = getParentElement()
                .$x(".//a[@data-s-id='ok']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        addButton.click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this.getParent();
    }

    public T clickCancel() {
        Utils.waitForAjaxCallFinish();
        SelenideElement cancelButton = getParentElement()
                .$x(".//a[@data-s-id='cancel']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        cancelButton.click();
        Utils.waitForAjaxCallFinish();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this.getParent();
    }
}
