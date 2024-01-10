/*
 * Copyright (c) 2024  Evolveum
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

package com.evolveum.midpoint.schrodinger.page.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

public class DeltasToBeApprovedTable<T> extends Table<T, Table> {

    public DeltasToBeApprovedTable(T parent) {
        super(parent, $(Schrodinger.byDataId("deltasToBeApproved"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S));
    }

    public DeltasToBeApprovedTable<T> assertCorrelationCandidate1Exists(String name, String givenName, String familyName,
                                                                     String locality, String personalNumber) {
        assertTableContainsColumnWithValue("Correlation candidate 1", name);
        assertTableContainsColumnWithValue("Correlation candidate 1", givenName);
        assertTableContainsColumnWithValue("Correlation candidate 1", familyName);
        assertTableContainsColumnWithValue("Correlation candidate 1", locality);
        assertTableContainsColumnWithValue("Correlation candidate 1", personalNumber);
        return this;
    }

    public MyWorkitemsPage clickCorrelateButtonForCandidate1() {
        SelenideElement cellWithButton = getTableCellElement("Correlation candidate 1", 1);
        cellWithButton.$(Schrodinger.byDataId("correlateButton1")).click();
        return new MyWorkitemsPage();
    }
}
