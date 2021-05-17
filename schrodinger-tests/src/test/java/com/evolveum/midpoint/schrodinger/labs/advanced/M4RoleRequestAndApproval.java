package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.login.LoginPage;
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

    private static final File HR_ORG_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs.csv");
    private static final File CSV_1_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-1.csv");
    private static final File CSV_2_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-2.csv");
    private static final File CSV_3_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-3.csv");
    private static final File CONTRACTORS_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "contractors.csv");
    private static final File SYSTEM_CONFIGURATION_FILE_4_1 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-1-update-1.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_4_2 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-4-1-update-2.xml");
    private static final File ROLE_META_POLICY_RULE_BIGBROTHER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-bigbrother.xml");
    private static final File ROLE_META_POLICY_RULE_APPROVER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-role-approver.xml");
    private static final File ROLE_META_POLICY_RULE_SECURITY_OFFICER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-security-officer-skip-employees.xml");
    private static final File ROLE_META_POLICY_RULE_USER_MANAGER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-meta-policy-rule-user-manager.xml");
    private static final File ORG_EXAMPLE_APPROVER_POLICY_ROOT = new File(LAB_OBJECTS_DIRECTORY + "orgs/org-example-approver-policy-root.xml");
    private static final File ROLE_BASIC_USER = new File(LAB_OBJECTS_DIRECTORY + "roles/role-basicuser.xml");
    private static final File ROLE_INTERNAL_EMPLOYEE_LAB_1_UPDATE_2 = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee-lab-1-update-2.xml");


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

    @Test(groups={"advancedM1"})
    public void mod04test01configureApprovalsUsingPolicyRules() throws IOException {
        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_4_1);
        addObjectFromFile(ROLE_META_POLICY_RULE_BIGBROTHER);
        addObjectFromFile(ROLE_META_POLICY_RULE_APPROVER);
        addObjectFromFile(ROLE_META_POLICY_RULE_SECURITY_OFFICER);
        addObjectFromFile(ROLE_META_POLICY_RULE_USER_MANAGER);
        addObjectFromFile(ORG_EXAMPLE_APPROVER_POLICY_ROOT);

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
                            .getMemberPanel()
                                .selectType("All")
                                .table()
                                    .assertTableContainsText("Metarole - Request Additional Approval by Big Brother")
                                    .assertTableContainsText("Metarole - Request Approval by Role Approver(s)")
                                    .assertTableContainsText("Metarole - Request Approval by Security Officer for Non-employees")
                                    .assertTableContainsText("Metarole - Request Approval by User Manager(s)");

        addObjectFromFile(SYSTEM_CONFIGURATION_FILE_4_2);

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
        Utils.addAssignmentsWithRelation(assignmentsTab, "Approver", true,"Secret Projects I", "Secret Projects II");
        Utils.addAssignmentsWithRelation(assignmentsTab, "Member", true, "Basic Approver");

        FocusSetAssignmentsModal modal = showRole("Top Secret Projects I")
                .selectTabGovernance()
                    .membersPanel()
                        .assignMember();
        modal.setRelation("Approver")
                .table()
                    .selectCheckboxByName("administrator");
        modal.clickAdd();
        basicPage
                .feedback()
                    .assertInfo();

        Utils.addAssignments(showUser("X000158").selectTabAssignments(), false, "Secret Projects I");
        basicPage.listAllCases()
                .table()
                    .clickByName("Approving and executing change of user \"X000158\"")
                        .selectTabChildren()
                            .table()
                            .clickByPartialName("Assigning role \"Secret Projects I\" to user \"X000158\"")
                                .selectTabWorkitems()
                                    .table()
                                        .clickByName("") //todo enter the name
                                            .setComment("Test of approvals, stage 1")
                                            .approveButtonClick();

        basicPage.listAllCases()
                .table()
                    .clickByName("Approving and executing change of user \"X000158\"")
                        .selectTabChildren()
                            .table()
                            .clickByPartialName("Assigning role \"Secret Projects I\" to user \"X000158\"")
                                .selectTabWorkitems()
                                    .table()
                                        .clickByName("") //todo enter the name for the second workitem
                                            .setComment("Test of approvals, stage 2")
                                            .approveButtonClick();

        showUser("X000158").selectTabAssignments()
                .assertAssignmentsWithRelationExist("Member", "Secret Projects I")
                .and()
                .selectTabProjections()
                    .viewProjectionDetails("", "CSV-1") //todo enter projection name and check group attribute
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
        basicPage.listAllCases();

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

    @Test(groups={"advancedM1"}, dependsOnMethods = "mod04test01configureApprovalsUsingPolicyRules")
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


    }
}
