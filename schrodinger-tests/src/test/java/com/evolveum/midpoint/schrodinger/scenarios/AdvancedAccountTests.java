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
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


public class AdvancedAccountTests extends AbstractSchrodingerTest {
// TODO in progress
  private static File csvTargetFile;

  private static final String FILE_RESOUCE_NAME = "midpoint-advanced-sync.csv";
  private static final String INITIALIZE_TEST_CONFIGURATION_DEPENDENCY = "initializeTestConfiguration";
  private static final String DIRECTORY_CURRENT_TEST = "advancedAccountTests";

  @Test
  public void initializeTestConfiguration() throws IOException {

    initTestDirectory(DIRECTORY_CURRENT_TEST);

    csvTargetFile = new File(testTargetDir, FILE_RESOUCE_NAME);
    FileUtils.copyFile(ScenariosCommons.CSV_SOURCE_FILE, csvTargetFile);


    addObjectFromFile(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_FILE);
    addObjectFromFile(ScenariosCommons.USER_TEST_RAPHAEL_FILE);
    changeResourceAttribute(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME, ScenariosCommons.CSV_RESOURCE_ATTR_FILE_PATH, csvTargetFile.getAbsolutePath());
  }

  @Test (dependsOnMethods ={INITIALIZE_TEST_CONFIGURATION_DEPENDENCY})
  public void iterateForUniqueAttribute(){
 ListUsersPage listUsersPage = basicPage.listUsers();
      listUsersPage
              .table()
                .search()
                  .byName()
                  .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                .updateSearch()
              .and()
              .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                .selectTabProjections()
                  .clickAddProjection()
                      .table()
                        .search()
                          .byName()
                          .inputValue(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                        .updateSearch()
                      .and()
                      .selectCheckboxByName(ScenariosCommons.RESOURCE_CSV_GROUPS_AUTHORITATIVE_NAME)
                    .and()
                .clickAdd()
              .and()
                .clickSave()
                .feedback()
                .isSuccess();

    listUsersPage = basicPage.listUsers();

     listUsersPage
              .table()
                .search()
                  .byName()
                  .inputValue(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                .updateSearch()
              .and()
              .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME)
                .selectTabProjections()
                  .table()
                    .clickByName(ScenariosCommons.TEST_USER_RAPHAEL_NAME);

    }

}
