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
package com.cola.lib.logging.beans;

import org.slf4j.Marker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;

/**
 * cola
 * Created by jiachen.shi on 7/5/2016.
 */
public class LoggingCondition implements Serializable {

    public transient static final String MARKER = "logging.marker";
    public transient static final String LEVEL = "logging.level";
    public transient static final String THROWABLE = "logging.throwable";
    public transient static final String LEVEL_WHITELIST = "logging.level.whitelist";
    public transient static final String MDCKEY_PREFIX = "loggging.";
    public transient static final String CONTENT = "logging.contents";

    private Marker marker;
    private Level level;
    private String throwable;
    private List<String> classNamesWhiteList;
    private Map<String, String> mdcKeysWhiteList;
    private List<String> content;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public List<String> getClassNamesWhiteList() {
        return classNamesWhiteList;
    }

    public void setClassNamesWhiteList(List<String> classNamesWhiteList) {
        this.classNamesWhiteList = classNamesWhiteList;
    }

    public Map<String, String> getMdcKeysWhiteList() {
        return mdcKeysWhiteList;
    }

    public void setMdcKeysWhiteList(Map<String, String> mdcKeysWhiteList) {
        this.mdcKeysWhiteList = mdcKeysWhiteList;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
