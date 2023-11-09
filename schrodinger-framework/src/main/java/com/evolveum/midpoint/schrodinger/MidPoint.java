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

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.apache.commons.lang3.Validate;

import com.evolveum.midpoint.schrodinger.component.LoggedUser;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.login.SamlSelectPage;

import java.time.Duration;

/**
 * Created by Viliam Repan (lazyman).
 */
public class MidPoint {

    public static final Duration TIMEOUT_DEFAULT_2_S = Duration.ofMillis(2000);

    public static final Duration TIMEOUT_SHORT_4_S = Duration.ofMillis(4000);
    public static final Duration TIMEOUT_EXTRA_SHORT_1_S = Duration.ofMillis(1000);

    public static final Duration TIMEOUT_MEDIUM_6_S = Duration.ofMillis(6000);

    public static final Duration TIMEOUT_LONG_1_M = Duration.ofMillis(60000);

    public static final Duration TIMEOUT_LONG_30_S = Duration.ofMillis(30000);

    public static final Duration TIMEOUT_LONG_20_S = Duration.ofMillis(20000);

    public static final Duration TIMEOUT_MEDIUM_LONG_3_M = Duration.ofMillis(180000);

    public static final Duration TIMEOUT_EXTRA_LONG_10_M = Duration.ofMillis(600000);

    private static EnvironmentConfiguration configuration;

    public MidPoint(EnvironmentConfiguration configuration) {
        Validate.notNull(configuration, "Environment configuration must not be null");

        this.configuration = configuration;

        init();
    }

    private void init() {
        configuration.validate();
        Configuration.headless = configuration.isHeadless();
        Configuration.browserSize = "1600x1200";
        Configuration.baseUrl = configuration.getBaseUrl();
        System.setProperty("selenide.baseUrl", configuration.getBaseUrl());
        if (configuration.isUseRemoteWebdriver()) {
            System.setProperty("selenide.headlessStart", Boolean.toString(configuration.isHeadless()));
            System.setProperty("chromeoptions.args", "--no-sandbox");
            System.setProperty("selenide.remote", configuration.getRemoteWebdriverUrl());
        } else {
            System.setProperty(configuration.getDriver().getDriver(), configuration.getDriverLocation());
        }
        System.setProperty("selenide.browser", configuration.getDriver().name().toLowerCase());
        System.setProperty("locale", configuration.getLocale());
        System.setProperty("chromeoptions.args", "--remote-allow-origins=*");

        Configuration.timeout = 6000L;
    }

    public MidPoint open() {
        Selenide.open(configuration.getBaseUrl());

        return this;
    }

    public FormLoginPage formLogin() {
        return new FormLoginPage();
    }

    public SamlSelectPage samlSelect() {
        return new SamlSelectPage();
    }

    public MidPoint logout() {
        new LoggedUser().logout();

        return this;
    }
}
