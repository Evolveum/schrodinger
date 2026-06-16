package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import org.testng.annotations.Test;

import java.io.File;

/**
 * Runs the TOTP login scenarios with the {@code simple-otp-enrolled.xml} security policy:
 * both modules are REQUISITE and the OTP module has {@code acceptEmpty=true}.
 * <p>
 * The key difference from {@link OtpLoginRequisiteTest}: users without TOTP credentials
 * have the OTP module "called off" and are logged in directly after a successful password
 * login, without visiting the OTP verification page.
 * <p>
 * Tests that involve the OTP page for the no-TOTP user are not applicable here
 * and are handled by the {@link #noTotpUserExpectsOtpPage()} guard in the parent.
 */
public class OtpLoginAcceptEmptyTest extends AbstractOtpLoginTest {

    private static final File SECURITY_POLICY_SIMPLE_OTP_ENROLLED =
            new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp-enrolled.xml");

    @Override
    protected File getSecurityPolicyFile() {
        return SECURITY_POLICY_SIMPLE_OTP_ENROLLED;
    }

    @Override
    protected boolean noTotpUserExpectsOtpPage() {
        // acceptEmpty=true: OTP module is skipped for users without TOTP credentials
        return false;
    }

    @Override
    protected void assertAfterNoTotpUserCorrectPasswordLogin() {
        // acceptEmpty=true: OTP module called off -> user is directly on home page
        basicPage.assertUserMenuExist();
    }

    @Override
    protected void assertAfterNoTotpUserOtpAttempt() {
        // Not called: noTotpUserExpectsOtpPage() returns false
        throw new AssertionError(
                "assertAfterNoTotpUserOtpAttempt must not be called when acceptEmpty=true");
    }

    @Override
    protected void assertAfterNoTotpUserOneWrongOtpCode() {
        // Not called: noTotpUserExpectsOtpPage() returns false
        throw new AssertionError(
                "assertAfterNoTotpUserOneWrongOtpCode must not be called when acceptEmpty=true");
    }
}
