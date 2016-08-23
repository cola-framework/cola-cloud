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
package com.cola.libs.jpa.keygen;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import org.hibernate.hql.internal.ast.HqlParser;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * cola
 * Created by jiachen.shi on 8/17/2016.
 */
public class JpqlKeyGenerator implements KeyGenerator{

    @Override
    public Object generate(Object target, Method method, Object... args) {
        Object key = null;
        if(args != null){
            String jpql = (String)args[0];
            HqlParser parser = HqlParser.getInstance(jpql);
            try {
                parser.statement();
            } catch (RecognitionException e) {
                e.printStackTrace();
            } catch (TokenStreamException e) {
                e.printStackTrace();
            }
            parser.getTokenNames();
            AST ast = parser.getAST();
        }
        return key;
    }

}
