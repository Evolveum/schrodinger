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

import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.component.configuration.InfrastructureTab;
import com.evolveum.midpoint.schrodinger.component.configuration.NotificationsTab;
import com.evolveum.midpoint.schrodinger.component.report.AuditRecordTable;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;
import com.evolveum.midpoint.schrodinger.page.login.FormLoginPage;
import com.evolveum.midpoint.schrodinger.page.report.AuditLogViewerPage;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.*;

/**
 * @author skublik
 */

public abstract class AbstractLoginPageTest extends AbstractSchrodingerTest {

    protected static final File MAIL_NONCE_VALUE_POLICY = new File("src/test/resources/objects/valuepolicies/mail-nonce.xml");
    protected static final File USER_WITHOUT_SUPERUSER = new File("src/test/resources/objects/users/user-without-superuser.xml");
    protected static final File SYSTEM_CONFIG_WITH_NOTIFICATION = new File("src/test/resources/objects/systemconfiguration/system-configuration-notification.xml");
    protected static final File CREATE_NAME_OBJECT_TEMPLATE = new File("src/test/resources/objects/objecttemplate/create-name-after-self-reg.xml");
    protected static final File NOTIFICATION_FILE = new File("./target/notification.txt");
    protected static final File ARCHETYPE_NODE_GUI = new File("src/test/resources/objects/archetypes/archetype-node-group-gui.xml");

    protected static final String NAME_OF_ENABLED_USER = "enabled_user";
    protected static final String MAIL_OF_ENABLED_USER = "enabled_user@evolveum.com";

    private static final File ENABLED_USER = new File("src/test/resources/objects/users/enabled-user.xml");
    private static final File DISABLED_USER = new File("src/test/resources/objects/users/disabled-user.xml");
    private static final File ENABLED_USER_WITHOUT_AUTHORIZATIONS = new File("src/test/resources/objects/users/enabled-user-without-authorizations.xml");

    @BeforeClass
    @Override
    public void beforeClass() throws IOException{
        super.beforeClass();
        addObjectFromFile(ENABLED_USER, true);
        addObjectFromFile(DISABLED_USER, true);
        addObjectFromFile(ENABLED_USER_WITHOUT_AUTHORIZATIONS, true);
        addObjectFromFile(MAIL_NONCE_VALUE_POLICY, true);
        addObjectFromFile(ARCHETYPE_NODE_GUI, true);
        addObjectFromFile(getSecurityPolicyMailNonceResetPass(), true);
        //todo smth goes wrong after security policy import
        importObject(USER_WITHOUT_SUPERUSER, true);
        importObject(CREATE_NAME_OBJECT_TEMPLATE, true);
        importObject(SYSTEM_CONFIG_WITH_NOTIFICATION, true);
        basicPage.infrastructure();
        SystemPage systemPage = new SystemPage();
        PrismForm<InfrastructureTab> infrastructureForm = systemPage.infrastructureTab().form();
        infrastructureForm.expandContainerPropertiesPanel("Infrastructure");
        infrastructureForm.showEmptyAttributes("Infrastructure");
        infrastructureForm.addAttributeValue("publicHttpUrlPattern", getConfiguration().getBaseUrl());
        File notificationFile = NOTIFICATION_FILE;
        try {
            notificationFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationsTab notificationTab = systemPage.notificationsTab();
        notificationTab.setRedirectToFile(notificationFile.getAbsolutePath());
        systemPage.clickSave();
        systemPage.feedback().assertSuccess();
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
        unsuccessfulLogin("enabled_user", "5ecr3t");
    }

    @Test
    public void test002loginDisabledUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        unsuccessfulLogin("disabled_user", "5ecr3t");
    }

    @Test
    public void test003loginEnabledUserWithoutAuthorizationsUser() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        unsuccessfulLogin("enabled_user_without_authorizations", "5ecr3t");
    }

    protected void unsuccessfulLogin(String username, String password){
        FormLoginPage login = midPoint.formLogin();
        login.login(username, password);

        login
                .feedback()
                .assertError("0");
    }

    @Test
    public void test010auditingSuccessfulLogin() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        FormLoginPage login = midPoint.formLogin();
        login.login("administrator", "5ecr3t");
        auditingSuccessfulLogin("administrator");
    }

    protected void auditingSuccessfulLogin(String username) {
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
        login.login("bad_administrator", "5ecr3t");
        login.login("administrator", "5ecr3t");

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
        login.login("administrator", "5ecr3t");
        basicPage.loggedUser().logout();
        login.login("administrator", "5ecr3t");

        AuditLogViewerPage auditLogViewer = basicPage.auditLogViewer();
        AuditRecordTable auditRecordsTable = auditLogViewer.table();
        auditRecordsTable.checkInitiator(2, "administrator");
        auditRecordsTable.checkEventType(2, "Terminate session");
        auditRecordsTable.checkOutcome(2, "Success");
    }

    protected String readLastNotification() throws IOException {
        String separator = "============================================";
        byte[] encoded = Files.readAllBytes(Paths.get(NOTIFICATION_FILE.getAbsolutePath()));
        String notifications = new String(encoded, Charset.defaultCharset());
        return notifications.substring(notifications.lastIndexOf(separator) + separator.length(), notifications.length()-1);
    }

    protected abstract File getSecurityPolicyMailNonceResetPass();
}
