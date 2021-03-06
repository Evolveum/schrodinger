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

import com.evolveum.midpoint.schrodinger.page.user.ListUsersPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by matus on 5/11/2018.
 */
public class UserPhotoTests extends AbstractSchrodingerTest {

    private static final String TEST_USER_LEO_NAME= "leonardo";
    private static final File PHOTO_SOURCE_FILE_LARGE = new File("./src/test/resources/images/leonardo_large_nc.jpg");
    private static final File PHOTO_SOURCE_FILE_SMALL = new File("./src/test/resources/images/leonardo_small_nc.jpg");

    private static final String CREATE_USER_WITH_LARGE_PHOTO_DEPENDENCY = "test0010createMidpointUserWithPhotoLarge";
    private static final String CREATE_USER_WITH_NORMAL_PHOTO_DEPENDENCY = "test0020createMidpointUserWithPhotoJustRight";

    //@Test TODO test commented out because of MID-4774
    public void test0010createMidpointUserWithPhotoLarge(){
        UserPage user = basicPage.newUser();
        user
                    .selectBasicPanel()
                        .form()
                        .addAttributeValue("name", TEST_USER_LEO_NAME)
                        .addAttributeValue(UserType.F_GIVEN_NAME, "Leonardo")
                        .addAttributeValue(UserType.F_FAMILY_NAME, "di ser Piero da Vinci")
                        .setFileForUploadAsAttributeValue("Jpeg photo", PHOTO_SOURCE_FILE_LARGE)
                    .and()
                .and()
                .checkKeepDisplayingResults()
                .clickSave()
                    .feedback()
                    .assertError();
    }

    @Test //(dependsOnMethods = {CREATE_USER_WITH_LARGE_PHOTO_DEPENDENCY}) // TODO uncomment test dependency after MID-4774 fix
    public void test0020createMidpointUserWithPhotoJustRight(){
        UserPage user = basicPage.newUser();
        user
                        .selectBasicPanel()
                            .form()
                            .addAttributeValue("name", TEST_USER_LEO_NAME)
                            .addAttributeValue(UserType.F_GIVEN_NAME, "Leonardo")
                            .addAttributeValue(UserType.F_FAMILY_NAME, "di ser Piero da Vinci")
                            .setFileForUploadAsAttributeValue("Jpeg photo", PHOTO_SOURCE_FILE_SMALL)
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .assertSuccess();
    }

    @Test (dependsOnMethods = {CREATE_USER_WITH_NORMAL_PHOTO_DEPENDENCY})
    public void test0030deleteUserPhoto(){
         ListUsersPage usersPage = basicPage.listUsers();
         usersPage
                    .table()
                        .search()
                        .byName()
                            .inputValue(TEST_USER_LEO_NAME)
                            .updateSearch()
                    .and()
                    .clickByName(TEST_USER_LEO_NAME)
                        .selectBasicPanel()
                            .form()
                            .removeFileAsAttributeValue("Jpeg photo")
                        .and()
                    .and()
                    .checkKeepDisplayingResults()
                    .clickSave()
                        .feedback()
                        .assertSuccess();
    }
}
