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

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.evolveum.midpoint.schrodinger.component.org.MemberPanel;
import com.evolveum.midpoint.schrodinger.component.org.MemberTable;

import com.evolveum.midpoint.schrodinger.component.org.OrgRootTab;

import com.evolveum.midpoint.schrodinger.page.service.ServicePage;

import org.testng.annotations.Test;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import static com.evolveum.midpoint.schrodinger.util.ConstantsUtil.MEMBER_RELATION_KEY;

/**
 * Created by honchar.
 */
public class OrgMembersTests extends AbstractSchrodingerTest {

    private static final String CONFIGURATION_OBJECTS_DIRECTORY = "./src/test/resources/objects/";
    private static final String CONFIGURATION_USERS_DIRECTORY = CONFIGURATION_OBJECTS_DIRECTORY + "users/";
    private static final String CONFIGURATION_ORGS_DIRECTORY = CONFIGURATION_OBJECTS_DIRECTORY + "orgs/";

    private static final File USER_ORG_MEMBER_FILE = new File(CONFIGURATION_USERS_DIRECTORY + "user-org-member.xml");
    private static final File USER_NOT_ORG_MEMBER_FILE = new File(CONFIGURATION_USERS_DIRECTORY + "user-not-org-member.xml");
    private static final File ORG_WITH_MEMBER_FILE = new File(CONFIGURATION_ORGS_DIRECTORY + "org-with-member.xml");
    private static final String ORG_NAME = "TestOrgWithMembers";
    private static final String ORG_WITH_MEMBER_NAME = "Assign member test";
    private static final String USER_NAME = "OrgMembersWithDefaultRelation";

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(USER_ORG_MEMBER_FILE, USER_NOT_ORG_MEMBER_FILE, ORG_WITH_MEMBER_FILE);
    }

    @Test (priority = 1)
    public void test00100createOrgWithinMenuItem(){
        OrgPage newOrgPage = basicPage.newOrgUnit();
        newOrgPage
                .selectBasicPanel()
                    .form()
                    .addAttributeValue("name", ORG_NAME)
                    .and()
                .and()
                .clickSave()
                .feedback()
                .assertSuccess();
        basicPage.orgStructure().assertRootOrgExists(ORG_NAME);
    }

    @Test (dependsOnMethods = {"test00100createOrgWithinMenuItem"}, priority = 2)
    public void test00200assignDefaultRelationMember(){
        UserPage user = basicPage.newUser();

        user.selectBasicPanel()
                .form()
                    .addAttributeValue("name", USER_NAME)
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                .feedback()
                .assertSuccess();

        OrgTreePage orgTreePage = basicPage.orgStructure();
        orgTreePage
                .selectTabWithRootOrg(ORG_NAME)
                    .getMemberPanel()
                        .table()
                            .search()
                            .byRelation(MEMBER_RELATION_KEY)
                        .and()
                        .and()
                    .assignMember()
                        .table()
                            .search()
                            .byName()
                            .inputValue(USER_NAME)
                            .updateSearch()
                        .and()
                        .selectCheckboxByName(USER_NAME)
                    .and()
                    .clickAdd();
        orgTreePage = basicPage.orgStructure();
        orgTreePage
                .selectTabWithRootOrg(ORG_NAME)
                    .getMemberPanel()
                        .table()
                        .assertTableContainsLinkTextPartially(USER_NAME);
    }

    @Test (priority = 3)
    public void test00300assignExistingUserAsMember(){
        basicPage.orgStructure()
                    .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                        .getMemberPanel()
                            .table()
                                .search()
                                .byRelation(MEMBER_RELATION_KEY)
                                .and()
                            .and()
                        .assignMember()
                            .table()
                                .search()   // the goal is to check search on the parent
                                .byName()
                                .inputValue("UniqueNameUserForMemberTest")
                                .updateSearch()
                            .and()
                            .selectCheckboxByName("UniqueNameUserForMemberTest")
                        .and()
                        .clickAdd();

                basicPage.orgStructure()
                    .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                        .getMemberPanel()
                            .table()
                                .search()
                                    .byName()
                                    .inputValue("UniqueNameUserForMemberTest")
                                .updateSearch()
                            .and()
                        .clickRefreshButton()
                                .screenshot("test00300assignExistingUserAsMember_membersPanel_" + System.currentTimeMillis())
                    .assertTableContainsText("UniqueNameUserForMemberTest");
    }

    @Test (priority = 4)
    public void test00400createNewUserMemberObject() {
        UserPage newUserPage = (UserPage) basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                    .getMemberPanel()
                        .newMember("Create User type member with Approver relation", "User");
        newUserPage.selectBasicPanel()
                    .form()
                        .addAttributeValue("name", "NewUserAsOrgApprover")
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .isSuccess();
        basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                    .getMemberPanel()
//                        .selectRelation("Approver")
                        .table()
                            .search()
                                .byRelation("Approver")
                            .byName()
                            .inputValue("NewUserAsOrgApprover")
                            .updateSearch()
                        .and()
                        .assertVisibleObjectsCountEquals(1)
                        .assertTableContainsText("Approver");
    }

    @Test (priority = 5)
    public void test00500createNewUserManagerObject() {
        resetToDefaultAndRelogin();
        addObjectFromFile(ORG_WITH_MEMBER_FILE);
        UserPage newUserPage = (UserPage) basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                    .getMemberPanel()
                        .newMember("Create User type member with Manager relation", "User");
        newUserPage.selectBasicPanel()
                    .form()
                        .addAttributeValue("name", "NewUserAsOrgManager")
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .isSuccess();
        MemberPanel<OrgRootTab> memberPanel = basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                .getMemberPanel();
        MemberTable<MemberPanel<OrgRootTab>> memberTable = memberPanel
                .table();
//        memberPanel
//                .selectType("Focus")
//                .selectRelation("Manager");
        memberTable
                .search()
                    .byType("All")
                    .byRelation("Manager")
                    .byName()
                        .inputValue("NewUserAsOrgManager")
                    .updateSearch();

        memberTable
                    .assertVisibleObjectsCountEquals(1)
                .assertTableContainsText("Manager");
    }

    @Test (priority = 6)
    public void test00600createNewOrgOwnerObject() {
        resetToDefaultAndRelogin();
        addObjectFromFile(ORG_WITH_MEMBER_FILE);
        OrgPage newOrgPage = (OrgPage) basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                    .getMemberPanel()
                        .newMember("Create Organization type member with Owner relation", "Organization");
        newOrgPage.selectBasicPanel()
                    .form()
                        .addAttributeValue("name", "NewOrgAsOrgOwner")
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .isSuccess();
        MemberPanel<OrgRootTab> memberPanel = basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                .getMemberPanel();
        MemberTable<MemberPanel<OrgRootTab>> memberTable = memberPanel
                .table();
//        memberPanel
////                .selectType("All")
////                .selectRelation("Owner")
//                .table()
//                .search()
//
//                .updateSearch();
        memberTable
                .search()
                    .byType("All")
                    .byRelation("Owner")
                    .byName()
                        .inputValue("NewOrgAsOrgOwner")
                    .updateSearch()
                .and()
                    .assertVisibleObjectsCountEquals(1)
                    .assertTableContainsText("Owner");
    }

    @Test (priority = 7)
    public void test00700createNewOrganizationApproverObject() {
        resetToDefaultAndRelogin();
        addObjectFromFile(ORG_WITH_MEMBER_FILE);
        ServicePage newServicePage = (ServicePage) basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                    .getMemberPanel()
                        .newMember("Create Organization type member with Approver relation", "Service");
        newServicePage.selectBasicPanel()
                    .form()
                        .addAttributeValue("name", "NewOrgAsOrgApprover")
                        .and()
                    .and()
                .clickSave()
                .feedback()
                .isSuccess();
        MemberPanel<OrgRootTab> memberPanel = basicPage.orgStructure()
                .selectTabWithRootOrg(ORG_WITH_MEMBER_NAME)
                .getMemberPanel();
        MemberTable<MemberPanel<OrgRootTab>> memberTable = memberPanel
                .table();
//        memberPanel
//                .selectType("All")
//                .selectRelation("Approver");
        memberTable
                .search()
                    .byType("All")
                    .byRelation("Approver")
                    .byName()
                        .inputValue("NewOrgAsOrgApprover")
                .updateSearch()
                .and()
                .assertVisibleObjectsCountEquals(1)
                .assertTableContainsText("Approver");
    }
}
