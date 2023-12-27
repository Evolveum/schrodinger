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

package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class CasesPanel<P extends FocusPage> extends Component<P, CasesPanel<P>> {

    public CasesPanel(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public <T extends TableWithPageRedirect<CasesPanel<P>, CasePage, T>> TableWithPageRedirect<CasesPanel<P>, CasePage, T> table() {
        return new TableWithPageRedirect<>(this,
                $(Schrodinger.byDataId("taskTable")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)) {
            @Override
            public CasePage clickByName(String name) {
                return new CasePage();
            }

            @Override
            public CasePage getObjectDetailsPage() {
                return new CasePage();
            }
        };
    }
}
