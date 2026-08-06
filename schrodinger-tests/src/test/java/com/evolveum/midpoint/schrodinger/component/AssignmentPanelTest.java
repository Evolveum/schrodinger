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
package com.evolveum.midpoint.schrodinger.component;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssignmentPanelTest extends AbstractSchrodingerTest {

    private static final File ASSIGNMENTS_COUNT_TEST_USER = new File("./src/test/resources/objects/assignment/assignments-count-test.xml");
    private static final File ARCHETYPE_APPLICATION = new File("./src/test/resources/objects/archetypes/archetype-application.xml");
    private static final File ARCHETYPE_LICENSE = new File("./src/test/resources/objects/archetypes/archetype-licence.xml");
    private static final File SYSTEM_CONFIG_WITH_LICENSE_OBJ_COL_VIEW = new File("./src/test/resources/objects/systemconfiguration/system-configuration-with-license-obj-col-view.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ASSIGNMENTS_COUNT_TEST_USER,
                ARCHETYPE_LICENSE, ARCHETYPE_APPLICATION);
    }

    @Test
    public void test0010assignmentsCountMatchesTableRowsCount() {
        basicPage
                .listUsers()
                    .table()
                        .search()
                            .byName()
                            .inputValue("assignmentsCountTest")
                            .updateSearch()
                        .and()
                    .clickByName("assignmentsCountTest")
                        .selectAssignmentsPanel()
                            .assertAssignmentsCountLabelEquals("1")
                            .table()
                                .assertVisibleObjectsCountEquals(1);
        basicPage
                .listUsers()
                    .table()
                        .search()
                            .byName()
                            .inputValue("userForDelegation")
                            .updateSearch()
                        .and()
                    .clickByName("userForDelegation")
                        .selectAssignmentsPanel()
                            .assertAssignmentsCountLabelEquals("0")
                            .table()
                                .assertVisibleObjectsCountEquals(0);

    }

    //covers #11914
    @Test (enabled = false)
    public void test0020assignmentWithHolderArchetypeRef() {
        importObject(SYSTEM_CONFIG_WITH_LICENSE_OBJ_COL_VIEW);
        reloginAsAdministrator();

        basicPage
                .listOrgs("Licenses")
                .newObjectButtonClick("New Licence")
                .selectAssignmentsPanel()
                .clickAddAssignment("Application (Default)");
    }
}
