package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.util.stream.Collectors;

public class FeedbackContainerPanel<T> extends Component<T> {

    public FeedbackContainerPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public FeedbackContainerPanel<T> assertFeedbackMessagesCountEquals(int expectedFeedbackMessagesCount) {
        int realMessagesCount = countFeedbackMessages();
        assertion.assertEquals(realMessagesCount, expectedFeedbackMessagesCount);
        return this;
    }

    private int countFeedbackMessages() {
        return getMessagesCollection().size();
    }

    private ElementsCollection getMessagesCollection() {
        return getParentElement().$$(Schrodinger.byDataId("message"))
                .filterBy(Condition.visible);
    }
}
