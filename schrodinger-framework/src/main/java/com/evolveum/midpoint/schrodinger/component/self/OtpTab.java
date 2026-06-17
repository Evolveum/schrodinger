/*
 * Copyright (c) 2010-2026 Evolveum and contributors
 *
 * Licensed under the EUPL-1.2 or later.
 */
package com.evolveum.midpoint.schrodinger.component.self;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.self.CredentialsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents the OTP tab on the self-service credentials page ({@code /self/credentials}).
 * Wraps the {@code FocusOtpListPanel} and provides actions for adding, verifying, deleting,
 * and saving TOTP credentials.
 */
public class OtpTab extends Component<CredentialsPage, OtpTab> {

    public OtpTab(CredentialsPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    /**
     * Clicks the "add new credential" button, opening the OTP setup popup.
     */
    public OtpTab clickAddNewCredential() {
        getParentElement()
                .find(Schrodinger.byDataId("button"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Returns the currently open OTP setup popup.
     */
    public SelenideElement getPopup() {
        return Utils.getModalWindowSelenideElement();
    }

    /**
     * Reads the TOTP secret from the disabled input in the setup popup.
     * The secret is used to compute a verification code.
     */
    public String readSecretFromPopup(SelenideElement popup) {
        return popup.find(Schrodinger.byDataId("secret"))
                .shouldBe(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S)
                .getAttribute("value");
    }

    /**
     * Fills the credential name field in the setup popup.
     */
    public OtpTab fillCredentialName(SelenideElement popup, String name) {
        popup.find(Schrodinger.byDataId("name"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(name);
        return this;
    }

    /**
     * Fills the verification code field in the setup popup.
     */
    public OtpTab fillVerificationCode(SelenideElement popup, String code) {
        popup.find(Schrodinger.byDataId("codeGroup"))
                .find(Schrodinger.byDataId("code"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(code);
        return this;
    }

    /**
     * Clicks the "Verify & Enable" confirm button in the setup popup.
     */
    public OtpTab clickConfirmInPopup(SelenideElement popup) {
        popup.find(Schrodinger.byDataId("confirm"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Waits until the modal dialog is no longer visible.
     */
    public OtpTab waitForPopupToClose() {
        $(Schrodinger.byDataId("mainPopup"))
                .find(Schrodinger.byDataId("div", "overlay"))
                .shouldNotBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Clicks the delete button on the first row in the credential list.
     */
    public OtpTab deleteFirstCredential() {
        getOtpTableRows().get(0)
                .find(Schrodinger.byDataId("delete"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Clicks the Save button to persist credential changes.
     */
    public CredentialsPage save() throws InterruptedException {
        $(Schrodinger.byDataId("save"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        Thread.sleep(2000L);
        return getParent();
    }

    /**
     * Returns the number of credential rows currently visible in the list.
     */
    public int countCredentials() {
        return getOtpTableRows().size();
    }

    private ElementsCollection getOtpTableRows() {
        return $(Schrodinger.byDataId("tableContainer"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S)
                .findAll("tbody tr");
    }
}
