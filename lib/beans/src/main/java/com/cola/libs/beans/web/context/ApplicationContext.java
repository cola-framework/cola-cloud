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

import java.util.Map;
import java.util.Objects;

/**
 * cola
 * Created by jiachen.shi on 7/25/2016.
 */
public class ApplicationContext {

    public static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static Map<String, Object> getCurrentContext(){
        return threadLocal.get();
    }

}
