package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ApplicablePoliciesTab<T extends FocusPage> extends Component<T> {

    public ApplicablePoliciesTab(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ApplicablePoliciesTab<T> selectPolicyByName(String name) {
        $(byText(name)).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .find(By.tagName("input")).setSelected(true);
        return this;
    }

    public ApplicablePoliciesTab<T> deselectPolicyByName(String name) {
        $(byText(name)).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .find(By.tagName("input")).setSelected(false);
        return this;
    }
}
