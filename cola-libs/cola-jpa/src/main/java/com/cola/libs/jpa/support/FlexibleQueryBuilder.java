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
package com.cola.libs.jpa.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cola
 * Created by jiachen.shi on 8/5/2016.
 */
public class FlexibleQueryBuilder {

    private StringBuilder builder;
    private List<Object> paramList;
    private Map<String, Object> paramMap;

    public FlexibleQueryBuilder(){
        this.builder = new StringBuilder();
        this.paramList = new ArrayList<>();
        this.paramMap = new HashMap<>();
    }

    public FlexibleQueryBuilder(String jpql){
        this.builder = new StringBuilder(jpql);
        this.paramList = new ArrayList<>();
        this.paramMap = new HashMap<>();
    }

    public FlexibleQueryBuilder(String jpql, List<Object> paramList){
        this.builder = new StringBuilder(jpql);
        if(paramList != null){
            this.paramList = paramList;
        }else{
            this.paramList = new ArrayList<>();
        }
        this.paramMap = new HashMap<>();
    }

    public FlexibleQueryBuilder(String jpql, Map<String, Object> paramMap){
        this.builder = new StringBuilder(jpql);
        this.paramList = new ArrayList<>();
        if(paramMap != null){
            this.paramMap = paramMap;
        }else{
            this.paramMap = new HashMap<>();
        }
    }

    public FlexibleQueryBuilder append(String str){
        this.builder.append(str);
        return this;
    }

    public FlexibleQueryBuilder addParameter(Object param){
        this.paramList.add(param);
        return this;
    }

    public FlexibleQueryBuilder addParameters(List<Object> params){
        this.paramList.addAll(params);
        return this;
    }

    public FlexibleQueryBuilder putParameter(String key, Object value){
        this.paramMap.put(key, value);
        return this;
    }

    public FlexibleQueryBuilder putParameters(Map<String, Object> paramMap){
        if(paramMap != null && paramMap.keySet() != null && paramMap.keySet().size() > 0){
            for(String key:paramMap.keySet()){
                this.paramMap.put(key, paramMap.get(key));
            }
        }
        return this;
    }

    public FlexibleQueryBuilder removeAllParameters(){
        this.paramList = new ArrayList<>();
        this.paramMap = new HashMap<>();
        return this;
    }

    public FlexibleQueryBuilder setParameters(Map<String, Object> paramMap){
        this.paramMap = paramMap;
        return this;
    }

    public String toJPQL(){
        return builder.toString();
    }

    public List<Object> getParamList(){
        return paramList;
    }

    public Map<String, Object> getParamMap(){
        return paramMap;
    }

}
