package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

public class FeedbackContainerPanel<T> extends Component<T, FeedbackContainerPanel<T>> {

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

    public TaskPage clickShowTask() {
        SelenideElement backgroundTaskLink = getParentElement().$(Schrodinger.byDataId("backgroundTaskLink"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        Utils.scrollToElement(backgroundTaskLink);
        backgroundTaskLink
                .click();

        return new TaskPage();
    }
}
