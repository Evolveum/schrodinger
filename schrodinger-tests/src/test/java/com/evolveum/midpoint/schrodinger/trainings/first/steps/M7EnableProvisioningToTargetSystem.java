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

import com.evolveum.midpoint.schrodinger.trainings.AbstractTrainingTest;
import org.testng.annotations.Test;

public class M7EnableProvisioningToTargetSystem extends AbstractTrainingTest {

    @Test(groups = MODULE_7_GROUP)
    public void test1reviewingADResourceProvisioningConfiguration() {
        basicPage
                .listResources()
                .table()
                .clickByName("HR")
                .selectAccountsPanel()
                .configureMappings()
                .outboundMappings()
                .table()
                .assertVisibleObjectsCountEquals(12)    //todo check if 12 is right number
                .assertAllLifecycleStateValuesEqual("Draft")
                .and()
                .and()
                .exitWizard()
                .configureActivation()
                .outbound()
                .assertTilesNumberEquals(3)
                .assertTilesLifecycleStateValueEquals("set-account-status-based-on-midpoint-user", "Draft")
                .assertTilesLifecycleStateValueEquals("Disable instead of delete", "Draft")
                .assertTilesLifecycleStateValueEquals("Delayed delete", "Draft")
                .scenarioConfigurationSettings("Delayed delete")
                .assertPropertyInputValueEquals("Delete after", "5")
                .assertPropertyDropdownValueEquals("Delete after", "Minutes")
                .done()
                .and()
                .exitWizard();



/**
  *
 * click Configure, then click Credentials item in the context menu
 *
 *
 * click Outbound
 *
 *
 * there are two outbound credentials mappings, all of them in Draft lifecycle state, so they are effectively disabled:
 *
 *
 *
 * mapping initial-password-generate will be later used to generate a random initial password (using a weak mapping) for AD account (as the account cannot be passwordless). This password won’t be stored and will be unknown to the user; we assume the user will activate his/her AD account by visiting the company’s helpdesk
 *
 *
 * mapping password-change will be later used to allow password changes from midPoint to Active Directory
 *
 *
 *
 *
 *
 *
 *
 * We will not allow end-user access nor password changes via midPoint in this training.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * Click Exit wizard to get back to account list.
 *
 *
 * The resource created from resource template is ready to be used for provisioning simulations.
 */
    }
}
