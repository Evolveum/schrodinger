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
package com.evolveum.midpoint.schrodinger.reports;

import com.codeborne.selenide.logevents.EventsCollector;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.logevents.SimpleReport;
import com.google.common.base.Joiner;

import java.util.Collections;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.logging.Logger;

public class SchrodingerSimpleReport extends SimpleReport {
    private static final Logger LOGGER = Logger.getLogger(SchrodingerSimpleReport.class.getName());

    public SchrodingerSimpleReport() {
        super();
    }

    @Override
    public void finish(String title) {
        EventsCollector logEventListener = (EventsCollector) SelenideLogger.removeListener("simpleReport");
        if (logEventListener == null) {
            LOGGER.warning("Can not publish report because Selenide logger has not started.");
        } else {
            OptionalInt maxLineLength = logEventListener.events().stream().map(LogEvent::getElement).map(String::length).mapToInt(Integer::intValue).max();
            int count = maxLineLength.orElse(0) >= 20 ? maxLineLength.getAsInt() + 1 : 20;
            StringBuilder sb = new StringBuilder();
            sb.append("Report for ").append(title).append('\n');
            String delimiter = '+' + Joiner.on('+').join(this.line(count), this.line(70), new Object[]{this.line(10), this.line(10)}) + "+\n";
            sb.append(delimiter);
            sb.append(String.format("|%-" + count + "s|%-70s|%-10s|%-10s|%n", "Element", "Subject", "Status", "ms."));
            sb.append(delimiter);
            Iterator var7 = logEventListener.events().iterator();

            while(var7.hasNext()) {
                LogEvent e = (LogEvent)var7.next();
                sb.append(String.format("|%-" + count + "s|%-70s|%-10s|%-10s|%n", e.getElement(), e.getSubject(), e.getStatus(), e.getDuration()));
            }

            sb.append(delimiter);
            LOGGER.info(sb.toString());
        }
    }

    private String line(int count) {
        return Joiner.on("").join(Collections.nCopies(count, "-"));
    }

}
