package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.evolveum.midpoint.schrodinger.page.role.RolePage;
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
    private static final File ROLE_ORG_EXAMPLE_APPROVER_POLICY_ROOT = new File(LAB_OBJECTS_DIRECTORY + "orgs/org-example-approver-policy-root.xml");

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
        addObjectFromFile(ROLE_ORG_EXAMPLE_APPROVER_POLICY_ROOT);

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
    }

}
