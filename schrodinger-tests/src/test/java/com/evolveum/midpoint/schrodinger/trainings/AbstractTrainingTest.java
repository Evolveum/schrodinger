/*
 * Copyright (c) 2023 Evolveum
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
package com.evolveum.midpoint.schrodinger.trainings;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class AbstractTrainingTest extends AbstractSchrodingerTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTrainingTest.class);
    protected static final String RESOURCES_DIRECTORY = "./src/test/resources/";
    protected static final String FIRST_STEPS_DIRECTORY = RESOURCES_DIRECTORY + "first-steps/";
    protected static final String FIRST_STEPS_RESOURCES_DIRECTORY = FIRST_STEPS_DIRECTORY + "resources/";
    protected static final String CSV_HR_FILE_SOURCE_NAME = "hr.csv";
    protected static final File HR_CSV_SOURCE_FILE = new File(FIRST_STEPS_RESOURCES_DIRECTORY + "hr.csv");
    protected static final String MODULE_2_GROUP = "Module2";
    protected static final String MODULE_3_GROUP = "Module3";

    protected static File hrCsvFile;

    protected File getTestTargetDir() throws IOException {
        return new File(fetchMidpointHome(), "sources");
    }

    @Override
    protected boolean resetToDefaultAfterTests() {
        return false;
    }
}
