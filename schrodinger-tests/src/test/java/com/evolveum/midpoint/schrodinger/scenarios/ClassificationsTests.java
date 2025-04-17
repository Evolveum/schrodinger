/*
 * Copyright (c) 2025 Evolveum and contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
import java.util.Arrays;
import java.util.List;

public class ClassificationsTests extends AbstractSchrodingerTest {

    private static final File PUBLIC_WEBSITE_SERVICE_FILE = new File("src/test/resources/objects/service/app-website.xml");
    private static final File TLP_CLEAR_POLICY_FILE = new File("src/test/resources/objects/policy/classification-tlp-clear.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(PUBLIC_WEBSITE_SERVICE_FILE, TLP_CLEAR_POLICY_FILE);
    }

    @Test
    public void test00100classificationsPanelTableShowsClassifications() {
        basicPage
                .listServices()
                .table()
                .search()
                    .byName()
                    .inputValue("Public Website")
                    .updateSearch()
                    .and()
                .clickByName("Public Website")
                .selectClassificationsPanel()
                .table()
                    .assertVisibleObjectsCountEquals(1)
                    .assertTableContainsText("TLP:CLEAR");
    }
}
