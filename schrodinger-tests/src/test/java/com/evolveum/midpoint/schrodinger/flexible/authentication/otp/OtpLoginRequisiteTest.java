package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import org.testng.annotations.Test;

import java.io.File;

/**
 * Runs the TOTP login scenarios with the {@code simple-otp.xml} security policy:
 * both modules are REQUISITE and the OTP module does NOT set {@code acceptEmpty},
 * meaning users without TOTP credentials are still routed through the OTP page.
 */
public class OtpLoginRequisiteTest extends AbstractOtpLoginTest {

    private static final File SECURITY_POLICY_SIMPLE_OTP =
            new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp.xml");

    @Override
    protected File getSecurityPolicyFile() {
        return SECURITY_POLICY_SIMPLE_OTP;
    }

    @Override
    protected boolean noTotpUserExpectsOtpPage() {
        return true;
    }

    @Override
    protected void assertAfterNoTotpUserCorrectPasswordLogin() {
        // acceptEmpty=false: user has no TOTP credentials but the module is still entered
        midPoint.otpCode().assertIsOnOtpPage();
    }

    @Override
    protected void assertAfterNoTotpUserOtpAttempt() {
        // No credentials exist for this user — any submitted code fails, user stays on OTP page with error
        midPoint.otpCode()
                .assertIsOnOtpPage()
                .feedback()
                .assertError();
    }

    @Override
    protected void assertAfterNoTotpUserOneWrongOtpCode() {
        // No TOTP credentials: any submitted code fails, user stays on OTP page with error
        midPoint.otpCode()
                .assertIsOnOtpPage()
                .feedback()
                .assertError();
    }
}
