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

package com.evolveum.midpoint.schrodinger.labs.advanced;

import com.evolveum.midpoint.schrodinger.labs.AbstractLabTest;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
/**
 * @author honchar
 */
public class AbstractAdvancedLabTest extends AbstractLabTest {

    protected static final File EXTENSION_SCHEMA_FILE = new File(LAB_ADVANCED_DIRECTORY + "schema/extension-example.xsd");
    protected static final String POST_INITIAL_OBJECTS_DIR = LAB_ADVANCED_DIRECTORY + "post-initial-objects";

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextBeforeTestClass" })
    @Override
    protected void springTestContextPrepareTestInstance() throws Exception {
        String home = System.getProperty("midpoint.home");
        File mpHomeDir = new File(home);
        if (!mpHomeDir.exists()) {
            super.springTestContextPrepareTestInstance();
        }

        File schemaDir = new File(home, "schema");
        if (!schemaDir.mkdir()) {
            if (schemaDir.exists()) {
                FileUtils.cleanDirectory(schemaDir);
            } else {
                throw new IOException("Creation of directory \"" + schemaDir.getAbsolutePath() + "\" unsuccessful");
            }
        }
        File schemaFile = new File(schemaDir, EXTENSION_SCHEMA_NAME);
        FileUtils.copyFile(EXTENSION_SCHEMA_FILE, schemaFile);

        File postInitObjectsDir = new File(home, "post-initial-objects");
        if (!postInitObjectsDir.mkdir()) {
            if (postInitObjectsDir.exists()) {
                FileUtils.cleanDirectory(postInitObjectsDir);
            } else {
                throw new IOException("Creation of directory \"" + postInitObjectsDir.getAbsolutePath() + "\" unsuccessful");
            }
        }
        File postInitObjectsSourceDir = new File(POST_INITIAL_OBJECTS_DIR);
        File[] objList = postInitObjectsSourceDir.listFiles();
        Arrays.sort(objList);
        for (File postInitFile : objList) {
            File objFile = new File(postInitObjectsDir, postInitFile.getName());
            FileUtils.copyFile(postInitFile, objFile);
        }

        super.springTestContextPrepareTestInstance();
    }


}
