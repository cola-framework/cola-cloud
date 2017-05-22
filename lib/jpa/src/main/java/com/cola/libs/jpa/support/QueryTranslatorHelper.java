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

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.classic.ParserHelper;
import org.hibernate.internal.util.StringHelper;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * cola
 * Created by jiachen.shi on 7/21/2016.
 */
public class QueryTranslatorHelper {

    private static final Set<String> BEFORE_CLASS_TOKENS = new HashSet();
    private static final Set<String> NOT_AFTER_CLASS_TOKENS = new HashSet();

    static {
        BEFORE_CLASS_TOKENS.add(JpqlAnalysisConstant.Clause.FROM.name());
        BEFORE_CLASS_TOKENS.add(JpqlAnalysisConstant.StatementType.DELETE.name());
        BEFORE_CLASS_TOKENS.add(JpqlAnalysisConstant.StatementType.UPDATE.name());
        BEFORE_CLASS_TOKENS.add(",");
        NOT_AFTER_CLASS_TOKENS.add(JpqlAnalysisConstant.Clause.IN.name());
        NOT_AFTER_CLASS_TOKENS.add(JpqlAnalysisConstant.Clause.FROM.name());
        NOT_AFTER_CLASS_TOKENS.add(")");
    }

    private static boolean isPossiblyClassName(String last, String next) {
        return "class".equals(last) || BEFORE_CLASS_TOKENS.contains(last) && !NOT_AFTER_CLASS_TOKENS.contains(next);
    }

    private static boolean isJavaIdentifier(String token) {
        return Character.isJavaIdentifierStart(token.charAt(0));
    }

    private static String nextNonWhite(String[] tokens, int start) {
        for(int i = start + 1; i < tokens.length; ++i) {
            if(!ParserHelper.isWhitespace(tokens[i])) {
                return tokens[i];
            }
        }

        return tokens[tokens.length - 1];
    }

    private static int getStartingPositionFor(String[] tokens) {
        if(!JpqlAnalysisConstant.StatementType.SELECT.name().equals(tokens[0].toUpperCase()) || !JpqlAnalysisConstant.StatementType.DELETE.name().equals(tokens[0].toUpperCase())) {
            return 1;
        } else {
            for(int i = 1; i < tokens.length; ++i) {
                if(JpqlAnalysisConstant.Clause.FROM.name().equals(tokens[i].toUpperCase())) {
                    return i;
                }
            }
            return tokens.length;
        }
    }

    public static String getImportedClass(String name, SessionFactoryImplementor factory) {
        return factory.getImportedClassName(name);
    }

    public static List<String> findTableClassNameFromJpql(String jpql, SessionFactoryImplementor factory) throws MappingException {
        List<String> tableClassNames = new ArrayList<>();
        String[] tokens = StringHelper.split(" \n\r\f\t(),", jpql, true);
        if(tokens.length == 0) {
            return null;
        } else {
            int start = getStartingPositionFor(tokens);
            String last = tokens[start-1].toUpperCase();

            for(int results = start; results < tokens.length; ++results) {
                String token = tokens[results];
                if(!ParserHelper.isWhitespace(token)) {
                    String next = nextNonWhite(tokens, results).toUpperCase();
                    boolean process = isJavaIdentifier(token) && isPossiblyClassName(last, next);
                    last = token.toUpperCase();
                    if(process) {
                        String importedClassName = getImportedClass(token, factory);
                        if(importedClassName != null) {
                            String[] implementors = factory.getImplementors(importedClassName);
                            if(implementors != null) {
                                tableClassNames.add(implementors[0]);
                            }
                        }
                    }
                }
            }
            return tableClassNames;
        }
    }

    public static JpqlAnalysisConstant.StatementType getStatementType(String jpql){
        Assert.notNull(jpql, "The JPQL must not be null!");
        jpql = jpql.toUpperCase().trim();
        if (jpql.startsWith(JpqlAnalysisConstant.StatementType.SELECT.name()) || jpql.startsWith(JpqlAnalysisConstant.Clause.FROM.name())) {
            return JpqlAnalysisConstant.StatementType.SELECT;
        } else if (jpql.startsWith(JpqlAnalysisConstant.StatementType.INSERT.name())) {
            return JpqlAnalysisConstant.StatementType.INSERT;
        } else if (jpql.startsWith(JpqlAnalysisConstant.StatementType.UPDATE.name())) {
            return JpqlAnalysisConstant.StatementType.UPDATE;
        } else if (jpql.startsWith(JpqlAnalysisConstant.StatementType.DELETE.name())) {
            return JpqlAnalysisConstant.StatementType.DELETE;
        }
        throw new IllegalArgumentException("Invalid Statement.");
    }

    public static List<String> getTableNames(String jpql){
        List<String> tableNames = new ArrayList<>();
        JpqlAnalysisConstant.StatementType statementType = getStatementType(jpql);
        if(JpqlAnalysisConstant.StatementType.UPDATE.equals(statementType)){

        }else if(JpqlAnalysisConstant.StatementType.DELETE.equals(statementType)){

        }
        return null;
    }

    public static List<String> getTableAndAliasNameForUpdate(String jpql){
        String temp = jpql.toUpperCase().trim();
        int setIndex = temp.indexOf(JpqlAnalysisConstant.Clause.SET.name());
        String sub = null;
        if(setIndex > 0){
            sub = jpql.substring(6, setIndex);
        }

        if (null == sub) {
            throw new IllegalArgumentException("Invalid Update Statement.");
        }

        sub = sub.replaceAll(JpqlAnalysisConstant.SEPARATOR, "");
        return null;
    }

    public static String appendVersionIncrementForUpdate(String jpql){
        Assert.notNull(jpql, "The JPQL must not be null!");
        JpqlAnalysisConstant.StatementType statementType = getStatementType(jpql);
        if (JpqlAnalysisConstant.StatementType.UPDATE.equals(statementType)) {
            String temp = jpql.toUpperCase().trim();
            int setIndex = temp.indexOf(JpqlAnalysisConstant.Clause.SET.name());
            int whereIndex = temp.indexOf(JpqlAnalysisConstant.Clause.WHERE.name());
            String sub = null;
            if (setIndex > 0) {
                if (whereIndex < 0) {
                    sub = jpql.substring(setIndex + 3);
                } else {
                    sub = jpql.substring(setIndex + 3, whereIndex);
                }
            }

            if (null == sub) {
                throw new IllegalArgumentException("Invalid Update Statement.");
            }

            Set<String> aliasSet = new HashSet<>();
            String[] split = sub.split(JpqlAnalysisConstant.SEPARATOR);
            for (String s : split) {
                if (s.contains(JpqlAnalysisConstant.EVALUATION)) {
                    String[] s1 = s.split(JpqlAnalysisConstant.EVALUATION);
                    String s2 = s1[0];
                    if (s2.contains(JpqlAnalysisConstant.ALIAS_REFRENCE)) {
                        String[] s3 = s2.split(JpqlAnalysisConstant.SPLIT_ALIAS_REFRENCE);
                        aliasSet.add(s3[0]);
                    } else {
                        aliasSet.add(JpqlAnalysisConstant.NOT_ALIAS);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid Update Statement.");
                }
            }

            if (aliasSet.size() == 0) {
                throw new IllegalArgumentException("Invalid Update Statement.");
            }

            StringBuilder builder = new StringBuilder();
            for (String alias : aliasSet) {
                builder.append(JpqlAnalysisConstant.SEPARATOR);
                if (!JpqlAnalysisConstant.NOT_ALIAS.equals(alias)) {
                    builder.append(alias).append(JpqlAnalysisConstant.ALIAS_REFRENCE);
                }
                builder.append(JpqlAnalysisConstant.OPTIMISTIC_LOCKING_NAME)
                        .append(JpqlAnalysisConstant.EVALUATION);
                if (!JpqlAnalysisConstant.NOT_ALIAS.equals(alias)) {
                    builder.append(alias).append(JpqlAnalysisConstant.ALIAS_REFRENCE);
                }
                builder.append(JpqlAnalysisConstant.OPTIMISTIC_LOCKING_NAME)
                        .append(JpqlAnalysisConstant.INCREMENT).append(JpqlAnalysisConstant.BLANK_SPACE);
            }

            if (whereIndex < 0) {
                return jpql + builder.toString();
            } else {
                return jpql.substring(0, whereIndex) + builder.toString() + jpql.substring(whereIndex);
            }
        }
        return jpql;
    }

}
