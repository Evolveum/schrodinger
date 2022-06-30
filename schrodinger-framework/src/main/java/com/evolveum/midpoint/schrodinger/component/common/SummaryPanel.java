/*
 * Copyright (c) 2010-2021 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.component.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.summarytagbox.SummaryBox;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.Assert;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by matus on 3/21/2018.
 */
public class SummaryPanel<T> extends Component<T> {
    public SummaryPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public String fetchDisplayName() {
        return $(Schrodinger.byDataId("summaryDisplayName")).getText();
    }

    public String fetchSummaryTitle() {
        return $(Schrodinger.byDataId("summaryDisplayName")).getText();
    }

    public String fetchSummaryTitle2() {
        return $(Schrodinger.byDataId("summaryDisplayName2")).getText();
    }

    public String fetchSummaryTitle3() {
        return $(Schrodinger.byDataId("summaryDisplayName3")).getText();
    }

    public String fetchSummaryOrganization() {
        return $(Schrodinger.byDataId("summaryOrganization")).getText();
    }

    public SummaryBox<SummaryPanel<T>> summaryBox() {

        SelenideElement summaryBox = $(Schrodinger.byDataId("summaryTagBox")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new SummaryBox<>(this, summaryBox);

    }

    public SummaryPanel<T> assertDisplayNameEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, fetchDisplayName(), "Display name on Summary panel doesn't match");
        return this;
    }

    public SummaryPanel<T> assertSummaryTitleEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, fetchSummaryTitle(), "Summary title on Summary panel doesn't match");
        return this;
    }

    public SummaryPanel<T> assertSummaryTitle2Equals(String expectedValue) {
        assertion.assertEquals(expectedValue, fetchSummaryTitle2(), "Summary title (2) on Summary panel doesn't match");
        return this;
    }

    public SummaryPanel<T> assertSummaryTitle3Equals(String expectedValue) {
        assertion.assertEquals(expectedValue, fetchSummaryTitle3(), "Summary title (3) on Summary panel doesn't match");
        return this;
    }

    public SummaryPanel<T> assertSummaryOrganizationEquals(String expectedValue) {
        assertion.assertEquals(expectedValue, fetchSummaryOrganization(), "Summary organization on Summary panel doesn't match");
        return this;
    }

    public SummaryPanel<T> assertSummaryTagWithTextExists(String summaryTagText) {
        List<SelenideElement> summaryTagElements = $$(Schrodinger.byDataId("summaryTag"));
        boolean exists = false;
        if (summaryTagText != null) {
            for (SelenideElement el : summaryTagElements) {
                if (summaryTagText.equals(el.getText())) {
                    exists = true;
                    break;
                }
            }
        }
        assertion.assertTrue(exists, "Summary tag with text " + summaryTagText + " doesn't exists");
        return this;
    }

}
