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

import com.cola.libs.beans.enums.ResponseStatus;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * cola
 * Created by jiachen.shi on 6/20/2016.
 */
public class ResponseMessage<T> implements Serializable{

    private ResponseHeader header;
    private T body;

    public ResponseMessage(){
    }

    public ResponseMessage(ResponseHeader header, T body){
        this.header = header;
        this.body = body;
    }

    public int hashCode() {
        return  ObjectUtils.nullSafeHashCode(this.body) * 29 +
                ObjectUtils.nullSafeHashCode(this.header);
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        } else if(null == other) {
            return false;
        } else {
            ResponseMessage otherMessage = (ResponseMessage)other;
            return ObjectUtils.nullSafeEquals(this.header, otherMessage.header)
                    && ObjectUtils.nullSafeEquals(this.body, otherMessage.body);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("\"message\":{");
        builder.append(header).append(",");
        builder.append("\"body\":").append(this.body);
        builder.append("}");
        return builder.toString();
    }

    public static <T> ResponseMessage<T> ok(T body){
        ResponseHeader header = new ResponseHeader();
        header.setCode(ResponseStatus.OK.getCode());
        return new ResponseMessage(header, body);
    }

    public static <T> ResponseMessage<T> status(int statusCode, String message, T body){
        ResponseHeader header = new ResponseHeader();
        header.setCode(statusCode);
        header.setMessage(message);
        return new ResponseMessage(header, body);
    }

    public static <T> ResponseMessage<T> error(int errorCode, String message){
        ResponseHeader header = new ResponseHeader();
        header.setCode(errorCode);
        header.setMessage(message);
        return new ResponseMessage(header, null);
    }

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
}
