package com.evolveum.midpoint.schrodinger.flexible.authentication.otp;

import com.codeborne.selenide.WebDriverRunner;
import com.evolveum.midpoint.common.Clock;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.login.OtpCodePage;
import com.evolveum.midpoint.schrodinger.util.Utils;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Viliam Repan
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OtpLoginPageTest extends AbstractSchrodingerTest {

    private static final File OBJECTS_DIR = new File("src/test/resources/objects");

    private static final File SECURITY_POLICY_DEFAULT = new File(OBJECTS_DIR, "/securitypolicies/otp/default.xml");

    private static final File SECURITY_POLICY_SIMPLE_OTP = new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp.xml");

    private static final File SECURITY_POLICY_SIMPLE_OTP_ENROLLED = new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp-enrolled.xml");

    private static final File SECURITY_POLICY_SIMPLE_OTP_CUSTOM_SEQUENCE = new File(OBJECTS_DIR, "/securitypolicies/otp/simple-otp-custom-sequence.xml");

    private static final File USER_TOTP = new File(OBJECTS_DIR, "/users/user-totp.xml");

    private static final File USER_NO_TOTP = new File(OBJECTS_DIR, "/users/user-no-totp.xml");

    private static final String TOTP_SECRET = "R2YTPJHLUUYEJ2BUUEXDRWTMWMAU73ZF";

    @Autowired
    private Clock clock;

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.beforeClass();

        addObjectFromFile(USER_TOTP);
        addObjectFromFile(USER_NO_TOTP);
    }

    @AfterClass
    @Override
    public void afterClass() {
        if (basicPage.userMenuExists()) {
            midPoint.logout();
        }

        addObjectFromFile(SECURITY_POLICY_DEFAULT);

        super.afterClass();
    }

    /**
     * Both form login and OTP login modules are required.
     */
    @Test
    public void test100LoginWithFormLoginAndOtp() throws Exception {
        addObjectFromFile(SECURITY_POLICY_SIMPLE_OTP);

        loginWithOtp("totp-user", "Test5ecr3t", computeCode());

        basicPage.assertUserMenuExist();
    }

    @Test
    public void test110LoginWithWrongCode() throws Exception {
        loginWithOtp("totp-user", "Test5ecr3t", "12345");   // wrong code

        midPoint.formLogin()
                .assertIsOnLoginPage()
                .feedback()
                .assertError();

        // todo assert proper error message
    }

    @Test
    public void test200LoginEnrolled() throws Exception {
        addObjectFromFile(SECURITY_POLICY_SIMPLE_OTP_ENROLLED);

        loginWithOtp("totp-user", "Test5ecr3t", computeCode());

        basicPage.assertUserMenuExist();
    }

    @Test
    public void test210LoginNotEnrolled() throws Exception {
        midPoint.logout();

        clearBrowser();

        midPoint.open();

        FormLoginPage login = midPoint.formLogin();
        login.login("no-totp-user", "Test5ecr3t");

        basicPage.assertUserMenuExist();
    }

    private String computeCode() throws CodeGenerationException {
        CodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        return generator.generate(TOTP_SECRET, new Clock().getEpochSecond() / 30);
    }

    private void loginWithOtp(String username, String password, String otpCode) throws InterruptedException {
        if (basicPage.userMenuExists()) {
            midPoint.logout();
        }

        clearBrowser();

        midPoint.open();

        FormLoginPage login = midPoint.formLogin();
        login.login(username, password);

        Utils.waitForAjaxCallFinish();

        Assertions.assertThat(WebDriverRunner.url()).contains(OtpCodePage.PAGE_PATH);

        OtpCodePage otp = midPoint.otpCode();
        otp.setCode(otpCode)
                .submit();
    }
}
