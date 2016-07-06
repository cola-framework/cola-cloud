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
package com.cola.libs.logging.util;

import com.cola.libs.logging.beans.LoggerCondition;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;

/**
 * cola
 * Created by jiachen.shi on 7/5/2016.
 */
public class LoggerConditionHelper {

    public static final String class_name_separator = ";";

    public static LoggerCondition getLoggerConditionFromMDC(){

        LoggerCondition condition = new LoggerCondition();

        //get marker from MDC
        String markerStr = MDC.get(LoggerCondition.MARKER);
        if(StringUtils.isNotEmpty(markerStr)){
            Marker marker = MarkerFactory.getMarker(markerStr);
            condition.setMarker(marker);
        }
        //get level from MDC
        String levelStr = MDC.get(LoggerCondition.LEVEL);
        if(StringUtils.isNotEmpty(levelStr)){
            condition.setLevel(Level.valueOf(levelStr));
        }
        //get throwable from MDC
        String throwable = MDC.get(LoggerCondition.THROWABLE);
        if(StringUtils.isNotEmpty(throwable)){
            condition.setThrowable(throwable);
        }
        //get white list via level from MDC
        String levelTraceClassNames = MDC.get(LoggerCondition.LEVEL_WHITELIST);
        if(StringUtils.isNotEmpty(levelTraceClassNames)){
            String[] split = levelTraceClassNames.split(class_name_separator);
            condition.setClassNamesWhiteList(Arrays.asList(split));
        }

        //get mdcKeys from MDC
        Map<String, String> mdcKeysWhiteList = new HashMap<>();
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        if(copyOfContextMap != null && copyOfContextMap.size() > 0){
            copyOfContextMap.keySet().stream().filter(key -> key.startsWith(LoggerCondition.MDCKEY_PREFIX)).forEach(key -> {
                String value = copyOfContextMap.get(key);
                String mdcKey = key.substring(LoggerCondition.MDCKEY_PREFIX.length());
                mdcKeysWhiteList.put(mdcKey, value);
            });
        }
        condition.setMdcKeysWhiteList(mdcKeysWhiteList);

        //get content from MDC
        String contentStr = MDC.get(LoggerCondition.CONTENT);
        if(StringUtils.isNotEmpty(contentStr)){
            String[] split = contentStr.split(class_name_separator);
            condition.setContent(Arrays.asList(split));
        }

        return condition;
    }

    public static void saveToMDC(LoggerCondition condition) throws NullPointerException{
        if(condition == null){
            throw new NullPointerException("LoggerCondition is null");
        }
        Marker marker = condition.getMarker();
        if(marker != null){
            MDC.put(LoggerCondition.MARKER, marker.getName());
        }
        Level level = condition.getLevel();
        if(level != null){
            MDC.put(LoggerCondition.LEVEL, level.levelStr);
        }
        String throwable = condition.getThrowable();
        if(StringUtils.isNotEmpty(throwable)){
            MDC.put(LoggerCondition.THROWABLE, throwable);
        }
        List<String> classNamesWhiteList = condition.getClassNamesWhiteList();
        if(classNamesWhiteList != null && classNamesWhiteList.size() > 0){
            StringBuilder str = new StringBuilder();
            for(String className:classNamesWhiteList){
                str.append(className);
                str.append(class_name_separator);
            }
            MDC.put(LoggerCondition.LEVEL_WHITELIST, str.toString());
        }
        List<String> contents = condition.getContent();
        if(contents != null && contents.size() > 0){
            StringBuilder str = new StringBuilder();
            for(String c:contents){
                str.append(c);
                str.append(class_name_separator);
            }
            MDC.put(LoggerCondition.CONTENT, str.toString());
        }
        Map<String, String> mdcKeysWhiteList = condition.getMdcKeysWhiteList();
        if(mdcKeysWhiteList != null && mdcKeysWhiteList.size() > 0){
            for(String key:mdcKeysWhiteList.keySet()){
                MDC.put(LoggerCondition.MDCKEY_PREFIX+key, mdcKeysWhiteList.get(key));
            }
        }
    }

    public static void removeLogger4MDC(){
        MDC.remove(LoggerCondition.MARKER);
        MDC.remove(LoggerCondition.LEVEL);
        MDC.remove(LoggerCondition.THROWABLE);
        MDC.remove(LoggerCondition.LEVEL_WHITELIST);
        MDC.remove(LoggerCondition.CONTENT);
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        if(copyOfContextMap != null && copyOfContextMap.size() > 0){
            copyOfContextMap.keySet().stream().filter(key -> key.startsWith(LoggerCondition.MDCKEY_PREFIX)).forEach(key -> {
                MDC.remove(key);
            });
        }
    }

}
