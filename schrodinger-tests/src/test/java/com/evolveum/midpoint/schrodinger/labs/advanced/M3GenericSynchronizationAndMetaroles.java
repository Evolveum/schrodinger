package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.org.OrgHierarchyPanel;
import com.evolveum.midpoint.schrodinger.component.org.OrgRootTab;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.TaskType;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class M3GenericSynchronizationAndMetaroles extends AbstractAdvancedLabTest {
    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M3/";
    private static final String M3_LAB_SOURCES_DIRECTORY = ADVANCED_LABS_DIRECTORY + "sources/";

    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File CSV_1_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-1-lab-3-initial.csv");
    private static final File CSV_2_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-2-lab-3-initial.csv");
    private static final File CSV_3_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-3-lab-3-initial.csv");
    private static final File HR_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-initial.csv");
    private static final File HR_SOURCE_FILE_LAB_3_4_UPDATE_1 = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-4-update-1.csv");
    private static final File HR_SOURCE_FILE_LAB_3_5_UPDATE_1 = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-5-update-1.csv");
    private static final File HR_SOURCE_FILE_LAB_3_5_UPDATE_2 = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-5-update-2.csv");
    private static final File HR_SOURCE_FILE_LAB_3_5_UPDATE_3 = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-5-update-3.csv");
    private static final File HR_SOURCE_FILE_LAB_3_5_UPDATE_4 = new File(M3_LAB_SOURCES_DIRECTORY + "source-lab-3-5-update-4.csv");
    private static final File HR_ORG_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-initial.csv");
    private static final File HR_ORG_SOURCE_FILE_LAB_3_4_UPDATE_1 = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-4-update-1.csv");
    private static final File HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_1 = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-5-update-1.csv");
    private static final File HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_2 = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-5-update-2.csv");
    private static final File HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_3 = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-5-update-3.csv");
    private static final File HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_4 = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs-lab-3-5-update-4.csv");
    private static final File CONTRACTORS_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "contractors-lab-3-initial.csv");

    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_ORG_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr-org.xml");

    private static final File RIMSY_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/rimsy-user.xml");
    private static final File SEQUENCE_MEALCARD_FILE = new File(LAB_OBJECTS_DIRECTORY + "sequences/sequence-mealcard.xml");
    private static final File HR_SYNCHRONIZATION_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/hr-synchronization.xml");
    private static final File INITIAL_IMPORT_FROM_HR_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/initial-import-from-hr.xml");
    private static final File ROLE_INTERNAL_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File KIRK_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-user.xml");

    private static final File OBJECT_TEMPLATE_EXAMPLE_ORG = new File(LAB_OBJECTS_DIRECTORY + "objecttemplates/object-template-example-org.xml");
    private static final File OBJECT_TEMPLATE_EXAMPLE_USER = new File(LAB_OBJECTS_DIRECTORY + "objecttemplates/object-template-example-user.xml");
    private static final String CORPORATE_DIRECTORY_RESOURCE_NAME = "New Corporate Directory";
    private static final File SYSTEM_CONFIGURATION_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration.xml");

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

    @Test(groups={"advancedM3"})
    public void mod03test01sequences() throws IOException {
        addObjectFromFile(OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE);
        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());

        addObjectFromFile(RIMSY_USER_FILE);
        addObjectFromFile(KIRK_USER_FILE);

        addObjectFromFile(SEQUENCE_MEALCARD_FILE);
        addObjectFromFile(ROLE_INTERNAL_EMPLOYEE_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());

        showUser("kirk")
                .checkReconcile()
                .clickSave()
                .feedback()
                    .assertSuccess();

        showUser("kirk")
                .selectBasicPanel()
                    .form()
                        .assertPropertyInputValueContainsText("Meal Card Number", "1001")
                        .and()
                    .and()
                .selectProjectionsPanel()
                    .viewProjectionDetails("jkirk", "CSV-2 (Canteen Ordering System)")
                            .assertPropertyInputValue("Meal Card Number", "1001");

        addObjectFromFile(INITIAL_IMPORT_FROM_HR_TASK_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        addObjectFromFile(HR_SYNCHRONIZATION_TASK_FILE);

        showRole("Internal Employee")
                .selectTabMembers()
                    .membersPanel()
                        .table()
                            .recompute()
                                .clickYes();

        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M.getSeconds());
        basicPage.listUsers()
                .table()
                    .search()
                        .byName()
                        .inputValue("X000021")
                        .updateSearch()
                    .and()
                    .clickByName("X000021")
                        .selectBasicPanel()
                            .form()
                                .assertPropertyInputValueContainsText("Meal Card Number", "10");
    }

    @Test(dependsOnMethods = {"mod03test01sequences"}, groups={"advancedM3"})
    public void mod03test04orgStructureSynchronization() throws IOException {
        addCsvResourceFromFileAndTestConnection(HR_ORG_RESOURCE_FILE, HR_ORGS_RESOURCE_NAME, hrOrgsTargetFile.getAbsolutePath());
        TaskPage task = basicPage.newTask("Reconciliation task");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        task.selectBasicPanel()
                .form()
                    .addAttributeValue(TaskType.F_NAME, "HR Org Reconciliation")
                    .addAttributeValue("objectclass", "AccountObjectClass")
                    .addAttributeValue("Kind", "Generic")
                    .editRefValue("objectRef")
                        .selectType("Resource")
                            .table()
                                .search()
                                    .byName()
                                    .inputValue(HR_ORGS_RESOURCE_NAME)
                                .updateSearch()
                                .and()
                            .clickByName(HR_ORGS_RESOURCE_NAME)
                    .selectOption("Dry run","True")
                    .and()
                .and()
                .clickSaveAndRun()
                    .feedback()
                        .assertInfo();

        basicPage.listTasks()
                .table()
                    .search()
                        .byName()
                        .inputValue(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                        .updateSearch()
                        .and()
                    .clickByName(CONTRACTORS_RESOURCE_IMPORT_TASK_NAME)
                        .selectOperationStatisticsPanel()
                        .assertProgressSummaryObjectsCountEquals(10)
                        .assertSucceededCountMatch(10);
        basicPage
                .listResources()
                    .table()
                        .search()
                            .resetBasicSearch()
                            .and()
                        .clickByName("ExAmPLE Inc. HR Organization Structure Source")
                            .clickGenericsTab()
                                .clickSearchInResource()
                                    .table()
                                        .assertVisibleObjectsCountEquals(10); //todo check UNLINKED: 7 objects; UNMATCHED: 3 objects

        addObjectFromFile(OBJECT_TEMPLATE_EXAMPLE_ORG);

        basicPage
                .listTasks()
                    .table()
                        .search()
                            .byName()
                            .inputValue("HR Org Reconciliation")
                            .updateSearch()
                            .and()
                        .clickByName("HR Org Reconciliation")
                            .selectBasicPanel()
                                .form()
                                    .selectOption("Dry run", "False")
                                    .and()
                                .and()
                            .clickSaveAndRun();

        OrgHierarchyPanel<OrgRootTab> orgHierarchyPanel = basicPage
                .orgStructure()
                    .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .assertChildOrgExists("Special operations", "Augmentation Development", "Xenomorph Acquisition");
        orgHierarchyPanel
                        .showTreeNodeDropDownMenu("Special operations")
                            .edit()
                                .summary()
                                    .assertSummaryTagWithTextExists("Organization")
                //todo check LDAP Org Group Metarole is assigned
                                    .and()
                                .clickBack();
        orgHierarchyPanel
                .showTreeNodeDropDownMenu("Augmentation Development")
                    .edit()
                        .summary()
                        .assertSummaryTagWithTextExists("Organization")
                //todo check LDAP Org Group Metarole is assigned
                        .and()
                    .clickBack();

        orgHierarchyPanel
                .showTreeNodeDropDownMenu("Xenomorph Acquisition")
                    .edit()
                        .summary()
                        .assertSummaryTagWithTextExists("Organization")
                //todo check LDAP Org Group Metarole is not assigned
                        .and()
                    .clickBack();

        TaskPage hrOrgsSyncTask = basicPage.newTask("Live synchronization task");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        hrOrgsSyncTask.selectBasicPanel()
                .form()
                    .addAttributeValue(TaskType.F_NAME, "HR Orgs Synchronization")
                    .addAttributeValue("Kind", "Generic")
                    .selectOption("Recurrence","Recurring")
                    .selectOption("Binding","Tight")
                    .editRefValue("objectRef")
                        .selectType("Resource")
                        .table()
                        .search()
                            .byName()
                            .inputValue(HR_ORGS_RESOURCE_NAME)
                            .updateSearch()
                            .and()
                        .clickByName(HR_ORGS_RESOURCE_NAME)
                    .selectOption("Dry run","True")
                    .and()
                .and()
                .selectSchedulePanel()
                    .form()
                        .addAttributeValue("Interval", "5")
                        .and()
                    .and()
                .clickSaveAndRun()
                    .feedback()
                        .assertInfo();

        FileUtils.copyFile(HR_ORG_SOURCE_FILE_LAB_3_4_UPDATE_1, hrOrgsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
        //TODO check notification

        basicPage
                .orgStructure()
                    .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                        .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("Special operations")
                            .expandAll()
                            .selectOrgInTree("Biomechanic Arm Development")
                                .showTreeNodeDropDownMenu("Biomechanic Arm Development")
                                .edit()
                                    .summary()
                                    .assertSummaryTagWithTextExists("Organization")
                                    //todo check LDAP Org Group Metarole is not assigned
                                    //todo check parent organization (Special Operations) should be assigned
                                    .and()
                                .selectAssignmentsPanel()
                                    .assertAssignmentsWithRelationExist("Member", "LDAP Org Group Metarole")
                                    .and()
                                .clickBack();

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_3_4_UPDATE_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage
                .listUsers("Employees")
                    .table()
                        .search()
                            .byName()
                            .inputValue("X000979")
                            .updateSearch()
                            .and()
                        .clickByName("X000979")
                            .selectAssignmentsPanel()
                                .assertAssignmentsWithRelationExist("Member", "Biomechanic Arm Development", "Internal Employee")
                                .and()
                            .selectProjectionsPanel()
                                .table()
                                    .assertVisibleObjectsCountEquals(4); //todo actual result should be 5 when ldap resource is up
        //todo check notification
    }

    @Test(dependsOnMethods = {"mod03test04orgStructureSynchronization"}, groups={"advancedM3"})
    public void mod03test05orgSynchWithCreateOnDemand() throws IOException {
        addObjectFromFile(OBJECT_TEMPLATE_EXAMPLE_USER);

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_3_5_UPDATE_1, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("X000993")
                .selectAssignmentsPanel()
                    .assertAssignmentsWithRelationExist("Member", "Active Employees")
                    .assertAssignmentsWithRelationExist("Member", "0440")
                    .assertAssignmentsWithRelationExist("Member", "Internal Employee")
                //todo account in New Corporate Directory should be member of cn=org-0440,ou=orgStruct,dc=example,dc=com group
        ;

        OrgTreePage orgTree = basicPage.orgStructure();
        orgTree.selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Temporary")
                                .assertChildOrgExists("0440")
                                .showTreeNodeDropDownMenu("0440")
                                    .edit()
                                        .selectAssignmentsPanel()
                                        .assertAssignmentsWithRelationExist("Member", "LDAP Org Group Metarole")
                                        .and()
                                        .selectProjectionsPanel()
        //todo check that a group cn=org-0440,ou=orgStruct,dc=example,dc=com has
        //been created in New Corporate Directory
        ;

        //todo check notification

        FileUtils.copyFile(HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_1, hrOrgsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                            .expandAll()
                                .selectOrgInTree("Special operations")
                                .assertChildOrgExists("Investigation")
                                .showTreeNodeDropDownMenu("Investigation")
                                    .edit()
                                        .selectProjectionsPanel()
                                            .assertProjectionExist("ExAmPLE, Inc. HR Organization Structure Source", "Investigation");

        FileUtils.copyFile(HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_2, hrOrgsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                            .expandAll()
                            .selectOrgInTree("Special operations")
                                .assertChildOrgExists("Private Investigation");

        FileUtils.copyFile(HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_3, hrOrgsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                            .expandAll()
                            .selectOrgInTree("Augmentation Development")
                                .assertChildOrgExists("Private Investigation");

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_3_5_UPDATE_2, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("000992")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Member", "Active Employees")
                .assertAssignmentsWithRelationExist("Member", "0470")
                .assertAssignmentsWithRelationExist("Member", "Internal Employee")
        //todo account in New Corporate Directory should be member of cn=org-0470,ou=orgStruct,dc=example,dc=com group
        ;

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Temporary")
                            .assertChildOrgExists("0470")
                            .showTreeNodeDropDownMenu("0470")
                            .edit()
                                .selectAssignmentsPanel()
                                    .assertAssignmentsWithRelationExist("Member", "LDAP Org Group Metarole")
                                    .and()
                                .selectProjectionsPanel()
                            //todo check that a group cn=org-0470,ou=orgStruct,dc=example,dc=com has
                            //been created in New Corporate Directory
        ;

        //todo check notification

        showUser("000991")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Member", "Active Employees")
                .assertAssignmentsWithRelationExist("Member", "0450")
                .assertAssignmentsWithRelationExist("Member", "Internal Employee")
        //todo account in New Corporate Directory should be member of cn=org-0450,ou=orgStruct,dc=example,dc=com group
        ;

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Temporary")
                            .assertChildOrgExists("0450")
                            .showTreeNodeDropDownMenu("0450")
                            .edit()
                                .selectAssignmentsPanel()
                                    .assertAssignmentsWithRelationExist("Member", "LDAP Org Group Metarole")
                                    .and()
                                .selectProjectionsPanel()
                            //todo check that a group cn=org-0450,ou=orgStruct,dc=example,dc=com has
                            //been created in New Corporate Directory
        ;

        //todo check notification

        showUser("X000990")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Member", "Active Employees")
                .assertAssignmentsWithRelationExist("Member", "0460")
                .assertAssignmentsWithRelationExist("Member", "Internal Employee")
        //todo account in New Corporate Directory should be member of cn=org-0460,ou=orgStruct,dc=example,dc=com group
        ;

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Temporary")
                            .assertChildOrgExists("0460")
                            .showTreeNodeDropDownMenu("0460")
                            .edit()
                                .selectAssignmentsPanel()
                                    .assertAssignmentsWithRelationExist("Member", "LDAP Org Group Metarole")
                                    .and()
                                .selectProjectionsPanel()
                            //todo check that a group cn=org-0460,ou=orgStruct,dc=example,dc=com has
                            //been created in New Corporate Directory
        ;

        //todo check notification

        FileUtils.copyFile(HR_ORG_SOURCE_FILE_LAB_3_5_UPDATE_4, hrOrgsTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Special operations")
                            .assertChildOrgExists("Public Relations")
                            .showTreeNodeDropDownMenu("Public Relations")
                                .edit()
                                    .selectProjectionsPanel()
                                        .assertProjectionExist("ExAmPLE, Inc. HR Organization Structure Source", "Investigation");

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Special operations")
                            .assertChildOrgExists("Interrogation Specialists")
                            .showTreeNodeDropDownMenu("Interrogation Specialists")
                                .edit()
                                    .selectProjectionsPanel()
                                        .assertProjectionExist("ExAmPLE, Inc. HR Organization Structure Source", "Investigation");

        basicPage.orgStructure()
                .selectTabWithRootOrg("ExAmPLE, Inc. - Functional Structure")
                    .getOrgHierarchyPanel()
                        .showTreeNodeDropDownMenu("ExAmPLE, Inc. - Functional Structure")
                        .expandAll()
                            .selectOrgInTree("Special operations")
                            .assertChildOrgExists("Explosions Department")
                            .showTreeNodeDropDownMenu("Explosions Department")
                                .edit()
                                    .selectProjectionsPanel()
                                        .assertProjectionExist("ExAmPLE, Inc. HR Organization Structure Source", "Investigation");

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_3_5_UPDATE_3, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("000993")
                .selectAssignmentsPanel()
                .assertAssignmentsWithRelationExist("Member", "Active Employees")
                .assertAssignmentsWithRelationExist("Member", "Explosions Department")
                .assertAssignmentsWithRelationExist("Member", "Internal Employee")
                //todo check projection cn=org-0470,ou=orgStruct,dc=example,dc=com in New Corporate Directory
        ;

        FileUtils.copyFile(HR_SOURCE_FILE_LAB_3_5_UPDATE_4, hrTargetFile);
        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());

        showUser("000991")
                .assertActivationStateEquals("Disabled")
                .selectProjectionsPanel()
                    .assertProjectionDisabled("epresleigh", CSV_1_RESOURCE_NAME)
                    .assertProjectionDisabled("", CSV_2_RESOURCE_NAME)
                    .assertProjectionDisabled("", CSV_3_RESOURCE_NAME)
                    .assertProjectionDisabled("", CORPORATE_DIRECTORY_RESOURCE_NAME);
    }
}
