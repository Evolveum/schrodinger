/*
 * Copyright (c) 2010-2026 Evolveum and contributors
 *
 * Licensed under the EUPL-1.2 or later.
 */
package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Validates that a custom emergency authentication sequence that requires TOTP
 * behaves identically to having OTP in the default sequence.
 *
 * Security policy ({@code emergency-otp-sequence.xml}) has two sequences:
 * <ul>
 *   <li>Default GUI sequence — loginForm only, SUFFICIENT, no OTP.</li>
 *   <li>Emergency sequence ({@code urlSuffix=emergency}) — loginForm + mytotp, both
 *       REQUISITE, {@code acceptEmpty=true} on the totp module.</li>
 * </ul>
 * Tests inherited from {@link AbstractOtpLoginTest} are run against the emergency sequence
 * by overriding {@link #openFreshLoginPage()} to navigate to the custom URL.
 */
public class OtpEmergencySequenceTest extends AbstractOtpLoginTest {

    /**
     * URL suffix of the emergency sequence as declared in the security-policy XML.
     * Must match the {@code <urlSuffix>} of the emergency channel.
     */
    private static final String EMERGENCY_URL_SUFFIX = "emergency";

    private static final File SECURITY_POLICY_EMERGENCY_SEQUENCE =
            new File(OBJECTS_DIR, "/securitypolicies/otp/emergency-otp-sequence.xml");

    @Override
    protected File getSecurityPolicyFile() {
        return SECURITY_POLICY_EMERGENCY_SEQUENCE;
    }

    @Override
    protected boolean noTotpUserExpectsOtpPage() {
        // acceptEmpty=true: the OTP module is called off for users without credentials.
        return false;
    }

    /**
     * After correct password on the emergency sequence the TOTP user must be on the
     * OTP verification page (same expectation as in the default requisite tests).
     */
    @Override
    protected void assertAfterTotpUserCorrectPasswordLogin() {
        midPoint.otpCode().assertIsOnOtpPage();
    }

    /**
     * acceptEmpty=true: no-TOTP user is logged in directly after password,
     * without visiting the OTP page.
     */
    @Override
    protected void assertAfterNoTotpUserCorrectPasswordLogin() {
        basicPage.assertUserMenuExist();
        assertNotOnOtpPage();
    }

    @Override
    protected void assertAfterNoTotpUserOtpAttempt() {
        // Never called: noTotpUserExpectsOtpPage() returns false.
        throw new AssertionError(
                "assertAfterNoTotpUserOtpAttempt must not be called — acceptEmpty=true");
    }

    @Override
    protected void assertAfterNoTotpUserOneWrongOtpCode() {
        // Never called: noTotpUserExpectsOtpPage() returns false.
        throw new AssertionError(
                "assertAfterNoTotpUserOneWrongOtpCode must not be called — acceptEmpty=true");
    }

    /**
     * Opens the emergency sequence login page instead of the default login page,
     * so all inherited tests exercise the custom channel URL.
     */
    @Override
    protected void openFreshLoginPage() {
        clearBrowser();
        // GUI authentication sequences are reachable at /auth/<urlSuffix>, not at the bare suffix
        // (see SchemaConstants.AUTH_MODULE_PREFIX and FormLoginPage.EMERGENCY_PATH).
        Selenide.open("/auth/" + EMERGENCY_URL_SUFFIX);
        Utils.waitForAjaxCallFinish();
    }

    /**
     * Additional test: default sequence (no OTP) still works while the emergency
     * sequence requires OTP. Verifies both paths co-exist correctly.
     *
     * Test with user that has totp configured.
     */
    @Test
    public void test500DefaultSequenceStillWorksWithoutOtp() throws InterruptedException {
        testDefaultSequenceStillWorksWithoutOtp(USER_NAME, USER_PASSWORD);
    }

    /**
     * Additional test: default sequence (no OTP) still works while the emergency
     * sequence requires OTP. Verifies both paths co-exist correctly. Test with no-totp-user.
     */
    @Test
    public void test520DefaultSequenceStillWorksWithoutOtp() throws InterruptedException {
        testDefaultSequenceStillWorksWithoutOtp(USER_NO_TOTP_NAME, USER_NO_TOTP_PASSWORD);
    }

    private void testDefaultSequenceStillWorksWithoutOtp(String username, String password) throws InterruptedException {
        ensureLoggedOut();
        clearBrowser();
        midPoint.open();    // default sequence URL

        midPoint.formLogin().login(username, password);
        waitForPageTransition();

        basicPage.assertUserMenuExist();
        assertNotOnOtpPage();
    }
}
