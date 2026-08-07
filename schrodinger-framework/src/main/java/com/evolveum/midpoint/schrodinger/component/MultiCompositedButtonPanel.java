package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.*;

public class MultiCompositedButtonPanel<T> extends Component<T, MultiCompositedButtonPanel<T>> {

    public MultiCompositedButtonPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public CompositedButtonPanel<MultiCompositedButtonPanel<T>> findCompositedButtonByTitle(String buttonTitle) {
        SelenideElement button = $(Schrodinger.byElementAttributeValue("button", "title", buttonTitle));
        if (!button.exists()) {
            button = $x(".//span[@data-s-id='label' and normalize-space(text())='" +
                    buttonTitle + "']/ancestor::button[@data-s-id='compositedButton']");
        }
        button.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new CompositedButtonPanel<>(this, button);
    }

    public CompositedButtonPanel<MultiCompositedButtonPanel<T>> findCompositedButtonByLabel(String buttonLabel) {
        SelenideElement button = $(Schrodinger.byElementValue("span", "class", "compositedButtonLabel", buttonLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new CompositedButtonPanel<>(this, button);
    }

    public ElementsCollection findCompositedButtonsListByBasicIcon(String basicIcon) {
        return $$x(".//i[contains(@class,\"" + basicIcon + "\")]");
    }

}
