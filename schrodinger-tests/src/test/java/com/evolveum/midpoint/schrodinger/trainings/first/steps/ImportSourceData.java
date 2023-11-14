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
                .assertTableContainsColumnWithValue("SimulationResultProcessedObjectType.state", "Added");
    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return false;
    }
}
