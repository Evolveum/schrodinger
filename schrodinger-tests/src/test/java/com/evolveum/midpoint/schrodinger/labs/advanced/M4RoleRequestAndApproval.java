package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.component.GovernanceTab;
import com.evolveum.midpoint.schrodinger.component.common.PrismFormWithActionButtons;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.component.org.MemberPanel;
import com.evolveum.midpoint.schrodinger.page.cases.CasePage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.role.RolePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class M4RoleRequestAndApproval extends AbstractAdvancedLabTest {
    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M4/";
    private static final String M3_LAB_SOURCES_DIRECTORY = LAB_OBJECTS_DIRECTORY + "sources/";

    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File HR_ORG_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs.csv");
    private static final File CSV_1_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-1.csv");
    private static final File CSV_2_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-2.csv");
    private static final File CSV_3_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-3.csv");
    protected static final File HR_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source.csv");
    private static final File CONTRACTORS_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "contractors.csv");
    private static final File SYSTEM_CONFIGURATION_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_1 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-1-update-1.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_2 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-1-update-2.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_3_UPDATE_1 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-3-update-1.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_3_UPDATE_2 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-3-update-2.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_6 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-6.xml");
    private static final File ROLE_META_POLICY_RULE_BIGBROTHER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-bigbrother.xml");
    private static final File ROLE_META_POLICY_RULE_APPROVER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-role-approver.xml");
    private static final File ROLE_META_POLICY_RULE_SECURITY_OFFICER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-security-officer-skip-employees.xml");
    private static final File ROLE_META_POLICY_RULE_USER_MANAGER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-user-manager.xml");
    private static final File ROLE_BASICUSER_LAB_4_3 = new File(LAB_OBJECTS_DIRECTORY + "roles/role-basicuser-lab-4-3.xml");
    private static final File ROLE_MANAGER_4_4 = new File(LAB_OBJECTS_DIRECTORY + "roles/role-manager-4-4.xml");
    private static final File ROLE_META_POLICY_ROLE_MODIFICATION_SO = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-role-modification-so.xml");
    private static final File ROLE_META_POLICY_ROLE_MODIFICATION_OWNER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-role-modification-owner.xml");
    private static final File ORG_EXAMPLE_APPROVER_POLICY_ROOT = new File(LAB_OBJECTS_DIRECTORY + "orgs/org-example-approver-policy-root.xml");
    private static final File ORG_EXAMPLE_ROLE_CATALOG_ROOT = new File(LAB_OBJECTS_DIRECTORY + "orgs/org-example-role-catalog-root.xml");
    private static final File ROLE_BASIC_USER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-basicuser.xml");
    private static final File ROLE_INTERNAL_EMPLOYEE_LAB_1_UPDATE_2 = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee-lab-1-update-2.xml");
    private static final File HR_SYNCHRONIZATION_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/hr-synchronization.xml");
    private static final File INITIAL_IMPORT_FROM_HR_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/initial-import-from-hr.xml");
    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_ORG_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr-org.xml");


    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        csv1TargetFile = new File(getTestTargetDir(), CSV_1_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_1_SOURCE_FILE, csv1TargetFile);
        csv2TargetFile = new File(getTestTargetDir(), CSV_2_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_2_SOURCE_FILE, csv2TargetFile);
        csv3TargetFile = new File(getTestTargetDir(), CSV_3_FILE_SOURCE_NAME);
        FileUtils.copyFile(CSV_3_SOURCE_FILE, csv3TargetFile);

        hrTargetFile = new File(getTestTargetDir(), HR_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_SOURCE_FILE, hrTargetFile);

        hrOrgsTargetFile = new File(getTestTargetDir(), HR_ORGS_FILE_SOURCE_NAME);
        FileUtils.copyFile(HR_ORG_SOURCE_FILE, hrOrgsTargetFile);

        contractorsTargetFile = new File(getTestTargetDir(), CONTRACTORS_FILE_SOURCE_NAME);
        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE, contractorsTargetFile);
    }

    @Override
    protected File getModuleInitialSystemConfigXml() {
        return SYSTEM_CONFIGURATION_FILE;
    }

    @Test(groups={"advancedM4"})
    public void mod04test01configureApprovalsUsingPolicyRules() throws IOException {
        addObjectFromFile(OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE);
        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_ORG_RESOURCE_FILE, HR_ORGS_RESOURCE_NAME, hrOrgsTargetFile.getAbsolutePath());

        addObjectFromFile(Utils.changeAttributeIfPresent(SYSTEM_CONFIGURATION_FILE_4_1, "redirectToFile",
                fetchTestHomeDir() + "/example-mail-notifications.log", fetchTestHomeDir()));
        addObjectFromFile(ROLE_META_POLICY_RULE_BIGBROTHER);
        addObjectFromFile(ROLE_META_POLICY_RULE_APPROVER);
        addObjectFromFile(ROLE_META_POLICY_RULE_SECURITY_OFFICER);
        addObjectFromFile(ROLE_META_POLICY_RULE_USER_MANAGER);
        addObjectFromFile(ORG_EXAMPLE_APPROVER_POLICY_ROOT);

        addObjectFromFile(INITIAL_IMPORT_FROM_HR_TASK_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S);
        addObjectFromFile(HR_SYNCHRONIZATION_TASK_FILE);

        basicPage
                .orgStructure()
                    .selectTabWithRootOrg("ExAmPLE, Inc. Approval Policies")
                        .getOrgHierarchyPanel()
                            .selectOrgInTree("ExAmPLE, Inc. Approval Policies")
                            .and()
                        .getMemberPanel()
                            .assignMember()
                                .selectType("Role")
                                .table()
                                    .selectCheckboxByName("Metarole - Request Additional Approval by Big Brother")
                                    .selectCheckboxByName("Metarole - Request Approval by Role Approver(s)")
                                    .selectCheckboxByName("Metarole - Request Approval by Security Officer for Non-employees")
                                    .selectCheckboxByName("Metarole - Request Approval by User Manager(s)")
                                    .and()
                                .clickAdd()
                                .selectType("All")
                                .table()
                                    .assertTableContainsText("Metarole - Request Additional Approval by Big Brother")
                                    .assertTableContainsText("Metarole - Request Approval by Role Approver(s)")
                                    .assertTableContainsText("Metarole - Request Approval by Security Officer for Non-employees")
                                    .assertTableContainsText("Metarole - Request Approval by User Manager(s)");

        addObjectFromFile(Utils.changeAttributeIfPresent(SYSTEM_CONFIGURATION_FILE_4_2, "redirectToFile",
                fetchTestHomeDir() + "/example-mail-notifications.log", fetchTestHomeDir()));

        showUser("administrator")
                .selectTabBasic()
                    .form()
                        .showEmptyAttributes("Properties")
                        .addAttributeValue("Email", "administrator@example.com")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();
        RolePage rolePage = showRole("Secret Projects I");
        rolePage
                .selectTabBasic()
                    .form()
                        .showEmptyAttributes("Properties")
                        .setDropDownAttributeValue("Requestable", "True")
                        .and()
                    .and()
                .selectTabApplicablePolicies()
                    .selectPolicyByName("Request Approval by Role Approver(s)")
                    .selectPolicyByName("Request Approval by Security Officer for Non-employees")
                    .selectPolicyByName("Request Approval by User Manager(s)");
        rolePage
                .clickSave()
                    .feedback()
                        .assertSuccess();
        rolePage = showRole("Secret Projects II");
        rolePage
                .selectTabBasic()
                    .form()
                        .showEmptyAttributes("Properties")
                        .setDropDownAttributeValue("Requestable", "True")
                        .and()
                    .and()
                .selectTabApplicablePolicies()
                    .selectPolicyByName("Request Approval by Role Approver(s)")
                    .selectPolicyByName("Request Approval by Security Officer for Non-employees")
                    .selectPolicyByName("Request Approval by User Manager(s)");
        rolePage
                .clickSave()
                    .feedback()
                        .assertSuccess();
        rolePage = showRole("Top Secret Projects I");
        rolePage
                .selectTabBasic()
                    .form()
                        .showEmptyAttributes("Properties")
                        .setDropDownAttributeValue("Requestable", "True")
                        .and()
                    .and()
                .selectTabApplicablePolicies()
                    .selectPolicyByName("Request Approval by Role Approver(s)")
                    .selectPolicyByName("Request Approval by Security Officer for Non-employees")
                    .selectPolicyByName("Request Approval by User Manager(s)")
                    .selectPolicyByName("Request Additional Approval by Big Brother");
        rolePage
                .clickSave()
                    .feedback()
                        .assertSuccess();

        AssignmentsTab<UserPage> assignmentsTab = showUser("X000089")
                .selectTabBasic()
                    .form()
                        .addPasswordAttributeValue("qwerty12345XXXX")
                        .and()
                    .and()
                    .selectTabAssignments();
        Utils.addAssignmentsWithRelation(assignmentsTab, "Approver", "Secret Projects I", "Secret Projects II");
        Utils.addAssignmentsWithRelationAndSave(assignmentsTab, "Member", true, "Basic Approver");

        FocusSetAssignmentsModal<MemberPanel<GovernanceTab>> modal = showRole("Top Secret Projects I")
                .selectTabGovernance()
                    .membersPanel()
                        .assignMember();
        modal.setRelation("Approver")
                .table()
                    .search()
                        .byName()
                        .inputValue("administrator")
                        .updateSearch()
                    .and()
                    .selectCheckboxByName("administrator");
        modal.clickAdd();
        basicPage
                .feedback()
                    .assertInfo();

        Utils.addAssignmentsWithDefaultRelationAndSave(showUser("X000158").selectTabAssignments(), false, "Secret Projects I");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S);
        basicPage.listAllCases()
                .table()
                    .clickByName("Approving and executing change of user ")
                        .selectTabChildren()
                            .table()
                            .clickByPartialName("Assigning role")
                                .selectTabWorkitems()
                                    .table()
                                        .clickNameByState("open")
                                            .setComment("Test of approvals, stage 1")
                                            .approveButtonClick()
                                            .and()
                                        .and()
                                    .assertFeedbackExists();

        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);
        basicPage.listAllCases()
                .table()
                    .clickByName("Approving and executing change of user")
                        .selectTabChildren()
                            .table()
                            .clickByPartialName("Assigning role")
                                .selectTabWorkitems()
                                    .table()
                                        .clickNameByState("open")
                                            .setComment("Test of approvals, stage 2")
                                            .approveButtonClick()
                                            .and()
                                        .and()
                                    .assertFeedbackExists();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);

        PrismFormWithActionButtons projectionForm = showUser("X000158").selectTabAssignments()
                .assertAssignmentsWithRelationExist("Member", "Secret Projects I")
                .and()
                .selectTabProjections()
                    .viewProjectionDetails("ablack", "CSV-1");
        projectionForm
                .assertPropertyInputValues("Groups", "Internal Employees", "Teleportation",
                        "Time Travel", "Essential Documents");
        projectionForm
                .clickCancel()
                //todo click the New Corporate Directory projection to show its details. You should see the project
        //groups updated
        ;

        //todo check notification
        addObjectFromFile(ROLE_BASIC_USER);
        addObjectFromFile(ROLE_INTERNAL_EMPLOYEE_LAB_1_UPDATE_2);

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login("X000089", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.listMyCases();

        basicPage.loggedUser().logout();
        loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword())
                .assertUserMenuExist();

        showUser("X000158")
                .selectTabBasic()
                    .form()
                        .addPasswordAttributeValue("qwerty12345XXXX")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();
        showUser("X000390")
                .selectTabBasic()
                    .form()
                        .addPasswordAttributeValue("qwerty12345XXXX")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();
        Utils.removeAssignments(showUser("X000158").selectTabAssignments(), "Secret Projects I");
    }

    @Test(groups={"advancedM4"}, dependsOnMethods = "mod04test01configureApprovalsUsingPolicyRules")
    public void mod04test02selfServiceRequestingRoles() throws IOException {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();

        basicPage.requestRole()
                .selectRoleCatalogViewTab()
                    .getItemsPanel()
                        .addItemToCart("Secret Projects I")
                        .and()
                    .goToShoppingCart()
                        .setRequestComment("Please approve, I need to work on the X911 project")
                        .clickRequestButton()
                            .feedback()
                            .assertInfo();

        //todo check notification "[IDM] Workflow Process Assigning role "Secret
        //Projects I" to user "X000158" has been started" addressed to user himself/herself (alice.black@example.com)
        //there should a notification with a subject: [IDM] Work Item Allocation Event for X000158 (by
        //X000158), allocated to: X000390 addressed to user’s manager (john.wicks@example.com)

        basicPage.loggedUser().logout();
        loginPage.login("X000390", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.myItems()
                    .table()
                        .clickByName("") //todo insert name
                            //todo approve
;

        basicPage.loggedUser().logout();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();
        CasePage casePage = basicPage.home().
                myRequestsTable()
                    .clickByName("");
        casePage
                .selectTabChildren()
                    .table()
                        .clickByPartialName("");
        //todo check the second (2/2) stage of approval by Ivan Rockerteller (X000089)

        basicPage.loggedUser().logout();
        loginPage.login("X000089", "qwerty12345XXXX")
                .assertUserMenuExist();

        basicPage.myItems()
                .table()
                    .clickByName("")
                        .detailsPanel()
                            .approveButtonClick()
                            .and()
                        .assertFeedbackExists();
        Selenide.sleep(MidPoint.TIMEOUT_SHORT_4_S);

        //todo check notification
        basicPage.loggedUser().logout();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();

    }

    @Test(groups={"advancedM4"}, dependsOnMethods = "mod04test01configureApprovalsUsingPolicyRules")
    public void mod04test03selfServiceRequestingRolesFromRoleCatalog() {
        addObjectFromFile(ORG_EXAMPLE_ROLE_CATALOG_ROOT);
        addObjectFromFile(ROLE_BASICUSER_LAB_4_3);
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_4_3_UPDATE_1);

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword())
                .assertUserMenuExist();

        OrgTreePage orgTree = basicPage.orgStructure();
        orgTree.selectTabWithRootOrg("ExAmPLE, Inc. Role Catalog")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("ExAmPLE, Inc. Role Catalog")
                    .expandAll()
                    .selectOrgInTree("ExAmPLE, Inc. Role Catalog")
                    .and()
                .getMemberPanel()
                    .selectType("Role")
                    .table()
                        .assertTableObjectsCountEquals(0);

        basicPage.loggedUser().logout();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.requestRole()
                .selectRoleCatalogViewTab()
                    .getRoleCatalogHierarchyPanel()
                        .selectOrgInTree("ExAmPLE, Inc. Role Catalog")
                        .and()
                        .getItemsPanel()
                            .assertItemsCountEqual(0)
                            .and()
                        .and()
                    .selectRoleCatalogViewTab()
                        .getItemsPanel()
                            .assertItemsCountEqual(0)
                            .and()
                        .and()
                    .selectAllOrganizationsViewTab()
                        .getItemsPanel()
                            .assertItemsCountEqual(0)
                            .and()
                        .and()
                    .selectAllServicesViewTab()
                        .getItemsPanel()
                            .assertItemsCountEqual(0);

        basicPage.loggedUser().logout();
        loginPage.login(getUsername(), getPassword())
                .assertUserMenuExist();

        basicPage.orgStructure();
        orgTree.selectTabWithRootOrg("ExAmPLE, Inc. Role Catalog")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("ExAmPLE, Inc. Role Catalog")
                    .expandAll()
                        .selectOrgInTree("Application Bundles")
                            .and()
                        .getMemberPanel()
                            .assignMember()
                                .selectType("Role")
                                .table()
                                    .search()
                                    .byName()
                                    .inputValue("Secret")
                                    .updateSearch()
                                    .and()
                                .selectAll()
                                .and()
                            .clickAdd()
                            .and()
                        .and()
                    .feedback()
                    .assertInfo();
        orgTree.selectTabWithRootOrg("ExAmPLE, Inc. Role Catalog")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("ExAmPLE, Inc. Role Catalog")
                    .expandAll()
                        .selectOrgInTree("Application Bundles")
                            .and()
                        .getMemberPanel()
                            .selectType("Role")
                            .table()
                                .assertTableContainsText("Secret Projects I")
                                .assertTableContainsText("Secret Projects II")
                                .assertTableContainsText("Secret Operations");
        basicPage.loggedUser().logout();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage
                .requestRole()
                    .selectRoleCatalogViewTab()
                        .getRoleCatalogHierarchyPanel()
                            .selectOrgInTree("Application bundles")
                            .and()
                        .getItemsPanel()
                            .assertItemsCountEqual(3);

        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_4_3_UPDATE_2);
        basicPage.loggedUser().logout();
        loginPage.login("X000158", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage
                .requestRole()
                    .assertAllRolesViewTabExists()
                    .assertRoleCatalogViewTabExists()
                    .assertAllServicesViewTabDoesntExist()
                    .assertAllOrganizationsViewTabDoesntExist()
                    .assertUserAssignmentsTabDoesntExist();
    }

    @Test(groups={"advancedM4"}, dependsOnMethods = "mod04test01configureApprovalsUsingPolicyRules")
    public void mod04test04selfServiceRequestingRolesForSubordinateEmployees() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login("X000390", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.listUsers("Employees")
                .table()
                    .assertTableContainsText("X000158");
        basicPage.requestRole()
                .setRequestingForUser("X000158")
                .selectRoleCatalogViewTab()
                    .getRoleCatalogHierarchyPanel()
                        .selectOrgInTree("Application Bundles")
                        .and()
                    .getItemsPanel()
                    .assertItemsCountEqual(0);

        basicPage.loggedUser().logout();

        addObjectFromFile(ROLE_MANAGER_4_4);

        loginPage = midPoint.formLogin();
        loginPage.login("X000390", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.listUsers("Employees")
                .table()
                    .assertTableContainsText("X000158");
        basicPage.requestRole()
                .setRequestingForUser("X000158")
                .selectRoleCatalogViewTab()
                    .getRoleCatalogHierarchyPanel()
                        .selectOrgInTree("Application Bundles")
                        .and()
                    .getItemsPanel()
                        .addItemToCart("Secret Projects II")
                        .and()
                    .goToShoppingCart()
                        .clickRequestButton()
                        .assertFeedbackExists();
        basicPage
                .myItems()
                    .table()
                        .approveWorkitemByName("X000158")
                        .and()
                    .assertFeedbackExists();

        basicPage.loggedUser().logout();
        loginPage = midPoint.formLogin();
        loginPage.login("X000390", "qwerty12345XXXX")
                .assertUserMenuExist();

        addObjectFromFile(ROLE_MANAGER_4_4);

        basicPage.loggedUser().logout();
        loginPage.login("X000089", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage
                .myItems()
                    .table()
                        .approveWorkitemByName("X000158")
                        .and()
                    .assertFeedbackExists();
        basicPage.loggedUser().logout();
        loginPage.login(getUsername(), getPassword())
                .assertUserMenuExist();
        PrismFormWithActionButtons projectionForm = showUser("X000158").selectTabAssignments()
                .assertAssignmentsWithRelationExist("Member", "Secret Projects II")
                .and()
                .selectTabProjections()
                .viewProjectionDetails("ablack", "CSV-1");
        projectionForm
                .assertPropertyInputValues("Groups", "Internal Employees", "Teleportation",
                        "Time Travel", "Essential Documents", "Lucky Numbers", "Presidential Candidates Motivation");
    }

    @Test(groups={"advancedM4"}, dependsOnMethods = "mod04test04selfServiceRequestingRolesForSubordinateEmployees")
    public void mod04test05roleModificationApproval() {
        MemberPanel<GovernanceTab<RolePage>> memberPanel = showRole("Secret Projects I")
                .selectTabGovernance()
                    .membersPanel();
        memberPanel
                .assignMember()
                    .setRelation("Owner")
                    .table()
                        .search()
                            .byName()
                            .inputValue("administrator")
                            .updateSearch()
                            .and()
                    .selectCheckboxByName("administrator")
                    .and()
                .clickAdd()
                .and()
                .and()
                .assertFeedbackExists();
        memberPanel
                .selectRelation("Any")
                .table()
                    .clickRefreshButton()
                    .assertTableContainsLinksTextPartially("administrator");

        addObjectFromFile(ROLE_META_POLICY_ROLE_MODIFICATION_SO);
        addObjectFromFile(ROLE_META_POLICY_ROLE_MODIFICATION_OWNER);

        basicPage
                .orgStructure()
                    .selectTabWithRootOrg("ExAmPLE, Inc. Approval Policies")
                        .getOrgHierarchyPanel()
                            .selectOrgInTree("ExAmPLE, Inc. Approval Policies")
                            .and()
                        .getMemberPanel()
                            .assignMember()
                                .selectType("Role")
                                .table()
                                    .selectCheckboxByName("Metarole - Role Modification Approval by Role Owner(s)")
                                    .selectCheckboxByName("Metarole - Role Modification Approval by Security Officer")
                                    .and()
                                .clickAdd()
                                .selectType("All")
                                .table()
                                    .assertTableContainsText("Metarole - Role Modification Approval by Role Owner(s)")
                                    .assertTableContainsText("Metarole - Role Modification Approval by Security Officer")
                                    .assertTableObjectsCountEquals(6);

        RolePage rolePage = showRole("Secret Projects I");
        rolePage
                .selectTabApplicablePolicies()
                    .selectPolicyByName("Role Modification Approval by Role Owner(s)")
                    .selectPolicyByName("Role Modification Approval by Security Officer");
        rolePage
                .clickSave()
                    .feedback()
                        .assertInfo();

        basicPage.listMyCases()
                .table()
                    .clickByName("Approving and executing change of role")
                        .selectTabChildren()
                            .table()
                            .clickByPartialName("Modifying role")
                                .selectTabWorkitems()
                                    .table()
                                        .clickNameByState("open")
                                            .approveButtonClick()
                                            .and()
                                        .and()
                                    .assertFeedbackExists();

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login("security-officer", "sECurit400")
                .assertUserMenuExist();

        basicPage.myItems()
                   .table()
                        .clickByName("Modifying role")
                            .detailsPanel()
                                .approveButtonClick()
                                .and()
                            .assertFeedbackExists();

        rolePage = showRole("Area 52 Managers");
        rolePage
                .selectTabApplicablePolicies()
                    .selectPolicyByName("Role Modification Approval by Role Owner(s)")
                    .selectPolicyByName("Role Modification Approval by Security Officer");
        rolePage.clickSave()
                .feedback()
                    .assertInfo();
        basicPage.myItems()
                .table()
                    .clickByName("Modifying role")
                        .detailsPanel()
                        .approveButtonClick()
                        .and()
                    .assertFeedbackExists();
    }

    @Test(groups={"advancedM4"}, dependsOnMethods = "mod04test05roleModificationApproval")
    public void mod04test06roleCreationApproval() {
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_4_6);

        basicPage
                .newRole()
                    .selectTabBasic()
                        .form()
                            .addAttributeValue("Name", "Test Role 1")
                            .addAttributeValue("Subtype", "auto")
                            .and()
                        .and()
                    .clickSave()
                        .feedback()
                            .assertSuccess();

        basicPage
                .newRole()
                    .selectTabBasic()
                        .form()
                            .addAttributeValue("Name", "Test Role 2")
                            .addAttributeValue("Subtype", "xxx")
                            .and()
                        .and()
                    .clickSave()
                        .feedback()
                            .assertInfo()
                            .clickShowCase()
                                .selectTabChildren()
                                    .table()
                                        .clickByPartialName("Adding role")
                                            .selectTabWorkitems()
                                                .table()
                                                    .clickByName("Adding role")
                                                        .approveButtonClick()
                                                        .and()
                                                    .and()
                                                .assertFeedbackExists();
        showRole("Test Role 2");
    }
}
