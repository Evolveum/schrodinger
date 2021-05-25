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

import com.codeborne.selenide.Selenide;
import org.testng.Reporter;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

public class AssertionWithScreenshot extends Assertion {

    private String screenshotFileName;

    @Override
    public void onAssertFailure(IAssert<?> var1, AssertionError var2) {
        String fileName = screenshotFileName != null ? screenshotFileName : generateScreenshotName();
        Selenide.screenshot(fileName);
        super.onAssertFailure(var1, var2);
        throw new AssertionError(var2.getMessage() + ", screenshot is created " + fileName + ".png");
    }

    private String generateScreenshotName() {
        StackTraceElement[] stack = new Throwable().fillInStackTrace().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement el = stack[i];
            if (el.getMethodName().contains("test")) {
                return el.getClassName().substring(el.getClassName().lastIndexOf(".") + 1) + "_" + el.getMethodName();
            }
        }
        return "assertionError";
    }

    public AssertionWithScreenshot screenshotFileName(String screenshotFileName) {
        this.screenshotFileName = screenshotFileName;
        return this;
    }

}
