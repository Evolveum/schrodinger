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

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class CleanupPoliciesTest extends AbstractSchrodingerTest {

    public static final File CLOSED_TASKS_XML = new File("./src/test/resources/objects/tasks/closed-tasks.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Collections.singletonList(CLOSED_TASKS_XML);
    }

    @Test
    public void test00100closedTasksCleanup() {
        basicPage
                .listTasks()
                    .table()
                        .search()
                            .byName()
                            .inputValue("ClosedTask")
                            .updateSearch()
                            .and()
                        .assertCurrentTableContains("ClosedTask1")
                        .assertCurrentTableContains("ClosedTask2")
                    .and()
                .cleanupPolicy()
                    .closedTasksCleanupInterval("P6M")
                    .and()
                .clickSave()
                .feedback()
                    .assertSuccess()
                    .and()
                .listTasks()
                    .table()
                        .search()
                            .byName()
                            .inputValue("Cleanup")
                            .updateSearch()
                            .and()
                        .clickByName("Cleanup")
                            .clickRunNow();
        Selenide.sleep(MidPoint.TIMEOUT_LONG_1_M.getSeconds());
        basicPage
                .listTasks()
                .table()
                .assertCurrentTableDoesntContain("ClosedTask1")
                .assertCurrentTableDoesntContain("ClosedTask2");
    }
}
