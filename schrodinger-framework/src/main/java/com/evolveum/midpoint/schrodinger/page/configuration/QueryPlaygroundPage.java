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
package com.evolveum.midpoint.schrodinger.page.configuration;

import com.codeborne.selenide.Condition;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class QueryPlaygroundPage extends BasicPage {

    public enum QueryPlaygroundSample{

        ALL_OBJECTS_IN_A_SUBTREE("All objects in a subtree"),
        ALL_OBJECTS_IN_ORG("All objects in an organization (directly)"),
        ALL_ORG_ROOTS("All organizational roots"),
        ALL_USERS("All users"),
        FIRST_10_USERS_WITH_FIRST_A("First 10 users starting with \"a\""),
        ORGS_WIT_TYPE1("Organizations of type \"type1\""),
        RESOURCE_SHADOWS("Shadows on a given resource"),
        USERS_CONTAINING_JACK_IN_NAME("Users containing \"jack\" in a name (normalized)"),
        USERS_IN_COST_CENTERS("Users in cost centers 100000-999999 or X100-X999"),
        USERS_WIT_FIRST_A_NORM("Users starting with \"a\" (normalized)"),
        USERS_WITH_DIRECT_ROLE_ASSIGNMENT("Users that have a direct assignment of a role"),
        USERS_WITH_ACTIVE_ROLE_ASSIGNEMT("Users that have active assignment of a role"),
        USERS_WITH_MAIL_DOMAIN("Users with a given mail domain"),
        USERS_WITH_GIVEN_NAME_JACK("Users with a given name of \"jack\" (normalized)"),
        USERS_WITH_LINKED_SHADOW("Users with linked shadow on a given resource");

        private String querySampleId;

        QueryPlaygroundSample(String querySampleName){
            this.querySampleId = querySampleName;
        }

        public String getQuerySampleId() {
            return querySampleId;
        }
    }

    public QueryPlaygroundPage setQuerySampleValue(QueryPlaygroundSample sample){
        $(Schrodinger.byDataId("querySample"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .$(By.tagName("select"))
                .selectOption(sample.getQuerySampleId());
        return this;
    }

    public void useInObjectListButtonClick(){
        // Use in object list button redirects to the object list page
        // according to the selected query sample type
        $(Schrodinger.byDataId("useInObjectList"))
                .shouldBe(Condition.visible, MidPoint.TIMEOUT_DEFAULT_2_S)
                .click();
    }
}
