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
package com.evolveum.midpoint.schrodinger.component.table;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.evolveum.midpoint.schrodinger.component.Component;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author skublik
 */

public class DirectIndirectAssignmentTable<T> extends Component<T, DirectIndirectAssignmentTable<T>> {

    public DirectIndirectAssignmentTable(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public boolean containsIndirectAssignments(String... expectedAssignments) {
        return containsAssignments("Indirect", expectedAssignments);
    }

    public boolean containsDirectAssignments(String... expectedAssignments) {
        return containsAssignments("Direct", expectedAssignments);
    }

    private boolean containsAssignments(String status, String... expectedAssignments) {
        ElementsCollection labels = getParentElement()
                .$$(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("span", "data-s-id", "label", "data-s-id", "3", status));
        List<String> indirectAssignments = new ArrayList<String>();
        for (SelenideElement label : labels) {
            if (!label.getText().isEmpty()) {
                indirectAssignments.add(label.getText());
            }
        }
        return indirectAssignments.containsAll(Arrays.asList(expectedAssignments));
    }

    public DirectIndirectAssignmentTable<T> assertIndirectAssignmentsExist(String... expectedAssignments) {
        assertion.assertTrue(containsIndirectAssignments(expectedAssignments));
        return this;
    }

    public DirectIndirectAssignmentTable<T> assertDirectAssignmentsExist(String... expectedAssignments) {
        assertion.assertTrue(containsDirectAssignments(expectedAssignments));
        return this;
    }

    public DirectIndirectAssignmentTable<T> assertAssignmentWithRelationExists(String relation, String... expectedAssignments) {
        ElementsCollection labels = getParentElement()
                .$$(Schrodinger.byAncestorFollowingSiblingDescendantOrSelfElementEnclosedValue("span", "data-s-id", "label",
                        "data-s-id", "9", relation));
        List<String> assignmentNamesList = new ArrayList<String>();
        for (SelenideElement label : labels) {
            if (!label.getText().isEmpty()) {
                assignmentNamesList.add(label.getText());
            }
        }
        assertion.assertTrue(assignmentNamesList.containsAll(Arrays.asList(expectedAssignments)), "Assignments doesn't exist.");
        return this;
    }

}
