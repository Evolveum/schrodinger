/*
 * Copyright (c) 2023  Evolveum
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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class ResourceDetailsPanel<T> extends Component<T, ResourceDetailsPanel<T>> {
    public ResourceDetailsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ResourceDetailsPanel<T> changeCapabilityState(String buttonTitle) {
        SelenideElement button = getCapabilityButton(buttonTitle);
        if (button != null && button.isDisplayed()) {
            button.click();
            Utils.waitForAjaxCallFinish();
        }
        return ResourceDetailsPanel.this;
    }

    public ResourceDetailsPanel<T> changeUpdateCapabilityState(Map<String, String> propertyValueMap) {
        SelenideElement button = getCapabilityButton("Update");
        if (button == null || button.isDisplayed()) {
            return ResourceDetailsPanel.this;
        }
        button.click();
        SelenideElement modal = Utils.getModalWindowSelenideElement(MidPoint.TIMEOUT_DEFAULT_2_S);
        PrismForm<ResourceDetailsPanel<T>> form = new PrismForm<>(ResourceDetailsPanel.this,
                modal.$x(".//div[@data-s-id='mainForm']").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        propertyValueMap.forEach(form::setDropDownAttributeValue);
        modal.$(Schrodinger.byDataId("okButton")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Utils.waitForAjaxCallFinish();
        return ResourceDetailsPanel.this;
    }

    private SelenideElement getCapabilityButton(String title) {
        String titleTranslated = Utils.translate(title);
        ElementsCollection buttons = $$x(".//a[@data-s-id='capabilityButton']");
        ElementsCollection.SelenideElementIterable it = buttons.asFixedIterable();
        return it.stream().filter(b -> b.getText().equals(titleTranslated))
                .findFirst()
                .orElse(null);
    }
}
