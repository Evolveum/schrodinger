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
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerDetailsPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

public class AuditLogViewerPageTest extends AbstractSchrodingerTest {

    @Test
    public void test00100returnBackToAuditLogDetails() {
        createUser("auditLogViewerTestUser");
        basicPage
                .listUsers()
                    .table()
                        .search()
                            .byName()
                            .inputValue("auditLogViewerTestUser")
                            .updateSearch()
                        .and()
                    .clickByName("auditLogViewerTestUser")
                        .selectBasicPanel()
                            .form()
                                .changeAttributeValue("Name", "auditLogViewerTestUser", "auditLogViewerTestUser1")
                                .and()
                            .and()
                        .clickSave();
        AuditLogViewerDetailsPage detailsPage = basicPage
                .auditLogViewer()
                    .table()
                        .search()
                            .referencePanelByItemName("Target", true)
                            .propertySettings()
                            .inputRefName("auditLogViewerTestUser")
                            .inputRefType("User")
                            .applyButtonClick()
                            .and()
                            .updateSearch()
                        .and()
                    .clickByRowColumnNumber(0, 0);
        detailsPage.deltaPanel()
                    .header()
                        .assertIsLink()
                        .clickNameLink()
                    .clickBack();
        detailsPage.assertAuditLogViewerDetailsPageIsOpened();
    }
}
