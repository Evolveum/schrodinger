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
package com.evolveum.midpoint.schrodinger.component.task;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.common.table.Table;
import com.evolveum.midpoint.schrodinger.component.common.table.TableRow;
import com.evolveum.midpoint.schrodinger.page.task.TaskPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.util.Objects;

/**
 * @author honchar
 */
public class ResultTab extends Component<TaskPage> {

    public ResultTab(TaskPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public String getOperationValueByToken(String tokenValue){
        return getColumnValueByToken(tokenValue, "Operation");
    }

    public String getStatusValueByToken(String tokenValue){
        return getColumnValueByToken(tokenValue, "Status");
    }

    public String getTimestampValueByToken(String tokenValue){
        return getColumnValueByToken(tokenValue, "Timestamp");
    }

    public String getMessageValueByToken(String tokenValue){
        return getColumnValueByToken(tokenValue, "Message");
    }

    public String getColumnValueByToken(String tokenValue, String columnName) {
        TableRow row = getResultsTableRowByToken(tokenValue);
        if (row == null) {
            return null;
        }
        SelenideElement cell = row.getColumnCellElementByColumnName(columnName);
        if (cell == null) {
            return null;
        }
        return cell.getText();
    }

    public TableRow<ResultTab, Table<ResultTab>> getResultsTableRowByToken(String tokenValue) {
        return getResultsTable().rowByColumnResourceKey("pageTaskEdit.opResult.token", tokenValue);
    }

    public Table<ResultTab> getResultsTable() {
        return new Table<>(this, $(Schrodinger.byDataId("operationResult")).waitUntil(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S));
    }

    public ResultTab assertOperationValueByTokenMatch(String tokenValue, String expectedValue) {
        String realValue = getOperationValueByToken(tokenValue);
        assertion.assertEquals(realValue, expectedValue, "'Operation' value doesn't match to " + expectedValue);
        return this;
    }

    public ResultTab assertStatusValueByTokenMatch(String tokenValue, String expectedValue) {
        String realValue = getStatusValueByToken(tokenValue);
        assertion.assertEquals(realValue, expectedValue, "'Status' value doesn't match to " + expectedValue);
        return this;
    }

    public ResultTab assertTimestampValueByTokenMatch(String tokenValue, String expectedValue) {
        String realValue = getTimestampValueByToken(tokenValue);
        assertion.assertEquals(realValue, expectedValue, "'Timestamp' value doesn't match to " + expectedValue);
        return this;
    }

    public ResultTab assertMessageValueByTokenMatch(String tokenValue, String expectedValue) {
        String realValue = getMessageValueByToken(tokenValue);
        assertion.assertEquals(realValue, expectedValue, "'Message' value doesn't match to " + expectedValue);
        return this;
    }

}
