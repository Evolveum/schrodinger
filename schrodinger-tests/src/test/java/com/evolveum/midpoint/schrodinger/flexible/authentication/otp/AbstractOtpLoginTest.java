package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.common.Clock;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.login.OtpCodePage;
import com.evolveum.midpoint.schrodinger.util.ImportOptions;
import com.evolveum.midpoint.schrodinger.util.Utils;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.assertj.core.api.Assertions;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Abstract base for two-module TOTP authentication tests (loginForm + TOTP, both REQUISITE).
 *
 * Each subclass supplies a security policy file via {@link #getSecurityPolicyFile()} and
 * provides concrete assertion methods. This allows the same test scenarios to be verified
 * against different module configurations (e.g. {@code acceptEmpty=false} vs {@code acceptEmpty=true}).
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractOtpLoginTest extends AbstractSchrodingerTest {

    protected static final File OBJECTS_DIR = new File("src/test/resources/objects");

    protected static final File SECURITY_POLICY_DEFAULT =
            new File(OBJECTS_DIR, "/securitypolicies/otp/default.xml");

    protected static final File USER_TOTP =
            new File(OBJECTS_DIR, "/users/user-totp.xml");

    protected static final File USER_NO_TOTP =
            new File(OBJECTS_DIR, "/users/user-no-totp.xml");

    protected static final String USER_NAME = "totp-user";
    protected static final String USER_PASSWORD = "Test5ecr3t";

    protected static final String USER_NO_TOTP_NAME = "no-totp-user";
    protected static final String USER_NO_TOTP_PASSWORD = "Test5ecr3t";

    /**
     * TOTP secret matching the credential in user-totp.xml.
     */
    protected static final String TOTP_SECRET = "R2YTPJHLUUYEJ2BUUEXDRWTMWMAU73ZF";

    protected static final String WRONG_OTP_CODE = "000000";

    private static final int MAX_FAILED_ATTEMPTS = 3;

    /**
     * Security policy to load at the start of this test class.
     */
    protected abstract File getSecurityPolicyFile();

    /**
     * Whether, under this policy, a user without TOTP credentials is expected
     * to be redirected to the OTP verification page after a successful password login.
     *
     * Returns {@code true} when {@code acceptEmpty=false} (user must go through OTP page).
     * Returns {@code false} when {@code acceptEmpty=true} (OTP module is called off; user
     * lands directly on the home page).
     */
    protected abstract boolean noTotpUserExpectsOtpPage();

    /**
     * After TOTP user submits correct password: should be on OTP verification page.
     */
    protected void assertAfterTotpUserCorrectPasswordLogin() {
        midPoint.otpCode().assertIsOnOtpPage();
    }

    /**
     * After TOTP user submits correct OTP code: should be logged in.
     */
    protected void assertTotpUserLoggedIn() {
        basicPage.assertUserMenuExist();
    }

    /**
     * After no-TOTP user submits correct password.
     * With {@code acceptEmpty=false}: should be on OTP page.
     * With {@code acceptEmpty=true}: should already be logged in.
     */
    protected abstract void assertAfterNoTotpUserCorrectPasswordLogin();

    protected abstract void assertAfterNoTotpUserOtpAttempt();

    /**
     * After a non-existent username is submitted: should be on login page with error.
     */
    protected void assertAfterBadUsernameLogin() {
        midPoint.formLogin()
                .assertIsOnLoginPage()
                .feedback()
                .assertError();
    }

    /**
     * After user submits correct username but wrong password: login page + error.
     */
    protected void assertAfterUserBadPasswordLogin() {
        midPoint.formLogin()
                .assertIsOnLoginPage()
                .feedback()
                .assertError();
    }

    /**
     * After TOTP user submits one wrong OTP code.
     * Expected: still on OTP page with an error (retry is available).
     */
    protected void assertAfterTotpUserOneWrongOtpCode() {
        midPoint.otpCode()
                .assertIsOnOtpPage()
                .feedback()
                .assertError();
    }

    /**
     * After no-TOTP user submits one wrong OTP code.
     * Only called when {@link #noTotpUserExpectsOtpPage()} is {@code true}.
     */
    protected abstract void assertAfterNoTotpUserOneWrongOtpCode();

    /**
     * After the nth wrong OTP code (n = 1, 2, or 3) for the TOTP user, before lockout.
     * Expected: still on OTP page with an error.
     *
     * @param attempt 1-based attempt number (1, 2, or 3)
     */
    protected void assertAfterWrongOtpCodeBeforeLockout(int attempt) {
        midPoint.otpCode()
                .assertIsOnOtpPage()
                .feedback()
                .assertError()
                .assertMessageExists("Incorrect OTP code");
    }

    /**
     * After the 4th wrong OTP code, which triggers account lockout for the TOTP user.
     * Expected: on login page with a "user is locked" message.
     */
    protected void assertAfterOtpLockout() {
        midPoint.formLogin()
                .assertIsOnLoginPage()
                .assertErrorText("User is locked, please wait.");
    }

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.beforeClass();

        addObjectFromFile(getSecurityPolicyFile());

        // SystemObjectCache caches the active SecurityPolicy for up to 1000ms
        Selenide.sleep(1500L);
    }

    @BeforeMethod
    public void beforeMethod() {
        addObjectFromFile(USER_TOTP, new ImportOptions(false, true).createOptionList());
        addObjectFromFile(USER_NO_TOTP, new ImportOptions(false, true).createOptionList());
    }

    @AfterClass
    @Override
    public void afterClass() {
        ensureLoggedOut();

        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.afterClass();
    }

    /**
     * TOTP user: correct password -> assert on OTP page -> correct code -> assert logged in.
     */
    @Test
    public void test100TotpUserSuccessfulLogin() throws Exception {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NAME, USER_PASSWORD);
        waitForPageTransition();

        assertAfterTotpUserCorrectPasswordLogin();

        midPoint.otpCode().setCode(computeCode()).submit();
        Utils.waitForAjaxCallFinish();

        assertTotpUserLoggedIn();
    }

    /**
     * No-TOTP user: correct password -> assert current state (OTP page or home, depending
     * on policy) -> if OTP page expected, submit any code -> assert final outcome.
     */
    @Test
    public void test110NoTotpUserLogin() throws Exception {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NO_TOTP_NAME, USER_NO_TOTP_PASSWORD);
        waitForPageTransition();

        assertAfterNoTotpUserCorrectPasswordLogin();

        if (noTotpUserExpectsOtpPage()) {
            midPoint.otpCode().setCode(WRONG_OTP_CODE).submit();
            Utils.waitForAjaxCallFinish();

            assertAfterNoTotpUserOtpAttempt();
        }
    }

    /**
     * Non-existent username: must stay on the login page with an error.
     * Must NOT advance to the OTP page.
     */
    @Test
    public void test200BadUsernameLogin() {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login("no-such-user-xyz", "whatever");
        Utils.waitForAjaxCallFinish();

        assertAfterBadUsernameLogin();
        assertNotOnOtpPage();
    }

    /**
     * TOTP user — correct username, wrong password: must stay on the login page with an error.
     * Must NOT advance to the OTP page.
     */
    @Test
    public void test210TotpUserBadPasswordLogin() {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NAME, "wrong-password");
        Utils.waitForAjaxCallFinish();

        assertAfterUserBadPasswordLogin();
        assertNotOnOtpPage();
    }

    /**
     * No-TOTP user — correct username, wrong password: must stay on the login page with an error.
     * Must NOT advance to the OTP page.
     */
    @Test
    public void test220NoTotpUserBadPasswordLogin() {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NO_TOTP_NAME, "wrong-password");
        Utils.waitForAjaxCallFinish();

        assertAfterUserBadPasswordLogin();
        assertNotOnOtpPage();
    }

    /**
     * TOTP user: correct password -> OTP page -> one wrong
     * code -> must remain on OTP page (retry is available).
     */
    @Test
    public void test300TotpUserOneWrongOtpCode() throws Exception {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NAME, USER_PASSWORD);
        waitForPageTransition();

        Assertions.assertThat(WebDriverRunner.url())
                .as("After correct password the TOTP user must be on the OTP page")
                .contains(OtpCodePage.PAGE_PATH);

        midPoint.otpCode().setCode(WRONG_OTP_CODE).submit();
        waitForPageTransition();

        assertAfterTotpUserOneWrongOtpCode();
    }

    /**
     * No-TOTP user: correct password -> if policy routes through OTP page, submit wrong code
     * and assert the result. If acceptEmpty=true the user is already logged in after password.
     */
    @Test
    public void test310NoTotpUserOneWrongOtpCode() throws Exception {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NO_TOTP_NAME, USER_NO_TOTP_PASSWORD);
        waitForPageTransition();

        if (noTotpUserExpectsOtpPage()) {
            Assertions.assertThat(WebDriverRunner.url())
                    .as("No-TOTP user must be on the OTP page (acceptEmpty=false)")
                    .contains(OtpCodePage.PAGE_PATH);

            midPoint.otpCode().setCode(WRONG_OTP_CODE).submit();
            waitForPageTransition();

            assertAfterNoTotpUserOneWrongOtpCode();
        } else {
            // acceptEmpty=true: user skipped OTP entirely
            assertAfterNoTotpUserCorrectPasswordLogin();
        }
    }

    /**
     * TOTP user: correct password -> 3 wrong OTP codes (each stays on OTP page with
     * error) -> 4th wrong code -> account locked, user ends on login page with locked message.
     *
     * This test MUST run last because it permanently locks the TOTP user's OTP credential.
     * {@link #afterClass()} reloads the user to reset the lockout before the next test class runs.
     */
    @Test
    public void test400TotpUserFourWrongOtpCodesTriggersLockout() throws Exception {
        ensureLoggedOut();
        openFreshLoginPage();

        midPoint.formLogin().login(USER_NAME, USER_PASSWORD);
        waitForPageTransition();

        Assertions.assertThat(WebDriverRunner.url())
                .as("After correct password the TOTP user must be on the OTP page")
                .contains(OtpCodePage.PAGE_PATH);

        for (int attempt = 1; attempt <= MAX_FAILED_ATTEMPTS; attempt++) {
            midPoint.otpCode().setCode(WRONG_OTP_CODE).submit();
            waitForPageTransition();

            assertAfterWrongOtpCodeBeforeLockout(attempt);
        }

        // 4th attempt triggers lockout
        midPoint.otpCode().setCode(WRONG_OTP_CODE).submit();
        waitForPageTransition();

        assertAfterOtpLockout();
    }

    protected String computeCode() throws CodeGenerationException {
        CodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        return generator.generate(TOTP_SECRET, new Clock().getEpochSecond() / 30);
    }

    protected void ensureLoggedOut() {
        if (basicPage.userMenuExists()) {
            midPoint.logout();
        }
    }

    protected void openFreshLoginPage() {
        clearBrowser();
        midPoint.open();
    }

    protected void waitForPageTransition() throws InterruptedException {
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);
    }

    protected void assertNotOnOtpPage() {
        Assertions.assertThat(WebDriverRunner.url())
                .as("URL must NOT contain the OTP path")
                .doesNotContain(OtpCodePage.PAGE_PATH);
    }

    protected void assertOnOtpPage() {
        Assertions.assertThat(WebDriverRunner.url())
                .as("URL must contain the OTP path")
                .contains(OtpCodePage.PAGE_PATH);
    }
}
