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

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by honchar
 */
public class AssignmentArchetypeTest extends AbstractSchrodingerTest {

    private static final File ARCHETYPE_BUSINESS_ROLE_FILE = new File("src/test/resources/objects/archetypes/archetype-business-role.xml");
    private static final String RELATIONS_CONTAINER_HEADER_KEY = "Relations";
    private static final String RELATION_CONTAINER_HEADER_KEY = "Relation";
    private static final String NEWLY_ADDED_RELATION_CONTAINER_HEADER_KEY = "RelationDefinitionType.details";

    @Test(priority = 0)
    public void test0010importArchetypeBusinessRole() {
        addObjectFromFile(ARCHETYPE_BUSINESS_ROLE_FILE);
    }

    @Test
    public void test0020configureRelationDefinitions(){
        //TODO wait till MID-5144 fix
//        basicPage
//                .roleManagement()
//                    .form()
//                        .expandContainerPropertiesPanel(RELATIONS_CONTAINER_HEADER_KEY)
//                        .addNewContainerValue(RELATION_CONTAINER_HEADER_KEY, NEWLY_ADDED_RELATION_CONTAINER_HEADER_KEY)
//                        .expandContainerPropertiesPanel(NEWLY_ADDED_RELATION_CONTAINER_HEADER_KEY)

    }
}
