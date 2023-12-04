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
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceDataPreviewPanel;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.ResourceWizardResultStep;
import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

public class M4ConnectingTargetSystem extends AbstractTrainingTest {

    @Test(groups = MODULE_4_GROUP)
    public void test00100createActiveDirectoryResourceFromTemplate() {
        ResourceDataPreviewPanel<ResourceWizardResultStep> dataPreviewPanel = basicPage
                .newResource()
                .copyFromTemplate("Training Active Directory Resource Template")
                .next()
                .name("AD")
                .description("ExAmPLE, Inc. AD resource")
                .lifecycle("Proposed")
                .next()
                .host("ad")
                .port("389")
                .bindDN("cn=idm,ou=Administrators,dc=example,dc=com")
                .bindPassword("secret")
                .next()
                .baseContext("dc=example,dc=com")
                .next()
                .createResource()
                .previewResourceData();
        //select inetOrgPerson  object class to display the existing account in your AD resource
        Selenide.screenshot("M4ConnectingTargetSystem_resourceData");
        dataPreviewPanel
                .clickBack()
                .goToResource();


    }
}
