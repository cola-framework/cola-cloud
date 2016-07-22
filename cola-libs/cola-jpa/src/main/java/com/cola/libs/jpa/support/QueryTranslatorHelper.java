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

import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * cola
 * Created by jiachen.shi on 7/21/2016.
 */
public class QueryTranslatorHelper {

    public static JpqlAnalysisConstant.StatementType getStatementType(String jpql){
        Assert.notNull(jpql, "The JPQL must not be null!");
        jpql = jpql.toUpperCase().trim();
        if (jpql.startsWith(JpqlAnalysisConstant.StatementType.SELECT.name())) {
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

    public static String appendVersionForUpdate(String jpql){
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
