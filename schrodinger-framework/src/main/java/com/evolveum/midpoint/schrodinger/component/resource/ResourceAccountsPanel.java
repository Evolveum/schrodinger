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
package com.evolveum.midpoint.schrodinger.component.resource;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.component.user.ProjectionsDropDown;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.activation.ActivationConfigurationStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.MappingsWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.mappings.credentials.CredentialsConfigurationStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.objecttype.ObjectTypeBasicInformationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization.CorrelationWizardStep;
import com.evolveum.midpoint.schrodinger.page.resource.wizard.synchronization.SynchronizationWizardStep;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by matus on 5/22/2018.
 */
public class ResourceAccountsPanel<T> extends Component<T, ResourceAccountsPanel<T>> {
    public ResourceAccountsPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public ResourceTaskQuickAccessDropDown<ResourceAccountsPanel<T>> tasks() {
        SelenideElement tasksButton = $x(".//div[@data-s-id='tasks']")
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        tasksButton
                .click();
        SelenideElement dropDownElement = tasksButton.$x(".//div[@data-s-id='dropDownMenu']")
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
        return new ResourceTaskQuickAccessDropDown<>(this, dropDownElement);
    }


//    public ResourceTaskQuickAccessDropDown<ResourceAccountsPanel<T>> importTask() {
//        SelenideElement importDiv = $(Schrodinger.byDataId("div", "import"));
//        importDiv.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//
//        SelenideElement dropDownElement = importDiv.lastChild().lastChild()
//                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
//
//        return new ResourceTaskQuickAccessDropDown<>(this, dropDownElement);
//    }

//    public ResourceTaskQuickAccessDropDown<ResourceAccountsPanel<T>> reconciliationTask() {
//        SelenideElement reconcileDiv = $(Schrodinger.byDataId("div", "reconciliation"));
//        reconcileDiv.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S).click();
//
//        SelenideElement dropDownElement = reconcileDiv.lastChild().lastChild()
//                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
//
//        return new ResourceTaskQuickAccessDropDown<>(this, dropDownElement);
//    }


//    public ResourceTaskQuickAccessDropDown<ResourceAccountsPanel<T>> liveSyncTask() {
//        SelenideElement liveSyncButton = getParentElement()
//                .find(Schrodinger.byDataId("liveSync"))
//                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);
//
//        liveSyncButton.find(By.tagName("button")).click();
//        Utils.waitForAjaxCallFinish();
//        SelenideElement dropDownMenu = liveSyncButton.$x(".//div[@data-s-id='dropDownMenu']")
//                .shouldHave(Condition.cssClass("show"), MidPoint.TIMEOUT_MEDIUM_6_S);
//        return new ResourceTaskQuickAccessDropDown<>(this, dropDownMenu);
//    }

    public ResourceAccountsPanel<T> clickSearchInRepository() {

        $(Schrodinger.byDataId("a", "repositorySearch"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        $(Schrodinger.byDataId("a", "repositorySearch"))
                .shouldBe(Condition.cssClass("active"), MidPoint.TIMEOUT_LONG_1_M);

        return this;
    }

    public ResourceAccountsPanel<T> clickSearchInResource() {
        SelenideElement resourceSearch = $(Schrodinger.byDataId("a", "resourceSearch"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S);
        sleep(500);
        resourceSearch.click();
        $(Schrodinger.byDataId("a", "resourceSearch"))
                .shouldBe(Condition.cssClass("active"), MidPoint.TIMEOUT_LONG_1_M);
        return this;
    }

    public ResourceAccountsPanel<T> reload() {
        toolbarButtonClickByTitle("ReloadableButton.reload");
        Selenide.sleep(5000);
        return this;
    }

    public ResourceAccountsPanel<T> refresh() {
        toolbarButtonClickByTitle("MainObjectListPanel.refresh");
        return ResourceAccountsPanel.this;
    }

    /**
     *
     * @param title can be a key or a translated title
     */
    private void toolbarButtonClickByTitle(String title) {
        table()
                .getToolbarButtonByTitleKey(title)
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
    }

    public ResourceAccountsPanel<T> assertTableContainsColumnWithValue(String columnResourceKey, String value) {
        table().assertTableContainsColumnWithValue(columnResourceKey, value);
        return this;
    }

    public ResourceAccountsPanel<T> assertTableDoesntContainColumnWithValue(String columnResourceKey, String value) {
        table().assertTableDoesntContainColumnWithValue(columnResourceKey, value);
        return this;
    }

    public ResourceShadowTable<ResourceAccountsPanel<T>> table() {

        SelenideElement element = $(Schrodinger.byDataId("div", "itemsTable"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S);

        return new ResourceShadowTable<>(this, element);
    }

    public void setIntent(String intent) {
        $(Schrodinger.byDataId("div", "intent")).shouldBe(Condition.visible, MidPoint.TIMEOUT_SHORT_4_S)
                .$(Schrodinger.byDataId("input", "input"))
                .setValue(intent);
    }

    public ObjectTypeBasicInformationWizardStep<ResourceAccountsPanel<T>> configureBasicAttributes() {
        clickButton("ResourceObjectTypePreviewTileType.BASIC");
        return new ObjectTypeBasicInformationWizardStep<>(ResourceAccountsPanel.this);
    }

    public MappingsWizardStep<ResourceAccountsPanel<T>> configureMappings() {
        clickButton("ResourceObjectTypePreviewTileType.ATTRIBUTE_MAPPING");
        return new MappingsWizardStep<>(ResourceAccountsPanel.this);
    }

    public SynchronizationWizardStep<ResourceAccountsPanel<T>> configureSynchronization() {
        clickButton("ResourceObjectTypePreviewTileType.SYNCHRONIZATION");
        return new SynchronizationWizardStep<>(ResourceAccountsPanel.this);
    }

    public CorrelationWizardStep<ResourceAccountsPanel<T>> configureCorrelation() {
        clickButton("ResourceObjectTypePreviewTileType.CORRELATION");
        return new CorrelationWizardStep<>(ResourceAccountsPanel.this);
    }

    public void configureCapabilities() {
        clickButton("ResourceObjectTypePreviewTileType.CAPABILITIES");
    }

    public ActivationConfigurationStep<ResourceAccountsPanel<T>> configureActivation() {
        clickButton("ResourceObjectTypePreviewTileType.ACTIVATION");
        return new ActivationConfigurationStep<>(ResourceAccountsPanel.this);
    }

    public CredentialsConfigurationStep<ResourceAccountsPanel<T>> configureCredentials() {
        clickButton("ResourceObjectTypePreviewTileType.CREDENTIALS");
        return new CredentialsConfigurationStep<>(ResourceAccountsPanel.this);
    }

    public void configureAssociations() {
        clickButton("ResourceObjectTypePreviewTileType.ASSOCIATIONS");
    }

    private void clickButton(String title) {
        String translatedTitle = Utils.translate(title);
        SelenideElement buttonsContainer = $(Schrodinger.byDataId("configuration"));
        Utils.scrollToElement(buttonsContainer);
        buttonsContainer
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
        ElementsCollection buttons = buttonsContainer.$$x(".//a");
        SelenideElement button = buttons.findBy(Condition.text(translatedTitle));
        if (!button.exists()) {
            return;
        }
        Utils.scrollToElement(button);
        button.shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
        Utils.waitForAjaxCallFinish();
    }

    public ProjectionsDropDown<ResourceAccountsPanel<T>> clickHeaderActionDropDown() {

        $(By.tagName("thead"))
                .$(Schrodinger.byDataId("inlineMenuPanel"))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();

        SelenideElement dropDownMenu = $(Schrodinger.byElementAttributeValue("ul", "class", "dropdown-menu pull-right"));

        return new ProjectionsDropDown<ResourceAccountsPanel<T>>(this, dropDownMenu);
    }

}
