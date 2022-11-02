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
package com.evolveum.midpoint.schrodinger;

import org.apache.commons.lang3.Validate;

/**
 * Created by Viliam Repan (lazyman).
 */
public class EnvironmentConfiguration {

    private WebDriver driver = WebDriver.CHROME;

    private String driverLocation;

    private boolean headless;

    private String baseUrl;

    private boolean useRemoteWebdriver;

    private String remoteWebdriverUrl;

    private String locale;

    public EnvironmentConfiguration driver(final WebDriver driver) {
        Validate.notNull(driver, "Web driver must not be null");

        this.driver = driver;
        return this;
    }

    public EnvironmentConfiguration baseUrl(final String baseUrl) {
        Validate.notNull(driver, "Base url must not be null");

        this.baseUrl = baseUrl;
        return this;
    }

    public EnvironmentConfiguration driverLocation(final String driverLocation) {
        this.driverLocation = driverLocation;
        return this;
    }

    public EnvironmentConfiguration remoteWebdriverUrl(final String remoteWebdriverUrl) {
        this.remoteWebdriverUrl = remoteWebdriverUrl;
        return this;
    }

    public EnvironmentConfiguration useRemoteWebdriver(final boolean useRemoteWebdriver) {
        this.useRemoteWebdriver = useRemoteWebdriver;
        return this;
    }

    public EnvironmentConfiguration headless(final boolean headless) {
        this.headless = headless;
        return this;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public boolean isHeadless() {
        return headless;
    }

    public boolean isUseRemoteWebdriver() {
        return useRemoteWebdriver;
    }

    public String getRemoteWebdriverUrl() {
        return remoteWebdriverUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void locale(String locale) {
        this.locale = locale;
    }

    public void validate() {
        Validate.notNull(baseUrl, "Base url must not be null");
    }
}
