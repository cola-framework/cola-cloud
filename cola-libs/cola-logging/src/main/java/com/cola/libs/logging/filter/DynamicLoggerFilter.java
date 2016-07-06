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
package com.cola.libs.logging.filter;

import com.cola.libs.logging.beans.LoggerCondition;
import com.cola.libs.logging.util.LoggerConditionHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.slf4j.Marker;

import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * cola
 * Created by jiachen.shi on 7/4/2016.
 */
public class DynamicLoggerFilter extends TurboFilter {

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable throwable) {
        LoggerCondition condition = LoggerConditionHelper.getLoggerConditionFromMDC();
        if(condition != null){
            boolean result = false;
            Level cLevel = condition.getLevel();
            if(cLevel != null && level.isGreaterOrEqual(cLevel)){
                result = true;
                List<String> whiteList = condition.getClassNamesWhiteList();
                if(logger != null && whiteList != null && whiteList.size() > 0){
                    for(String className:whiteList){
                        if(!logger.getName().startsWith(className)){
                            result  = false;
                        }
                    }
                }
            }
            Marker cMarker = condition.getMarker();
            if(cMarker != null){
                if(cMarker.equals(marker)){
                    result = true;
                }else{
                    result = false;
                }
            }
            if(throwable != null){
                String cThrowable = condition.getThrowable();
                if(StringUtils.isNotEmpty(cThrowable)){
                    if(throwable.getLocalizedMessage().contains(cThrowable)){
                        result = true;
                    }else{
                        result = false;
                    }
                }
            }

            Map<String, String> mdcKeysWhiteList = condition.getMdcKeysWhiteList();
            if(mdcKeysWhiteList != null && mdcKeysWhiteList.size() > 0){
                for(String key:mdcKeysWhiteList.keySet()){
                    String value = (String)MDC.get(LoggerCondition.MDCKEY_PREFIX + key);
                    if(value.equals(MDC.get(key))){
                        result = true;
                    }else{
                        result = false;
                    }
                }
            }

            List<String> contents = condition.getContent();
            if(contents != null && contents.size() > 0){
                for(String content:contents){
                    if(format.contains(content)){
                        result = true;
                    }else{
                        result = false;
                    }
                }
            }

            if(result){
                return FilterReply.ACCEPT;
            }
        }
        return FilterReply.NEUTRAL ;
    }
}
