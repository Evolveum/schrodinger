package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ApplicablePoliciesTab<T> extends Component<T> {

    public ApplicablePoliciesTab(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

}
