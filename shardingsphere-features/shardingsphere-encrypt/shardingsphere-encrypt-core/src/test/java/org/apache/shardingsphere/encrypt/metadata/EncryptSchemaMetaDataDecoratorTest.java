/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.metadata;

import org.apache.shardingsphere.encrypt.rule.EncryptColumn;
import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.encrypt.rule.EncryptTable;
import org.apache.shardingsphere.encrypt.spi.context.EncryptColumnDataType;
import org.apache.shardingsphere.infra.metadata.database.schema.builder.GenericSchemaBuilderMaterials;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.spi.RuleBasedSchemaMetaDataDecoratorFactory;
import org.apache.shardingsphere.infra.metadata.database.schema.loader.model.ColumnMetaData;
import org.apache.shardingsphere.infra.metadata.database.schema.loader.model.SchemaMetaData;
import org.apache.shardingsphere.infra.metadata.database.schema.loader.model.TableMetaData;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class EncryptSchemaMetaDataDecoratorTest {
    
    private static final String TABLE_NAME = "t_encrypt";
    
    @Test
    public void assertDecorate() {
        EncryptRule rule = createEncryptRule();
        EncryptSchemaMetaDataDecorator loader = getEncryptMetaDataBuilder(rule, Collections.singleton(rule));
        Collection<TableMetaData> tableMetaDataList = new LinkedList<>();
        tableMetaDataList.add(createTableMetaData());
        TableMetaData actual = loader.decorate(Collections.singletonMap("logic_db",
                new SchemaMetaData("logic_db", tableMetaDataList)), rule, mock(GenericSchemaBuilderMaterials.class)).get("logic_db").getTables().iterator().next();
        assertThat(actual.getColumns().size(), is(2));
        Iterator<ColumnMetaData> columnsIterator = actual.getColumns().iterator();
        assertThat(columnsIterator.next().getName(), is("id"));
        assertThat(columnsIterator.next().getName(), is("pwd"));
    }
    
    @Test
    public void assertDecorateWithConfigDataType() {
        EncryptRule rule = createEncryptRuleWithDataTypeConfig();
        EncryptSchemaMetaDataDecorator loader = getEncryptMetaDataBuilder(rule, Collections.singleton(rule));
        Collection<TableMetaData> tableMetaDataList = new LinkedList<>();
        tableMetaDataList.add(createTableMetaData());
        GenericSchemaBuilderMaterials materials = mock(GenericSchemaBuilderMaterials.class, RETURNS_DEEP_STUBS);
        TableMetaData actual = loader.decorate(Collections.singletonMap("logic_db",
                new SchemaMetaData("logic_db", tableMetaDataList)), rule, materials).get("logic_db").getTables().iterator().next();
        assertThat(actual.getColumns().size(), is(2));
        Iterator<ColumnMetaData> columnsIterator = actual.getColumns().iterator();
        assertThat(columnsIterator.next().getName(), is("id"));
        ColumnMetaData columnMetaData = columnsIterator.next();
        assertThat(columnMetaData.getName(), is("pwd"));
        assertThat(columnMetaData.getDataType(), is(12));
    }
    
    private EncryptRule createEncryptRuleWithDataTypeConfig() {
        EncryptRule result = createEncryptRule();
        assertTrue(result.findEncryptTable(TABLE_NAME).isPresent());
        EncryptTable encryptTable = result.findEncryptTable(TABLE_NAME).get();
        EncryptColumn encryptColumn = mock(EncryptColumn.class);
        EncryptColumnDataType encryptColumnDataType = mock(EncryptColumnDataType.class);
        when(encryptColumnDataType.getDataType()).thenReturn(12);
        when(encryptColumn.getLogicDataType()).thenReturn(encryptColumnDataType);
        when(encryptTable.findEncryptColumn("pwd")).thenReturn(Optional.of(encryptColumn));
        return result;
    }
    
    private EncryptRule createEncryptRule() {
        EncryptRule result = mock(EncryptRule.class);
        EncryptTable encryptTable = mock(EncryptTable.class);
        when(result.findEncryptTable(TABLE_NAME)).thenReturn(Optional.of(encryptTable));
        when(encryptTable.getAssistedQueryColumns()).thenReturn(Collections.emptyList());
        when(encryptTable.getPlainColumns()).thenReturn(Collections.singleton("pwd_plain"));
        when(encryptTable.isCipherColumn("pwd_cipher")).thenReturn(true);
        when(encryptTable.getLogicColumn("pwd_cipher")).thenReturn("pwd");
        return result;
    }
    
    private TableMetaData createTableMetaData() {
        Collection<ColumnMetaData> columns = Arrays.asList(new ColumnMetaData("id", 1, true, true, true),
                new ColumnMetaData("pwd_cipher", 2, false, false, true),
                new ColumnMetaData("pwd_plain", 2, false, false, true));
        return new TableMetaData(TABLE_NAME, columns, Collections.emptyList(), Collections.emptyList());
    }
    
    private EncryptSchemaMetaDataDecorator getEncryptMetaDataBuilder(final EncryptRule encryptRule, final Collection<ShardingSphereRule> rules) {
        return (EncryptSchemaMetaDataDecorator) RuleBasedSchemaMetaDataDecoratorFactory.getInstances(rules).get(encryptRule);
    }
}
