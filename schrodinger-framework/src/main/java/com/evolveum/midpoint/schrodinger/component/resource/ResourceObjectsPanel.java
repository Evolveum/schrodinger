package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class ResourceObjectsPanel<T> extends Component<T> {
    public ResourceObjectsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ResourceShadowTable<ResourceObjectsPanel<T>> table() {

        SelenideElement element = $(Schrodinger.byDataId("div", "itemsTable"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceShadowTable<>(this, element);
    }

}
