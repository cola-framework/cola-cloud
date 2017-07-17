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
package com.cola.libs.beans.web.restful;

import java.io.Serializable;
import java.util.Map;

/**
 * cola
 * Created by jiachen.shi on 6/20/2016.
 */
public class ResponseMessage<T> {

    private Map<String, Serializable> applicationContext;
    private ResponseHeader header;
    private T body;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Map<String, Serializable> getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Map<String, Serializable> applicationContext) {
        this.applicationContext = applicationContext;
    }
}
