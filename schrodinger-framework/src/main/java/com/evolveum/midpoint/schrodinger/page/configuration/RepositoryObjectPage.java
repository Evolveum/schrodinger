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
package com.evolveum.midpoint.schrodinger.page.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RepositoryObjectPage extends BasicPage {

    public String getObjectXmlText() {
//        Utils.setCheckFormGroupOptionCheckedById("switchToPlainText", true);
        $(By.name("switchToPlainText:container:check")).setSelected(true).shouldBe(Condition.selected, MidPoint.TIMEOUT_SHORT_4_S);
        $(Schrodinger.byDataId("plainTextarea")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        String xml = $(Schrodinger.byDataId("plainTextarea")).getValue();
        return xml == null ? "" : xml;
    }

    public RepositoryObjectPage insertIntoXmlText(String textToInsert, String textToInsertAfter) {
        getOptionsCheckBoxByTitleKey("pageDebugView.switchToPlainText.help").setSelected(true)
                .shouldBe(Condition.selected, MidPoint.TIMEOUT_SHORT_4_S);
        SelenideElement textArea = $(Schrodinger.byDataId("plainTextarea"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        String xml = textArea.getValue();
        if (xml == null) {
            xml = "";
        }
        if (xml.contains(textToInsertAfter)) {
            int position = xml.indexOf(textToInsertAfter) + textToInsertAfter.length();
            xml = xml.substring(0, position) + textToInsert + xml.substring(position + 1);
        } else {
            xml = xml + textToInsert;
        }
        textArea.setValue(xml);
        Selenide.sleep(1000);
        return RepositoryObjectPage.this;
    }

    private SelenideElement getOptionsCheckBoxByTitleKey(String checkBoxTitleKey) {
        return $x(".//div[@title='" + checkBoxTitleKey + "']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$x(".//input[@type='checkbox']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public ListRepositoryObjectsPage clickSaveButton() {
        SelenideElement buttonsPanel = $x(".//fieldset[contains(@class, 'objectButtons')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        buttonsPanel.$x(".//a[contains(text(), 'Save')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new ListRepositoryObjectsPage();
    }

    public ListRepositoryObjectsPage clickBackButton() {
        SelenideElement buttonsPanel = $x(".//fieldset[contains(@class, 'objectButtons')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        buttonsPanel.$x(".//a[contains(text(), 'Back')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new ListRepositoryObjectsPage();
    }

    public RepositoryObjectPage assertObjectXmlContainsText(String expectedText) {
        String objectXml = getObjectXmlText();
        assertion.assertTrue(objectXml.contains(expectedText), "Object xml doesn't contain text: " + expectedText);
        return this;
    }
}
