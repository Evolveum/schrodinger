package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class M5AdvancedSecurityFeatures extends AbstractAdvancedLabTest {

    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M4/";
    private static final String M3_LAB_SOURCES_DIRECTORY = LAB_OBJECTS_DIRECTORY + "sources/";

    private static final File HR_ORG_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source-orgs.csv");
    private static final File CSV_1_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-1.csv");
    private static final File CSV_2_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-2.csv");
    private static final File CSV_3_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "csv-3.csv");
    private static final File HR_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "source.csv");
    private static final File CONTRACTORS_SOURCE_FILE = new File(M3_LAB_SOURCES_DIRECTORY + "contractors.csv");

    private static final File CSV_1_SIMPLE_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-1-document-access.xml");
    private static final File CSV_2_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-2-canteen.xml");
    private static final File CSV_3_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-csvfile-3-ldap.xml");
    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File HR_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr.xml");
    private static final File HR_ORG_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-hr-org.xml");
    private static final File OPENLDAP_NEW_CORPORATE_DIRECTORY_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/openldap-new-corporate-directory.xml");
    private static final File SECURITY_POLICY_HASHING_FILE = new File(LAB_OBJECTS_DIRECTORY + "securityPolicies/example-security-policy-hashing.xml");
    private static final File SYSTEM_CONFIGURATION_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration.xml");
    private static final File SYSTEM_CONFIGURATION_5_1_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-5-1.xml");
    private static final File SYSTEM_CONFIGURATION_5_1_UPDATE_2_FILE = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-5-1-update-2.xml");

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

    @Test(groups={"advancedM5"})
    public void mod05test01passwordHashingAndAccountActivation() throws IOException {
        addCsvResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        addCsvResourceFromFileAndTestConnection(HR_ORG_RESOURCE_FILE, HR_ORGS_RESOURCE_NAME, hrOrgsTargetFile.getAbsolutePath());

        addObjectFromFile(OPENLDAP_NEW_CORPORATE_DIRECTORY_RESOURCE_FILE);
        addObjectFromFile(SECURITY_POLICY_HASHING_FILE);
        addObjectFromFile(Utils.changeAttributeIfPresent(SYSTEM_CONFIGURATION_5_1_FILE, "redirectToFile",
                fetchTestHomeDir() + "/example-mail-notifications.log", fetchTestHomeDir()));

        basicPage.listRepositoryObjects()
                .table()
                    .showObjectInTableByTypeAndName("User", "picard")
                        .clickByName("picard")
                            .assertObjectXmlContainsText("<t:algorithm>http://www.w3.org/2001/04/xmlenc#aes256-cbc</t:algorithm>");
        UserPage userPage = showUser("picard");
        userPage
                .selectTabProjections()
                    .table()
                        .assertTableObjectsCountEquals(0)
                        .and()
                    .and()
                .selectTabBasic()
                    .form()
                        .addPasswordAttributeValue("qwerty12345XXXX");
        userPage.clickSave()
                .feedback()
                    .assertSuccess();
        basicPage.listRepositoryObjects()
                .table()
                    .showObjectInTableByTypeAndName("User", "picard")
                        .clickByName("picard")
                            .assertObjectXmlContainsText("<t:hashedData>")
                            .assertObjectXmlContainsText("<t:algorithm>http://prism.evolveum.com/xml/ns/public/crypto/algorithm/pbkd-3#PBKDF2WithHmacSHA512</t:algorithm>");
        showUser("badobi")
                .selectTabBasic()
                    .form()
                        .addPasswordAttributeValue("qwerty12345XXXX")
                        .and()
                    .and()
                .clickSave()
                    .feedback()
                        .assertSuccess();
        AssignmentsTab<UserPage> assignmentsTab = showUser("badobi").selectTabAssignments();
        Utils.addPredefinedAssignmentByTitle(assignmentsTab, "Member",
                "New Organization type assignment with default relation", "IT Administration Department");
        Utils.addAssignmentsWithRelationAndSave(assignmentsTab, "", true, "Basic user");

        basicPage.loggedUser().logout();
        FormLoginPage loginPage = midPoint.formLogin();
        loginPage.login("badobi", "qwerty12345XXXX")
                .assertUserMenuExist();
        basicPage.requestRole()
                .selectRoleCatalogViewTab()
                    .getRoleCatalogHierarchyPanel()
                        .selectOrgInTree("Application bundles")
                        .and()
                    .getItemsPanel()
                        .addItemToCart("Secret Projects I")
                        .and()
                    .goToShoppingCart()
                        .setRequestComment("Please approve, I need to work on the X911 project")
                        .clickRequestButton();

        basicPage.loggedUser().logout();
        loginPage = midPoint.formLogin();
        loginPage.login(getUsername(), getPassword())
                .assertUserMenuExist();
        basicPage.allItems()
                .table()
                    .approveWorkitemByName("badobi");   //first stage approval
        basicPage.allItems()
                .table()
                    .approveWorkitemByName("badobi");   //second stage approval
        basicPage.allItems()
                .table()
                    .approveWorkitemByName("badobi");   //third stage approval
        showUser("badobi")
                .selectTabAssignments()
                    .assertAssignmentsWithRelationExist("Member", "Secret Projects I")
                    .and()
                .selectTabProjections()
                    .assertProjectionExist("badobi", "CSV-1 (Document Access)")
                    .assertProjectionForResourceDoesntExist("New Corporate Directory");
    }
}
