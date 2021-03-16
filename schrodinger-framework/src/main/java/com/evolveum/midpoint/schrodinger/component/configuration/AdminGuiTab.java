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
package com.evolveum.midpoint.schrodinger.component.configuration;

import static com.evolveum.midpoint.schrodinger.util.ConstantsUtil.*;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.TabWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;

/**
 * Created by Viliam Repan (lazyman).
 */
public class AdminGuiTab extends TabWithContainerWrapper<SystemPage> {

    public AdminGuiTab(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SystemPage addNewObjectCollection(String identifier, String type, String objectCollectionType, String objectCollectionName) {
        this.form()
                .expandContainerPropertiesPanel(OBJECT_COLLECTION_VIEWS_HEADER)
                .addNewContainerValue(OBJECT_COLLECTION_VIEW_HEADER, NEW_GUI_OBJECT_LIST_VIEW_HEADER)
                    .getPrismContainerPanel(NEW_GUI_OBJECT_LIST_VIEW_HEADER)
                        .getContainerFormFragment()
                        .addAttributeValue("Identifier", identifier)
                        .setDropDownAttributeValue("Type", type)
                            .getPrismContainerPanel("Display")
                                .getContainerFormFragment()
                                .addAttributeValue("Label", identifier)
                                .addAttributeValue("Singular label", identifier)
                                .addAttributeValue("Plural label", identifier)
                            .and()
                        .and()
                            .getPrismContainerPanel("Collection")
                                .getContainerFormFragment()
                                .editRefValue("Collection ref")
                                    .selectType(objectCollectionType)
                                    .table()
                                        .search()
                                            .byName()
                                            .inputValue(objectCollectionName)
                                        .updateSearch()
                                    .and()
                                    .clickByName(objectCollectionName)
                                .and()
                            .and()
                        .and()
                    .and()
                .and()
                .and()
                .clickSave();
        return getParent();
    }
}
