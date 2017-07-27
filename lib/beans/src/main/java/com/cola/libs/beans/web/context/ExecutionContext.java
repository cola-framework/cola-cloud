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
package com.cola.libs.beans.web.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * cola
 * Created by jiachen.shi on 7/25/2016.
 */
public class ExecutionContext {

    private static String LOCALE_SESSION_ATTRIBUTE_NAME = "org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE";
    private static String SESSION_ID = "sessionId";
    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void setCurrnetContext(Map<String,Object> context){
        threadLocal.set(context);
    }

    public static Map<String, Object> getCurrentContext(){
        Map<String, Object> currentContext = threadLocal.get();
        if(null == currentContext){
            currentContext = new HashMap<>();
            threadLocal.set(currentContext);
        }
        return currentContext;
    }

    public static Locale getCurrentLocale(){
        Map<String, Object> context = getCurrentContext();
        if(null != context && null != context.get(LOCALE_SESSION_ATTRIBUTE_NAME)){
            return (Locale)context.get(LOCALE_SESSION_ATTRIBUTE_NAME);
        }
        return null;
    }

    public static String getLanguage(){
        Locale currentLocale = getCurrentLocale();
        if(null != currentLocale){
            return currentLocale.getLanguage();
        }
        return null;
    }

    public static String getSessionId(){
        Map<String, Object> context = getCurrentContext();
        if(null != context && null != context.get(SESSION_ID)){
            return (String)context.get("sessionId");
        }
        return null;
    }

}
