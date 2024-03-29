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
package com.evolveum.midpoint.schrodinger.component.assignmentholder;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.BasicPage;

import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author skublik
 */

public abstract class ObjectListPageWithPageRedirect<T extends TableWithPageRedirect, 
        D extends BasicPage> extends BasicPage {

    public abstract T table();

    public abstract D getObjectDetailsPage();

    protected SelenideElement getTableBoxElement(){
        SelenideElement box = $(Schrodinger.byDataId("div", "itemsTable"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return box;
    }

    protected String getTableAdditionalClass(){
        return null;
    }

    public int getCountOfObjects() {
        String countString = $(Schrodinger.byDataId("div", "count")).getText();
        return Integer.valueOf(countString.substring(countString.lastIndexOf(" ")+1));
    }

    public D newObjectButtonClick(String title) {
        return (D) table()
                .newObjectButtonByTitleClick(title);
    }

    public ObjectListPageWithPageRedirect<T, D> assertObjectsCountEquals(int expectedCount) {
        assertion.assertEquals(getCountOfObjects(), expectedCount, "Objects count doesn't equal to " + expectedCount);
        return this;
    }

}
