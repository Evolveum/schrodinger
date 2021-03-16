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
package com.evolveum.midpoint.schrodinger.component.org;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class ManagerPanel<T> extends Component<T> {

    public ManagerPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public boolean containsManager(String... expectedManagers) {
        ElementsCollection managersElements = getParentElement().$$x(".//span[@" + Schrodinger.DATA_S_ID + "='summaryDisplayName']");
        List<String> managers = new ArrayList<String>();
        for (SelenideElement managerElement : managersElements) {
            managers.add(managerElement.getText());
        }

        return managers.containsAll(Arrays.asList(expectedManagers));
    }

    public ManagerPanel<T> assertContainsManager(String... expectedManagers) {
        assertion.assertTrue(containsManager(expectedManagers));
        return this;
    }
}
