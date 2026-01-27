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
package com.evolveum.midpoint.schrodinger.page.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.cases.WorkitemDetailsPanel;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithComponentRedirect;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.self.QuickSearch;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class HomePage extends BasicPage {

    public QuickSearch<HomePage> search() {
        SelenideElement searchElement = $(Schrodinger.byDataId("searchPanel"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new QuickSearch<HomePage>(this, searchElement);
    }

    public <T extends TableWithPageRedirect<HomePage, CasePage, T>> TableWithPageRedirect<HomePage, CasePage, T> myRequestsTable() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("My requests")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .$x(".//table[@data-s-id='table']");
        return new TableWithPageRedirect<>(HomePage.this, table) {
            @Override
            public CasePage clickByName(String name) {
                getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                        .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
                $(Schrodinger.byDataId("summaryBox")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
                return new CasePage();
            }

            @Override
            public CasePage getObjectDetailsPage(){
                return new CasePage();
            }

        };
    }

    private SelenideElement getTableWidgetElementByHeaderTitle(String headerTitle) {
        String titleTranslated = Utils.translate(headerTitle);
         ElementsCollection tables = $$x(".//div[@data-s-id='objectCollectionViewWidget']");
         for (SelenideElement table : tables) {
             if (table.$(byText(titleTranslated)).exists()) {
                 return table;
             }
         }
         return null;
    }

    public HomePage assertMyAccessesWidgetVisible() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("My accesses");
        assertion.assertTrue(table != null && table.exists() && table.isDisplayed(),
                "'My accesses' widget is not displayed though it should be.");
        return HomePage.this;
    }

    public HomePage assertMyRequestsWidgetVisible() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("My requests");
        assertion.assertTrue(table != null && table.exists() && table.isDisplayed(),
                "'My requests' widget is not displayed though it should be.");
        return HomePage.this;
    }

    public HomePage assertMyAccountsWidgetVisible() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("My accounts");
        assertion.assertTrue(table != null && table.exists() && table.isDisplayed(),
                "'My accounts' widget is not displayed though it should be.");
        return HomePage.this;
    }

    public HomePage assertMyWorkItemsWidgetVisible() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("My work items");
        assertion.assertTrue(table != null && table.exists() && table.isDisplayed(),
                "'My work items' widget is not displayed though it should be.");
        return HomePage.this;
    }

    public HomePage assertCertificationItemsWidgetVisible() {
        SelenideElement table = getTableWidgetElementByHeaderTitle("Certification items");
        assertion.assertTrue(table != null && table.exists() && table.isDisplayed(),
                "'Certification items' widget is not displayed though it should be.");
        return HomePage.this;
    }
}
