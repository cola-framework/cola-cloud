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

import com.cola.libs.jpa.support.QueryTranslatorHelper;

import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.classic.ParserHelper;
import org.hibernate.internal.util.StringHelper;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * cola
 * Created by jiachen.shi on 8/17/2016.
 */
public class JpqlKeyGenerator implements KeyGenerator{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Object generate(Object target, Method method, Object... args) {
        List<String> keys = new ArrayList<>();
        if(args != null){
            String jpql = (String)args[0];
            List<String> strings = QueryTranslatorHelper.findTableClassNameFromJpql(jpql, this.getSessionFactory());
            if(strings != null){
                for(String str:strings){
                    keys.add(str += ":*");
                }
            }
        }
        return keys;
    }

    private SessionFactoryImplementor getSessionFactory(){
        Session session = (Session)em.getDelegate();
        return (SessionFactoryImplementor) session.getSessionFactory();
    }

}
