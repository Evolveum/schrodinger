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

package com.evolveum.midpoint.schrodinger.page;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.codeborne.selenide.Selenide;

import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import com.evolveum.midpoint.schrodinger.util.ConstantsUtil;

import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.component.prism.show.PreviewChangesTab;
import com.evolveum.midpoint.schrodinger.component.prism.show.ScenePanel;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;

public class PreviewPageTest  extends AbstractSchrodingerTest {

    private static final String TEST_DIR = "./src/test/resources/objects/roles";

    private static final File ROLE_USER_PREVIEW_FILE = new File(TEST_DIR, "role-user-preview.xml");
    private static final String ROLE_USER_PREVIEW_NAME = "rolePreviewChanges";

    private static final File ROLE_USER_NO_PREVIEW_FILE = new File(TEST_DIR, "role-user-no-preview.xml");
    private static final String ROLE_USER_NO_PREVIEW_NAME = "roleNoPreviewChanges";

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(ROLE_USER_PREVIEW_FILE, ROLE_USER_NO_PREVIEW_FILE);
    }

    @Test (priority = 1)
    public void test001createUser() {

        //@formatter:off
        UserPage user = basicPage.newUser();

        PreviewPage previewPage = user.selectTabBasic()
                .form()
                    .addAttributeValue("name", "jack")
                    .addAttributeValue(UserType.F_GIVEN_NAME, "Jack")
                    .addPasswordAttributeValue("asd123")
                    .and()
                .and()
                .clickPreview();
        //@formatter:on

        ScenePanel<PreviewChangesTab> primaryDeltaScene = previewPage.selectPanelByName("jack").primaryDeltas();
        primaryDeltaScene
                .assertExpanded()
                .assertDeltasSizeEquals(3);

        List<ScenePanel> deltas = primaryDeltaScene.objectDeltas();
        ScenePanel<ScenePanel> primaryDelta = deltas.get(0);

        primaryDelta.header()
                .assertChangeTypeEquals("Add")
                .assertChangedObjectNameEquals("jack")
                .assertIsNotLink();

        previewPage
                .clickSave()
                .feedback()
                .assertSuccess();
    }

    @Test (priority = 2, dependsOnMethods = {"test001createUser"})
    public void test002modifyUser() {

        //@formatter:off
        PreviewPage previewPage = basicPage.listUsers()
                .table()
                    .clickByName("jack")
                    .selectTabBasic()
                        .form()
                            .addAttributeValue(UserType.F_FAMILY_NAME, "Sparrow")
                        .and()
                    .and()
                .clickPreview();
        //@formatter:on

        ScenePanel<PreviewChangesTab> primaryDeltaScene = previewPage.selectPanelByName("jack").primaryDeltas();
        primaryDeltaScene
                .assertExpanded()
                .assertDeltasSizeEquals(1);

        List< ScenePanel> deltas = primaryDeltaScene.objectDeltas();
        ScenePanel<ScenePanel> primaryDelta = deltas.get(0);

        primaryDelta.header()
                .assertChangeTypeEquals("Modify")
                .assertChangedObjectNameEquals("jack")
                .assertIsLink();

        previewPage.clickSave().feedback().assertSuccess();
    }

    @Test (priority = 3, dependsOnMethods = {"test001createUser"})
    public void test003assignRolePreview() {
        //@formatter:off
        ProgressPage previewPage = basicPage.listUsers()
                .table()
                    .clickByName("jack")
                    .selectTabAssignments()
                        .clickAddAssignment()
                            .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ROLE)
                            .table()
                                .search()
                                    .byName()
                                        .inputValue(ROLE_USER_PREVIEW_NAME)
                                    .updateSearch()
                                .and()
                            .selectCheckboxByName(ROLE_USER_PREVIEW_NAME)
                        .and()
                    .clickAdd()
                .and()
                .clickSave();
        //@formatter:on

        previewPage.feedback().assertSuccess();

    }

    @Test (priority = 4, dependsOnMethods = {"test001createUser"})
    public void test004loginWithUserJack() {

        midPoint.logout();

        basicPage = midPoint.formLogin().login("jack", "asd123");

        PreviewPage previewPage = basicPage.profile()
                .selectTabBasic()
                    .form()
                        .addAttributeValue(UserType.F_FULL_NAME, "Jack Sparrow")
                    .and()
                .and()
                .clickPreview();

        ScenePanel<PreviewChangesTab> primaryDeltaScene = previewPage
                .selectPanelByName("jack")
                    .primaryDeltas();
        primaryDeltaScene
                .assertExpanded()
                .assertDeltasSizeEquals(1);

        ScenePanel<ScenePanel> primaryDelta = primaryDeltaScene.objectDeltas().get(0);

        primaryDelta.header()
                .assertChangeTypeEquals("Modify")
                .assertChangedObjectNameEquals("jack")
                .assertIsNotLink();

        midPoint.logout();
        basicPage = midPoint.formLogin().login("administrator", "5ecr3t");

    }

    @Test (priority = 5, dependsOnMethods = {"test001createUser", "test003assignRolePreview"})
    public void test005unassignRolePreview() {
        //@formatter:off
        ProgressPage previewPage = basicPage.listUsers()
                .table()
                    .clickByName("jack")
                        .selectTabAssignments()
                            .table()
                                .selectCheckboxByName(ROLE_USER_PREVIEW_NAME)
                                .removeByName(ROLE_USER_PREVIEW_NAME)
                            .and()
                        .and()
                    .clickSave();
        //@formatter:on

        previewPage.feedback().assertSuccess();

    }

    @Test (priority = 6, dependsOnMethods = {"test001createUser"})
    public void test006assignRoleNoPreview() {
        //@formatter:off
        ProgressPage previewPage = basicPage.listUsers()
                .table()
                    .clickByName("jack")
                        .selectTabAssignments()
                            .clickAddAssignment()
                                .selectType(ConstantsUtil.ASSIGNMENT_TYPE_SELECTOR_ROLE)
                                .table()
                                    .search()
                                        .byName()
                                            .inputValue(ROLE_USER_NO_PREVIEW_NAME)
                                            .updateSearch()
                                        .and()
                                    .selectCheckboxByName(ROLE_USER_NO_PREVIEW_NAME)
                                .and()
                            .clickAdd()
                        .and()
                    .clickSave();
        //@formatter:on

        previewPage.feedback().assertSuccess();

    }

    @Test (priority = 7, dependsOnMethods = {"test001createUser", "test003assignRolePreview", "test005unassignRolePreview", "test006assignRoleNoPreview"})
    public void test007loginWithUserJack() {

        midPoint.logout();

        basicPage = midPoint.formLogin().login("jack", "asd123");

        UserPage userPage = basicPage.profile()
                .selectTabBasic()
                    .form()
                        .addAttributeValue(UserType.F_FULL_NAME, "Jack Sparrow")
                    .and()
                .and();

        Selenide.screenshot("previewVisible");
        userPage.assertPreviewButtonIsNotVisible();
        midPoint.logout();
    }

}
