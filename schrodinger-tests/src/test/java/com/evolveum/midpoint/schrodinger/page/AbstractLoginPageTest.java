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

import com.codeborne.selenide.Selenide;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.configuration.InfrastructurePanel;
import com.evolveum.midpoint.schrodinger.component.report.AuditRecordTable;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.login.SelfRegistrationPage;
import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.*;

/**
 * @author skublik
 */

public abstract class AbstractLoginPageTest extends AbstractSchrodingerTest {

    protected static final File MAIL_NONCE_VALUE_POLICY = new File("src/test/resources/objects/valuepolicies/mail-nonce.xml");
    protected static final File USER_WITHOUT_SUPERUSER = new File("src/test/resources/objects/users/user-without-superuser.xml");
    protected static final File SYSTEM_CONFIG_WITH_NOTIFICATION = new File("src/test/resources/objects/systemconfiguration/system-configuration-notification.xml");
    protected static final File CREATE_NAME_OBJECT_TEMPLATE = new File("src/test/resources/objects/objecttemplate/create-name-after-self-reg.xml");
    protected static final File ARCHETYPE_NODE_GUI = new File("src/test/resources/objects/archetypes/archetype-node-group-gui.xml");

    protected static final String NAME_OF_ENABLED_USER = "enabled_user";
    protected static final String NAME_OF_RESET_PASSWORD_TEST_USER = "resetPasswordTestUser";
    protected static final String PASSWORD_OF_ENABLED_USER = "Test5ecr3t";
    protected static final String MAIL_OF_ENABLED_USER = "enabled_user@evolveum.com";

    private static final File ENABLED_USER = new File("src/test/resources/objects/users/enabled-user.xml");
    private static final File END_USER_ROLE_UPDATED = new File("src/test/resources/objects/roles/role-enduser-updated-with-ui-auth.xml");
    private static final File RESET_PASSWORD_TEST_USER = new File("src/test/resources/objects/users/reset-password-test-user.xml");
    private static final File DISABLED_USER = new File("src/test/resources/objects/users/disabled-user.xml");
    private static final File ENABLED_USER_WITHOUT_AUTHORIZATIONS = new File("src/test/resources/objects/users/enabled-user-without-authorizations.xml");
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLoginPageTest.class);
    private static final String NOTIFICATIONS_FILE_NAME = "notification.txt";
    protected static File notificationFile;

    @BeforeClass
    @Override
    public void beforeClass() throws IOException{
        super.beforeClass();
        System.setProperty("midpoint.schrodinger","true");

        importObject(ENABLED_USER, true);
        importObject(RESET_PASSWORD_TEST_USER, true);
        importObject(DISABLED_USER, true);
        importObject(ENABLED_USER_WITHOUT_AUTHORIZATIONS, true);
        importObject(MAIL_NONCE_VALUE_POLICY, true);
        importObject(ARCHETYPE_NODE_GUI, true);
        importObject(END_USER_ROLE_UPDATED, true);
        importObject(getSecurityPolicyMailNonceResetPass(), true);
        //todo smth goes wrong after security policy import
        importObject(USER_WITHOUT_SUPERUSER, true);
        importObject(CREATE_NAME_OBJECT_TEMPLATE, true);
        notificationFile = new File(fetchTestHomeDir(), NOTIFICATIONS_FILE_NAME);
        if (!notificationFile.exists()) {
            notificationFile.createNewFile();
        }
        importObject(Utils.changeAttributeIfPresent(SYSTEM_CONFIG_WITH_NOTIFICATION, "redirectToFile",
                notificationFile.getAbsolutePath(), fetchTestHomeDir()), true);
//        basicPage.infrastructure();
        SystemPage systemPage = new SystemPage();
        PrismForm<InfrastructurePanel> infrastructureForm = basicPage.infrastructure().form();
        infrastructureForm.expandContainerPropertiesPanel("Infrastructure");
        infrastructureForm.showEmptyAttributes("Infrastructure");
        infrastructureForm.addAttributeValue("Public http url pattern", getConfiguration().getBaseUrl());
        systemPage.clickSave();
        systemPage.feedback().assertSuccess();
    }

    @Override
    protected boolean resetToDefaultAfterTests() {
        return true;
    }
//    @Override
//    protected List<File> getObjectListToImport(){
//        return Arrays.asList(USER_WITHOUT_SUPERUSER, ENABLED_USER, DISABLED_USER, ENABLED_USER_WITHOUT_AUTHORIZATIONS, MAIL_NONCE_VALUE_POLICY, ARCHETYPE_NODE_GUI,
//                getSecurityPolicyMailNonceResetPass(), CREATE_NAME_OBJECT_TEMPLATE, SYSTEM_CONFIG_WITH_NOTIFICATION);
//    }

    @Test
    public void test001loginLockoutUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        open("/login");
        for (int i = 0; i < 4; i++) {
            unsuccessfulLogin("enabled_user", "bad_password");
        }
        unsuccessfulLogin("enabled_user", "Test5ecr3t");
    }

    @Test
    public void test002loginDisabledUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        unsuccessfulLogin("disabled_user", "Test5ecr3t");
    }

    @Test
    public void test003loginEnabledUserWithoutAuthorizationsUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        unsuccessfulLogin("enabled_user_without_authorizations", "Test5ecr3t");
    }

    protected void unsuccessfulLogin(String username, String password){
        FormLoginPage login = midPoint.formLogin();
        login.login(username, password);

        login
                .feedback()
                .assertError();
    }

    @Test
    public void test010auditingSuccessfulLogin() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login("administrator", "Test5ecr3t");
        auditingSuccessfulLogin("administrator");
    }

    private void auditingSuccessfulLogin(String username) {
        AuditLogViewerPage auditLogViewer = basicPage.auditLogViewer();
        AuditRecordTable auditRecordsTable = auditLogViewer.table();
        auditRecordsTable.checkInitiator(1, username);
        auditRecordsTable.checkEventType(1, "Create session");
        auditRecordsTable.checkOutcome(1, "Success");
    }

    @Test
    public void test011auditingFailLogin() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login("bad_administrator", "Test5ecr3t");
        login.login("administrator", "Test5ecr3t");

        AuditLogViewerPage auditLogViewer = basicPage.auditLogViewer();
        AuditRecordTable auditRecordsTable = auditLogViewer.table();
        auditRecordsTable.checkInitiator(2, "");
        auditRecordsTable.checkEventType(2, "Create session");
        auditRecordsTable.checkOutcome(2, "Fatal Error");
    }

    @Test
    public void test012auditingSuccessfulLogout() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login("administrator", "Test5ecr3t");
        basicPage.loggedUser().logout();
        login.login("administrator", "Test5ecr3t");

        AuditLogViewerPage auditLogViewer = basicPage.auditLogViewer();
        AuditRecordTable auditRecordsTable = auditLogViewer.table();
        auditRecordsTable.checkInitiator(2, "administrator");
        auditRecordsTable.checkEventType(2, "Terminate session");
        auditRecordsTable.checkOutcome(2, "Success");
    }

    protected abstract File getSecurityPolicyMailNonceResetPass();

    protected void clearNotificationFile() throws FileNotFoundException {
        new PrintWriter(notificationFile.getAbsolutePath()).close();
    }

    protected void securityOfSelfRegistrationPasswords(Consumer<SelfRegistrationPage> process) throws IOException, InterruptedException {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        open("/login");
        open("/");
        open("/");
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        SelfRegistrationPage registrationPage = login.register();
        process.accept(registrationPage);
        basicPage.feedback().isError();
        basicPage.feedback().assertMessageExists("Invalid request.");
    }
}
