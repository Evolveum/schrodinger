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
package com.evolveum.midpoint.schrodinger.component.org;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.page.org.OrgPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import com.evolveum.midpoint.schrodinger.util.Utils;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class OrgHierarchyPanel<T> extends Component<T, OrgHierarchyPanel<T>> {
    public OrgHierarchyPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public OrgHierarchyPanel<T> selectOrgInTree(String orgName) {
        boolean exist = getParentElement().$(Schrodinger.byElementValue("span", "class", "tree-label", orgName)).exists();
        if (!exist) {
            expandAllIfNeeded(orgName);
        }
        getParentElement().$(Schrodinger.byElementValue("span", "class", "tree-label", orgName))
                .shouldBe(Condition.appear, MidPoint.TIMEOUT_DEFAULT_2_S).click();

        getParentElement().$(By.xpath(".//div[contains(@class,\"tree-node\")][.//span[@class=\"tree-label\"][contains(.,\"" +  orgName + "\")]/..]"))
                .shouldHave(Condition.cssClass("success"), MidPoint.TIMEOUT_SHORT_4_S);

        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    private void expandAllIfNeeded(String orgName) {
        boolean existExpandButton = getParentElement().$(By.cssSelector(".tree-junction-collapsed")).exists();
        if (!existExpandButton) {
            showTreeNodeDropDownMenu(orgName).expandAll();
        } else {
            getParentElement().$(By.cssSelector(".tree-junction-collapsed")).shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                    .click();
            Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        }
    }

    private void clickOnTreeMenu() {
        getParentElement().$(Schrodinger.byDataId("div", "treeMenu")).click();
    }

    public boolean containsChildOrg(String parentOrg, Boolean expandParent, String... expectedChild){
        if (expandParent) {
            expandAllIfNeeded(parentOrg);
        }
        SelenideElement parentNode = getParentOrgNode(parentOrg);
        SelenideElement subtree = parentNode.$x(".//div[@"+Schrodinger.DATA_S_ID+"='subtree']");
        ElementsCollection childLabels = subtree.$$x(".//span[@"+Schrodinger.DATA_S_ID+"='label']");
        List<String> child = new ArrayList<String>();
        if (!subtree.exists()) {
            return false;
        }
        for (SelenideElement childLabel : childLabels) {
            child.add(childLabel.getText());
        }
        return child.containsAll(Arrays.asList(expectedChild));
    }

    public OrgHierarchyPanel<T> assertChildOrgsDontExist(String parentOrg, Boolean expandParent){
        if (expandParent) {
            expandAllIfNeeded(parentOrg);
        }
        SelenideElement parentNode = getParentOrgNode(parentOrg);
        SelenideElement subtree = parentNode.$x(".//div[@"+Schrodinger.DATA_S_ID+"='subtree']");
        assertion.assertFalse(subtree.exists(), "Child orgs shouldn't exist, ");
        return this;
    }

    public boolean containsChildOrg(String parentOrg, String... expectedChild){
        return containsChildOrg(parentOrg, true, expectedChild);
    }

    private SelenideElement getParentOrgNode (String parentOrg) {
        selectOrgInTree(parentOrg);
        return getParentElement().$(By.cssSelector(".tree-node.success")).parent();
    }

    public OrgHierarchyPanel<T> expandOrg(String orgName) {
        selectOrgInTree(orgName);
        selectOrgInTree(orgName);
        SelenideElement node = getParentElement().$(By.cssSelector(".tree-node.success"));
        SelenideElement expandButton = node.$x(".//a[@" + Schrodinger.DATA_S_ID + "='junction']");
        if (expandButton.has(Condition.cssClass("tree-junction-collapsed"))) {
            expandButton.shouldBe(Condition.appear, MidPoint.TIMEOUT_MEDIUM_6_S).click();
            expandButton.shouldBe(Condition.cssClass("tree-junction-collapsed"), MidPoint.TIMEOUT_MEDIUM_6_S);
        }
        return this;
    }

    public OrgTreeNodeDropDown<OrgHierarchyPanel> showTreeNodeDropDownMenu(String orgName) {
        Utils.waitForAjaxCallFinish();
        SelenideElement parentNode = getParentOrgNode(orgName);
        SelenideElement node = parentNode.$x(".//div[@"+Schrodinger.DATA_S_ID+"='node']");
        SelenideElement menuButton = node.$x(".//button[@data-toggle='dropdown']");
        menuButton.shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S).click();
//        Selenide.sleep(MidPoint.TIMEOUT_MEDIUM_6_S.getSeconds());
//        menuButton.shouldBe(Condition.attribute("aria-expanded", "true"), MidPoint.TIMEOUT_LONG_20_S);

        SelenideElement menu = node.$x(".//div[@" + Schrodinger.DATA_S_ID + "=\"dropDownMenu\"]").shouldBe(Condition.visible, MidPoint.TIMEOUT_LONG_20_S);

        return new OrgTreeNodeDropDown<OrgHierarchyPanel>(this, menu);
    }

    public OrgHierarchyPanel<T> assertChildOrgExists(String parentOrg, String... expectedChild){
        return assertChildOrgExists(parentOrg, true, expectedChild);
    }

    public OrgHierarchyPanel<T> assertChildOrgExists(String parentOrg, Boolean expandParent, String... expectedChild){
        assertion.assertTrue(containsChildOrg(parentOrg,expandParent, expectedChild), "Organization " + parentOrg + " doesn't contain expected children orgs.");
        return this;
    }

    public OrgHierarchyPanel<T> assertChildOrgDoesntExist(String parentOrg, String... expectedChild){
        return assertChildOrgDoesntExist(parentOrg, true, expectedChild);
    }

    public OrgHierarchyPanel<T> assertChildOrgDoesntExist(String parentOrg, Boolean expandParent, String... expectedChild){
        assertion.assertFalse(containsChildOrg(parentOrg, expectedChild), "Organization " + parentOrg + " doesn't contain expected children orgs.");
        return this;
    }
}
