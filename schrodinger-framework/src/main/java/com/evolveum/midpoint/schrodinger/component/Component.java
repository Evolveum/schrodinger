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
package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.util.AssertionWithScreenshot;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import org.testng.asserts.Assertion;

/**
 * Created by Viliam Repan (lazyman).
 */
public abstract class Component<T> {

    private T parent;

    private SelenideElement parentElement;

    protected static AssertionWithScreenshot assertion = new AssertionWithScreenshot();

    public Component(T parent) {
        this(parent, null);
    }

    public Component(T parent, SelenideElement parentElement) {
        this.parent = parent;
        this.parentElement = parentElement;
    }

    public T and() {
        return parent;
    }

    public T getParent() {
        return parent;
    }

    public SelenideElement getParentElement() {
        return parentElement;
    }

    protected SelenideElement getDisplayedElement(ElementsCollection elements) {
        for (SelenideElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        return null;
    }
}
