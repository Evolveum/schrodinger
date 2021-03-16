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
package com.evolveum.midpoint.schrodinger.page;

import com.evolveum.midpoint.schrodinger.page.configuration.BulkActionsPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

/**
 * Created by Kate Honchar
 */
public class BulkActionsTest extends AbstractSchrodingerTest {

    private static final String PARSING_ERROR_MESSAGE = "Couldn't parse bulk action object";

    @Test
    public void wrongBulkActionXmlExecution(){
        BulkActionsPage bulkActionsPage = basicPage.bulkActions();
        bulkActionsPage
                .insertOneLineTextIntoEditor("<objects></objects>")
                .startButtonClick()
                .feedback()
                .assertError()
                .assertMessageExists(PARSING_ERROR_MESSAGE);

        bulkActionsPage.assertAceEditorVisible();
    }
}
