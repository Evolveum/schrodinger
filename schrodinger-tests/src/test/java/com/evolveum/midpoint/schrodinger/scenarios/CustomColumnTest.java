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

import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.modal.ExportPopupPanel;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.screenshot;

/**
 * Created by honchar
 */
public class CustomColumnTest extends AbstractSchrodingerTest {

    private static final File CUSTOM_COLUMNS_OBJECT_COLLECTION_SIMPLE_FILE = new File("./src/test/resources/objects/objectcollections/object-collection-custom-columns-simple.xml");
    private static final File CUSTOM_COLUMNS_OBJECT_COLLECTION_KEY_LABELS_FILE = new File("./src/test/resources/objects/objectcollections/object-collection-custom-columns-key-labels.xml");
    private static final File CUSTOM_COLUMNS_SYSTEM_CONFIGURATION = new File("./src/test/resources/objects/systemconfig/system-configuration-custom-columns.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(CUSTOM_COLUMNS_OBJECT_COLLECTION_SIMPLE_FILE, CUSTOM_COLUMNS_SYSTEM_CONFIGURATION,
                CUSTOM_COLUMNS_OBJECT_COLLECTION_KEY_LABELS_FILE);
    }

    @Test(priority = 1)
    public void test00100checkUserCustomColumns() {
        basicPage.listUsers("Custom columns view")
                .table()
                    .assertColumnIndexMatches("Name (custom)", 3);
        basicPage.listUsers("Custom columns view")
                .table()
                    .assertColumnIndexMatches("Role membership", 4);
        basicPage.listUsers("Custom columns view")
                .table()
                    .assertColumnIndexMatches("Preferred language", 5);
    }

    @Test(priority = 2)
    public void test00200checkUserCustomColumnsKeyLabels() {
        ListUsersPage usersPage = basicPage.listUsers("Custom columns label test");
        usersPage
                .table()
                    .assertColumnIndexMatches("Enable", 3);
        usersPage
                .table()
                    .assertColumnIndexMatches("Disable", 4);
        usersPage
                .table()
                    .assertColumnIndexMatches("Unlink", 5);
    }

    @Test(priority = 3)
    public void test00300checkExportColumns() {
        ListUsersPage usersPage = basicPage.listUsers("Custom columns view");
        Table<ExportPopupPanel<ListUsersPage>> exportTable = usersPage.table()
                .clickExportButton()
                    .table();
        screenshot("exportTable");
        exportTable
                .assertTableRowExists("Column name", "Name (custom)")
                .assertTableRowExists("Column name", "Role membership")
                .assertTableRowExists("Column name", "Preferred language");
    }
}
