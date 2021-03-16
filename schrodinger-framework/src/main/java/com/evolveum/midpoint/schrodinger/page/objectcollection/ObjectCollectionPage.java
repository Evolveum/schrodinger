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
package com.evolveum.midpoint.schrodinger.page.objectcollection;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentHolderBasicTab;
import com.evolveum.midpoint.schrodinger.component.common.search.SearchPropertiesConfigPanel;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Kate Honchar.
 */
public class ObjectCollectionPage extends AssignmentHolderDetailsPage {

    public SearchPropertiesConfigPanel<ObjectCollectionPage> configSearch() {
        selectTabBasic()
                .form()
                .findProperty("Filter")
                .$(Schrodinger.byDataId("configureButton"))
                .waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return new SearchPropertiesConfigPanel<>(this, Utils.getModalWindowSelenideElement());
    }

    @Override
    public AssignmentHolderBasicTab<ObjectCollectionPage> selectTabBasic() {
        SelenideElement element = getTabPanel().clickTab("pageObjectCollection.basic.title")
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new AssignmentHolderBasicTab<>(this, element);
    }

}
