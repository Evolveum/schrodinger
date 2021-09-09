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
package com.evolveum.midpoint.schrodinger.component.configuration;

import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.PanelWithContainerWrapper;
import com.evolveum.midpoint.schrodinger.component.common.PrismContainerPanel;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.page.configuration.SystemPage;

public class CleanupPolicyPanel extends PanelWithContainerWrapper<SystemPage> {

    public CleanupPolicyPanel(SystemPage parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public CleanupPolicyPanel auditRecordsCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("Audit records", interval);
        return this;
    }

    public CleanupPolicyPanel auditRecordsMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("Audit records", maxRecordsToKeep);
        return this;
    }

    public String getAuditRecordsCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("Audit records");
    }

    public String getAuditRecordsMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("Audit records");
    }

    public CleanupPolicyPanel closedCertificationCampaignsCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.closedCertificationCampaigns", interval);
        return this;
    }

    public CleanupPolicyPanel closedCertificationMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.closedCertificationCampaigns", maxRecordsToKeep);
        return this;
    }

    public String getClosedCertificationCampaignsCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.closedCertificationCampaigns");
    }

    public String getClosedCertificationCampaignsMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.closedCertificationCampaigns");
    }

    public CleanupPolicyPanel closedTasksCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("Closed tasks", interval);
        return this;
    }

    public CleanupPolicyPanel closedTasksMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("Closed tasks", maxRecordsToKeep);
        return this;
    }

    public String getClosedTasksCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("Closed tasks");
    }

    public String getClosedTasksMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("Closed tasks");
    }

    public CleanupPolicyPanel closedCasesCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.closedCases", interval);
        return this;
    }

    public CleanupPolicyPanel closedCasesMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.closedCases", maxRecordsToKeep);
        return this;
    }

    public String getClosedCasesCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.closedCases");
    }

    public String getClosedCasesMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.closedCases");
    }

    public CleanupPolicyPanel outputReportsCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.outputReports", interval);
        return this;
    }

    public CleanupPolicyPanel outputReportsMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.outputReports", maxRecordsToKeep);
        return this;
    }

    public String getOutputReportsCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.outputReports");
    }

    public String getOutputReportsMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.outputReports");
    }

    public CleanupPolicyPanel objectResultsCleanupInterval(String interval) {
        setCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.objectResults", interval);
        return this;
    }

    public CleanupPolicyPanel objectResultsMaxRecordsToKeep(String maxRecordsToKeep) {
        setMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.objectResults", maxRecordsToKeep);
        return this;
    }

    public String getObjectResultsCleanupInterval() {
        return getCleanupIntervalValueByContainerResourceKey("CleanupPoliciesType.objectResults");
    }

    public String getObjectResultsMaxRecordsToKeep() {
        return getMaxRecordsToKeepValueByContainerResourceKey("CleanupPoliciesType.objectResults");
    }

    private void setCleanupIntervalValueByContainerResourceKey(String containerResourceKey, String interval) {
        getContainerFormPanel(containerResourceKey)
                .showEmptyAttributes(containerResourceKey)
                .addAttributeValue("Cleanup interval", interval);
    }

    private String getCleanupIntervalValueByContainerResourceKey(String containerResourceKey) {
        return getContainerFormPanel(containerResourceKey)
                .findProperty("Cleanup interval")
                    .getText();
    }

    private String getMaxRecordsToKeepValueByContainerResourceKey(String containerResourceKey) {
        return getContainerFormPanel(containerResourceKey)
                .findProperty("Max records to keep")
                    .getText();
    }

    private void setMaxRecordsToKeepValueByContainerResourceKey(String containerResourceKey, String maxRecordsToKeep) {
        getContainerFormPanel(containerResourceKey)
                .showEmptyAttributes(containerResourceKey)
                .addAttributeValue("Max records to keep", maxRecordsToKeep);
    }

    private PrismForm<PrismContainerPanel<PrismForm<PanelWithContainerWrapper<SystemPage>>>> getContainerFormPanel(String containerResourceKey) {
        return form()
                .expandContainerPropertiesPanel("Cleanup policy")
                .expandContainerPropertiesPanel(containerResourceKey)
                    .getPrismContainerPanelByResourceKey(containerResourceKey)
                        .getContainerFormFragment();
    }
}
