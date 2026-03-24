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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.page.configuration.RepositoryObjectPage;
import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerDetailsPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class AuditLogViewerPageTest extends AbstractSchrodingerTest {

    private static final File OLD_ROLE_FILE =
            new File("src/test/resources/advanced-labs/post-initial-objects/146-role-secret-i.xml");
    private static final File NEW_ROLE_FILE =
            new File("src/test/resources/advanced-labs/post-initial-objects/148-role-top-secret-i.xml");
    private static final File RAW_AUDIT_USER_FILE =
            new File("src/test/resources/objects/users/raw-audit-user.xml");
    private static final String OLD_ROLE_NAME = "Secret Projects I";
    private static final String NEW_ROLE_NAME = "Top Secret Projects I";
    private static final String OLD_ROLE_OID = "10000000-9999-9999-0000-a000d0000001";
    private static final String NEW_ROLE_OID = "10000000-9999-9999-0000-a000d0000002";
    private static final String USER_NAME = "raw-audit-user";

    @BeforeClass
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        importObject(OLD_ROLE_FILE, true, true);
        importObject(NEW_ROLE_FILE, true, true);
        importObject(RAW_AUDIT_USER_FILE, true, true);
    }

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

    @Test
    public void test00110rawAssignmentReplaceShowsOldValuesInRequestAndExecutionAudit() throws IOException {
        var repositoryObjectsPage = basicPage.listRepositoryObjects();
        repositoryObjectsPage.table().search()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue("User")
                .updateSearch()
                .and()
                .search()
                .byName()
                .inputValue(USER_NAME)
                .updateSearch();
        RepositoryObjectPage repositoryObjectPage = repositoryObjectsPage.table().clickByName(USER_NAME);

        SelenideElement xmlButton = $(com.codeborne.selenide.Selectors.byText("XML"))
                .shouldBe(Condition.visible);
        if (!xmlButton.has(Condition.cssClass("active"))) {
            xmlButton.click();
        }
        executeJavaScript(
                "ace.edit(arguments[0]).setValue(arguments[1], -1)",
                $(By.className("ace_editor")),
                Files.readString(RAW_AUDIT_USER_FILE.toPath())
                        .replaceAll(OLD_ROLE_OID, NEW_ROLE_OID));

        repositoryObjectPage.clickSaveButton();

        var verifyRepositoryObjectsPage = basicPage.listRepositoryObjects();
        verifyRepositoryObjectsPage.table().search()
                .dropDownPanelByItemName("Type")
                .inputDropDownValue("User")
                .updateSearch()
                .and()
                .search()
                .byName()
                .inputValue(USER_NAME)
                .updateSearch();
        verifyRepositoryObjectsPage.table().clickByName(USER_NAME).clickBackButton();

        assertRoleChangeInDetails(openAuditDetailsRow(0));
        back();
        assertRoleChangeInDetails(openAuditDetailsRow(1));
    }

    private AuditLogViewerDetailsPage openAuditDetailsRow(int rowIndex) {
        var auditTable = basicPage.auditLogViewer().table();
        auditTable.search()
                .resetBasicSearch()
                .dropDownPanelByItemName("Event type")
                .inputDropDownValue("Execute changes raw")
                .updateSearch();
        auditTable.assertTableContainsText(USER_NAME);
        return auditTable.clickByRowColumnNumber(rowIndex, 0);
    }

    private void assertRoleChangeInDetails(AuditLogViewerDetailsPage detailsPage) {
        detailsPage.assertAuditLogViewerDetailsPageIsOpened();

        new AuditLogViewerDetailsPage().deltaPanel().expandVisualizationPanel();

        SelenideElement roleToggle = $(By.xpath(
                "//div[@data-s-id='delta']//div[@data-s-id='partialVisualization']" +
                        "[.//*[contains(normalize-space(.), '" + NEW_ROLE_NAME + "')]]" +
                        "//a[@data-s-id='minimize']"))
                .shouldBe(Condition.visible);
        SelenideElement roleIcon = roleToggle.$(By.tagName("i"));
        if (!roleIcon.has(Condition.cssClass("fa-chevron-down"))) {
            roleToggle.click();
        }

        assertDeltaContainsText(OLD_ROLE_NAME);
        assertDeltaContainsText(NEW_ROLE_NAME);
    }

    private void assertDeltaContainsText(String text) {
        $(By.xpath("//div[@data-s-id='delta']")).shouldHave(Condition.text(text));
    }

}
