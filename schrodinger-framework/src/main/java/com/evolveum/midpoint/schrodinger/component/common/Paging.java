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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByAttribute;
import com.evolveum.midpoint.schrodinger.SchrodingerException;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.apache.commons.lang3.Validate;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Paging<T> extends Component<T> {

    public Paging(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public Paging<T> first() {
        getParentElement().$(Schrodinger.byDataId("a", "firstLink")).click();
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> previous() {
        getParentElement().$(Schrodinger.byDataId("a", "previousLink")).click();
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> next() {
        getParentElement().$(Schrodinger.byDataId("a", "nextLink")).click();
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> last() {
        getParentElement().$(Schrodinger.byDataId("a", "lastLink")).click();
        Selenide.sleep(1000);
        return this;
    }

    private void moveThroughPages(int offsetFromActual) {
        SelenideElement ul = getParentElement().find(Schrodinger.byDataId("pagination"));

        ElementsCollection col = ul.$$x(".//li");
        SelenideElement active = col.find(Condition.cssClass("active"));
        int index = col.indexOf(active);

        index = index + offsetFromActual;
        if (index < 2 || index > col.size() - 2) {
            // it's <<, <, >, >>
            Selenide.screenshot("PageDoesntExist.png");
            throw new SchrodingerException("Can't move through paging, page doesn't exist. Please, see screenshot PageDoesntExist.png");
        }

        col.get(index).$x(".//a").click();
    }

    public Paging<T> actualPageMinusOne() {
        moveThroughPages(-1);
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> actualPageMinusTwo() {
        moveThroughPages(-2);
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> actualPagePlusOne() {
        moveThroughPages(1);
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> actualPagePlusTwo() {
        moveThroughPages(2);
        Selenide.sleep(1000);
        return this;
    }

    public Paging<T> pageSize(int size) {
        Validate.isTrue(size > 0, "Size must be larger than zero.");

        SelenideElement parent = getParentElement();

        SelenideElement pagingSize = parent.parent().$x(".//div[contains(@class, 'paging-size')]");


        pagingSize.$x(".//select[@data-s-id='size']").selectOption("" + size);
        Selenide.sleep(2000);
        return this;
    }

    public int currentPageNumber() {
        SelenideElement activePageElement = getParentElement()
                .$x(".//li[contains(@class, 'page-item' and contains(@class, 'active'))]");
        if (activePageElement.isDisplayed()) {
            String selectedIndexStr = activePageElement.getAttribute("data-s-id");
            try {
                return Integer.parseInt(selectedIndexStr + 1);
            } catch (Exception e) {
                //nothing to do here
            }
        }
        return -1;
    }

    public int getPagesCount() {
        ElementsCollection pageItems = getParentElement()
                .$$x(".//li[contains(@class, 'page-item')]");
        int pagesCount = 0;
        for (SelenideElement el : pageItems.asFixedIterable()) {
            try {
                String selectedIndexStr = el.getAttribute("data-s-id");
                int temp = Integer.parseInt(selectedIndexStr) + 1;
                if (temp > pagesCount) {
                    pagesCount = temp;
                }
            } catch (Exception e) {
                //nothing to do here
            }
        }
        return pagesCount;
    }
}
