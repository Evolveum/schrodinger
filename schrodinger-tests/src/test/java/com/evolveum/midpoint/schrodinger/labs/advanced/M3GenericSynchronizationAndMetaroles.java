package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class M3GenericSynchronizationAndMetaroles extends AbstractAdvancedLabTest {
    private static final String LAB_OBJECTS_DIRECTORY = ADVANCED_LABS_DIRECTORY + "M3/";
    private static final String M3_LAB_SOURCES_DIRECTORY = LAB_OBJECTS_DIRECTORY + "sources/";

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

    private static final File RIMSY_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/rimsy-user.xml");
    private static final File SEQUENCE_MEALCARD_FILE = new File(LAB_OBJECTS_DIRECTORY + "sequences/sequence-mealcard.xml");
    private static final File HR_SYNCHRONIZATION_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/hr-synchronization.xml");
    private static final File INITIAL_IMPORT_FROM_HR_TASK_FILE = new File(LAB_OBJECTS_DIRECTORY + "tasks/initial-import-from-hr.xml");
    private static final File ROLE_INTERNAL_EMPLOYEE_FILE = new File(LAB_OBJECTS_DIRECTORY + "roles/role-internal-employee.xml");
    private static final File KIRK_USER_FILE = new File(LAB_OBJECTS_DIRECTORY + "users/kirk-user.xml");

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

        contractorsTargetFile = new File(getTestTargetDir(), CONTRACTORS_FILE_SOURCE_NAME);
        FileUtils.copyFile(CONTRACTORS_SOURCE_FILE, contractorsTargetFile);

        addResourceFromFileAndTestConnection(CSV_1_SIMPLE_RESOURCE_FILE, CSV_1_RESOURCE_NAME, csv1TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_2_RESOURCE_FILE, CSV_2_RESOURCE_NAME, csv2TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CSV_3_RESOURCE_FILE, CSV_3_RESOURCE_NAME, csv3TargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(CONTRACTORS_RESOURCE_FILE, CONTRACTORS_RESOURCE_NAME, contractorsTargetFile.getAbsolutePath());
        addResourceFromFileAndTestConnection(HR_RESOURCE_FILE, HR_RESOURCE_NAME, hrTargetFile.getAbsolutePath());

        addObjectFromFile(INITIAL_IMPORT_FROM_HR_TASK_FILE);
        Selenide.sleep(MidPoint.TIMEOUT_LONG_20_S);
        addObjectFromFile(HR_SYNCHRONIZATION_TASK_FILE);

    }

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(RIMSY_USER_FILE,   //todo we need to take rimsy user xml after all M2 tests are executed.
                SEQUENCE_MEALCARD_FILE, KIRK_USER_FILE);
    }

    @Test(groups={"advancedM2"})
    public void mod03test01sequences() throws IOException {
        addObjectFromFile(ROLE_INTERNAL_EMPLOYEE_FILE);

        showUser("kirk")
                .checkReconcile()
                .clickSave()
                .feedback()
                    .assertSuccess();

        showUser("kirk")
                .selectTabBasic()
                    .form()
                        .assertPropertyInputValueContainsText("Meal Card Number", "1001")
                        .and()
                    .and()
                .selectTabProjections()
                    .table()
                        .clickByName("jkirk")
                            .assertPropertyInputValue("Meal Card number", "1001");

        showRole("Internal Employee")
                .selectTabMembers()
                    .membersPanel()
                        .table()
                            .recompute()
                                .clickYes();

        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M);
        basicPage.listUsers("Internal Employees")
                .table()
                    .clickByName("jsmith")
                        .selectTabBasic()
                            .form()
                                .assertPropertyInputValueContainsText("Meal Card Number", "10");
    }
}
