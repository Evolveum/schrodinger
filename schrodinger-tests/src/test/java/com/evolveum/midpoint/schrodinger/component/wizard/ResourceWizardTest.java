/*
 * Copyright (c) 2025 Evolveum
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
package com.evolveum.midpoint.schrodinger.component.wizard;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.ImportOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResourceWizardTest extends AbstractSchrodingerTest {

    private static final File RESOURCE_FILE = new File("./src/test/resources/objects/resources/resource-template.xml");
    private static final File RESOURCE_CSV_FILE = new File("./src/test/resources/sources/midpoint-username.csv");
    private static final String RESOURCE_TEMPLATE_NAME = "Resource template";
    private static final String RESOURCE_NEW_NAME = "NewResourceFromTemplate";
    private static File hrCsvFile;

    @BeforeClass(alwaysRun = true, dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();
        hrCsvFile = copyTestFileToMidpointHome("resources", RESOURCE_CSV_FILE);
    }


    @Override
    protected List<String> createImportOptionList() {
        return new ImportOptions(false, true).createOptionList();
    }

    @Override
    protected List<File> getObjectListToImport(){
        return List.of(RESOURCE_FILE);
    }

    //covers #10476
    @Test
    public void test0010createResourceFromTemplate() {
        String csvFilePath = hrCsvFile.getAbsolutePath();
        basicPage
                .newResource()
                .copyFromTemplate(RESOURCE_TEMPLATE_NAME)
                .name(RESOURCE_NEW_NAME)
                .next()
                .filePath(csvFilePath)
                .next()
                .uniqueAttributeName("username")
                .nameAttribute("username")
                .userPasswordAttributeName("password")
                .next()
                .createResource()
                .assertResourceIsCreated(RESOURCE_NEW_NAME);
    }

}
