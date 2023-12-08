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

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.DiscoveryWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceDataPreviewPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceWizardResultStep;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class M4ConnectingTargetSystem extends AbstractTrainingTest {

    private static final Logger LOG = LoggerFactory.getLogger(M4ConnectingTargetSystem.class);

    @Test(groups = MODULE_4_GROUP)
    public void test00100createADResourceFromTemplate() {
         DiscoveryWizardStep discoveryWizardStep = basicPage
                .newResource()
                .copyFromTemplate("Training Active Directory Resource Template")
                .name("AD")
                .description("ExAmPLE, Inc. AD resource")
                .lifecycle("Proposed")
                .next()
                .host("ad")
                .port("389")
                .bindDN("cn=idm,ou=Administrators,dc=example,dc=com")
                .bindPassword("secret")
                .next();
         Selenide.screenshot("test00100createADResourceFromTemplate_DiscoveryWizardStep");
        ResourceDataPreviewPanel<ResourceWizardResultStep> dataPreviewPanel = discoveryWizardStep
                .baseContext("dc=example,dc=com")
                .next()
                .createResource()
                .previewResourceData()
                        .selectObjectType("inetOrgPerson");
        //select inetOrgPerson  object class to display the existing account in your AD resource
        Selenide.screenshot("M4ConnectingTargetSystem_resourceData");
        LOG.info("dataPreviewPanel");
        LOG.info(WebDriverRunner.source());
        dataPreviewPanel
                .clickBack()
                .goToResource();
    }

    @Test(groups = MODULE_4_GROUP)
    public void test00200reviewADResourceSyncConfiguration() {
        basicPage
                .listResources()
                .table()
                .clickByName("AD")
                .selectAccountsPanel()
                .configureSynchronization()
                .assertAllLifecycleStateValuesEqual("Proposed")
                .assertActionValueForSituationEquals("Unmatched", "Delete resource object")
                .exitWizard()
                .configureCorrelation()
                .assertCorrelationRuleExist("personalNumber-correlation")
                .assertCorrelationRuleEnabled("personalNumber-correlation")
                .assertCorrelationRuleExist("last-resort-correlation")
                .assertCorrelationRuleDisabled("last-resort-correlation")
                .exitWizard()
                .configureMappings();
        Selenide.screenshot("test00200reviewADResourceSyncConfiguration");
        LOG.info("configureMappings");
        LOG.info(WebDriverRunner.source());
                //todo there are several inbound mappings, all of them are active, but used only for the correlation (indicated by  icon)
//                .exitWizard();
    }
}
