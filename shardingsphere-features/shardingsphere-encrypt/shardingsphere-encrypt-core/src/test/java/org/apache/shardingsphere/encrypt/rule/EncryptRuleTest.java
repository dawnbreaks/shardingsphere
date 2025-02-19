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

package org.apache.shardingsphere.encrypt.rule;

import org.apache.shardingsphere.encrypt.algorithm.config.AlgorithmProvidedEncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.EncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.encrypt.fixture.CoreEncryptAlgorithmFixture;
import org.apache.shardingsphere.encrypt.fixture.CoreSchemaMetaDataAwareEncryptAlgorithmFixture;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.database.DefaultDatabase;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.model.ShardingSphereSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class EncryptRuleTest {
    
    @Test
    public void assertNewInstanceWithAlgorithmProvidedEncryptRuleConfiguration() {
        EncryptColumnRuleConfiguration encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), null);
        AlgorithmProvidedEncryptRuleConfiguration ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(
                Collections.singleton(tableConfig), Collections.singletonMap("test_encryptor", new CoreEncryptAlgorithmFixture()), true);
        EncryptRule actual = new EncryptRule(ruleConfig, Collections.emptyMap());
        assertTrue(actual.findEncryptTable("t_encrypt").isPresent());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void assertNewInstanceWithInvalidConfiguration() {
        ShardingSphereAlgorithmConfiguration encryptAlgorithmConfig = new ShardingSphereAlgorithmConfiguration("CORE.FIXTURE", new Properties());
        EncryptColumnRuleConfiguration encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), null);
        EncryptRuleConfiguration ruleConfig = new EncryptRuleConfiguration(Collections.singleton(tableConfig), Collections.singletonMap("invalid_encryptor", encryptAlgorithmConfig));
        new EncryptRule(ruleConfig, Collections.emptyMap());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void assertNewInstanceWithInvalidAlgorithmProvidedEncryptRuleConfiguration() {
        EncryptColumnRuleConfiguration encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), null);
        AlgorithmProvidedEncryptRuleConfiguration ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(
                Collections.singleton(tableConfig), Collections.singletonMap("invalid_encryptor", new CoreEncryptAlgorithmFixture()), true);
        new EncryptRule(ruleConfig, Collections.emptyMap());
    }
    
    @Test
    public void assertFindEncryptTable() {
        assertTrue(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findEncryptTable("t_encrypt").isPresent());
    }
    
    @Test
    public void assertFindEncryptor() {
        assertTrue(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findEncryptor("t_encrypt", "pwd").isPresent());
    }
    
    @Test
    public void assertNotFindEncryptor() {
        assertFalse(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findEncryptor("t_encrypt", "other_column").isPresent());
    }
    
    @Test
    public void assertGetEncryptValues() {
        List<Object> encryptAssistedQueryValues = new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap())
                .getEncryptValues(DefaultDatabase.LOGIC_NAME, DefaultDatabase.LOGIC_NAME, "t_encrypt", "pwd", Collections.singletonList(null));
        for (final Object value : encryptAssistedQueryValues) {
            assertNull(value);
        }
    }
    
    @Test
    public void assertGetCipherColumnWhenEncryptColumnExist() {
        assertThat(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getCipherColumn("t_encrypt", "pwd"), is("pwd_cipher"));
    }
    
    @Test(expected = NullPointerException.class)
    public void assertGetCipherColumnWhenNoEncryptColumn() {
        new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getCipherColumn("t_encrypt", "pwd_cipher");
    }
    
    @Test
    public void assertGetLogicAndCipherColumns() {
        assertFalse(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getLogicAndCipherColumns("t_encrypt").isEmpty());
    }
    
    @Test
    public void assertFindAssistedQueryColumn() {
        assertFalse(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findAssistedQueryColumn("t_encrypt", "pwd_cipher").isPresent());
    }
    
    @Test
    public void assertGetEncryptAssistedQueryValues() {
        List<Object> encryptAssistedQueryValues = new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap())
                .getEncryptAssistedQueryValues(DefaultDatabase.LOGIC_NAME, DefaultDatabase.LOGIC_NAME, "t_encrypt", "pwd", Collections.singletonList(null));
        for (final Object value : encryptAssistedQueryValues) {
            assertNull(value);
        }
    }
    
    @Test
    public void assertGetAssistedQueryColumns() {
        assertTrue(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getAssistedQueryColumns("t_encrypt").isEmpty());
    }
    
    @Test
    public void assertFindPlainColumn() {
        assertTrue(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findPlainColumn("t_encrypt", "pwd").isPresent());
        assertTrue(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findPlainColumn("t_encrypt", "credit_card".toLowerCase()).isPresent());
        assertFalse(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).findPlainColumn("t_encrypt", "notExistLogicColumn").isPresent());
    }
    
    @Test
    public void assertIsQueryWithCipherColumn() {
        EncryptColumnRuleConfiguration encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), null);
        AlgorithmProvidedEncryptRuleConfiguration ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(
                Collections.singleton(tableConfig), Collections.singletonMap("test_encryptor", new CoreEncryptAlgorithmFixture()), true);
        EncryptRule actual = new EncryptRule(ruleConfig, Collections.emptyMap());
        assertTrue(actual.isQueryWithCipherColumn("t_encrypt", "encrypt_column"));
        
        encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", null);
        tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), false);
        ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(Collections.singleton(tableConfig), Collections.singletonMap("test_encryptor", new CoreEncryptAlgorithmFixture()), true);
        actual = new EncryptRule(ruleConfig, Collections.emptyMap());
        assertFalse(actual.isQueryWithCipherColumn("t_encrypt", "encrypt_column"));
        
        encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", true);
        tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), false);
        ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(Collections.singleton(tableConfig), Collections.singletonMap("test_encryptor", new CoreEncryptAlgorithmFixture()), true);
        actual = new EncryptRule(ruleConfig, Collections.emptyMap());
        assertTrue(actual.isQueryWithCipherColumn("t_encrypt", "encrypt_column"));
        
        encryptColumnConfig = new EncryptColumnRuleConfiguration("encrypt_column", "encrypt_cipher", "", "", "test_encryptor", false);
        tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Collections.singletonList(encryptColumnConfig), null);
        ruleConfig = new AlgorithmProvidedEncryptRuleConfiguration(Collections.singleton(tableConfig), Collections.singletonMap("test_encryptor", new CoreEncryptAlgorithmFixture()), true);
        actual = new EncryptRule(ruleConfig, Collections.emptyMap());
        assertFalse(actual.isQueryWithCipherColumn("t_encrypt", "encrypt_column"));
    }
    
    @Test
    public void assertGetTables() {
        assertThat(new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getTables(), is(Collections.singleton("t_encrypt")));
    }
    
    @Test
    public void assertGetTableWithLowercase() {
        assertThat(new EncryptRule(createEncryptRuleConfigurationWithUpperCaseLogicTable(), Collections.emptyMap()).getTables(), is(Collections.singleton("t_encrypt")));
    }
    
    @Test
    public void assertTheSameLogicTable() {
        Collection<String> logicTables = new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap()).getTables();
        Collection<String> theSameLogicTables = new EncryptRule(createEncryptRuleConfigurationWithUpperCaseLogicTable(), Collections.emptyMap()).getTables();
        assertThat(logicTables, is(theSameLogicTables));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void assertGetSchemaMetaData() {
        EncryptRule encryptRule = new EncryptRule(createEncryptRuleConfiguration(), Collections.emptyMap());
        ShardingSphereSchema schema = mock(ShardingSphereSchema.class);
        encryptRule.setSchemaMetaData("foo_db", Collections.singletonMap("foo_schema", schema));
        Optional<EncryptAlgorithm> actual = encryptRule.findEncryptor("t_encrypt", "name");
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(CoreSchemaMetaDataAwareEncryptAlgorithmFixture.class));
        assertThat(((CoreSchemaMetaDataAwareEncryptAlgorithmFixture) actual.get()).getDatabaseName(), is("foo_db"));
        assertThat(((CoreSchemaMetaDataAwareEncryptAlgorithmFixture) actual.get()).getSchemas(), is(Collections.singletonMap("foo_schema", schema)));
        assertFalse(((CoreSchemaMetaDataAwareEncryptAlgorithmFixture) actual.get()).getSchemas().isEmpty());
    }
    
    private EncryptRuleConfiguration createEncryptRuleConfiguration() {
        ShardingSphereAlgorithmConfiguration queryAssistedEncryptConfig = new ShardingSphereAlgorithmConfiguration("CORE.QUERY_ASSISTED.FIXTURE", new Properties());
        ShardingSphereAlgorithmConfiguration metaDataAwareEncryptConfig = new ShardingSphereAlgorithmConfiguration("CORE.METADATA_AWARE.FIXTURE", new Properties());
        EncryptColumnRuleConfiguration pwdColumnConfig = new EncryptColumnRuleConfiguration("pwd", "pwd_cipher", "", "pwd_plain", "test_encryptor", null);
        EncryptColumnRuleConfiguration creditCardColumnConfig = new EncryptColumnRuleConfiguration("credit_card", "credit_card_cipher", "", "credit_card_plain", "test_encryptor", null);
        EncryptColumnRuleConfiguration nameColumnConfig = new EncryptColumnRuleConfiguration("name", "name_cipher", "", "name_plain", "customized_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("t_encrypt", Arrays.asList(pwdColumnConfig, creditCardColumnConfig, nameColumnConfig), null);
        return new EncryptRuleConfiguration(Collections.singleton(tableConfig), getEncryptors(queryAssistedEncryptConfig, metaDataAwareEncryptConfig));
    }
    
    private EncryptRuleConfiguration createEncryptRuleConfigurationWithUpperCaseLogicTable() {
        ShardingSphereAlgorithmConfiguration queryAssistedEncryptConfig = new ShardingSphereAlgorithmConfiguration("CORE.QUERY_ASSISTED.FIXTURE", new Properties());
        ShardingSphereAlgorithmConfiguration metaDataAwareEncryptConfig = new ShardingSphereAlgorithmConfiguration("CORE.METADATA_AWARE.FIXTURE", new Properties());
        EncryptColumnRuleConfiguration pwdColumnConfig = new EncryptColumnRuleConfiguration("pwd", "pwd_cipher", "", "pwd_plain", "test_encryptor", null);
        EncryptColumnRuleConfiguration creditCardColumnConfig = new EncryptColumnRuleConfiguration("credit_card", "credit_card_cipher", "", "credit_card_plain", "test_encryptor", null);
        EncryptColumnRuleConfiguration nameColumnConfig = new EncryptColumnRuleConfiguration("name", "name_cipher", "", "name_plain", "customized_encryptor", null);
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration("T_ENCRYPT", Arrays.asList(pwdColumnConfig, creditCardColumnConfig, nameColumnConfig), null);
        return new EncryptRuleConfiguration(Collections.singleton(tableConfig), getEncryptors(queryAssistedEncryptConfig, metaDataAwareEncryptConfig));
    }
    
    private Map<String, ShardingSphereAlgorithmConfiguration> getEncryptors(final ShardingSphereAlgorithmConfiguration queryAssistedEncryptConfig,
                                                                            final ShardingSphereAlgorithmConfiguration metaDataAwareEncryptConfig) {
        Map<String, ShardingSphereAlgorithmConfiguration> result = new HashMap<>(2, 1);
        result.put("test_encryptor", queryAssistedEncryptConfig);
        result.put("customized_encryptor", metaDataAwareEncryptConfig);
        return result;
    }
}
