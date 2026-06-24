/*
 * Copyright (c) 2010-2026 Evolveum and contributors
 *
 * Licensed under the EUPL-1.2 or later.
 */
package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Verifies UI behavior when no TOTP module is defined in the security policy.
 *
 * Uses {@code default.xml}, which contains only a plain password (loginForm) sequence
 * with no {@code <totp>} module. Asserts that OTP-specific UI elements are absent
 * on both the self-service credentials page and the admin user-details page.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OtpNotConfiguredTest extends AbstractSchrodingerTest {

    private static final File OBJECTS_DIR = new File("src/test/resources/objects");

    private static final File SECURITY_POLICY_DEFAULT =
            new File(OBJECTS_DIR, "/securitypolicies/otp/default.xml");

    private static final File USER_FILE =
            new File(OBJECTS_DIR, "/users/user-no-totp.xml");

    private static final String USER_NAME = "no-totp-user";
    private static final String USER_PASSWORD = "Test5ecr3t";

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        addObjectFromFile(USER_FILE);
        addObjectFromFile(SECURITY_POLICY_DEFAULT);
    }

    @AfterClass
    @Override
    public void afterClass() {
        reloginAsAdministrator();
        addObjectFromFile(SECURITY_POLICY_DEFAULT);
        super.afterClass();
    }

    /**
     * The OTP tab must not appear on the self-service credentials page when the active
     * security policy has no TOTP module defined.
     */
    @Test
    public void test100OtpTabNotPresentOnSelfServiceCredentialsPage() throws InterruptedException {
        loginAsUser(USER_NAME, USER_PASSWORD);
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);

        basicPage.credentials()
                .assertOtpTabNotPresent();
    }

    /**
     * OTP panel on user details must have disabled add button.
     * Panel is visible based on default adminGuiConfiguration.
     */
    @Test
    public void test200CantADdOtpOnUserDetailsPage() throws InterruptedException {
        reloginAsAdministrator();
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);

        basicPage.listUsers()
                .table()
                .search()
                .byName()
                .inputValue(USER_NAME)
                .updateSearch()
                .and()
                .clickByName(USER_NAME)
                .selectOtpPanel()
                .assertAddButtonNotVisible();
    }
}
