package com.evolveum.midpoint.schrodinger.labs.advanced;

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

    @Test(groups={"advancedM5"})
    public void mod05test01passwordHashingAndAccountActivation() throws IOException {
        addResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(HR_ORG_RESOURCE_FILE, HR_ORGS_RESOURCE_NAME, hrOrgsTargetFile.getAbsolutePath());

        addObjectFromFile(OPENLDAP_NEW_CORPORATE_DIRECTORY_RESOURCE_FILE);
        addObjectFromFile(SECURITY_POLICY_HASHING_FILE);

    }
}
