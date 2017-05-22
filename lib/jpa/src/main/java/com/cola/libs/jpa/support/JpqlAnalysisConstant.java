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

/**
 * cola
 * Created by jiachen.shi on 7/21/2016.
 */
public class JpqlAnalysisConstant {

    public static String SEPARATOR = ",";
    public static String EVALUATION = "=";
    public static String ALIAS_REFRENCE = ".";
    public static String SPLIT_ALIAS_REFRENCE = "\\.";
    public static String BLANK_SPACE = " ";

    public static String NOT_ALIAS = "NOT_ALIAS";
    public static String OPTIMISTIC_LOCKING_NAME = "version";
    public static String INCREMENT = "+1";

    public enum StatementType{
        SELECT, INSERT, UPDATE ,DELETE
    }

    public enum TableRefrence {
        LEFT, RIGHT, INNER, JOIN
    }

    public enum Clause {
        FROM, SET, WHERE, GROUP, ORDER, HAVING, ON, IN, UNION, AS
    }

}
