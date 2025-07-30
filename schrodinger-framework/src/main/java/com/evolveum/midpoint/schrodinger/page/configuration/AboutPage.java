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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.modal.ConfirmationModal;
import com.evolveum.midpoint.schrodinger.component.common.table.ReadOnlyTable;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selectors.byText;

/**
 * Created by Viliam Repan (lazyman).
 */
public class AboutPage extends BasicPage {

    // public static Trace LOGGER = TraceManager.getTrace(AboutPage.class);

    public AboutPage repositorySelfTest() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testRepository")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public AboutPage checkAndRepairOrgClosureConsistency() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testRepositoryCheckOrgClosure")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public AboutPage reindexRepositoryObjects() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.reindexRepositoryObjects")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public AboutPage provisioningSelfTest() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testProvisioning")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public AboutPage cleanupActivitiProcesses() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.checkWorkflowProcesses")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public AboutPage clearCssJsCache() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.clearCssJsCache")).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    public String version() {
        return $(Schrodinger.bySchrodingerDataId("wicket_message-1130625241")).parent().getText();
    }

    public String gitDescribe() {
        return $(Schrodinger.bySchrodingerDataResourceKey("midpoint.system.build")).parent().getText();
    }

    public String buildAt() {
        return $(Schrodinger.bySchrodingerDataId("build")).parent().getText();
    }


    public boolean isNativeRepositoryImplementation() {
        try {
            SelenideElement row = $(byText("Implementation name")).parent();
            row.should(Condition.text("Native"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String connIdFrameworkVersion() {
        return $(Schrodinger.bySchrodingerDataId("provisioningDetailValue")).parent().getText();
    }

    public List<String> getJVMproperties() {
        SelenideElement jvmProperties = $(Schrodinger.byDataId("jvmProperties"));
        String jvmPropertiesText = jvmProperties.getText();

        List<String> listOfProperties = new ArrayList<>();
        if (jvmPropertiesText != null && !jvmPropertiesText.isEmpty()) {
            String[] properties = jvmPropertiesText.split("\\r?\\n");

            listOfProperties = Arrays.asList(properties);

        } else {
            // LOGGER.info("JVM properties not found";

        }

        return listOfProperties;
    }

    public String getJVMproperty(String property) {

        List<String> listOfProperties = getJVMproperties();

        if (property != null && !property.isEmpty()) {

            for (String keyPair : listOfProperties) {

                String[] pairs = keyPair.split("\\=");

                if (pairs != null && pairs.length > 1) {
                    if (pairs[0].equals(property)) {
                        return pairs[1];
                    }
                } else if (pairs.length == 1) {
                    if (pairs[0].contains(property)) {
                        return pairs[0];
                    }

                }
            }
        }

        return "";
    }


    public ConfirmationModal<FormLoginPage> clickSwitchToFactoryDefaults() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.factoryDefault")).shouldBe(Condition.visible,MidPoint.TIMEOUT_DEFAULT_2_S).click();
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return new ConfirmationModal<>(new FormLoginPage(), Utils.getModalWindowSelenideElement());
    }

    public String getSystemProperty(String propertyNameUserHome) {
        SelenideElement propertiesTable = $(Schrodinger.byElementValue("h3","System properties")).shouldBe(Condition.appear,MidPoint.TIMEOUT_DEFAULT_2_S).parent().$(By.cssSelector(".table.table-striped"));

        ReadOnlyTable readOnlyTable = new ReadOnlyTable(this,propertiesTable);
        return readOnlyTable.getParameterValue(propertyNameUserHome);
    }

    public AboutPage assertVersionValueEquals(String expectedVersionValue) {
        assertion.assertEquals(version(), expectedVersionValue, "Version value doesn't match");
        return this;
    }

    public AboutPage assertGitDescribeValueEquals(String expectedGitDescribeValue) {
        assertion.assertEquals(expectedGitDescribeValue, gitDescribe(), "Git describe value doesn't match");
        return this;
    }

    public AboutPage assertGitDescribeValueIsNotEmpty() {
        assertion.assertTrue(StringUtils.isNotEmpty(gitDescribe()), "Git describe value shouldn't be empty");
        return this;
    }

    public AboutPage assertBuildAtValueEquals(String expectedBuildAtValue) {
        assertion.assertEquals(expectedBuildAtValue, buildAt(), "Build at value doesn't match");
        return this;
    }

    public AboutPage assertBuildAtValueIsNotEmpty() {
        assertion.assertTrue(StringUtils.isNotEmpty(buildAt()), "Build at value shouldn't be empty");
        return this;
    }


    public AboutPage assertRepositoryImplementationIsNative() {
        assertion.assertTrue(isNativeRepositoryImplementation(), "Repository implementation isn't Native");
        return this;
    }

    public AboutPage assertConnIdVersionValueEquals(String expectedValue) {
        assertion.assertEquals(connIdFrameworkVersion(), expectedValue, "Connid version value doesn't match");
        return this;
    }

    public AboutPage assertJVMPropertyValueEquals(String propertyName, String expectedValue) {
        assertion.assertEquals(expectedValue, getJVMproperty(propertyName), "JVM property " + propertyName + " value doesn't match");
        return this;
    }

    public AboutPage assertJVMPropertyValueIsNotEmpty(String propertyName) {
        assertion.assertTrue(StringUtils.isNotEmpty(getJVMproperty(propertyName)), "JVM property " + propertyName + " shouldn't be empty");
        return this;
    }

    public AboutPage assertSystemPropertyValueEquals(String propertyName, String expectedValue) {
        assertion.assertEquals(expectedValue, getSystemProperty(propertyName), "System property " + propertyName + " value doesn't match");
        return this;
    }

    public AboutPage assertSystemPropertyValueIsNotEmpty(String propertyName) {
        assertion.assertTrue(StringUtils.isNotEmpty(getSystemProperty(propertyName)), "System property " + propertyName + " shouldn't be empty");
        return this;
    }

}

