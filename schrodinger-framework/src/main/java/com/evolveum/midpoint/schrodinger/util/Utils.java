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

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        SelenideElement checkBox = $(By.name(optionName)).waitUntil(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S);
        checkBox.setSelected(checked).waitUntil(Condition.checked, MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public static void setOptionCheckedById(String id, boolean checked) {
        SelenideElement checkBox = $(Schrodinger.byDataId(id)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        checkBox.setSelected(checked);
    }

    public static void setCheckFormGroupOptionCheckedById(String id, boolean checked) {
        CheckFormGroupPanel checkBoxGroup = new CheckFormGroupPanel(null,
                $(Schrodinger.byDataId(id)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        checkBoxGroup.setOptionCheckedById(checked);
    }

    public static void setCheckFormGroupOptionCheckedByTitleResourceKey(String titleResourceKey, boolean checked) {
        CheckFormGroupPanel checkBoxGroup = new CheckFormGroupPanel(null,
                $(Schrodinger.byDataResourceKey(titleResourceKey)).parent().parent().waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
        checkBoxGroup.setOptionCheckedById(checked);
    }

    public static void setCheckFormGroupOptionCheckedByValue(String value, boolean checked) {
        CheckFormGroupPanel checkBoxGroup = new CheckFormGroupPanel(null,
                $(withText(value)).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
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
        return getModalWindowSelenideElement(MidPoint.TIMEOUT_MEDIUM_6_S);
    }

    public static SelenideElement getModalWindowSelenideElement(long waitTime) {
        return $(By.className("modal-dialog")).waitUntil(Condition.appear, waitTime);
    }

    public static boolean isModalWindowSelenideElementVisible() {
        return $(By.className("modal-dialog")).isDisplayed();
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
        String body = notification.substring(notification.indexOf(bodyTag) + bodyTag.length(), notification.lastIndexOf("'"));
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

}
