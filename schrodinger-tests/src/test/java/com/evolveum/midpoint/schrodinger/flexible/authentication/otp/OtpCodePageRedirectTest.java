/*
 * Copyright (c) 2010-2026 Evolveum and contributors
 *
 * Licensed under the EUPL-1.2 or later.
 */
package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.login.OtpCodePage;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.assertj.core.api.Assertions;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Verifies that direct navigation to the OTP code verification page ({@value OtpCodePage#PAGE_PATH})
 * is properly guarded:
 * <ul>
 *   <li>An unauthenticated user is redirected to the login page.</li>
 *   <li>An authenticated user who is not mid-authentication is redirected to the home page.</li>
 * </ul>
 *
 * Uses {@code simple-otp-enrolled.xml} so that the OTP module and its URL are registered,
 * making the verification page a real (but guarded) endpoint.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OtpCodePageRedirectTest extends AbstractSchrodingerTest {

    private static final File OBJECTS_DIR = new File("src/test/resources/objects");

    private static final File SECURITY_POLICY_DEFAULT =
            new File(OBJECTS_DIR, "/securitypolicies/otp/default.xml");

    private static final File SECURITY_POLICY_OTP_ENROLLED =
            new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp-enrolled.xml");

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.beforeClass();

        addObjectFromFile(SECURITY_POLICY_OTP_ENROLLED);
    }

    @AfterClass
    @Override
    public void afterClass() {
        reloginAsAdministrator();
        addObjectFromFile(SECURITY_POLICY_DEFAULT);
        super.afterClass();
    }

    /**
     * An unauthenticated user navigating directly to the OTP verification URL must be
     * redirected to the login page, not shown the OTP code form.
     */
    @Test
    public void test100UnauthenticatedAccessRedirectsToLoginPage() throws InterruptedException {
        clearBrowser();

        Selenide.open(OtpCodePage.PAGE_PATH);
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);

        midPoint.formLogin().assertIsOnLoginPage();

        Assertions.assertThat(WebDriverRunner.url())
                .as("Unauthenticated access to OTP page must redirect to login, not stay on OTP path")
                .doesNotContain(OtpCodePage.PAGE_PATH);
    }

    /**
     * An already-authenticated user navigating directly to the OTP verification URL must be
     * redirected to the home page (dashboard), not shown the OTP code form.
     * The user is not in the middle of a multi-step authentication, so the OTP step is irrelevant.
     */
    @Test
    public void test200AuthenticatedAccessRedirectsToHomePage() throws InterruptedException {
        reloginAsAdministrator();
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);
        basicPage.assertUserMenuExist();

        Selenide.open(OtpCodePage.PAGE_PATH);
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);

        basicPage.assertUserMenuExist();

        Assertions.assertThat(WebDriverRunner.url())
                .as("Authenticated access to OTP page must redirect to home, not stay on OTP path")
                .doesNotContain(OtpCodePage.PAGE_PATH);
    }
}
