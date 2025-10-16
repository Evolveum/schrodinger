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

import com.evolveum.midpoint.schrodinger.page.configuration.AboutPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by matus on 3/16/2018.
 */
public class AboutPageTest extends AbstractSchrodingerTest {

    private static final String VERSION_EXPECTED = "4.9.6-SNAPSHOT"; // Static value, should be changed each version change.
    private static final String CONNID_VERSION_EXPECTED = "1.6.0.0"; // Static value, should be changed each version change.
    private static final String UTILITY_TASK_EXPECTED = "Utility task";
    private static final String REINDEX_REPO_TASK_DISPLAY_NAME_EXPECTED = "Reindex repository objects";

    private static final String PROPERTY_JVM_NAME_XMX = "-Xmx";

    private AboutPage aboutPage;

    @BeforeMethod
    private void openPage() {
        aboutPage = basicPage.aboutPage();
    }

    @Test
    public void test0010checkMidpointVersion() {
        aboutPage.assertVersionValueEquals(VERSION_EXPECTED);
    }

    @Test
    public void test0020checkGitDescribeValue() {
        aboutPage.assertGitDescribeValueIsNotEmpty();
    }

    @Test
    public void test0030checkBuildAt() {
        aboutPage.assertBuildAtValueIsNotEmpty();
    }

    @Test
    public void test0040checkRepositoryImplementationIsNative() {
        aboutPage.assertRepositoryImplementationIsNative();
    }

    @Test
    public void test0050checkConnIdVersion() {
        aboutPage.assertConnIdVersionValueEquals(CONNID_VERSION_EXPECTED);
    }

    @Test
    public void test0060repoSelfTestFeedbackPositive() {

        aboutPage
                .repositorySelfTest()
                .feedback()
                .assertSuccess();
    }

    @Test
    public void test0070checkReindexRepositoryObjectsCategory() {
        aboutPage
                .reindexRepositoryObjects()
                    .feedback()
                        .assertInfo()
                        .clickShowTask()
                            .and()
                            .summary()
                                .assertSummaryTagWithTextExists(UTILITY_TASK_EXPECTED)
                                .assertDisplayNameEquals(REINDEX_REPO_TASK_DISPLAY_NAME_EXPECTED);
    }

    @Test (enabled = false)
    public void test0080checkJVMPropertiesMidpointHome(){
        aboutPage.assertJVMPropertyValueIsNotEmpty(AbstractSchrodingerTest.PROPERTY_NAME_MIDPOINT_HOME);
    }

    @Test
    public void test0090checkSystemProperty(){
        aboutPage.assertSystemPropertyValueIsNotEmpty(AbstractSchrodingerTest.PROPERTY_NAME_USER_HOME);
    }
}
