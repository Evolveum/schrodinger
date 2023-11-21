/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.page.resource.wizard;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ObjectTypeManagerPanel<T> extends Component<T> {

    public ObjectTypeManagerPanel(T parent) {
        super(parent, $x(".//div[@data-s-id='choicePanel']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public ObjectTypeBasicInformationWizardStep<ObjectTypeManagerPanel<T>> addObjectType() {
        String addNewObjectTypeKey = "ResourceSchemaHandlingPanel.newObject";
        String titleTranslated = Utils.translate(addNewObjectTypeKey);
        $(Schrodinger.byElementAttributeValue("a", "title", titleTranslated))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return new ObjectTypeBasicInformationWizardStep<>(ObjectTypeManagerPanel.this);
    }

    public T exitWizard() {
        $x(".//a[contains(text(), 'Exit wizard')]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        return getParent();
    }
}
