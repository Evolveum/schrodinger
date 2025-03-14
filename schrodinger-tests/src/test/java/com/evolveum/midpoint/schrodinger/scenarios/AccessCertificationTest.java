/*
 * Copyright (c) 2025 Evolveum
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AccessCertificationTest extends AbstractSchrodingerTest {

    private static final File ROLES_TEST_CERTIFICATION = new File("./src/test/resources/objects/roles/roles-test-certification.xml");
    private static final File USERS_TEST_CERTIFICATION = new File("./src/test/resources/objects/users/users-test-certification.xml");
    private static final File ACCESS_CERTIFICATION_DEFINITION = new File("./src/test/resources/objects/accessCertification/definition/all-user-assignments-def.xml");

    private static final String CAMPAIGN_DEFINITION_NAME = "All user assignments";
    private static final String CAMPAIGN_NAME = "All user assignments 1";

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ROLES_TEST_CERTIFICATION, USERS_TEST_CERTIFICATION, ACCESS_CERTIFICATION_DEFINITION);
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "springTestContextPrepareTestInstance" })
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        basicPage.campaignDefinitions()
                .table()
                .createCampaign(CAMPAIGN_DEFINITION_NAME);

        basicPage.campaigns()
                .startCampaign(CAMPAIGN_NAME)
                .assertCampaignInReviewStage(CAMPAIGN_NAME);
    }

    /**
     * Coverage for the ticket #10520 Certification deputy does not see Certification items
     */
    @Test
    public void test0010resolvingCertItemsByDeputy() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin()
                .loginWithReloadLoginPage("certReviewerUser", "Password123!");

        basicPage.myActiveCampaigns()
                .showItemsForCampaign(CAMPAIGN_NAME)
                .table()
                .assertAllObjectsCountEquals(30);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin()
                .loginWithReloadLoginPage("certDeputyUser", "Password123!");

        basicPage.myActiveCampaigns()
                .showItemsForCampaign(CAMPAIGN_NAME)
                .table()
                .assertAllObjectsCountEquals(30);

    }

    @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

}
