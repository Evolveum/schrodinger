/*
 * Copyright (c) 2021 Evolveum
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
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.Selenide.$;

public class DetailsNavigationPanel<T extends AssignmentHolderDetailsPage> extends Component<T> {

    public DetailsNavigationPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public SelenideElement selectPanelByName(String... name) {
        SelenideElement nav = null;
        for (String navigationItemName : name) {
            String translatedNavigationName = Utils.translate(navigationItemName);
            if (nav == null) {
                nav = getParentElement().$x(".//div[@data-s-id='navItem' and contains(@class, \"text-truncate\")" +
                                " and contains(text(), '" + translatedNavigationName + "')]");
            } else {
                while (nav.parent() != null) {
                    nav = nav.parent();
                    String cssClass = nav.getAttribute("class");
                    if (StringUtils.isNotEmpty(cssClass) && cssClass.contains("navigation-details")) {
                        break;
                    }
                }
                nav = nav.$x(".//div[@data-s-id='navItem' and contains(@class, \"text-truncate\")" +
                                " and contains(text(), '" + translatedNavigationName + "')]");
            }
            Utils.scrollToElement(nav);
            nav.shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
            nav.click();
            nav.parent().parent().parent().shouldBe(Condition.cssClass("active"), MidPoint.TIMEOUT_LONG_20_S);
        }
        return $(Schrodinger.byDataId("mainPanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
    }
}
