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
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;

/**
 * @author honchar
 */
public class AbstractAdvancedLabTest extends AbstractLabTest {

    protected static final File EXTENSION_SCHEMA_FILE = new File(ADVANCED_LABS_DIRECTORY + "schema/extension-example.xsd");
    protected static final String POST_INITIAL_OBJECTS_DIR = ADVANCED_LABS_DIRECTORY + "post-initial-objects";
    protected static final String CONTRACTORS_FILE_SOURCE_NAME = "contractors.csv";
    protected static File contractorsTargetFile;

    protected static final String OPENLDAP_CORPORATE_RESOURCE_NAME = "New Corporate Directory";
    protected static final String CONTRACTORS_RESOURCE_NAME = "ExAmPLE, Inc. Contractor DB";
    protected static final String CONTRACTORS_RESOURCE_IMPORT_TASK_NAME = "Initial import from Contractor DB";

    protected static File hrOrgsTargetFile;
    protected static final String HR_ORGS_FILE_SOURCE_NAME = "source-orgs.csv";
    protected static final String HR_ORGS_RESOURCE_NAME = "ExAmPLE, Inc. HR Organization Structure Source";
    protected static File notificationFile;
    protected static final String EXAMPLE_MAIL_NOTIFICATIONS_FILE_NAME = "example-mail-notifications.log";

    @Override
    protected File getExtensionSchemaFile() {
        return EXTENSION_SCHEMA_FILE;
    }

    @Override
    protected String getPostInitialObjectsFolderPath() {
        return POST_INITIAL_OBJECTS_DIR;
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        notificationFile = new File(getTestTargetDir(), EXAMPLE_MAIL_NOTIFICATIONS_FILE_NAME);
        if (!notificationFile.exists()) {
            notificationFile.createNewFile();
        }

        File systemConfig = getModuleInitialSystemConfigXml();
        if (systemConfig != null) {
            addObjectFromFile(Utils.changeAttributeIfPresent(systemConfig, "redirectToFile",
                    notificationFile.getAbsolutePath()));
        }
    }

    protected File getModuleInitialSystemConfigXml() {
        return null;
    }

}
