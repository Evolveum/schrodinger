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
import com.evolveum.midpoint.schrodinger.page.configuration.ImportObjectPage;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ImportTest extends AbstractSchrodingerTest {

    @Test
    public void test0010importXml() throws IOException {
        File user = new File("./src/test/resources/objects/users/user.xml");
        String xml = FileUtils.readFileToString(user, StandardCharsets.UTF_8);

        ImportObjectPage importObject = basicPage.importObject();
        importObject
                .checkKeepOid()
                .checkOverwriteExistingObject()
                .getObjectsFromEmbeddedEditor()
                .setEditorXmlText(xml.replaceAll("\n", " ").trim());

        importObject.clickImportXmlButton()
                .feedback()
                        .assertSuccess();
    }
}
