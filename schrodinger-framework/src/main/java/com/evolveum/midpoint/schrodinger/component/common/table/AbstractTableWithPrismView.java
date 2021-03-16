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
package com.evolveum.midpoint.schrodinger.component.common.table;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;

/**
 * Created by matus on 5/17/2018.
 */
public abstract class AbstractTableWithPrismView<T> extends Table<T> {
    public AbstractTableWithPrismView(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public abstract PrismFormWithActionButtons<AbstractTableWithPrismView<T>> clickByName(String name);

    public abstract AbstractTableWithPrismView<T> selectCheckboxByName(String name);

    public abstract AbstractTableWithPrismView<T> selectHeaderCheckbox();

    public abstract AbstractTableWithPrismView<T> removeByName(String name);

    public abstract AbstractTableWithPrismView<T> clickHeaderActionButton(String actionButtonStyle);
}
