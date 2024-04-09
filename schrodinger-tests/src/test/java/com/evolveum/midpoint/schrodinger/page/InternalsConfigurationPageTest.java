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

import com.evolveum.midpoint.schrodinger.component.DateTimePanel;
import com.evolveum.midpoint.schrodinger.component.configuration.ClockPanel;
import com.evolveum.midpoint.schrodinger.page.configuration.InternalsConfigurationPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author Hiroyuki Wada
 */
public class InternalsConfigurationPageTest extends AbstractSchrodingerTest {

    @AfterClass
    @Override
    public void afterClass() {
        InternalsConfigurationPage configPage = basicPage.internalsConfiguration();
        configPage = basicPage.internalsConfiguration();
        configPage.clockTab().resetTime();
        super.afterClass();
    }

    @Test (enabled = false)
    public void test001changeTime() {
        InternalsConfigurationPage configPage = basicPage.internalsConfiguration();
        ClockPanel clockPanel = configPage.clockTab();

        clockPanel.changeTime("May 15, 2099", "10", "30", DateTimePanel.AmOrPmChoice.PM);

        basicPage.feedback().assertSuccess();

        basicPage.aboutPage();
        basicPage
                .internalsConfiguration()
                    .clockTab()
                        .getOffsetPanel()
                            .assertDateTimeValueEquals("May 15, 2099 10:30 " + DateTimePanel.AmOrPmChoice.PM.name());
    }

    @Test
    public void test010resetTime() {
        InternalsConfigurationPage configPage = basicPage.internalsConfiguration();
        ClockPanel clockPanel = configPage.clockTab();

        clockPanel.resetTime();

        basicPage.feedback().assertSuccess();
    }
}
