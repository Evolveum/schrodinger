package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;

public class CompositedButtonPanel<T> extends Component<T> {

    public CompositedButtonPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public String getBasicIconCss() {
        return getParentElement().$x(".//i[@data-s-id='basicIcon']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).getAttribute("class");
    }

    public String getTopRightLayerIconCss() {
        return getParentElement().$x(".//i[contains(@class,\"top-right-layer\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).getAttribute("class");
    }

    public String getBottomRightLayerIconCss() {
        return getParentElement().$x(".//i[contains(@class,\"bottom-right-layer\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).getAttribute("class");
    }

    public String getLabelValue() {
        return getParentElement().$x(".//span[@class=\"compositedButtonLabel\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S).getValue();
    }

    public void assertBasicIconCssEquals(String expectedValue) {
        assertion.assertEquals(getBasicIconCss(), expectedValue, "Basic icon class doesn't equal, ");
    }

    public void assertBasicIconCssContains(String expectedValue) {
        assertion.assertTrue(getBasicIconCss() != null && getBasicIconCss().contains(expectedValue),
                "Basic icon class doesn't contain value " + expectedValue + ", ");
    }

    public void assertBottomRightLayerIconCssContains(String expectedValue) {
        assertion.assertTrue(getBottomRightLayerIconCss() != null && getBottomRightLayerIconCss().contains(expectedValue),
                "Bottom right icon class doesn't contain value " + expectedValue + ", ");
    }

    public void assertTopRightLayerIconCssContains(String expectedValue) {
        assertion.assertTrue(getTopRightLayerIconCss() != null && getTopRightLayerIconCss().contains(expectedValue),
                "Top right icon class doesn't contain value " + expectedValue + ", ");
    }

    public void assertLabelEquals(String expectedValue) {
        assertion.assertEquals(getBasicIconCss(), expectedValue, "Label doesn't equal, ");
    }
}
