/*
 * Copyright (c) 2024  Evolveum
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

public class ExecutedActionDto {

    private String objectType;
    private String operation;
    private String channel;
    private String countOK;
    private String lastOK;
    private String time;
    private String countFailure;

    public ExecutedActionDto(String objectType, String operation, String channel, String countOK, String lastOK,
                             String time, String countFailure) {
        this.objectType = objectType;
        this.operation = operation;
        this.channel = channel;
        this.countOK = countOK;
        this.lastOK = lastOK;
        this.time = time;
        this.countFailure = countFailure;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getOperation() {
        return operation;
    }

    public String getChannel() {
        return channel;
    }

    public String getCountOK() {
        return countOK;
    }

    public String getLastOK() {
        return lastOK;
    }

    public String getTime() {
        return time;
    }

    public String getCountFailure() {
        return countFailure;
    }
}
