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
package com.evolveum.midpoint.schrodinger.page.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;

import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import static com.evolveum.midpoint.schrodinger.util.Utils.setOptionCheckedByName;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ImportObjectPage extends BasicPage {

    public ImportObjectPage checkProtectedByEncryption() {
        setOptionCheckedByName("importOptions:protectedByEncryption:check", true);
        return this;
    }

    public ImportObjectPage checkFetchResourceSchema() {
        setOptionCheckedByName("importOptions:fetchResourceSchema:check", true);
        return this;
    }

    public ImportObjectPage checkKeepOid() {
        setOptionCheckedByName("importOptions:keepOid:check", true);
        return this;
    }

    public ImportObjectPage checkOverwriteExistingObject() {
        setOptionCheckedByName("importOptions:overwriteExistingObject:check", true);
        return this;
    }

    public ImportObjectPage checkReferentialIntegrity() {
        setOptionCheckedByName("importOptions:referentialIntegrity:check", true);
        return this;
    }

    public ImportObjectPage checkSummarizeSuccesses() {
        setOptionCheckedByName("importOptions:summarizeSuccesses:check", true);
        return this;
    }

    public ImportObjectPage checkValidateDynamicSchema() {
        setOptionCheckedByName("importOptions:validateDynamicSchema:check", true);
        return this;
    }

    public ImportObjectPage checkValidateStaticSchema() {
        setOptionCheckedByName("importOptions:validateStaticSchema:check", true);
        return this;
    }

    public ImportObjectPage checkSummarizeErrors() {
        setOptionCheckedByName("importOptions:summarizeErrors:check", true);
        return this;
    }

    public ImportObjectPage uncheckProtectedByEncryption() {
        setOptionCheckedByName("importOptions:protectedByEncryption:check", false);
        return this;
    }

    public ImportObjectPage uncheckFetchResourceSchema() {
        setOptionCheckedByName("importOptions:fetchResourceSchema:check", false);
        return this;
    }

    public ImportObjectPage uncheckKeepOid() {
        setOptionCheckedByName("importOptions:keepOid:check", false);
        return this;
    }

    public ImportObjectPage uncheckOverwriteExistingObject() {
        setOptionCheckedByName("importOptions:overwriteExistingObject:check", false);
        return this;
    }

    public ImportObjectPage uncheckReferentialIntegrity() {
        setOptionCheckedByName("importOptions:referentialIntegrity:check", false);
        return this;
    }

    public ImportObjectPage uncheckSummarizeSuccesses() {
        setOptionCheckedByName("importOptions:summarizeSuccesses:check", false);
        return this;
    }

    public ImportObjectPage uncheckValidateDynamicSchema() {
        setOptionCheckedByName("importOptions:validateDynamicSchema:check", false);
        return this;
    }

    public ImportObjectPage uncheckValidateStaticSchema() {
        setOptionCheckedByName("importOptions:validateStaticSchema:check", false);
        return this;
    }

    public ImportObjectPage uncheckSummarizeErrors() {
        setOptionCheckedByName("importOptions:summarizeErrors:check", false);
        return this;
    }

    public ImportObjectPage stopAfterErrorsExceed(Integer count) {
        String c = count == null ? "" : count.toString();
        $(By.name("importOptions:errors")).setValue(c);
        return this;
    }

    public ImportObjectPage getObjectsFromFile() {
        $(By.name("importRadioGroup")).selectRadio("radio0");
        return this;
    }

    public ImportObjectPage getObjectsFromEmbeddedEditor() {
        $(By.name("importRadioGroup")).selectRadio("radio1");
        return this;
    }

    public ImportObjectPage chooseFile(File file) {

        $(By.name("input:inputFile:fileInput")).uploadFile(file);
        //todo implement
        return this;
    }

    public ImportObjectPage setEditorXmlText(String text) {
        SelenideElement xmlButton = $(byText("XML"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        String xmlButtonClass = xmlButton.getAttribute("class");
        if (StringUtils.isNotEmpty(xmlButtonClass) && !xmlButtonClass.contains("active")) {
            xmlButton.click();
            xmlButton.shouldBe(Condition.cssClass("active"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        SelenideElement aceEditor = $(By.className("ace_text-input"));
        aceEditor.sendKeys(text);

        return this;
    }

    public ImportObjectPage clickImportFileButton() {
        $(".main-button-bar").$x(".//a[@data-s-id='importFileButton']").click();
        return this;
    }

    public ImportObjectPage clickImportXmlButton() {
        $(".main-button-bar").$x(".//a[@data-s-id='importXmlButton']").click();
        return this;
    }

}
