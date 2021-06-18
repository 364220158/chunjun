/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtstack.flinkx.connector.db2;

import com.dtstack.flinkx.connector.jdbc.JdbcDialect;

import java.util.Optional;

/**
 * Company: www.dtstack.com
 * @author xuchao
 * @date 2021-06-15
 */
public class Db2Dialect implements JdbcDialect {

    private static final String DIALECT_NAME = "db2";

    private static final String DRIVER_NAME = "com.ibm.db2.jcc.DB2Driver";

    @Override
    public String dialectName() {
        return DIALECT_NAME;
    }

    @Override
    public boolean canHandle(String url) {
        return url.startsWith("jdbc:db2:");
    }

    @Override
    public Optional<String> defaultDriverName() {
        return Optional.of(DRIVER_NAME);
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return identifier;
    }

}
