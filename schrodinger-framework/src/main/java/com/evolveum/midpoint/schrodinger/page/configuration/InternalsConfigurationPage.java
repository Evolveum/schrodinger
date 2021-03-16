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
package com.evolveum.midpoint.schrodinger.page.configuration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.TabPanel;
import com.evolveum.midpoint.schrodinger.component.configuration.*;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class InternalsConfigurationPage extends BasicPage {

    public ClockTab clockTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.clock");
        return new ClockTab(this, element);
    }

    public DebugUtilTab debugUtilTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.debugUtil");
        return new DebugUtilTab(this, element);
    }

    public InternalConfigurationTab internalConfigurationTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.internalConfig");
        return new InternalConfigurationTab(this, element);
    }

    public TracesTab tracesTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.traces");
        return new TracesTab(this, element);
    }

    public CountersTab countersTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.counters");
        return new CountersTab(this, element);
    }

    public CacheManagementTab cacheManagementTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.cache");
        return new CacheManagementTab(this, element);
    }

    public MemoryTab memoryTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.memory");
        return new MemoryTab(this, element);
    }

    public ThreadsTab threadsTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.threads");
        return new ThreadsTab(this, element);
    }

    public PerformanceTab performanceTab() {
        SelenideElement element = findTabPanel().clickTab("PageInternals.tab.performance");
        return new PerformanceTab(this, element);
    }

    protected TabPanel findTabPanel() {
        SelenideElement tabPanelElement = $(Schrodinger.byDataId("div", "tabPanel"))
                .waitUntil(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new TabPanel<>(this, tabPanelElement);
    }
}
