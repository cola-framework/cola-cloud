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
package com.cola.lib.beans.web.restful;

import org.springframework.util.ObjectUtils;

/**
 * cola
 * Created by jiachen.shi on 6/20/2016.
 */
public class ResponseHeader {

    private int code;
    private String message;

    public int hashCode() {
        return  ObjectUtils.nullSafeHashCode(this.message) * 29 +
                this.code;
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        } else if(null == other) {
            return false;
        } else {
            ResponseHeader otherHeader = (ResponseHeader)other;
            return this.code == otherHeader.getCode()
                    && ObjectUtils.nullSafeEquals(this.message, otherHeader.message);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("\"header\":{");
        builder.append("\"code\":").append(code).append(",");
        builder.append("\"message\":").append(this.message);
        builder.append("}");
        return builder.toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
