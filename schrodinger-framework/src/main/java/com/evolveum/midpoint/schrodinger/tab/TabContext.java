/*
 * Copyright (c) 2026 Evolveum
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
package com.evolveum.midpoint.schrodinger.tab;

import com.evolveum.midpoint.schrodinger.page.BasicPage;

/**
 * Represents a logical browser tab context used in multi-tab tests.
 * <p>
 * This class encapsulates a reference to a specific tab (identified by {@code tabId})
 * and provides a fluent API for activating the tab and continuing test execution
 * with page objects.
 * <p>
 * It is typically used together with {@link TabManager} to enable test flows such as:
 *
 * <pre>
 *     tab("tab1").activate().lastActive(page).clickSave();
 * </pre>
 *
 */
public class TabContext {
    private final TabManager tabManager;
    private final String tabId;
    private final BasicPage basicPage;

    public TabContext(TabManager tabManager, String tabId, BasicPage basicPage) {
        this.tabManager = tabManager;
        this.tabId = tabId;
        this.basicPage = basicPage;
    }

    public TabContext activate() {
        tabManager.switchTo(tabId);
        return TabContext.this;
    }

    public <BP extends BasicPage> BP lastActive(BP lastActivePage) {
        return lastActivePage;
    }

    public BasicPage getBasicPage() {
        return basicPage;
    }
}
