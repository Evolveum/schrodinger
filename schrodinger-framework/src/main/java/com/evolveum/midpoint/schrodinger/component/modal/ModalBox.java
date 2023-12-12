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

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.Component;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.By;

/**
 * Created by matus on 4/26/2018.
 */
public class ModalBox<T> extends Component<T, ModalBox<T>> {
    public ModalBox(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    /**
     * waits for 20 seconds for modal popup content to be hidden
     * @return true if popup content is empty
     */
    public boolean waitToBeHidden() {
        long end = System.currentTimeMillis() + 10000;
        while (System.currentTimeMillis() < end) {
            if (!getParentElement().exists()) {
                return true;
            }
        }
        return false;
    }
}
