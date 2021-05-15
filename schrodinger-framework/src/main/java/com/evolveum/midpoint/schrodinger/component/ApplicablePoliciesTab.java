package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class ApplicablePoliciesTab<T extends FocusPage> extends Component<T> {

    public ApplicablePoliciesTab(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ApplicablePoliciesTab<T> selectPolicyByName(String name) {
        Utils.setCheckFormGroupOptionCheckedByValue(name, true);
        return this;
    }

    public ApplicablePoliciesTab<T> deselectPolicyByName(String name) {
        Utils.setCheckFormGroupOptionCheckedByValue(name, false);
        return this;
    }
}
