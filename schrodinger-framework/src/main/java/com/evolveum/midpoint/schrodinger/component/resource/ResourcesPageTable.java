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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.resource.ViewResourcePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 4/25/2018.
 */
public class ResourcesPageTable<T> extends TableWithPageRedirect<T> {
    public ResourcesPageTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    @Override
    public TableWithPageRedirect<T> selectCheckboxByName(String name) {
        return this;
    }

    @Override
    protected TableHeaderDropDownMenu<ResourcesPageTable> clickHeaderActionDropDown() {
        return null;
    }

    @Override
    public ViewResourcePage clickByName(String name) {
        getParentElement().$(Schrodinger.byElementValue("span", "data-s-id", "label", name))
                .waitUntil(Condition.appears, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        ViewResourcePage detailsPage = new ViewResourcePage();
        if (detailsPage.isUseTabbedPanel()) {
            detailsPage.getTabPanel();
        } else {
            detailsPage.getNavigationPanel();
        }
        return new ViewResourcePage();
    }

    @Override
    public Search<ResourcesPageTable<T>> search() {
        SelenideElement searchElement = getParentElement().$(By.cssSelector(".form-inline.pull-right.search-form"));

        return new Search<>(this, searchElement);
    }
}
