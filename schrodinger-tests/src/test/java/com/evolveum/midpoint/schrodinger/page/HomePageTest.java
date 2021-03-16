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

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

/**
 * @author skublik
 */

public class HomePageTest extends AbstractSchrodingerTest {

    @Test
    public void test001OpenPageWithSlashOnEndOfUrl() {
        open("/self/dashboard/");
        //when request will be redirect to error page, then couldn't click on home menu button
        basicPage.home();
    }

    @Test
    public void test002OpenPageWithoutSlashOnEndOfUrl() {
        open("/self/dashboard");
        //when request will be redirect to error page, then couldn't click on home menu button
        basicPage.home();
    }
}
