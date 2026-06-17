package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.component.self.OtpTab;
import com.evolveum.midpoint.schrodinger.util.Utils;
import com.evolveum.midpoint.xml.ns._public.common.common_3.OtpCredentialType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.OtpCredentialsType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.assertj.core.api.Assertions;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Tests TOTP credential management via self-service credentials page.
 * <p>
 * Uses a security policy with {@code acceptEmpty=true} so the user without TOTP credentials
 * can log in with password only and then manage their OTP credentials from self-service.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OtpSelfServiceCredentialsTest extends AbstractSchrodingerTest {

    private static final File OBJECTS_DIR = new File("src/test/resources/objects");

    private static final File SECURITY_POLICY_DEFAULT =
            new File(OBJECTS_DIR, "/securitypolicies/otp/default.xml");

    private static final File SECURITY_POLICY_OTP_ACCEPT_EMPTY =
            new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp-enrolled.xml");

    private static final File USER_FILE =
            new File(OBJECTS_DIR, "/users/user-no-totp.xml");

    private static final String USER_OID = "80a044e4-5f20-4e9d-b7a4-efb9d81cd523";
    private static final String USER_NAME = "no-totp-user";
    private static final String USER_PASSWORD = "Test5ecr3t";

    private static final String CREDENTIAL_NAME_1 = "MyTOTP-1";
    private static final String CREDENTIAL_NAME_2 = "MyTOTP-2";

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.beforeClass();

        addObjectFromFile(USER_FILE);
        addObjectFromFile(SECURITY_POLICY_OTP_ACCEPT_EMPTY);
    }

    @AfterClass
    @Override
    public void afterClass() {
        ensureLoggedOut();

        addObjectFromFile(USER_FILE);
        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.afterClass();
    }

    /**
     * Add first TOTP credential via self-service, verify it and save.
     * Assert user has exactly one verified credential with the expected name.
     */
    @Test
    public void test100AddFirstOtpCredential() throws Exception {
        loginAsTestUser();

        OtpTab otpTab = basicPage.credentials().otpTab();
        otpTab.clickAddNewCredential();
        SelenideElement popup = otpTab.getPopup();

        String secret = otpTab.readSecretFromPopup(popup);
        otpTab.fillCredentialName(popup, CREDENTIAL_NAME_1);
        otpTab.fillVerificationCode(popup, computeTotpCode(secret));
        otpTab.clickConfirmInPopup(popup);
        otpTab.waitForPopupToClose();
        otpTab.save();

        Assertions.assertThat(otpTab.countCredentials())
                .as("OTP tab should show 1 credential after adding the first one")
                .isEqualTo(1);

        List<OtpCredentialType> totps = fetchUserOtpCredentials();
        Assertions.assertThat(totps)
                .as("User should have 1 TOTP credential persisted")
                .hasSize(1);
        Assertions.assertThat(totps.get(0).getName())
                .as("Credential name should match")
                .isEqualTo(CREDENTIAL_NAME_1);
        Assertions.assertThat(totps.get(0).getVerified())
                .as("Credential should be verified")
                .isTrue();
    }

    /**
     * Add a second TOTP credential while one already exists.
     * Assert user has exactly two credentials persisted.
     */
    @Test(dependsOnMethods = "test100AddFirstOtpCredential")
    public void test200AddSecondOtpCredential() throws Exception {
        OtpTab otpTab = basicPage.credentials().otpTab();
        otpTab.clickAddNewCredential();
        SelenideElement popup = otpTab.getPopup();

        String secret = otpTab.readSecretFromPopup(popup);
        otpTab.fillCredentialName(popup, CREDENTIAL_NAME_2);
        otpTab.fillVerificationCode(popup, computeTotpCode(secret));
        otpTab.clickConfirmInPopup(popup);
        otpTab.waitForPopupToClose();
        otpTab.save();

        Assertions.assertThat(otpTab.countCredentials())
                .as("OTP tab should show 2 credentials after adding the second one")
                .isEqualTo(2);

        List<OtpCredentialType> totps = fetchUserOtpCredentials();
        Assertions.assertThat(totps)
                .as("User should have 2 TOTP credentials persisted")
                .hasSize(2);
    }

    /**
     * Delete the first credential and save.
     * Assert user has exactly one credential remaining.
     */
    @Test(dependsOnMethods = "test200AddSecondOtpCredential")
    public void test300DeleteOneOtpCredential() throws Exception {
        OtpTab otpTab = basicPage.credentials().otpTab();
        otpTab.deleteFirstCredential();
        otpTab.save();

        Assertions.assertThat(otpTab.countCredentials())
                .as("OTP tab should show 1 credential after deleting one")
                .isEqualTo(1);

        List<OtpCredentialType> totps = fetchUserOtpCredentials();
        Assertions.assertThat(totps)
                .as("User should have 1 TOTP credential persisted after deletion")
                .hasSize(1);
    }

    /**
     * Delete the last remaining credential and save.
     * Assert user has no credentials left.
     */
    @Test(dependsOnMethods = "test300DeleteOneOtpCredential")
    public void test400DeleteLastOtpCredential() throws Exception {
        OtpTab otpTab = basicPage.credentials().otpTab();
        otpTab.deleteFirstCredential();
        otpTab.save();

        Assertions.assertThat(otpTab.countCredentials())
                .as("OTP tab should show 0 credentials after deleting the last one")
                .isEqualTo(0);

        List<OtpCredentialType> totps = fetchUserOtpCredentials();
        Assertions.assertThat(totps)
                .as("User should have no TOTP credentials persisted after deleting the last one")
                .isEmpty();
    }

    // --- helpers ---

    private void loginAsTestUser() throws InterruptedException {
        ensureLoggedOut();
        clearBrowser();
        midPoint.open();
        midPoint.formLogin().login(USER_NAME, USER_PASSWORD);
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);
        basicPage.assertUserMenuExist();
    }

    private void ensureLoggedOut() {
        if (basicPage.userMenuExists()) {
            midPoint.logout();
        }
    }

    /**
     * Fetches the user via REST (as admin) and returns their TOTP credential list.
     * Returns an empty list when no OTP credentials exist yet.
     */
    private List<OtpCredentialType> fetchUserOtpCredentials() throws Exception {
        UserType user = getUser(USER_OID);
        if (user.getCredentials() == null) {
            return List.of();
        }
        OtpCredentialsType otps = user.getCredentials().getOtps();
        if (otps == null || otps.getTotp() == null) {
            return List.of();
        }
        return otps.getTotp();
    }

    /**
     * Computes a 6-digit TOTP code from the given base32 secret using SHA-1 and 30-second time step.
     */
    private String computeTotpCode(String secret) throws CodeGenerationException {
        long timeStep = Instant.now().getEpochSecond() / 30;
        return new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6).generate(secret, timeStep);
    }
}
