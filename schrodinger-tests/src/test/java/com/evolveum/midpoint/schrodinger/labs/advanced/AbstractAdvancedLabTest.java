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

    @Override
    protected File getExtensionSchemaFile() {
        return EXTENSION_SCHEMA_FILE;
    }

    @Override
    protected String getPostInitialObjectsFolderPath() {
        return POST_INITIAL_OBJECTS_DIR;
    }

}
