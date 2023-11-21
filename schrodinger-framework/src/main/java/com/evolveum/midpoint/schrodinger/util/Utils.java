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
package com.evolveum.midpoint.schrodinger.util;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

import com.codeborne.selenide.*;
import com.evolveum.midpoint.schrodinger.component.modal.FocusSetAssignmentsModal;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsPanel;
import com.evolveum.midpoint.schrodinger.component.common.CheckFormGroupPanel;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ActivationType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Utils {

    public static <T> T createInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setOptionCheckedByName(String optionName, boolean checked) {
        SelenideElement checkBox = $(By.name(optionName)).shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        checkBox.setSelected(checked).shouldBe(Condition.checked, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public static void setOptionCheckedById(String id, boolean checked) {
        SelenideElement checkBox = $(Schrodinger.byDataId(id)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        checkBox.setSelected(checked);
    }

    public static void setCheckFormGroupOptionCheckedById(String id, boolean checked) {
        CheckFormGroupPanel checkBoxGroup = new CheckFormGroupPanel(null,
                $(Schrodinger.byDataId(id)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        checkBoxGroup.setOptionCheckedById(checked);
    }

    public static void setCheckFormGroupOptionCheckedByTitleResourceKey(String titleResourceKey, boolean checked) {
        CheckFormGroupPanel checkBoxGroup = new CheckFormGroupPanel(null,
                $(Schrodinger.byDataResourceKey(titleResourceKey)).parent().parent().shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        checkBoxGroup.setOptionCheckedById(checked);
    }

    public static void setCheckFormGroupOptionCheckedByValue(String value, boolean checked) {
        setCheckFormGroupOptionCheckedByValue(null, value, checked);
    }

    public static void setCheckFormGroupOptionCheckedByValue(SelenideElement parentPanel, String value, boolean checked) {
        CheckFormGroupPanel checkBoxGroup;
        if (parentPanel == null) {
            checkBoxGroup = new CheckFormGroupPanel(null,
                    $(withText(value)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).parent());
        } else {
            checkBoxGroup = new CheckFormGroupPanel(null,
                    parentPanel.$(withText(value)).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).parent());
        }
        checkBoxGroup.setOptionCheckedById(checked);
    }

    public static <P extends AssignmentHolderDetailsPage> void removeAssignments(AssignmentsPanel<P> tab, String... assignments) {
        AbstractTableWithPrismView<AssignmentsPanel<P>> table = tab.table();
        for (String assignment : assignments) {
            table.removeByName(assignment);
        }
        tab
        .and()
        .clickSave()
            .feedback()
                .isSuccess();

    }

    public static <P extends AssignmentHolderDetailsPage> void removeAllAssignments(AssignmentsPanel<P> tab) {
        tab
                .table()
                    .selectHeaderCheckbox()
                    .clickHeaderActionButton("fa fa-minus")
                .and()
            .and()
        .clickSave()
            .feedback()
                .isSuccess();
    }

    public static <P extends AssignmentHolderDetailsPage> void addAssignmentsWithDefaultRelationAndSave(AssignmentsPanel<P> tab, boolean checkIfSuccess, String... assignments){
        addAssignmentsWithRelationAndSave(tab, "", checkIfSuccess, assignments);
    }

    public static <P extends AssignmentHolderDetailsPage> void addAssignmentsWithRelationAndSave(AssignmentsPanel<P> tab, String relation,
                                                                                                 boolean checkIfSuccess, String... assignments){
        addAssignmentsWithRelation(tab, relation, assignments);
        ProgressPage progressPage = tab.and()
                .clickSave();
        if (checkIfSuccess) {
            progressPage
                    .feedback()
                            .assertSuccess();
        }
    }

    public static <P extends AssignmentHolderDetailsPage> void addAssignmentsWithDefaultRelation(AssignmentsPanel<P> tab, String... assignments) {
        addAssignmentsWithRelation(tab, "", assignments);
    }

    public static <P extends AssignmentHolderDetailsPage> void addAssignmentsWithRelation(AssignmentsPanel<P> tab, String relation,
                                                                                          String... assignments) {
        addPredefinedAssignmentByTitle(tab, relation, "", assignments);
    }

    public static <P extends AssignmentHolderDetailsPage> void addPredefinedAssignmentByTitle(AssignmentsPanel<P> tab, String relation,
                                                                                              String newAssignmentTitle, String... assignments) {
        for (String assignment : assignments) {
            FocusSetAssignmentsModal<AssignmentsPanel<P>> modal;
            if (StringUtils.isNotEmpty(newAssignmentTitle)) {
                modal = tab.clickAddAssignment(newAssignmentTitle);
            } else {
                modal = tab.clickAddAssignment();
            }
            if (StringUtils.isNotEmpty(relation)) {
                modal
                        .setRelation(relation);
            }
            modal
                        .table()
                            .search()
                            .byName()
                            .inputValue(assignment)
                            .updateSearch()
                            .and()
                        .selectCheckboxByName(assignment)
                        .and()
                    .clickAdd();
        }
    }

    public static <P extends AssignmentHolderDetailsPage> void setStatusForAssignment(AssignmentsPanel<P> tab, String assignment, String status) {
        tab.table()
                    .clickByName(assignment)
                        .selectFormTabByName("Activation")
                        .showEmptyAttributes("Activation")
                        .setDropDownAttributeValue(ActivationType.F_ADMINISTRATIVE_STATUS , status)
                        .and()
                    .and()
                .and()
                .clickSave()
                    .feedback()
                        .isSuccess();
    }

    public static SelenideElement getModalWindowSelenideElement() {
        return getModalWindowSelenideElement(MidPoint.TIMEOUT_LONG_20_S);
    }

    public static SelenideElement getModalWindowSelenideElement(Duration waitTime) {
        return $(Schrodinger.byDataId("mainPopup"))
                .find(Schrodinger.byDataId("div", "overlay"))
                .shouldBe(Condition.appear, waitTime);
    }

    public static boolean isModalWindowSelenideElementVisible() {
        SelenideElement modal = null;
        try {
            modal = $(Schrodinger.byDataId("mainPopup"))
                    .find(Schrodinger.byDataId("div", "overlay"));
        } catch (Exception ex) {
            return false;
        }
        if (modal.exists() && modal.isDisplayed() && modal.has(Condition.cssClass("show"))) {
            return true;
        }
        return false;
    }

    public static File changeResourceFilePathInXml(File resourceXml, String newFilePathValue, String tempFilePath) throws IOException {
        String content = FileUtils.readFileToString(resourceXml, "UTF-8");
        int startIndex = content.indexOf(":filePath>") + 10;
        int endIndex = content.indexOf("</", startIndex);
        content = content.substring(0, startIndex) + newFilePathValue + content.substring(endIndex);

        String tempFile = StringUtils.isNotEmpty(tempFilePath) ? tempFilePath : System.getProperty("midpoint.home");
        File changedResource = new File(tempFile + "/temp.xml");
        FileUtils.writeStringToFile(changedResource, content, "UTF-8");
        return changedResource;
    }

    public static File changeAttributeIfPresent(File xmlFile, String attributeName, String newValue, String tempFilePath) throws IOException {
        String content = FileUtils.readFileToString(xmlFile, "UTF-8");
        String openAttrValue = "<" + attributeName + ">";
        String closeAttrValue = "</" + attributeName + ">";
        if (!content.contains(openAttrValue) || !content.contains(closeAttrValue)) {
            return xmlFile;
        }
        int startIndex = content.indexOf(openAttrValue) + openAttrValue.length();
        int endIndex = content.indexOf(closeAttrValue, startIndex);
        content = content.substring(0, startIndex) + newValue + content.substring(endIndex);

        String tempFile = StringUtils.isNotEmpty(tempFilePath) ? tempFilePath : System.getProperty("midpoint.home");
        File changedResource = new File(tempFile + "/temp.xml");
        FileUtils.writeStringToFile(changedResource, content, "UTF-8");
        return changedResource;


    }

    public static String readBodyOfLastNotification(File notificationFile) throws IOException {
        return readBodyOfLastNotification(Paths.get(notificationFile.getAbsolutePath()));
    }

    public static String readBodyOfLastNotification(Path notificationFilePath) throws IOException {
        String separator = "============================================";
        byte[] encoded = Files.readAllBytes(notificationFilePath);
        String notifications = new String(encoded, Charset.defaultCharset());
        if (!notifications.contains(separator)) {
            return "";
        }
        String notification = notifications.substring(notifications.lastIndexOf(separator) + separator.length(), notifications.length()-1);
        String bodyTag = "body='";
        if (!notifications.contains(bodyTag)) {
            return "";
        }
        String body = notification.substring(notification.indexOf(bodyTag) + bodyTag.length(), notification.indexOf(Utils.translate("PageBase.nonActiveSubscriptionMessage")));
        return body;
    }

    public static String readWholeLastNotification(File notificationFile) throws IOException {
        String separator = "============================================";
        byte[] encoded = Files.readAllBytes(Paths.get(notificationFile.getAbsolutePath()));
        String notifications = new String(encoded, Charset.defaultCharset());
        if (!notifications.contains(separator)) {
            return "";
        }
        String notification = notifications.substring(notifications.lastIndexOf(separator) + separator.length(), notifications.length()-1);
        return notification;
    }

    public static String readSubjectOfLastNotification(File notificationFile) throws IOException {
        String separator = "============================================";
        byte[] encoded = Files.readAllBytes(Paths.get(notificationFile.getAbsolutePath()));
        String notifications = new String(encoded, Charset.defaultCharset());
        if (!notifications.contains(separator)) {
            return "";
        }
        String notification = notifications.substring(notifications.lastIndexOf(separator) + separator.length(), notifications.length()-1);
        String bodyTag = "subject='";
        if (!notifications.contains(bodyTag)) {
            return "";
        }
        int subjectStartIndex = notification.indexOf(bodyTag) + bodyTag.length();
        return notification.substring(subjectStartIndex, notification.indexOf("'", subjectStartIndex));
    }

    public static void waitForMainPanelOnDetailsPage() {
        waitForAjaxCallFinish();
        $(Schrodinger.byDataId("div", "mainPanel")).shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_1_M);
    }

    public static void waitForSummaryDisplayNameOnDetailsPage(String displayName) {
        waitForAjaxCallFinish();
        $(Schrodinger.byDataId("span", "summaryDisplayName"))
                .shouldHave(Condition.text(displayName), MidPoint.TIMEOUT_LONG_20_S);
    }

    public static void waitForAjaxCallFinish() {
        new WebDriverWait(WebDriverRunner.getWebDriver(), MidPoint.TIMEOUT_MEDIUM_6_S).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                if((Boolean) executor.executeScript("return window.jQuery != undefined")){
                    while(!(Boolean) executor.executeScript("return jQuery.active == 0")){
                        Selenide.sleep(1000);
                    }
                    return true;
                }
                return true;
            }
        });
    }

    public static String translate(String key) {
        return translate(key, null);
    }

    public static String translate(String key, String defaultValue) {
        String lang = System.getProperty("locale");
        if (StringUtils.isEmpty(lang)) {
            lang = "en";
        }

        String val = (defaultValue == null) ? key : defaultValue;
        ResourceBundle bundleMP;
        ResourceBundle bundleSch;
        try {
            bundleMP = ResourceBundle.getBundle("localization/Midpoint", new Locale(lang));
            bundleSch = ResourceBundle.getBundle("localization/schema", new Locale(lang));
        } catch (MissingResourceException e) {
            return (defaultValue != null) ? defaultValue : key;
        }
        if (bundleMP != null && bundleMP.containsKey(key)) {
            val = bundleMP.getString(key);
        } else if (bundleSch != null && bundleSch.containsKey(key)) {
            val = bundleSch.getString(key);
        }
        return val;
    }

    public static void setValueToElementAndFireBlurEvent(SelenideElement element, String value) {
        element.shouldBe(Condition.visible, MidPoint.TIMEOUT_MEDIUM_6_S);
        element.sendKeys(value);
        String answerInputId = element.getAttribute("id");
        Selenide.sleep(2000);
        ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("return document.getElementById(\"" + answerInputId + "\").blur();");
        Selenide.sleep(2000);

    }

    public static void scrollToElement(SelenideElement element) {
        if (element == null || !element.exists()) {
            return;
        }
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            try {
                WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), MidPoint.TIMEOUT_MEDIUM_6_S);
                Utils.waitForAjaxCallFinish();
                element.scrollIntoView(false);//"{behavior: \"instant\", block: \"center\", inline: \"center\"}");
                WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element));
                if (clickableElement != null && clickableElement.isDisplayed()) {
                    break;
                }
                Utils.waitForAjaxCallFinish();
            } catch (Exception e) {
                element.scrollIntoView(true);
            }
        }
    }

    public static boolean elementContainsCssClass(SelenideElement element, String cssClass) {
        if (element == null || !element.exists()) {
            return false;
        }
        String cssClassStr = element.getAttribute("class");
        return cssClassStr != null && cssClassStr.contains(cssClass);
    }

    /**
     *
     * @param title can be either title key or title value
     * @return
     */
    public static SelenideElement findTileElementByTitle(String title) {
        String translateTitle = translate(title);
        ElementsCollection tiles = $$x(".//div[@data-s-id='tile']");
        return tiles.findBy(Condition.text(translateTitle));
    }

    public static boolean elementContainsTextCaseInsensitive(SelenideElement el, String text) {
        try {
            return el.shouldHave(Condition.text(text)).exists();
        } catch (Exception e) {
            return false;
        }
    }

}
