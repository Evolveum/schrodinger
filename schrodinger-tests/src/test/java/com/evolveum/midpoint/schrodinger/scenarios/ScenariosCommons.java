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
package com.evolveum.midpoint.schrodinger.scenarios;

import java.io.File;

public class ScenariosCommons {

    protected static final File CSV_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups-authoritative.csv");
    protected static final String CSV_SOURCE_OLDVALUE = "target/midpoint.csv";
    protected static final File CSV_INITIAL_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups-authoritative-initial.csv");
    protected static final File CSV_UPDATED_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups-authoritative-updated.csv");
    protected static final File RESOURCE_CSV_GROUPS_AUTHORITATIVE_FILE = new File("./src/test/resources/objects/resources/resource-csv-groups-authoritative.xml");
    protected static final File SYSTEM_CONFIGURATION_INITIAL_FILE = new File("./src/test/resources/objects/systemconfiguration/000-system-configuration.xml");
    protected static final File USER_TEST_RAPHAEL_FILE = new File("./src/test/resources/objects/users/user-raphael.xml");

    protected static final String RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME = "CSV (target with groups) authoritative";
    protected static final String CSV_RESOURCE_NAME= "Test CSV: username";

    protected static final String TEST_USER_DON_NAME= "donatello";
    protected static final String TEST_USER_PROTECTED_NAME= "chief";
    protected static final String TEST_USER_RAPHAEL_NAME = "raphael";

    public static final String CSV_RESOURCE_ATTR_FILE_PATH= "File path";

}
