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

package com.dtstack.flinkx.connector.jdbc.source;

import com.dtstack.flinkx.conf.FieldConf;
import com.dtstack.flinkx.connector.jdbc.JdbcDialect;
import com.dtstack.flinkx.connector.jdbc.conf.JdbcConf;
import com.dtstack.flinkx.constants.ConstantValue;
import com.dtstack.flinkx.enums.ColumnType;
import com.dtstack.flinkx.inputformat.BaseRichInputFormatBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * The builder of JdbcInputFormat
 *
 * Company: www.dtstack.com
 * @author huyifan.zju@163.com
 */
public class JdbcInputFormatBuilder extends BaseRichInputFormatBuilder {

    protected JdbcInputFormat format;

    public JdbcInputFormatBuilder(JdbcInputFormat format) {
        super.format = this.format = format;
    }

    public void setJdbcConf(JdbcConf jdbcConf) {
        super.setConfig(jdbcConf);
        format.setJdbcConf(jdbcConf);
    }

    public void setJdbcDialect(JdbcDialect jdbcDialect) {
        format.setJdbcDialect(jdbcDialect);
    }

    @Override
    protected void checkFormat() {
        JdbcConf conf = format.getJdbcConf();
        StringBuilder sb = new StringBuilder(256);
        if (StringUtils.isBlank(conf.getUsername())) {
            sb.append("No username supplied;\n");
        }
        if (StringUtils.isBlank(conf.getPassword())) {
            sb.append("No password supplied;\n");
        }
        if (StringUtils.isBlank(conf.getJdbcUrl())) {
            sb.append("No jdbc url supplied;\n");
        }
        if(conf.isIncrement()){
            if (StringUtils.isBlank(conf.getIncreColumn())){
                sb.append("increColumn can't be empty when increment is true;\n");
            }
            conf.setSplitPk(conf.getIncreColumn());
            if (conf.getParallelism() > 1){
                conf.setSplitStrategy("mod");
            }
        }

        if(conf.getParallelism() > 1){
            if(StringUtils.isBlank(conf.getSplitPk())){
                sb.append("Must specify the split column when the channel is greater than 1;\n");
            }else{
                FieldConf field = null;
                for (FieldConf fieldConf : conf.getColumn()) {
                    if(conf.getSplitPk().equalsIgnoreCase(fieldConf.getName())){
                        field = fieldConf;
                        break;
                    }
                }
                if(field == null){
                    sb.append("split column must in columns;\n");
                }else if(!ColumnType.isNumberType(field.getType())){
                    sb.append("split column's type must be number type;\n");
                }
            }
        }

        if(StringUtils.isNotBlank(conf.getStartLocation())){
            String[] startLocations = conf.getStartLocation().split(ConstantValue.COMMA_SYMBOL);
            if(startLocations.length != 1 && startLocations.length != conf.getParallelism()){
                sb.append("startLocations is ")
                        .append(Arrays.toString(startLocations))
                        .append(", length = [")
                        .append(startLocations.length)
                        .append("], but the channel is [")
                        .append(conf.getParallelism())
                        .append("];\n");
            }
        }

        if(sb.length() > 0){
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
