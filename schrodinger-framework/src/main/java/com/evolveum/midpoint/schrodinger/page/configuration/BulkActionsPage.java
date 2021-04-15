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
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class BulkActionsPage extends BasicPage {

    public BulkActionsPage insertOneLineTextIntoEditor(String text){
        $(Schrodinger.byElementAttributeValue("textarea", "class", "ace_text-input"))
                .waitUntil(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S)
                .sendKeys(text);
        return this;
    }

    public BulkActionsPage startButtonClick(){
        $(Schrodinger.byDataId("a", "start"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return this;
    }

    public boolean isAceEditorVisible(){
        return $(By.className("aceEditor")).exists();
    }

    public BulkActionsPage assertAceEditorVisible() {
        assertion.assertTrue(isAceEditorVisible(), "Ace editor should be visible.");
        return this;
    }
}
