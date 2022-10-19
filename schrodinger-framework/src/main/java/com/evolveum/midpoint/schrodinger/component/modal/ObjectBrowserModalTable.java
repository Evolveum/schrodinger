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
package com.evolveum.midpoint.schrodinger.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class ObjectBrowserModalTable<T, M extends ModalBox<T>> extends Table<M> {

    public ObjectBrowserModalTable(M parent, SelenideElement parentElement){
        super(parent, parentElement);
    }

    public T clickByName(String name){
        Utils.waitForAjaxCallFinish();
        SelenideElement link = getParentElement().$(By.linkText(name));
        link.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
//        if (link.exists() && link.isDisplayed()) {
//            link.click();
//        }

        link
                .shouldBe(Condition.hidden, MidPoint.TIMEOUT_LONG_20_S);
        return getParent().getParent();
    }

    public ObjectBrowserModalTable<T, M> selectCheckboxByName(String name) {
        $(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("input", "type", "checkbox", "data-s-id", "3", name))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        return this;
    }

    @Override
    public Search<ObjectBrowserModalTable<T, M>> search() {
        return (Search<ObjectBrowserModalTable<T, M>>) super.search();
    }
}
