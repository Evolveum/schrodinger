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

package com.evolveum.midpoint.schrodinger;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementNotFound;
import com.evolveum.midpoint.client.api.ObjectAddService;
import com.evolveum.midpoint.client.api.exception.CommonException;
import com.evolveum.midpoint.client.impl.prism.RestPrismObjectAddService;
import com.evolveum.midpoint.client.impl.prism.RestPrismService;
import com.evolveum.midpoint.client.impl.prism.RestPrismServiceBuilder;
import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.util.PrismContextFactory;
import com.evolveum.midpoint.schema.MidPointPrismContextFactory;
import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceAccountsPanel;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceConfigurationPanel;
import com.evolveum.midpoint.schrodinger.component.resource.ResourceShadowTable;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.configuration.AboutPage;
import com.evolveum.midpoint.schrodinger.page.configuration.ImportObjectPage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.resource.AccountPage;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.resource.ResourcePage;
import com.evolveum.midpoint.schrodinger.page.role.ListRolesPage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.AssertionWithScreenshot;
import com.evolveum.midpoint.schrodinger.util.Utils;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.web.boot.MidPointSpringApplication;
import com.evolveum.midpoint.xml.ns._public.common.common_3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.*;

/**
 * Created by Viliam Repan (lazyman).
 */
@ActiveProfiles("default")
@SpringBootTest(classes = MidPointSpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"server.port=8180", "midpoint.schrodinger=true"})
//@Listeners({ BrowserPerClass.class, SchrodingerTextReport.class }) TODO fix
//@Report   TODO fix
public abstract class AbstractSchrodingerTest extends AbstractTestNGSpringContextTests {

    public static final String PROPERTY_NAME_MIDPOINT_HOME = "-Dmidpoint.home";
    public static final String SYSTEM_PROPERTY_NAME_MIDPOINT_HOME = "midpoint.home";
    public static final String PROPERTY_NAME_USER_HOME = "user.home";
    public static final String PROPERTY_NAME_FILE_SEPARATOR = "file.separator";

    protected static final String CSV_RESOURCE_ATTR_UNIQUE = "Unique attribute name";

    protected static final String SCHRODINGER_PROPERTIES = "./src/test/resources/configuration/schrodinger.properties";

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSchrodingerTest.class);

    protected static File testTargetDir;

    protected EnvironmentConfiguration configuration;

    protected String username;

    protected String password;

    protected MidPoint midPoint;

    protected BasicPage basicPage;

    protected static AssertionWithScreenshot assertion = new AssertionWithScreenshot();

    private boolean startMidpoint = true;

    public EnvironmentConfiguration getConfiguration() {
        return configuration;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private Properties props = null;

    @Autowired
    protected PrismContext prismContext;

    protected List<File> getObjectListToImport() {
        return new ArrayList<>();
    }

    @BeforeClass(
            alwaysRun = true,
            dependsOnMethods = {"springTestContextBeforeTestClass"}
    )
    protected void springTestContextPrepareTestInstance() throws Exception {
        startMidpoint = isStartMidpoint();
        if (startMidpoint) {
            startMidpoint();
            super.springTestContextPrepareTestInstance();
        } else if (prismContext == null) {
            PrismContextFactory pcf = new MidPointPrismContextFactory();
            try {
                prismContext = pcf.createPrismContext();
                prismContext.initialize();
            } catch (SchemaException | SAXException | IOException e) {
                throw new com.evolveum.midpoint.client.api.exception.SchemaException(e);
            }
        }
        getObjectListToImport().forEach(objFile -> addObjectFromFile(objFile, true));
    }

    private void startMidpoint() throws IOException {
        LOG.info("Starting midPoint....");
        String home = createMidPointHome();

        initializeExtensionSchemas(home);

        initializePostInitialObjects(home);
        LOG.info("midPoint started successfully");
    }

    private void initializePostInitialObjects(String home) throws IOException {
        String postInitialObjectsPath = getPostInitialObjectsFolderPath();
        if (StringUtils.isEmpty(postInitialObjectsPath)) {
            LOG.info("No post-initial objects to import, skipping post-inital objects.");
            return;
        }

        File postInitObjectsSourceDir = new File(postInitialObjectsPath);
        File[] objList = postInitObjectsSourceDir.listFiles();

        if (objList != null && objList.length > 0) {
            File postInitObjectsDir = new File(home, "post-initial-objects");
            if (!postInitObjectsDir.mkdir()) {
                if (postInitObjectsDir.exists()) {
                    FileUtils.cleanDirectory(postInitObjectsDir);
                } else {
                    throw new IOException("Creation of directory \"" + postInitObjectsDir.getAbsolutePath() + "\" unsuccessful");
                }
            }
            Arrays.sort(objList);
            for (File postInitFile : objList) {
                File objFile = new File(postInitObjectsDir, postInitFile.getName());
                FileUtils.copyFile(Utils.changeAttributeIfPresent(postInitFile, "redirectToFile",
                        home + "/example-mail-notifications.log", home), objFile);
            }
        }
        LOG.info("Post-initial objects successfully copied to midpoint.home/post-initial-objects directory.");

    }

    private String createMidPointHome() throws IOException {
        String home = System.getProperty("midpoint.home");
        File mpHomeDir = new File(home);
        if (!mpHomeDir.exists()) {
            LOG.info("Creating midpoint.home directory: {}", mpHomeDir.getAbsolutePath());
            if (!mpHomeDir.mkdir()) {
                throw new IOException("Creation of directory " + mpHomeDir.getAbsolutePath() + " unsuccessful");
            }
        }
        LOG.info("midpoint.home directory created successfully");
        return home;
    }

    private void initializeExtensionSchemas(String home) throws IOException {
        File extensionSchemaFile = getExtensionSchemaFile();
        if (extensionSchemaFile == null) {
            LOG.info("No extension files found, continue with midPoint initialization.");
            return;
        }

        LOG.info("Initializing extension schema from file {}", extensionSchemaFile.getName());

        File schemaDir = new File(home, "schema");
        if (!schemaDir.mkdir()) {
            if (schemaDir.exists()) {
                FileUtils.cleanDirectory(schemaDir);
            } else {
                throw new IOException("Creation of directory \"" + schemaDir.getAbsolutePath() + "\" unsuccessful");
            }
        }
        File schemaFile = new File(schemaDir, extensionSchemaFile.getName());
        FileUtils.copyFile(extensionSchemaFile, schemaFile);
        LOG.info("Extension schema initialized successfully.");
    }

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    public void beforeClass() throws IOException {
        LOG.info("Starting tests in class {}", getClass().getName());

        if (midPoint == null) {
            configuration = buildEnvironmentConfiguration();
            midPoint = new MidPoint(configuration);

            username = getConfigurationPropertyValue("username");
            password = getConfigurationPropertyValue("password");
        }

        FormLoginPage login = midPoint.formLogin();
        String locale = getConfigurationPropertyValue("locale");
        basicPage = login.loginIfUserIsNotLog(username, password, locale);
    }

    protected EnvironmentConfiguration buildEnvironmentConfiguration() throws IOException {
        EnvironmentConfiguration config = new EnvironmentConfiguration();
        config.driver(WebDriver.valueOf(getConfigurationPropertyValue("webdriver")));

        if (Boolean.parseBoolean(getConfigurationPropertyValue("useRemoteWebdriver"))) {
            config.useRemoteWebdriver(true);
            config.remoteWebdriverUrl(getConfigurationPropertyValue("remoteWebdriverUrl"));
        } else {
            config.driverLocation(getConfigurationPropertyValue("webdriverLocation"));
        }
        config.headless(Boolean.parseBoolean(getConfigurationPropertyValue("headlessStart")));
        config.locale(getConfigurationPropertyValue("locale"));
        String urlPropertyName = startMidpoint ? "base_url" : "base_url_mp_already_started";
        config.baseUrl(getConfigurationPropertyValue(urlPropertyName));

        return config;
    }

    @AfterClass
    public void afterClass() {
        LOG.info("Finished tests from class {}", getClass().getName());

//        basicPage.loggedUser().logoutIfUserIsLogin();
        clearBrowser();
        Selenide.closeWebDriver(); //todo or closeWindow?

        if (resetToDefaultAfterTests()) {
            resetToDefault();
        }
    }

    public void clearBrowser() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    protected void resetToDefault() {
        midPoint.formLogin().loginIfUserIsNotLog(username, password);

        LOG.info("Cleaning up midPoint.");

        AboutPage aboutPage = basicPage.aboutPage();
        aboutPage
                .clickSwitchToFactoryDefaults()
                .clickYes();
        Selenide.sleep(60000); //todo go to task page and wait the task to be finished
    }

    /**
     * use this method in case you need to test object importing through Import object page
     * or you need to import object inside the test
     * In case if you need to add an object before the test, use repoAddObjectFromFile(File file, OperationResult operationResult)
     *
     * @param source
     * @param overrideExistingObject
     */
    protected void importObject(File source, boolean overrideExistingObject, boolean ignoreWarning) {
        ImportObjectPage importPage = basicPage.importObject();

        if (overrideExistingObject) {
            importPage
                    .checkOverwriteExistingObject();
        }

        FeedbackBox<? extends BasicPage> feedback = importPage
                .getObjectsFromFile()
                .chooseFile(source)
                .checkOverwriteExistingObject()
                .clickImportFileButton()
                .feedback();
        boolean isSuccess = false;
        try {
            isSuccess = feedback.isSuccess();
        } catch (ElementNotFound e) {
            if (!ignoreWarning) {
                throw e;
            }
            // else ignoring exception but isSuccess is still false
        }
        if (!isSuccess && ignoreWarning) {
            isSuccess = feedback.isWarning();
        }

        String screenshotName = "NotSuccessfulImport" + System.currentTimeMillis();
        if (!isSuccess) {
            feedback.clickShowAll();
            Selenide.sleep(2000);
            Selenide.screenshot(screenshotName);
        }
        Assert.assertTrue(isSuccess, feedback.getFeedbackMessage() + "; screenshot: " + screenshotName);
    }

    protected void importObject(File source, boolean overrideExistingObject) {
        importObject(source, overrideExistingObject, false);
    }

    protected void importObject(File source) {
        importObject(source, false, false);
    }

    protected String fetchTestHomeDir() {
        return fetchMidpointHome();
    }

    protected String fetchMidpointHome() {
        AboutPage aboutPage = basicPage.aboutPage();
        String mpHomeDir = aboutPage.getJVMproperty(PROPERTY_NAME_MIDPOINT_HOME);

        if (mpHomeDir != null && !mpHomeDir.isEmpty() && !PROPERTY_NAME_MIDPOINT_HOME.equalsIgnoreCase(mpHomeDir)) {
            return mpHomeDir;
        } else if (StringUtils.isNotEmpty(System.getProperty(SYSTEM_PROPERTY_NAME_MIDPOINT_HOME))) {
            return System.getProperty(SYSTEM_PROPERTY_NAME_MIDPOINT_HOME);
        } else {
            mpHomeDir = aboutPage.getSystemProperty(PROPERTY_NAME_USER_HOME)
                    + aboutPage.getSystemProperty(PROPERTY_NAME_FILE_SEPARATOR)
                    + "midpoint";

            LOG.info("Midpoint home parameter is empty! Using defaults: " + mpHomeDir);

        }
        return mpHomeDir;
    }

    protected File initTestDirectory(String dir) throws IOException {
        return initTestDirectory(dir, true);
    }

    protected File initTestDirectory(String dir, boolean clearExist) throws IOException {

        String home = fetchTestHomeDir();
        File parentDir = new File(home, "schrodinger");
        if (!parentDir.exists()) {
            LOG.info("Creating schrodinger directory: {}", parentDir.getAbsolutePath());
            if (!parentDir.mkdir()) {
                LOG.error("Creating schrodinger directory in {} failed", home);
                throw new IOException("Cannot create schrodinger directory");
            }
        }
        testTargetDir = new File(parentDir, dir);

        if (testTargetDir.mkdir()) {
            LOG.debug("Created directory: {}", testTargetDir);
            return testTargetDir;
        } else {
            if (testTargetDir.exists()) {
                if (clearExist) {
                    FileUtils.cleanDirectory(testTargetDir);
                }
                return testTargetDir;
            } else {

                throw new IOException("Creation of directory \"" + testTargetDir.getAbsolutePath() + "\" unsuccessful. ");
            }
        }
    }

    // TODO workaround -> factory reset during clean up seems to leave some old cached information breaking the resource until version change
    public ResourcePage refreshResourceSchema(String resourceName) {

        ListResourcesPage listResourcesPage = basicPage.listResources();
        ResourcePage resourcePage = listResourcesPage
                .table()
                .clickByName(resourceName)
                .refreshSchema();
        resourcePage.feedback();    //just wait until refresh schema ends up
        return resourcePage;
    }

    public void changeResourceAttribute(String resourceName, String attributeName, String newValue) {
        changeResourceAttribute(resourceName, attributeName, null, newValue, true);
    }

    public void changeResourceAttribute(String resourceName, String attributeName, String newValue, Boolean shouldBeSuccess) {
        changeResourceAttribute(resourceName, attributeName, null, newValue, shouldBeSuccess);
    }

    public void changeResourceAttribute(String resourceName, String attributeName, String oldValue, String newValue, Boolean shouldBeSuccess) {
        ListResourcesPage listResourcesPage = basicPage.listResources();

        if (shouldBeSuccess) {
            ResourcePage viewResourcePage = listResourcesPage
                    .table()
                    .search()
                    .byName()
                    .inputValue(resourceName)
                    .updateSearch()
                    .and()
                    .clickByName(resourceName);
            ResourceConfigurationPanel resourceConfigurationTab = viewResourcePage
                    .getConnectorConfigurationPanel();
            Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

            resourceConfigurationTab
                    .form()
                    .changeAttributeValue(attributeName, oldValue, newValue);
            viewResourcePage
                    .selectBasicPanel();
            viewResourcePage
                    .clickSave()
                    .feedback()
                    .assertSuccess();
            listResourcesPage
                    .table()
                    .search()
                    .byName()
                    .inputValue(resourceName)
                    .updateSearch()
                    .and()
                    .clickByName(resourceName)
                    .clickTestConnection()
                    .assertIsTestSuccess()
                    .clickOk();
        } else {
            ResourcePage viewResourcePage = listResourcesPage
                    .table()
                    .search()
                    .byName()
                    .inputValue(resourceName)
                    .updateSearch()
                    .and()
                    .clickByName(resourceName);
            viewResourcePage
                    .getConnectorConfigurationPanel()
                    .form()
                    .changeAttributeValue(attributeName, oldValue, newValue);
            viewResourcePage
                    .selectBasicPanel();
            viewResourcePage.clickSave()
                    .feedback()
                    .assertSuccess();
            listResourcesPage
                    .table()
                    .search()
                    .byName()
                    .inputValue(resourceName)
                    .updateSearch()
                    .and()
                    .clickByName(resourceName)
                    .clickTestConnection()
                    .assertIsTestFailure();
        }

    }

    protected void addObjectFromFile(File file) {
        addObjectFromFile(file, true);
    }

    protected void addObjectFromFile(File file, boolean overwrite) {
        try {
            List<PrismObject<?>> objects = prismContext.parserFor(file).parseObjects();
            addObjects(objects, overwrite);
        } catch (CommonException | SchemaException | IOException ex) {
            LOG.error("Unable to add object, {}", ex);
        }
    }

    private void addObjects(List<PrismObject<?>> objects, boolean overwrite) throws IOException, CommonException {
        RestPrismServiceBuilder builder = RestPrismServiceBuilder.create();
        RestPrismService service = builder
                .baseUrl(getConfigurationPropertyValue(startMidpoint ? "base_url" : "base_url_mp_already_started") + "/ws/rest")
                .username(getConfigurationPropertyValue("username"))
                .password(getConfigurationPropertyValue("password"))
                .build();
        final List<String> options = new ArrayList<>();
        if (overwrite) {
            options.add("overwrite");
            options.add("raw");
        }
        objects.forEach(object -> {
            try {
                addObjectService(service, object).setOptions(options).post();
                LOG.trace("Object oid=" + object.getOid() + "; name='" + object.getName() + "' is added.");
            } catch (Exception e) {
                LOG.error("Unable to add object oid=" + object.getOid() + "; name='" + object.getName() + "' , {}", e);
            }
        });
    }

    protected void addObjectFromString(String object) {
        addObjectFromString(object, false);
    }

    protected void addObjectFromString(String object, boolean overwrite) {
        try {
            PrismObject<?> prismObject = prismContext.parseObject(object);
            addObjects(List.of(prismObject), overwrite);
        } catch (CommonException | SchemaException | IOException ex) {
            LOG.error("Unable to add object, {}", ex);
        }
    }

    private RestPrismObjectAddService addObjectService(RestPrismService service, PrismObject object) {
        ObjectAddService<? extends ObjectType> addService = null;
        if (object.isOfType(UserType.class)) {
            addService = service.users().add((UserType) object.asObjectable());
        } else if (object.isOfType(RoleType.class)) {
            addService = service.roles().add((RoleType) object.asObjectable());
        } else if (object.isOfType(OrgType.class)) {
            addService = service.orgs().add((OrgType) object.asObjectable());
        } else if (object.isOfType(ArchetypeType.class)) {
            addService = service.archetypes().add((ArchetypeType) object.asObjectable());
        } else if (object.isOfType(LookupTableType.class)) {
            addService = service.lookupTables().add((LookupTableType) object.asObjectable());
        } else if (object.isOfType(ObjectTemplateType.class)) {
            addService = service.objectTemplates().add((ObjectTemplateType) object.asObjectable());
        } else if (object.isOfType(ResourceType.class)) {
            addService = service.resources().add((ResourceType) object.asObjectable());
        } else if (object.isOfType(SystemConfigurationType.class)) {
            addService = service.systemConfigurations().add((SystemConfigurationType) object.asObjectable());
        } else if (object.isOfType(TaskType.class)) {
            addService = service.tasks().add((TaskType) object.asObjectable());
        } else if (object.isOfType(ValuePolicyType.class)) {
            addService = service.valuePolicies().add((ValuePolicyType) object.asObjectable());
        } else if (object.isOfType(SecurityPolicyType.class)) {
            addService = service.securityPolicies().add((SecurityPolicyType) object.asObjectable());
        } else if (object.isOfType(ObjectCollectionType.class)) {
            addService = service.objectCollections().add((ObjectCollectionType) object.asObjectable());
        } else if (object.isOfType(FormType.class)) {
            addService = service.forms().add((FormType) object.asObjectable());
        } else if (object.isOfType(SequenceType.class)) {
            addService = service.sequences().add((SequenceType) object.asObjectable());
        }
        return (RestPrismObjectAddService) addService;
    }


    public void addCsvResourceFromFileAndTestConnection(File resourceXml, String resourceName, String newFilePathValue) throws IOException {
        addObjectFromFile(Utils.changeResourceFilePathInXml(resourceXml, newFilePathValue, fetchTestHomeDir()));
        basicPage
                .listResources()
                .testConnectionClick(resourceName)
                .feedback()
                .assertSuccess();
    }

    public void addResourceFromFileAndTestConnection(File resourceXml, String resourceName) throws IOException {
        addObjectFromFile(resourceXml);
        basicPage
                .listResources()
                .testConnectionClick(resourceName)
                .feedback()
                .assertSuccess();
    }

    public void addResourceAndTestConnection(String resource, String resourceName) throws IOException {
        addObjectFromString(resource);
        basicPage
                .listResources()
                .testConnectionClick(resourceName)
                .feedback()
                .assertSuccess();
    }

    public UserPage showUser(String userName) {
        UserPage user = showUserInTable(userName).clickByName(userName);
        return user;
    }

    public RolePage showRole(String roleName) {
        RolePage role = showRoleInTable(roleName).clickByName(roleName);
        return role;
    }

    public AssignmentHolderObjectListTable<ListUsersPage, UserPage> showUserInTable(String userName) {
        return basicPage.listUsers()
                .table()
                .search()
                .byName()
                .inputValue(userName)
                .updateSearch()
                .and();
    }

    public AssignmentHolderObjectListTable<ListRolesPage, RolePage> showRoleInTable(String roleName) {
        return basicPage.listRoles()
                .table()
                .search()
                .byName()
                .inputValue(roleName)
                .updateSearch()
                .and();
    }

    public ResourcePage showResource(String resourceName) {
        return basicPage.listResources()
                .table()
                .search()
                .byName()
                .inputValue(resourceName)
                .updateSearch()
                .and()
                .clickByName(resourceName);
    }

    public AccountPage showShadow(String resourceName, String searchedItem, String itemValue) {
        return showShadow(resourceName, searchedItem, itemValue, null, false);
    }

    public AccountPage showShadow(String resourceName, String searchedItem, String itemValue, String intent, boolean useRepository) {
        return getShadowTable(resourceName, searchedItem, itemValue, intent, useRepository)
                .clickByName(itemValue);
    }

    public ResourceShadowTable assertShadowExists(String resourceName, String searchedItem, String itemValue) {
        return assertShadowExists(resourceName, searchedItem, itemValue, null, false);
    }

    public ResourceShadowTable assertShadowExists(String resourceName, String searchedItem, String itemValue, String intent, boolean useRepository) {
        ResourceShadowTable table = getShadowTable(resourceName, searchedItem, itemValue, intent, useRepository);
        return (ResourceShadowTable) table.assertTableContainsText(itemValue);
    }

    public ResourceShadowTable assertShadowDoesntExist(String resourceName, String searchedItem, String itemValue) {
        return assertShadowDoesntExist(resourceName, searchedItem, itemValue, null, false);
    }

    public ResourceShadowTable assertShadowDoesntExist(String resourceName, String searchedItem, String itemValue, String intent, boolean useRepository) {
        ResourceShadowTable table = getShadowTable(resourceName, searchedItem, itemValue, intent, useRepository);
        return (ResourceShadowTable) table.assertTableDoesntContainText(itemValue);
    }

    public ResourceShadowTable getShadowTable(String resourceName, String searchedItem, String itemValue) {
        return getShadowTable(resourceName, searchedItem, itemValue, null, false);
    }

    public ResourceShadowTable getShadowTable(String resourceName, String searchedItem, String itemValue, String intent, boolean useRepository) {
        return getShadowTabTable(resourceName, intent, useRepository)
                .search()
                .resetBasicSearch()
                .textInputPanelByItemName(searchedItem)
                .inputValue(itemValue)
                .updateSearch()
                .and();
    }

    public ResourceShadowTable<ResourceAccountsPanel<ResourcePage>> getShadowTabTable(String resourceName) {
        return getShadowTabTable(resourceName, null, false);
    }

    public ResourceShadowTable<ResourceAccountsPanel<ResourcePage>> getShadowTabTable(String resourceName, String intent, boolean useRepository) {
        ResourceAccountsPanel<ResourcePage> tab = basicPage.listResources()
                .table()
                .search()
                .byName()
                .inputValue(resourceName)
                .updateSearch()
                .and()
                .clickByName(resourceName)
                .selectAccountsPanel();
        if (useRepository) {
            tab.clickSearchInRepository();
        } else {
            tab.clickSearchInResource();
        }
        if (intent != null && !intent.isEmpty()) {
            Utils.waitForAjaxCallFinish();
            tab.setIntent(intent);
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        }
        return tab.table();
    }

    protected TaskPage showTask(String name, String menuKey) {
        return basicPage.listTasks(menuKey)
                .table()
                .search()
                .byName()
                .inputValue(name)
                .updateSearch()
                .and()
                .clickByName(name);
    }

    protected TaskPage showTask(String name) {
        return showTask(name, "");
    }

    protected void createUser(String newUserName) {
        Map<String, String> attr = new HashMap<>();
        attr.put("Name", newUserName);
        createUser(attr);
    }

    protected void createUser(Map<String, String> newUserAttributesMap) {
        PrismForm<PanelWithContainerWrapper<UserPage>> form = basicPage
                .newUser()
                .selectBasicPanel()
                .form();
        newUserAttributesMap.forEach((key, attr) -> form.addAttributeValue(key, newUserAttributesMap.get(key)));
        form
                .and()
                .and()
                .clickSave()
                .feedback()
                .assertSuccess();
    }

    protected String getConfigurationPropertyValue(String propertyName) throws IOException {
        if (props == null) {
            props = loadProperties(new File(SCHRODINGER_PROPERTIES));
        }
        String headlessStart = System.getProperty(propertyName);
        if (headlessStart == null) {
            headlessStart = props.getProperty(propertyName);
        }
        return headlessStart;
    }

    protected Properties loadProperties(File propertiesFile) throws IOException {
        Properties properties = new Properties();
        InputStream is = new FileInputStream(propertiesFile);
        properties.load(is);
        return properties;
    }

    protected File getExtensionSchemaFile() {
        return null;
    }

    protected String getPostInitialObjectsFolderPath() {
        return null;
    }

    protected boolean isStartMidpoint() throws IOException {
        String startMidpointStr = getConfigurationPropertyValue("startMidpoint");
        LOG.info("startMidpoint property set to: {}", startMidpointStr);
        if (!StringUtils.isEmpty(startMidpointStr) && startMidpointStr.equals("false")) {
            return false;
        }
        return true;
    }

    protected boolean resetToDefaultAfterTests() {
        return true;
    }

    public void assertLastNotificationBodyStartsWith(File notificationFile, String text) throws IOException {
        String lastNotification = Utils.readBodyOfLastNotification(notificationFile);
        Assert.assertTrue(lastNotification.startsWith(text), "Last notification body in the file " + notificationFile.getAbsolutePath()
                + " doesn't start with the text '" + text + "'");
    }

    public void assertLastNotificationBodyEndsWith(File notificationFile, String text) throws IOException {
        String lastNotification = Utils.readBodyOfLastNotification(notificationFile);
        Assert.assertTrue(lastNotification.endsWith(text), "Last notification body in the file " + notificationFile.getAbsolutePath()
                + " doesn't end with the text '" + text + "'");
    }

    public void assertLastNotificationBodyContains(File notificationFile, String text) throws IOException {
        String lastNotification = Utils.readBodyOfLastNotification(notificationFile);
        Assert.assertTrue(lastNotification.contains(text), "Last notification body in the file " + notificationFile.getAbsolutePath()
                + " doesn't contain with the text '" + text + "'");
    }

    public void assertWholeLastNotificationContains(File notificationFile, String text) throws IOException {
        String lastNotification = Utils.readWholeLastNotification(notificationFile);
        Assert.assertTrue(lastNotification.contains(text), "Last notification in the file " + notificationFile.getAbsolutePath()
                + " doesn't contain the text '" + text + "'");
    }

    public void assertLastNotificationSubjectContains(File notificationFile, String text) throws IOException {
        String lastNotificationSubject = Utils.readSubjectOfLastNotification(notificationFile);
        Assert.assertTrue(lastNotificationSubject.contains(text), "Last notification subject in the file " + notificationFile.getAbsolutePath()
                + " doesn't contain to the text '" + text + "'");
    }

    public String getAccountActivationLinkFromNotification(File notificationFile) throws IOException {
        String lastNotification = Utils.readWholeLastNotification(notificationFile);
        int activationLinkStart = lastNotification.indexOf("http://");
        String userOid = "user=";
        int activationLinkEnd = lastNotification.indexOf(userOid) + userOid.length() + 36; // 36 is a length of oid
        if (activationLinkEnd <= activationLinkStart) {
            return null;
        }
        return lastNotification.substring(activationLinkStart, activationLinkEnd).replace("\n", "");
    }

    protected void resetToDefaultAndRelogin() {
        resetToDefault();
        FormLoginPage login = midPoint.formLogin();
        basicPage = login.loginIfUserIsNotLog(username, password);
    }
}
