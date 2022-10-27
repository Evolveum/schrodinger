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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.LoggedUser;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.component.common.UserMenuPanel;
import com.evolveum.midpoint.schrodinger.component.configuration.*;
import com.evolveum.midpoint.schrodinger.page.archetype.ListArchetypesPage;
import com.evolveum.midpoint.schrodinger.page.cases.*;
import com.evolveum.midpoint.schrodinger.page.certification.*;
import com.evolveum.midpoint.schrodinger.page.configuration.*;
import com.evolveum.midpoint.schrodinger.page.objectcollection.ListObjectCollectionsPage;
import com.evolveum.midpoint.schrodinger.page.objectcollection.ObjectCollectionPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerPage;
import com.evolveum.midpoint.schrodinger.page.report.CreatedReportsPage;
import com.evolveum.midpoint.schrodinger.page.report.ImportJasperReportPage;
import com.evolveum.midpoint.schrodinger.page.report.ListReportsPage;
import com.evolveum.midpoint.schrodinger.page.resource.ImportResourceDefinitionPage;
import com.evolveum.midpoint.schrodinger.page.resource.ListConnectorHostsPage;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.resource.NewResourcePage;
import com.evolveum.midpoint.schrodinger.page.role.ListRolesPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.self.CredentialsPage;
import com.evolveum.midpoint.schrodinger.page.self.HomePage;
import com.evolveum.midpoint.schrodinger.page.self.ProfilePage;
import com.evolveum.midpoint.schrodinger.page.self.RequestRolePage;
import com.evolveum.midpoint.schrodinger.page.service.ListServicesPage;
import com.evolveum.midpoint.schrodinger.page.service.ServicePage;
import com.evolveum.midpoint.schrodinger.page.task.ListTasksPage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.page.user.FormSubmittablePage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.AssertionWithScreenshot;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Viliam Repan (lazyman).
 */
public class BasicPage {

    public LoggedUser loggedUser() {
        return new LoggedUser();
    }
    protected AssertionWithScreenshot assertion = new AssertionWithScreenshot();

    public HomePage home() {
        clickSelfServiceMenu("PageAdmin.menu.selfDashboard", null);
        return new HomePage();
    }

    public ProfilePage profile() {
        clickSelfServiceMenu("PageAdmin.menu.profile", null);
        return new ProfilePage();
    }

    public CredentialsPage credentials() {
        clickSelfServiceMenu("PageAdmin.menu.credentials", null);
        return new CredentialsPage();
    }

    public RequestRolePage requestRole() {
        clickSelfServiceMenu("PageAdmin.menu.request", null);
        return new RequestRolePage();
    }

    public DashboardPage dashboard() {
        clickAdministrationMenu("PageAdmin.menu.dashboard", null);
        return new DashboardPage();
    }

    public ListUsersPage listUsers() {
        return listUsers("");
    }

    public ListUsersPage listUsers(String objectListMenuItemKey) {
        if (StringUtils.isEmpty(objectListMenuItemKey)) {
            clickAdministrationMenu("PageAdmin.menu.top.users", "PageAdmin.menu.top.users.list");
        } else {
            clickAdministrationMenu("PageAdmin.menu.top.users", objectListMenuItemKey);
        }
        return new ListUsersPage();
    }

    public UserPage newUser() {
        return newUser("All users");
    }

    public UserPage newUser(String templateTitle) {
        clickAdministrationMenu("PageAdmin.menu.top.users", "PageAdmin.menu.top.users.new");
//        getPageTitleElement().shouldBe(Condition.text("New"), MidPoint.TIMEOUT_MEDIUM_6_S);
//        if ($(Schrodinger.byDataId("template")).isDisplayed()) {
//            NewObjectFromTemplatePage<UserPage> templatePage = new NewObjectFromTemplatePage<>();
//            return templatePage.clickTemplateButtonWithTitle(templateTitle, new UserPage());
//        }
        waitForDetailsPageIsLoaded();
        return new UserPage();
    }

    public OrgTreePage orgStructure() {
        clickAdministrationMenu("PageAdmin.menu.top.orgs", "PageAdmin.menu.top.orgs.tree");
        return new OrgTreePage();
    }

    public OrgPage newOrgUnit() {
        clickAdministrationMenu("PageAdmin.menu.top.orgs", "PageAdmin.menu.top.orgs.new");
        waitForDetailsPageIsLoaded();
        return new OrgPage();
    }

    public OrgPage newOrgUnit(String templateTitle) {
        clickAdministrationMenu("PageAdmin.menu.top.orgs", "PageAdmin.menu.top.orgs.new");
        getPageTitleElement().shouldBe(Condition.text("New"), MidPoint.TIMEOUT_MEDIUM_6_S);
        if (pageTitleEndsWith("from template")) {
            NewObjectFromTemplatePage<OrgPage> templatePage = new NewObjectFromTemplatePage<>();
            return templatePage.clickTemplateButtonWithTitle(templateTitle, new OrgPage());
        }
        waitForDetailsPageIsLoaded();
        return new OrgPage();
    }

    public ListRolesPage listRoles() {
        clickAdministrationMenu("PageAdmin.menu.top.roles", "PageAdmin.menu.top.roles.list");
        return new ListRolesPage();
    }

    public RolePage newRole() {
        return newRole("All roles");
    }

    public RolePage newRole(String templateTitle) {
        clickAdministrationMenu("PageAdmin.menu.top.roles", "PageAdmin.menu.top.roles.new");
        getPageTitleElement().shouldBe(Condition.text("New"), MidPoint.TIMEOUT_MEDIUM_6_S);
        if (pageTitleEndsWith("from template")) {
            NewObjectFromTemplatePage<RolePage> templatePage = new NewObjectFromTemplatePage<>();
            return templatePage.clickTemplateButtonWithTitle(templateTitle, new RolePage());
        }
        waitForDetailsPageIsLoaded();
        return new RolePage();
    }

    public ListServicesPage listServices() {
        clickAdministrationMenu("PageAdmin.menu.top.services", "PageAdmin.menu.top.services.list");
        return new ListServicesPage();
    }

    public ServicePage newService() {
        clickAdministrationMenu("PageAdmin.menu.top.services", "PageAdmin.menu.top.services.new");
        waitForDetailsPageIsLoaded();
        return new ServicePage();
    }

    public ServicePage newService(String templateTitle) {
        clickAdministrationMenu("PageAdmin.menu.top.services", "PageAdmin.menu.top.services.new");
        getPageTitleElement().shouldBe(Condition.text("New"), MidPoint.TIMEOUT_MEDIUM_6_S);
        if (pageTitleEndsWith("from template")) {
            NewObjectFromTemplatePage<ServicePage> templatePage = new NewObjectFromTemplatePage<>();
            return templatePage.clickTemplateButtonWithTitle(templateTitle, new ServicePage());
        }
        waitForDetailsPageIsLoaded();
        return new ServicePage();
    }

    public ListArchetypesPage listArchetypes() {
        clickAdministrationMenu("PageAdmin.menu.top.archetypes", "PageAdmin.menu.top.archetypes.list");
        return new ListArchetypesPage();
    }

    public ListResourcesPage listResources() {
        clickAdministrationMenu("PageAdmin.menu.top.resources", "PageAdmin.menu.top.resources.list");
        return new ListResourcesPage();
    }

    public NewResourcePage newResource() {
        clickAdministrationMenu("PageAdmin.menu.top.resources", "PageAdmin.menu.top.resources.new");
        return new NewResourcePage();
    }

    public AllCasesPage listAllCases(){
        clickAdministrationMenu(ConstantsUtil.MENU_TOP_CASES, ConstantsUtil.MENU_ALL_CASES);
        return new AllCasesPage();
    }

    public MyCasesPage listMyCases(){
        clickAdministrationMenu(ConstantsUtil.MENU_TOP_CASES, ConstantsUtil.MENU_MY_CASES_MENU_ITEM_LABEL_TEXT);
        return new MyCasesPage();
    }

    public AllManualCasesPage listAllManualCases(){
        clickAdministrationMenu(ConstantsUtil.MENU_TOP_CASES, ConstantsUtil.MENU_ALL_MANUAL_CASES_MENU_ITEM_LABEL_TEXT);
        return new AllManualCasesPage();
    }

    public AllRequestsPage listAllRequests() {
        clickAdministrationMenu(ConstantsUtil.MENU_TOP_CASES, ConstantsUtil.MENU_ALL_REQUESTS_MENU_ITEM_RESOURCE_KEY);
        return new AllRequestsPage();
    }

    public AllApprovalsPage listAllApprovals() {
        clickAdministrationMenu(ConstantsUtil.MENU_TOP_CASES, ConstantsUtil.MENU_ALL_APPROVALS_MENU_ITEM_LABEL_TEXT);
        return new AllApprovalsPage();
    }

    public MyWorkitemsPage myItems() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.list");
        return new MyWorkitemsPage();
    }

    public WorkitemsClaimableByMePage itemsClaimableByMe() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.listClaimable");
        return new WorkitemsClaimableByMePage();
    }

    public AttorneyItemsPage attorneyItems() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.listAttorney");
        return new AttorneyItemsPage();
    }

    public AllWorkitemsPage allItems() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.listAll");
        return new AllWorkitemsPage();
    }

    public MyRequestsPage myRequests() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.listProcessInstancesRequestedBy");
        return new MyRequestsPage();
    }

    public RequestsAboutMePage requestsAboutMe() {
        clickAdministrationMenu("PageAdmin.menu.top.workItems", "PageAdmin.menu.top.workItems.listProcessInstancesRequestedFor");
        return new RequestsAboutMePage();
    }

    public CampaignDefinitionsPage campaignDefinitions() {
        clickAdministrationMenu("PageAdmin.menu.top.certification", "PageAdmin.menu.top.certification.definitions");
        return new CampaignDefinitionsPage();
    }

    public NewCampaignDefinitionPage newCampaignDefinition() {
        clickAdministrationMenu("PageAdmin.menu.top.certification", "PageAdmin.menu.top.certification.newDefinition");
        return new NewCampaignDefinitionPage();
    }

    public CampaignsPage campaigns() {
        clickAdministrationMenu("PageAdmin.menu.top.certification", "PageAdmin.menu.top.certification.campaigns");
        return new CampaignsPage();
    }

    public CampaignsSchedulingPage campaignsScheduling() {
        clickAdministrationMenu("PageAdmin.menu.top.certification", "PageAdmin.menu.top.certification.scheduling");
        return new CampaignsSchedulingPage();
    }

    public MyWorkItemsPage myWorkItems() {
        clickAdministrationMenu("PageAdmin.menu.top.certification", "PageAdmin.menu.top.certification.decisions");
        return new MyWorkItemsPage();
    }

    public ListTasksPage listTasks() {
        return listTasks("");
    }

    public ListTasksPage listTasks(String objectListMenuItemKey) {
        if (StringUtils.isEmpty(objectListMenuItemKey)) {
            clickAdministrationMenu("PageAdmin.menu.top.serverTasks", "PageAdmin.menu.top.tasks.list");
        } else {
            clickAdministrationMenu("PageAdmin.menu.top.serverTasks", objectListMenuItemKey);
        }
        return new ListTasksPage();
    }

//    public TaskPage newTask() {
//        return newTask("Task");
//    }

    public TaskPage newTask(String templateTitle) {
        clickAdministrationMenu("PageAdmin.menu.top.serverTasks", "PageAdmin.menu.top.tasks.new");
        getPageTitleElement().shouldBe(Condition.text("New"), MidPoint.TIMEOUT_MEDIUM_6_S);
        if ($(Schrodinger.byDataId("template")).isDisplayed()) {
            NewObjectFromTemplatePage<TaskPage> templatePage = new NewObjectFromTemplatePage<>();
            return templatePage.clickTemplateButtonWithTitle(templateTitle, new TaskPage());
        }
        waitForDetailsPageIsLoaded();
        return new TaskPage();
    }

    public ListReportsPage listReports() {
        return listReports("");
    }

    public ListReportsPage listReports(String objectListMenuItemKey) {
        if (StringUtils.isEmpty(objectListMenuItemKey)) {
            clickAdministrationMenu("PageAdmin.menu.top.reports", "PageAdmin.menu.top.reports.list");
        } else {
            clickAdministrationMenu("PageAdmin.menu.top.reports", objectListMenuItemKey);
        }
        return new ListReportsPage();
    }

    public CreatedReportsPage createdReports() {
        clickAdministrationMenu("PageAdmin.menu.top.reports", "PageAdmin.menu.top.reports.created");
        return new CreatedReportsPage();
    }

    public ImportJasperReportPage importJasperReport() {
        clickAdministrationMenu("PageAdmin.menu.top.reports", "PageAdmin.menu.top.reports.new");
        return new ImportJasperReportPage();
    }

    public AuditLogViewerPage auditLogViewer() {
        clickAdministrationMenu("PageAdmin.menu.top.reports", "PageAuditLogViewer.menuName");
        return new AuditLogViewerPage();
    }

    public ListObjectCollectionsPage listObjectCollections() {
        clickAdministrationMenu("PageAdmin.menu.top.objectCollections", "PageAdmin.menu.top.objectCollections.list");
        return new ListObjectCollectionsPage();
    }

    public ObjectCollectionPage newObjectCollection() {
        clickAdministrationMenu("PageAdmin.menu.top.objectCollections", "PageAdmin.menu.top.objectCollections.new");
        return new ObjectCollectionPage();
    }

    public ImportResourceDefinitionPage importResourceDefinition() {
        clickAdministrationMenu("PageAdmin.menu.top.resources", "PageAdmin.menu.top.resources.import");
        return new ImportResourceDefinitionPage();
    }

    public ListConnectorHostsPage listConnectorHosts() {
        clickAdministrationMenu("PageAdmin.menu.top.resources", "PageAdmin.menu.top.connectorHosts.list");
        return new ListConnectorHostsPage();
    }

    public AboutPage aboutPage() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.about");
        return new AboutPage();
    }

    public BulkActionsPage bulkActions() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.bulkActions");
        return new BulkActionsPage();
    }

    public ImportObjectPage importObject() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.importObject");
        return new ImportObjectPage();
    }

    public ListRepositoryObjectsPage listRepositoryObjects() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.repositoryObjects", "PageAdmin.menu.top.configuration.repositoryObjectsList");
        return new ListRepositoryObjectsPage();
    }

    public SystemPanel system() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().systemPanel();
    }

    public ObjectPolicyPanel objectPolicy() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().objectPolicyPanel();
    }

    public NotificationsPanel notifications() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().notificationsPanel();
    }

    public LoggingPanel logging() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().loggingPanel();
    }

    public ProfilingPanel profiling() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().profilingPanel();
    }

    public AdminGuiPanel adminGui() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().adminGuiPanel();
    }

    public DeploymentInformationPanel deploymentInformation() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().deploymentInformationPanel();
    }

    public InfrastructurePanel infrastructure() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage()
                .infrastructurePanel();
    }

    public RoleManagementPanel roleManagement() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().roleManagementPanel();
    }

    public CleanupPolicyPanel cleanupPolicy() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.basic");
        return new SystemPage().cleanupPolicyPanel();
    }

    public InternalsConfigurationPage internalsConfiguration() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.internals", null, 1);
        return new InternalsConfigurationPage();
    }

    public QueryPlaygroundPage queryPlayground() {
        clickConfigurationMenu("PageAdmin.menu.top.configuration.repoQuery");
        return new QueryPlaygroundPage();
    }

    public FormSubmittablePage dynamicForm() {
        return new FormSubmittablePage();
    }

    private void clickSelfServiceMenu(String mainMenuKey, String menuItemKey) {
        clickMenuItem(ConstantsUtil.SELF_SERVICE_MENU_ITEMS_SECTION_VALUE, mainMenuKey, menuItemKey);
    }

    private void clickAdministrationMenu(String mainMenuKey, String menuItemKey) {
        clickMenuItem(ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE, mainMenuKey, menuItemKey);
    }

    public BasicPage assertAdministrationMenuItemIconClassEquals(String mainMenuKey, String menuItemKey, String expectedIconClass){
        SelenideElement menuItem = getMenuItemElement(ConstantsUtil.ADMINISTRATION_MENU_ITEMS_SECTION_VALUE, mainMenuKey, menuItemKey);
        assertion.assertEquals(expectedIconClass, menuItem.parent().$(By.tagName("i")).getAttribute("class"),
                "Menu item icon (menu item key is '" + menuItemKey + "') doesn't match to value '" + expectedIconClass + "'.");
        return this;
    }

    private void clickConfigurationMenu(String mainMenuKey) {
        clickConfigurationMenu(mainMenuKey, null);
    }

    private void clickConfigurationMenu(String mainMenuKey, String menuItemKey) {
        clickConfigurationMenu(mainMenuKey, menuItemKey, 0);
    }

    private void clickConfigurationMenu(String mainMenuKey, String menuItemKey, int index) {
        clickMenuItem(ConstantsUtil.CONFIGURATION_MENU_ITEMS_SECTION_VALUE, mainMenuKey, menuItemKey, index);
    }

    public FeedbackBox<? extends BasicPage> feedback() {
        SelenideElement feedback = $x(".//div[@data-s-id='detailsBox' and contains(@class, \"feedback-message\")]")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_EXTRA_LONG_10_M);
        return new FeedbackBox<>(this, feedback);
    }

    public BasicPage assertFeedbackExists() {
        assertion.assertTrue($x(".//div[@data-s-id='detailsBox' and contains(@class, \"feedback-message\")]")
                .is(Condition.visible), "Feedback message box is absent");
        return this;
    }

    private void clickMenuItem(String topLevelMenuValue, String mainMenuKey, String menuItemKey) {
        clickMenuItem(topLevelMenuValue, mainMenuKey, menuItemKey, 0);
    }

    private void clickMenuItem(String topLevelMenuValue, String mainMenuKey, String menuItemKey, int index) {
        Utils.waitForAjaxCallFinish();
        SelenideElement menu = getMenuItemElement(topLevelMenuValue, mainMenuKey, menuItemKey, index);
        scrollToElement(menu);
        menu.click();
        Utils.waitForAjaxCallFinish();
    }

    public SelenideElement getMenuItemElement(String topLevelMenuValue, String mainMenuKey, String menuItemKey) {
        return getMenuItemElement(topLevelMenuValue, mainMenuKey, menuItemKey, 0);
    }

    public SelenideElement getMenuItemElement(String topLevelMenuValue, String mainMenuKey, String menuItemKey, int index){
        SelenideElement mainMenu = getMainMenuItemElement(topLevelMenuValue, mainMenuKey, index);
        if (menuItemKey == null){
            return mainMenu;
        }

        SelenideElement menuItem = $(Schrodinger.byDataResourceKey(menuItemKey));
        scrollToElement(menuItem);
        menuItem.shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);

        return menuItem;
    }

    public void scrollToElement(SelenideElement element) {
        long endTime = System.currentTimeMillis() + 5000;
        while (!element.isDisplayed() && System.currentTimeMillis() < endTime) {
            Utils.waitForAjaxCallFinish();
            element.scrollIntoView(false);
            Utils.waitForAjaxCallFinish();
        }
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), MidPoint.TIMEOUT_MEDIUM_6_S);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            element.scrollIntoView(true);
        }
    }

    public SelenideElement getMenuItemElementByMenuLabelText(String topLevelMenuValue, String mainMenuKey, String menuItemLabelText){
        SelenideElement mainMenu = getMainMenuItemElement(topLevelMenuValue, mainMenuKey);
        if (StringUtils.isEmpty(menuItemLabelText)){
            return mainMenu;
        }
        SelenideElement menuItem = mainMenu.$(By.partialLinkText(menuItemLabelText));
        menuItem.shouldBe(Condition.visible);

        return menuItem;
    }

    private SelenideElement getMainMenuItemElement(String topLevelMenuValue, String mainMenuKey) {
        return getMainMenuItemElement(topLevelMenuValue, mainMenuKey, 0);
    }

    private SelenideElement getMainMenuItemElement(String topLevelMenuValue, String mainMenuKey, int index){
        Utils.waitForAjaxCallFinish();
        SelenideElement topLevelMenu = $x(".//span[@data-s-id='name' and contains(text(), '" + topLevelMenuValue + "')]");
        scrollToElement(topLevelMenu);
        topLevelMenu.shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);

        SelenideElement topLevelMenuChevron = topLevelMenu.parent().$(By.tagName("i"));
        if (!topLevelMenuChevron.has(Condition.cssClass("fa-chevron-down"))) {
            topLevelMenu.click();
            topLevelMenuChevron.shouldBe(Condition.cssClass("fa-chevron-down"), MidPoint.TIMEOUT_DEFAULT_2_S);
            Utils.waitForAjaxCallFinish();
        }

        SelenideElement mainMenu = $(Schrodinger.byDescendantElementAttributeValue("p", "data-s-resource-key", mainMenuKey));
        ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", mainMenu);
        mainMenu.shouldBe(Condition.visible);

        SelenideElement mainMenuLi = mainMenu.parent().parent();
        checkCssClass(mainMenuLi, mainMenu, "menu-open");
        return mainMenu;
    }

    private void checkCssClass(SelenideElement mainMenuLi, SelenideElement mainMenu, String cssClass) {
        //if no submenu, then no menu-open class is here
        if (!mainMenuLi.find(Schrodinger.byDataId("submenu")).find(byClassName("nav-item")).exists()) {
            return;
        }
        if (!mainMenuLi.has(Condition.cssClass(cssClass))) {
            scrollToElement(mainMenu);
            mainMenu.click();
            mainMenuLi.shouldBe(Condition.cssClass(cssClass), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
    }

    public String getCurrentUrl() {
        String url = WebDriverRunner.url();
        url = url.split("\\?")[0];
        return url;
    }

    public SelenideElement getMainHeaderPanelElement() {
        return $(Schrodinger.byDataId("header", "mainHeader"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public BasicPage assertMainHeaderPanelStyleMatch(String styleToCompare) {
        assertion.assertTrue(getMainHeaderPanelElement().getCssValue("background-color").equals(styleToCompare),
                "Main header panel background color doesn't match to " + styleToCompare);
        return this;
    }

    public SelenideElement getPageTitleElement() {
        return $(Schrodinger.byDataId("span", "pageTitle"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public BasicPage assertPageTitleStartsWith(String titleStartText) {
        assertion.assertTrue(pageTitleStartsWith(titleStartText), "Page title doesn't start with " + titleStartText);
        return this;
    }

    public BasicPage assertPageTitleEndsWith(String titleEndText) {
        assertion.assertTrue(pageTitleEndsWith(titleEndText), "Page title doesn't start with " + titleEndText);
        return this;
    }

    public boolean pageTitleStartsWith(String titleStartText) {
        return getPageTitleElement().getText().startsWith(titleStartText);
    }

    public boolean pageTitleEndsWith(String titleEndText) {
        return getPageTitleElement().getText().endsWith(titleEndText);
    }

    public UserMenuPanel clickUserMenu() {
        if(userMenuExists()) {
            SelenideElement userMenu = $(".dropdown.user.user-menu").shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            userMenu.$(By.cssSelector(".dropdown-toggle")).click();
            SelenideElement userMenuPanel = userMenu.$(By.cssSelector(".user-footer")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
            return new UserMenuPanel(this, userMenuPanel);
        }
        return null;
    }

    public boolean userMenuExists() {
        return $(".main-header.navbar").exists();
    }

    public BasicPage assertElementWithTextExists(String text) {
        assertion.assertTrue($(byText(text)).exists(), "Element with text '" + text + "' doesn't exist on the page.");
        return this;
    }

    public BasicPage assertElementWithValueExists(String value) {
        assertion.assertTrue($(byValue(value)).exists(), "Element with value '" + value + "' doesn't exist on the page.");
        return this;
    }

    public BasicPage assertUserMenuExist() {
        assertion.assertTrue(userMenuExists(), "User should be logged in, user menu should be visible.");
        return this;
    }

    public BasicPage assertUserMenuDoesntExist() {
        assertion.assertFalse(userMenuExists(), "User should be logged out, user menu shouldn't be visible.");
        return this;
    }

    public BasicPage assertMenuItemActive(SelenideElement menuItemElement) {
        SelenideElement menuItemLink = menuItemElement.parent();
        assertion.assertTrue(menuItemLink.has(Condition.cssClass("active")), "Menu item should be active");
        return this;
    }

    public BasicPage assertMenuItemDoesntActive(SelenideElement menuItemElement) {
        SelenideElement menuItemLi = menuItemElement.parent().parent();
        assertion.assertFalse(menuItemLi.has(Condition.cssClass("active")), "Menu item shouldn't be active");
        return this;
    }

    public BasicPage changeLanguage(String countryCode) {
        Validate.notNull(countryCode, "Country code must not be null");

        $(Schrodinger.bySchrodingerDataId("locale"))
                .parent()
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        String flagIconCss = "fi-" + countryCode.trim().toLowerCase();
        $(By.className(flagIconCss))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .parent()
                .click();

        return this;
    }

    public void waitForDetailsPageIsLoaded() {
        Utils.waitForMainPanelOnDetailsPage();
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
    }
}
