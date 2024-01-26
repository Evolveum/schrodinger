/*
 * Copyright (c) 2023  Evolveum
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

package com.evolveum.midpoint.schrodinger.component.task.dto;

/**
 * used to gather data for one row in Synchronization situation transitions table
 * on the Operation statistics panel on the Task details page
 */
public class SynchronizationSituationTransitionDto {

//    	Synchronization start	Synchronization end	Exclusion reason	Succeeded	Failed	Skipped	Total
public enum State {
        NO_RECORD("No record"),
        UNLINKED("Unlinked"),
        LINKED("Linked"),
        UNMATCHED("Unmatched"),
        PROTECTED("Protected");

        private String state;

        State(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

    }
    private State originalState;
    private State synchronizationStart;
    private State synchronizationEnd;
    private State exclusionReason;
    private String succeeded;
    private String failed;
    private String skipped;
    private String total;

    public String getOriginalState() {
        return originalState != null ? originalState.state : "";
    }

    public SynchronizationSituationTransitionDto originalState(State originalState) {
        this.originalState = originalState;
        return this;
    }

    public String getSynchronizationStart() {
        return synchronizationStart != null ? synchronizationStart.state : "";
    }

    public SynchronizationSituationTransitionDto synchronizationStart(State synchronizationStart) {
        this.synchronizationStart = synchronizationStart;
        return this;
    }

    public String getSynchronizationEnd() {
        return synchronizationEnd != null ? synchronizationEnd.state : "";
    }

    public SynchronizationSituationTransitionDto synchronizationEnd(State synchronizationEnd) {
        this.synchronizationEnd = synchronizationEnd;
        return this;
    }

    public String getExclusionReason() {
        return exclusionReason != null ? exclusionReason.state : "";
    }

    public SynchronizationSituationTransitionDto exclusionReason(State exclusionReason) {
        this.exclusionReason = exclusionReason;
        return this;
    }

    public String getSucceeded() {
        return succeeded;
    }

    public SynchronizationSituationTransitionDto succeeded(String succeeded) {
        this.succeeded = succeeded;
        return this;
    }

    public String getFailed() {
        return failed;
    }

    public SynchronizationSituationTransitionDto failed(String failed) {
        this.failed = failed;
        return this;
    }

    public String getSkipped() {
        return skipped;
    }

    public SynchronizationSituationTransitionDto skipped(String skipped) {
        this.skipped = skipped;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public SynchronizationSituationTransitionDto total(String total) {
        this.total = total;
        return this;
    }
}
