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

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WindowType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;

/**
 * Utility class for managing browser tabs/windows in tests.
 * <p>
 * This class provides a simple abstraction over Selenium window handles by mapping
 * custom tab identifiers ({@code tabId}) to actual browser window handles.
 * It allows tests to work with named tabs instead of raw handles, improving readability
 * and maintainability of multi-tab scenarios.
 * <p>
 * Typical usage:
 * <pre>
 *     tabManager.register("main");
 *     tabManager.openNewTab("tab2");
 *     tabManager.switchTo("main");
 * </pre>
 *
 * Notes:
 * <ul>
 *     <li>Each tab must be registered before it can be switched to.</li>
 * </ul>
 */
public class TabManager {

    private final Map<String, String> windowIdHandleMap = new HashMap<>();

    public void register(String tabId) {
        windowIdHandleMap.put(tabId, WebDriverRunner.getWebDriver().getWindowHandle());
    }

    public void openNewTab(String tabId) {
        WebDriverRunner.getWebDriver().switchTo().newWindow(WindowType.TAB);
        open("/");
        register(tabId);
    }

    public void openNewWindow() {
        WebDriverRunner.getWebDriver().switchTo().newWindow(WindowType.WINDOW);
        open("/");
    }

    public void switchTo(String tabId) {
        String handle = windowIdHandleMap.get(tabId);
        WebDriverRunner.getWebDriver().switchTo().window(handle);
    }

    public boolean tabExists(String tabId) {
        if (!windowIdHandleMap.containsKey(tabId)) {
            return false;
        }
        String windowHandle = windowIdHandleMap.get(tabId);
        Set<String> tabs = WebDriverRunner.getWebDriver().getWindowHandles();
        return tabs.stream().anyMatch(t -> t.equals(windowHandle));
    }
}