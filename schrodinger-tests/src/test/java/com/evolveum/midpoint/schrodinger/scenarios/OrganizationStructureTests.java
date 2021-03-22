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

import com.evolveum.midpoint.schrodinger.page.configuration.ImportObjectPage;
import com.evolveum.midpoint.schrodinger.page.org.OrgTreePage;
import com.evolveum.midpoint.schrodinger.page.resource.ListResourcesPage;
import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by matus on 5/11/2018.
 */
public class OrganizationStructureTests extends AbstractSchrodingerTest {

    private static File csvTargetFile;

    private static final File CSV_RESOURCE_ADVANCED_SYNC = new File("./src/test/resources/objects/resources/resource-csv-groups.xml");

    private static final File CSV_INITIAL_SOURCE_FILE = new File("./src/test/resources/sources/midpoint-groups.csv");
    private static final File ORG_ACCOUNT_INDUCEMENT_FILE = new File("./src/test/resources/objects/orgs/org-account-inducement.xml");
    private static final File ORG_MONKEY_ISLAND_SOURCE_FILE = new File("./src/test/resources/objects/orgs/org-monkey-island-simple.xml");

    private static final String TEST_USER_GUYBRUSH_NAME = "guybrush";

    private static final String NAME_ORG_UNIT_ASSIGN= "P0001";
    private static final String NAME_ORG_UNIT_UNASSIGN= "Save Elaine";
    private static final String NAME_ORG_UNIT_ASSIGN_AND_INDUCE= "testOrgUnit";
    private static final String NAME_CSV_RESOURCE_ADVANCED_SYNC ="CSV (target with groups)";
    private static final String TYPE_SELECTOR_ORG= "Org";

    private static final String IMPORT_ORG_STRUCT_DEPENDENCY = "importOrgStructure";
    private static final String ASSIGN_ORG_UNIT_DEPENDENCY = "assignOrgUnit";
    private static final String ORG_UNIT_ACCOUNT_INDUCEMENT_DEPENDENCY = "orgUnitAccountInducement";

    private static final String DIRECTORY_CURRENT_TEST = "organizationStructureTests";
    private static final String FILE_RESOUCE_NAME = "midpoint-advanced-sync.csv";

    @Test
    public void importOrgStructure() throws IOException {

        initTestDirectory(DIRECTORY_CURRENT_TEST);

        csvTargetFile = new File(testTargetDir, FILE_RESOUCE_NAME);
        FileUtils.copyFile(CSV_INITIAL_SOURCE_FILE, csvTargetFile);

        ImportObjectPage importPage = basicPage.importObject();
        importPage
                    .getObjectsFromFile()
                    .chooseFile(ORG_MONKEY_ISLAND_SOURCE_FILE)
                    .checkOverwriteExistingObject()
                    .clickImportFileButton()
                        .feedback()
                        .assertSuccess();
    }

//    @Test (dependsOnMethods ={IMPORT_ORG_STRUCT_DEPENDENCY})
    public void assignOrgUnit(){
         ListUsersPage users = basicPage.listUsers();
         UserPage userPage = users
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_GUYBRUSH_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_GUYBRUSH_NAME)
                    .selectTabAssignments()
                        .clickAddAssignemnt()
                            .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ORG)
                            .table()
                                .search()
                                    .byName()
                                    .inputValue(NAME_ORG_UNIT_ASSIGN)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(NAME_ORG_UNIT_ASSIGN)
                        .and()
                    .clickAdd()
                .and();
        userPage.checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .assertSuccess();
    }

//    @Test (dependsOnMethods ={ORG_UNIT_ACCOUNT_INDUCEMENT_DEPENDENCY})
    public void unassignOrgUnit(){
        ListUsersPage users = basicPage.listUsers();
        UserPage userPage = users
                .table()
                    .search()
                    .byName()
                    .inputValue(TEST_USER_GUYBRUSH_NAME)
                    .updateSearch()
                .and()
                .clickByName(TEST_USER_GUYBRUSH_NAME)
                    .selectTabAssignments()
                        .table()
                        .removeByName(NAME_ORG_UNIT_UNASSIGN)
                    .and()
                .and();
        userPage.checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .assertSuccess();
    }

//    @Test (dependsOnMethods ={ASSIGN_ORG_UNIT_DEPENDENCY})
    public void orgUnitAccountInducement(){
        addObjectFromFile(CSV_RESOURCE_ADVANCED_SYNC);
        addObjectFromFile(ORG_ACCOUNT_INDUCEMENT_FILE);
        addObjectFromFile(ScenariosCommons.USER_TEST_RAPHAEL_FILE);

        changeResourceFilePath();

       refreshResourceSchema(NAME_CSV_RESOURCE_ADVANCED_SYNC);

         ListUsersPage users = basicPage.listUsers();
         UserPage userPage = users
                .table()
                    .search()
                    .byName()
                    .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                    .updateSearch()
                .and()
                .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                    .selectTabAssignments()
                        .clickAddAssignemnt()
                            .selectType(TYPE_SELECTOR_ORG)
                            .table()
                                .search()
                                    .byName()
                                    .inputValue(NAME_ORG_UNIT_ASSIGN_AND_INDUCE)
                                .updateSearch()
                            .and()
                            .selectCheckboxByName(NAME_ORG_UNIT_ASSIGN_AND_INDUCE)
                        .and()
                    .clickAdd()
                .and();
         userPage
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .isSuccess();
   }

    @Test (dependsOnMethods ={IMPORT_ORG_STRUCT_DEPENDENCY})
    public void expandCollapseAllTests(){
        OrgTreePage orgPage = basicPage.orgStructure();
        orgPage.selectTabWithRootOrg("Governor Office")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("Ministry of Offense")
                        .expandAll()
                        .assertChildOrgExists("Swashbuckler Section", "Ministry of Health");

        orgPage.selectTabWithRootOrg("Governor Office")
                .getOrgHierarchyPanel()
                    .showTreeNodeDropDownMenu("Ministry of Offense")
                        .collapseAll()
                        .assertChildOrgDoesntExist("Swashbuckler Section", false, "Ministry of Health");
    }

    public void changeResourceFilePath(){
        ListResourcesPage listResourcesPage = basicPage.listResources();

        listResourcesPage
                .table()
                .search()
                .byName()
                .inputValue(NAME_CSV_RESOURCE_ADVANCED_SYNC)
                .updateSearch()
                .and()
                .clickByName(NAME_CSV_RESOURCE_ADVANCED_SYNC)
                    .clickEditResourceConfiguration()
                        .form()
                        .changeAttributeValue("File path", "", csvTargetFile.getAbsolutePath())
                        .changeAttributeValue(CSV_RESOURCE_ATTR_UNIQUE,"","login")
                    .and()
                .and()
                .clickSaveAndTestConnection()
                .assertIsTestSuccess();
    }
}
