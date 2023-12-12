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
package com.evolveum.midpoint.schrodinger.component.prism.show;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.service.ServicePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.Selenide.$;

public class PartialVisualizationHeader<T> extends Component<T, PartialVisualizationHeader<T>> {

    public PartialVisualizationHeader(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public PartialVisualizationHeader<T> assertChangeTypeEquals(String expectedValue) {
        SelenideElement element = $(Schrodinger.byDataId("changeType"));
        assertion.assertEquals(expectedValue, element.getText(), "Unexpected change type");
        return this;
    }

    public PartialVisualizationHeader<T> assertChangedObjectNameEquals(String expectedValue) {
        SelenideElement element;
//        if (isLink()) {
            element = getNameLink();
//        } else {
//            element = getNameLabel();
//        }
        assertion.assertTrue(StringUtils.equals(expectedValue, element.getText()), "Unexpected object name.");
        return this;
    }

    public PartialVisualizationHeader<T> assertChangedObjectTypeEquals(String expectedValue) {
        SelenideElement element = $(Schrodinger.byDataId("objectType"));
        assertion.assertEquals(expectedValue, element.getText(), "Unexpected change object type");
        return this;
    }

    private boolean changedObjectTypeEquals(String expectedValue) {
        SelenideElement element = $(Schrodinger.byDataId("objectType"));
        return element != null && expectedValue != null
                && element.getText() != null && expectedValue.toLowerCase().equals(element.getText().toLowerCase());
    }

    public AssignmentHolderDetailsPage clickNameLink() {
        if (!isLink()) {
            return null;
        }
        AssignmentHolderDetailsPage page = null;
        if (changedObjectTypeEquals("user")) {
            page = new UserPage();
        } else if (changedObjectTypeEquals("role")) {
            page = new RolePage();
        } else if (changedObjectTypeEquals("service")) {
            page = new ServicePage();
        } else if (changedObjectTypeEquals("org")) {
            page = new OrgPage();
        }
        getNameLink().click();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S.getSeconds());

        return page;
    }

    public boolean isLink() {
        SelenideElement element = getNameLink();
        return element.exists() && element.getTagName().equals("a");
    }

    public PartialVisualizationHeader<T> assertIsLink() {
        assertion.assertTrue(isLink(), "Link is expected.");
        return this;
    }

    public PartialVisualizationHeader<T> assertIsNotLink() {
        assertion.assertFalse(isLink(), "Link is not expected.");
        return this;
    }

    private SelenideElement getNameLabel() {
        return $(Schrodinger.byDataId("nameLabel"));
    }

    private SelenideElement getNameLink() {
        return $(Schrodinger.byDataId("nameLink"));
    }
}
