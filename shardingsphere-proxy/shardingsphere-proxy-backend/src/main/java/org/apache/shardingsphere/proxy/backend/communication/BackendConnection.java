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

package org.apache.shardingsphere.proxy.backend.communication;

import org.apache.shardingsphere.proxy.backend.exception.BackendConnectionException;
import org.apache.shardingsphere.proxy.backend.session.ConnectionSession;

import java.sql.SQLException;

/**
 * Backend connection for Proxy.
 *
 * @param <T> return type of methods
 */
public interface BackendConnection<T> {
    
    /**
     * Get connection session of backend connection.
     * 
     * @return connection session
     */
    ConnectionSession getConnectionSession();
    
    /**
     * Prepare for task execution.
     *
     * @return can be Void or Future
     * @throws SQLException SQL exception
     */
    T prepareForTaskExecution() throws SQLException;
    
    /**
     * Close resources used in execution.
     *
     * @return can be Void or Future
     * @throws BackendConnectionException backend connection exception
     */
    T closeExecutionResources() throws BackendConnectionException;
    
    /**
     * Close all resources.
     *
     * @return can be Void or Future
     * @throws BackendConnectionException backend connection exception
     */
    T closeAllResources() throws BackendConnectionException;
}
