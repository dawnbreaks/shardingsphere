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

package org.apache.shardingsphere.infra.metadata.database.schema.event;

import lombok.Getter;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.model.ShardingSphereTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Schema altered event.
 */
@Getter
public final class SchemaAlteredEvent {
    
    private final String databaseName;
    
    private final String schemaName;
    
    private final Collection<ShardingSphereTable> alteredTables = new ArrayList<>();
    
    private final Collection<String> droppedTables = new ArrayList<>();
    
    public SchemaAlteredEvent(final String databaseName, final String schemaName) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
    }
}
