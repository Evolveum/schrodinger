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
package com.evolveum.midpoint.schrodinger.page.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.resource.ResourcesPageTable;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ListResourcesPage extends BasicPage {

    public ResourcesPageTable<ListResourcesPage> table() {
        SelenideElement table = $(By.cssSelector(".box.boxed-table.object-resource-box")).waitUntil(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourcesPageTable<>(this, table);
    }

    public ListResourcesPage testConnectionClick(String resourceName){
        table().search()
                    .byName()
                    .inputValue(resourceName)
                    .updateSearch()
                .and()
            .clickMenuItemButton("ObjectType.name", resourceName, ".fa.fa-question");
        $(By.cssSelector("div.feedbackContainer")).waitUntil(Condition.appears, MidPoint.TIMEOUT_EXTRA_LONG_10_M);
        if (feedback().isError()) {
            if (feedback().getParentElement().$x(".//a[@data-s-id='showAll']").exists()) {
                feedback().getParentElement().$x(".//a[@data-s-id='showAll']").click();
                Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
            }
            Selenide.screenshot(resourceName + "testConnectionError"
                    + Long.toString(System.currentTimeMillis()).substring(5, 8));
        }
        return this;

    }
}
