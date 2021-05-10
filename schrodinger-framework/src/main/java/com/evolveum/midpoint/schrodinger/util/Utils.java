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

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.user.ProgressPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.AssignmentsTab;
import com.evolveum.midpoint.schrodinger.component.common.CheckFormGroupPanel;
import com.evolveum.midpoint.schrodinger.component.common.table.AbstractTableWithPrismView;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ActivationType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static <P extends AssignmentHolderDetailsPage> void removeAssignments(AssignmentsTab<P> tab, String... assignments) {
        AbstractTableWithPrismView<AssignmentsTab<P>> table = tab.table();
        for (String assignment : assignments) {
            table.removeByName(assignment);
        }
        tab
        .and()
        .clickSave()
            .feedback()
                .isSuccess();

    }

    public static <P extends AssignmentHolderDetailsPage> void removeAllAssignments(AssignmentsTab<P> tab) {
        tab
                .table()
                    .selectHeaderCheckbox()
                    .clickHeaderActionButton("fa fa-minus ")
                .and()
            .and()
        .clickSave()
            .feedback()
                .isSuccess();
    }

    public static <P extends AssignmentHolderDetailsPage> void addAsignments(AssignmentsTab<P> tab, String... assignments){
        for (String assignment : assignments) {
            tab.clickAddAssignemnt()
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

        tab.and()
            .clickSave()
                .feedback()
                    .isSuccess();
    }

    public static <P extends AssignmentHolderDetailsPage> void setStatusForAssignment(AssignmentsTab<P> tab, String assignment, String status) {
        tab.table()
                    .clickByName(assignment)
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
        return getModalWindowSelenideElement(MidPoint.TIMEOUT_DEFAULT_2_S);
    }

    public static SelenideElement getModalWindowSelenideElement(long waitTime) {
        return $(By.className("wicket-modal")).waitUntil(Condition.appear, waitTime);
    }

    public static File changeResourceFilePathInXml(File resourceXml, String newFilePathValue) throws IOException {
        String content = FileUtils.readFileToString(resourceXml, "UTF-8");
        int startIndex = content.indexOf(":filePath>") + 10;
        int endIndex = content.indexOf("</", startIndex);
        content = content.substring(0, startIndex) + newFilePathValue + content.substring(endIndex);

        String home = System.getProperty("midpoint.home");
        File changedResource = new File(home + "temp.xml");
        FileUtils.writeStringToFile(changedResource, content, "UTF-8");
        return changedResource;
    }
}
