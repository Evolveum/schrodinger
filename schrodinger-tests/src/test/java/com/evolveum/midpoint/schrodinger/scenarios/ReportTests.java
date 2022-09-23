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

import com.evolveum.midpoint.schrodinger.component.report.ReportTable;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

/**
 * Created by honchar
 */
public class ReportTests extends AbstractSchrodingerTest {

    @Test
    public void test00100createReport() {
        basicPage.listReports()
        .newObjectButtonClick("New collection report")
            .selectBasicPanel()
                .form()
                    .addAttributeValue("Name", "TestReport")
                    .and()
                .and()
            .clickSave()
            .feedback()
            .assertSuccess();
        basicPage.listReports()
            .table()
                .search()
                    .byName()
                    .inputValue("TestReport")
                    .updateSearch()
                    .and()
                .assertTableObjectsCountEquals(1);
        basicPage.listReports().table().assertTableContainsText("TestReport");
    }

    @Test
    public void test00200runUsersReport() {
        ReportTable reportTable = basicPage.listReports().table();
        reportTable
                .search()
                    .resetBasicSearch();
        reportTable
                .runReport("All audit records report") //model is shown
                .runReport(); //confirm report run
        basicPage.listReports("Collection reports")
            .table()
                .search()
                    .byName()
                    .inputValue("All audit records report")
                    .updateSearch()
                    .and()
                .assertTableObjectsCountEquals(1);
    }
}
