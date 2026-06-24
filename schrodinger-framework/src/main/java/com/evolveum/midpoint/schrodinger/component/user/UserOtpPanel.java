/*
 * Copyright (c) 2010-2026 Evolveum and contributors
 *
 * Licensed under the EUPL-1.2 or later.
 */
package com.evolveum.midpoint.schrodinger.component.user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents the OTP panel on the admin user details page (Users → user details → OTP panel).
 * Wraps the {@code OtpListPanel} and provides actions for adding, verifying, and deleting
 * TOTP credentials. Saving is done via the user page's own save button.
 */
public class UserOtpPanel extends Component<UserPage, UserOtpPanel> {

    public UserOtpPanel(UserPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    /**
     * Clicks the "add new credential" button, opening the OTP setup popup.
     */
    public UserOtpPanel clickAddNewCredential() {
        getParentElement()
                .find(Schrodinger.byDataId("button"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    public UserOtpPanel assertAddButtonNotVisible() {
        boolean exists = getParentElement()
                .find(Schrodinger.byDataId("button"))
                .exists();
        assertion.assertFalse(exists, "Add new credential button should not be visible.");
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
     */
    public String readSecretFromPopup(SelenideElement popup) {
        return popup.find(Schrodinger.byDataId("secret"))
                .shouldBe(Condition.exist, MidPoint.TIMEOUT_DEFAULT_2_S)
                .getAttribute("value");
    }

    /**
     * Fills the credential name field in the setup popup.
     */
    public UserOtpPanel fillCredentialName(SelenideElement popup, String name) {
        popup.find(Schrodinger.byDataId("name"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(name);
        return this;
    }

    /**
     * Fills the verification code field in the setup popup.
     */
    public UserOtpPanel fillVerificationCode(SelenideElement popup, String code) {
        popup.find(Schrodinger.byDataId("codeGroup"))
                .find(Schrodinger.byDataId("code"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(code);
        return this;
    }

    /**
     * Clicks the confirm button in the setup popup.
     */
    public UserOtpPanel clickConfirmInPopup(SelenideElement popup) {
        popup.find(Schrodinger.byDataId("confirm"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Waits until the modal dialog is no longer visible.
     */
    public UserOtpPanel waitForPopupToClose() {
        $(Schrodinger.byDataId("mainPopup"))
                .find(Schrodinger.byDataId("div", "overlay"))
                .shouldNotBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        Utils.waitForAjaxCallFinish();
        return this;
    }

    /**
     * Clicks the delete button on the first credential row.
     */
    public UserOtpPanel deleteFirstCredential() {
        getOtpTableRows().get(0)
                .find(Schrodinger.byDataId("delete"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        return this;
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

    public void assertFirstMarkedAsDeleted() {
        getOtpTableRows().get(0).has(Condition.cssClass("table-danger"));
    }
}
