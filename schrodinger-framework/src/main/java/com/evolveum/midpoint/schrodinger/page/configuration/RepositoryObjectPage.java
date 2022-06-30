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
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class RepositoryObjectPage extends BasicPage {

    public String getObjectXmlText() {
//        Utils.setCheckFormGroupOptionCheckedById("switchToPlainText", true);
        $(By.name("switchToPlainText:container:check")).setSelected(true).shouldBe(Condition.selected, MidPoint.TIMEOUT_SHORT_4_S);
        $(Schrodinger.byDataId("plainTextarea")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        String xml = $(Schrodinger.byDataId("plainTextarea")).getValue();
        return xml == null ? "" : xml;
    }

    public RepositoryObjectPage assertObjectXmlContainsText(String expectedText) {
        String objectXml = getObjectXmlText();
        assertion.assertTrue(objectXml.contains(expectedText), "Object xml doesn't contain text: " + expectedText);
        return this;
    }
}
