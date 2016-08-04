/*
 * Copyright 2002-${Year} the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cola.libs.ruleengine.beans;

/**
 * cola
 * Created by jiachen.shi on 8/2/2016.
 */
public class KieBaseModel {

    public enum EqualityBehaviorType {
        EQUALITY, IDENTITY
    }

    public enum EventProcessingModeType {
        CLOUD, STREAM
    }

    private String name;
    private EqualityBehaviorType equalsBehavior;
    private EventProcessingModeType eventProcessingMode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EqualityBehaviorType getEqualsBehavior() {
        return equalsBehavior;
    }

    public void setEqualsBehavior(EqualityBehaviorType equalsBehavior) {
        this.equalsBehavior = equalsBehavior;
    }

    public EventProcessingModeType getEventProcessingMode() {
        return eventProcessingMode;
    }

    public void setEventProcessingMode(EventProcessingModeType eventProcessingMode) {
        this.eventProcessingMode = eventProcessingMode;
    }
}
