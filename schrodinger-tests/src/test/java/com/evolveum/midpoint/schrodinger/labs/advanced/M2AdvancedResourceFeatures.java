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

import java.io.File;

/**
 * @author honchar
 */
public class M2AdvancedResourceFeatures extends AbstractAdvancedLabTest {
    protected static final String LAB_OBJECTS_DIRECTORY = LAB_ADVANCED_DIRECTORY + "M2/";

    private static final File CONTRACTORS_RESOURCE_FILE = new File(LAB_OBJECTS_DIRECTORY + "resources/localhost-contractors.xml");
    private static final File OBJECT_COLLECTION_EMP_WITHOUT_TELEPHONE_FILE = new File(LAB_OBJECTS_DIRECTORY + "objectcollections/objectCollection-employees-without-telephone.xml");
    private static final File SYSTEM_CONFIGURATION_FILE_1_4 = new File(LAB_OBJECTS_DIRECTORY + "systemconfiguration/system-configuration-1-4.xml");

}
