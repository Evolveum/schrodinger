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
package com.evolveum.midpoint.schrodinger.component.self;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.DropDown;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by matus on 5/10/2018.
 */
public class QuickSearchDropDown<T> extends DropDown<T> {

    public QuickSearchDropDown(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public T clickUsers(){
        $(Schrodinger.byDataResourceKey("SearchType.USERS")).parent()
                .click();

        return this.getParent();
    }

    public T clickResources(){
        $(Schrodinger.byElementValue("a" ,"Resources"))
                .click();

        return this.getParent();
    }

    public T clickTasks(){
        $(Schrodinger.byElementValue("a" ,"Tasks")).
                click();

        return this.getParent();
    }
}
