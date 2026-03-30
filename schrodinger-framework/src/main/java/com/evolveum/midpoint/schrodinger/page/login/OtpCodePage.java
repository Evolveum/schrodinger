package com.evolveum.midpoint.schrodinger.page.login;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class OtpCodePage extends FormLoginPage {

    public static final String PAGE_PATH = "/otp_verify";

    public OtpCodePage setCode(String code) {
        $(Schrodinger.byDataId("code"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .setValue(code);

        return this;
    }

    public BasicPage submit() {
        $x(".//button[@type='submit']").click();
        Utils.waitForAjaxCallFinish();
        return new BasicPage();
    }
}
