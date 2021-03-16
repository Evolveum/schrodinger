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
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.component.common.table.TableWithPageRedirect;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetProjectionModal;
import com.evolveum.midpoint.schrodinger.component.table.TableHeaderDropDownMenu;
import com.evolveum.midpoint.schrodinger.component.user.ProjectionsDropDown;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.FocusPage;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by honchar
 */
public class CasesTab<P extends FocusPage> extends Component<P> {

    public CasesTab(P parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public TableWithPageRedirect<CasesTab<P>> table() {
        return new TableWithPageRedirect<CasesTab<P>>(this,
                $(Schrodinger.byDataId("taskTable")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)) {
            @Override
            public CasePage clickByName(String name) {
                return new CasePage();
            }

            @Override
            public TableWithPageRedirect<CasesTab<P>> selectCheckboxByName(String name) {
                return null;
            }

            @Override
            protected TableHeaderDropDownMenu<P> clickHeaderActionDropDown() {
                return null;
            }
        };
    }
}
