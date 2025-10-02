package com.evolveum.midpoint.schrodinger.component.common.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class SaveSearchPopupPanel<T> extends Component<Search<T>, SaveSearchPopupPanel<T>> {

    public SaveSearchPopupPanel(Search<T> parent) {
        super(parent, Utils.getModalWindowSelenideElement());
    }

    public SaveSearchPopupPanel<T> setFilterName(String filterName) {
        getParentElement().$x(".//input[@data-s-id='searchName']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(filterName);
        Utils.waitForAjaxCallFinish();
        return SaveSearchPopupPanel.this;
    }

    public Search<T> save() {
        getParentElement().$x(".//a[@data-s-id='saveButton']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }

    public Search<T> cancel() {
        getParentElement().$x(".//a[@data-s-id='cancelButton']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return getParent();
    }
}
