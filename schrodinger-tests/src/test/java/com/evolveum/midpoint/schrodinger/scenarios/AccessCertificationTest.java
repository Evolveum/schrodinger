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
import com.evolveum.midpoint.schrodinger.page.certification.CampaignsPage;
import com.evolveum.midpoint.schrodinger.page.certification.CertificationItemsPage;
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
                .selectTilesViewAndShowItemsForCampaign(CAMPAIGN_NAME)
                .table()
                .assertAllObjectsCountEquals(30);

        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin()
                .loginWithReloadLoginPage("certDeputyUser", "Password123!");

        basicPage.myActiveCampaigns()
                .selectTilesViewAndShowItemsForCampaign(CAMPAIGN_NAME)
                .table()
                .assertAllObjectsCountEquals(30);

    }

    /**
     * Coverage for the ticket #10469
     * use case: go to the Campaigns page -> switch to Table view
     * -> open campaign details page -> go Back to Campaigns page
     */
    @Test
    public void test0020goBackFromCampaignDetailsPageToCampaignsTablePage() {
        reloginAsAdministrator();

        CampaignsPage campaignsPage = basicPage.campaigns();
        campaignsPage
                .selectTableView()
                .clickByName(CAMPAIGN_NAME)
                .clickBack();
        campaignsPage.assertPageTitleStartsWith("Campaigns");
        campaignsPage.assertTableViewIsSelected();
    }

    /**
     * Coverage for the ticket #10469
     * check that session storage saves the view mode on the campaigns page after switching between pages
     */
    @Test
    public void test0030campaignsPageSessionStorageCheck() {
        basicPage.campaigns()
                .selectTableView();
        basicPage.dashboard();
        basicPage
                .campaigns()
                .assertTableViewIsSelected()
                .selectTilesView();
        basicPage.dashboard();
        basicPage
                .campaigns()
                .assertTileViewIsSelected();
    }

   /**
     * Coverage for the ticket #10469
     * use case: reviewer user goes to the My Active Campaigns page -> clicks Show items for campaign
    * -> check that items page is displayed correctly -> click Back to Campaigns page
     */
    @Test
    public void test0040myActiveCampaignPageRedirectForReviewerUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin()
                .loginWithReloadLoginPage("certReviewerUser", "Password123!");

        CertificationItemsPage certItemsPage = basicPage
                .myActiveCampaigns()
                .selectTilesViewAndShowItemsForCampaign(CAMPAIGN_NAME);
        certItemsPage
                .table()
                .assertAllObjectsCountEquals(30);
        certItemsPage
                .navigateBackToActiveCampaigns()
                .assertPageTitleStartsWith("My active campaigns");
    }

    /**
     * covers #10879
     */
    @Test
    public void test0050activeCampaignsBackButton() {
        //several times navigating to cert. items page and returning back to Active campaigns page
        basicPage
                .activeCampaigns()
                .selectTableViewAndShowItemsForCampaign(CAMPAIGN_NAME)
                .navigateBackToActiveCampaigns()
                .selectTableViewAndShowItemsForCampaign(CAMPAIGN_NAME)
                .navigateBackToActiveCampaigns()
                .assertTableViewIsSelected()
                .selectTableViewAndShowItemsForCampaign(CAMPAIGN_NAME)
                .navigateBackToActiveCampaigns()
                .assertTableViewIsSelected()
                .assertPageTitleStartsWith("Active campaigns");
    }

        @Override
    protected boolean resetToDefaultBeforeTests() {
        return true;
    }

    protected void reloginAsAdministrator() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login(getUsername(), getPassword());
    }

}
