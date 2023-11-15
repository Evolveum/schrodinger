/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.trainings.first.steps;

import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

public class ImportSourceData extends AbstractTrainingTest {

    private static final String STATUS_EXPRESSION_SCRIPT_VALUE = "switch (input) {\n" +
            "    case 'In':\n" +
            "        'active'\n" +
            "        break\n" +
            "\n" +
            "    case 'Long-term leave':\n" +
            "        'suspended'\n" +
            "        break\n" +
            "\n" +
            "    case 'Former employee':\n" +
            "        'archived'\n" +
            "        break\n" +
            "\n" +
            "    //default:\n" +
            "       //'suspended'\n" +
            "       //break\n" +
            "}\n";

    @Test(groups = MODULE_3_GROUP)
    public void singleSourceSystemEntryImportSimulation() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .importPreview("Name", "1001")
                .selectTaskExecutionMode("Simulated development")
                .select()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .assertTableContainsText("Focus activated")
                .assertTableContainsColumnWithValue("SimulationResultProcessedObjectType.type", "User")
                .assertTableContainsColumnWithValue("SimulationResultProcessedObjectType.state", "Added")
                .clickByName("1001")
                .changes()
                .header()
                .assertChangeTypeEquals("Add")
                .assertChangedObjectTypeEquals("User")
                .assertChangedObjectNameEquals("1001")
                .and()
                .assertItemsDeltasSizeEquals(5)
                .assertItemValueEquals("Name", "1001")
                //todo check other item values
                .assertItemValueNotPresent("Locality")
                .assertItemValueNotPresent("Lifecycle status")
                .and()
                .back();

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureMappings()
                .inboundMappings()
                .addInbound()
                .name("locality-to-locality")
                .fromResourceAttribute("locality")
                .expression("As is")
                .target("locality")
                .lifecycleState("Active")
                .and()
                .addInbound()
                .name("status-to-lifecycle-state")
                .fromResourceAttribute("status")
                .scriptExpression("Groovy (default)", STATUS_EXPRESSION_SCRIPT_VALUE)
                .target("lifecycleState")
                .lifecycleState("Active")
                .and()
                .and()
                .saveMappings()
                .table()
                .search()
                .byName()
                .inputValue("1001")
                .updateSearch()
                .and()
                .importPreview("Name", "1001")
                .selectTaskExecutionMode("Simulated development")
                .select()
                .table()
                .clickByName("1001")
                .changes()
                .header()
                .assertChangeTypeEquals("Add")
                .assertChangedObjectTypeEquals("User")
                .assertChangedObjectNameEquals("1001")
                .and()
                .assertItemsDeltasSizeEquals(5) //todo
                .assertItemValueEquals("Locality", "Small Red Rock City")
                .assertItemValueEquals("Lifecycle state", "active")
                .and()
                .back();

        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .setLifecycleState("Active (production)");
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return false;
    }
}
