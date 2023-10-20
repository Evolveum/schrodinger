package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.Paging;
import com.evolveum.midpoint.schrodinger.util.AssertionWithScreenshot;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.*;

public class CorrelationPage {

    private CorrelationPanel correlationPanel;
    private AssertionWithScreenshot assertion;

    public CorrelationPage() {
        super();
        correlationPanel = new CorrelationPanel();
        assertion = correlationPanel.getAssertion();
    }

    public CorrelationPage setAttributeValue(String attributeLabel, String attributeValue) {
        SelenideElement attributePanel = findAttributePanel(attributeLabel);
        attributePanel.$x(".//input").setValue(attributeValue);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    private SelenideElement findAttributePanel(String attributeLabel) {
        return $(Schrodinger.byAncestorPrecedingSiblingDescendantOrSelfElementEnclosedValue("div",
                "data-s-id", "attributeValue", "data-s-id", "attributeName", attributeLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public CorrelationPage send() {
        $x(".//input[@type='submit']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    public CorrelationPage assertSingleIdentityFound(String identityName) {
        ElementsCollection detailsPanels = $$x(".//div[@data-s-id='detailsPanel']");
        assertion.assertEquals(detailsPanels.size(), 1, "Found identities count doesn't match expected " +
                "count (expected 1, actual )" + detailsPanels.size());
        SelenideElement userDetailsPanel = detailsPanels.get(0);
        userDetailsPanel.$x(".//div[@data-s-id='displayName' and contains(., '" + identityName + "')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        return this;
    }

    public CorrelationPage assertAttributeValueShown(String attributeLabel, String attributeValue) {
        SelenideElement attributeValueElement =
                $(Schrodinger.byAncestorPrecedingSiblingDescendantOrSelfElementEnclosedValue("div",
                "data-s-id", "itemValue", "data-s-id", "itemName", attributeLabel))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        assertion.assertEquals(attributeValueElement.getText(), attributeValue,
                "The value of the " + attributeLabel + "attribute doesn't match with expected value: " + attributeValue + ", ");
        return this;
    }

    public CorrelationPage clickNextPage() {
        getPaging().next();
        return CorrelationPage.this;
    }

    public CorrelationPage assertDisplayedIdentitiesCountEquals(int expectedCount) {
        ElementsCollection detailsPanels = $$x(".//div[@data-s-id='detailsPanel']");
        assertion.assertEquals(detailsPanels.size(), expectedCount);
        return this;
    }

    public CorrelationPage assertIdentityResultsPagingExist() {
        SelenideElement paging = $(Schrodinger.byDataId("paging"));
        assertion.assertTrue(paging.isDisplayed(), "Paging is to be present on the identity recovery results panel.");
        return this;
    }

    public CorrelationPage assertPageCountEquals(int expectedPageCount) {
        assertion.assertEquals(getPaging().getPagesCount(), expectedPageCount, "Page count doesn't match,");
        return this;
    }

    private Paging<CorrelationPage> getPaging() {
        assertIdentityResultsPagingExist();
        return new Paging<>(CorrelationPage.this, $(Schrodinger.byDataId("paging")));
    }

    private class CorrelationPanel extends Component {

        public CorrelationPanel() {
            super(CorrelationPage.this);
        }

        public AssertionWithScreenshot getAssertion() {
            return assertion;
        }
    }


}
