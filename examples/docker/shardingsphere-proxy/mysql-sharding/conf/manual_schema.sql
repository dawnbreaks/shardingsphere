/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DROP
DATABASE IF EXISTS demo_ds_0;
DROP
DATABASE IF EXISTS demo_ds_1;
CREATE
DATABASE demo_ds_0;
CREATE
DATABASE demo_ds_1;

-- t_order
CREATE TABLE demo_ds_0.`t_order_0`
(
    `order_id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) NOT NULL,
    `address_id` bigint(20) NOT NULL,
    `status`     varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE demo_ds_0.`t_order_1`
(
    `order_id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) NOT NULL,
    `address_id` bigint(20) NOT NULL,
    `status`     varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE demo_ds_1.`t_order_0`
(
    `order_id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) NOT NULL,
    `address_id` bigint(20) NOT NULL,
    `status`     varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE demo_ds_1.`t_order_1`
(
    `order_id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) NOT NULL,
    `address_id` bigint(20) NOT NULL,
    `status`     varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- t_order_item
CREATE TABLE demo_ds_0.`t_order_item_0`
(
    `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`      bigint(20) DEFAULT NULL,
    `user_id`       int(11) NOT NULL,
    `status`        varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE demo_ds_0.`t_order_item_1`
(
    `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`      bigint(20) DEFAULT NULL,
    `user_id`       int(11) NOT NULL,
    `status`        varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE demo_ds_1.`t_order_item_0`
(
    `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`      bigint(20) DEFAULT NULL,
    `user_id`       int(11) NOT NULL,
    `status`        varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE demo_ds_1.`t_order_item_1`
(
    `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`      bigint(20) DEFAULT NULL,
    `user_id`       int(11) NOT NULL,
    `status`        varchar(50) DEFAULT NULL,
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- t_address
CREATE TABLE demo_ds_0.`t_address`
(
    `address_id`   bigint(20) NOT NULL,
    `user_id`      varchar(100) NOT NULL,
    `address_name` varchar(100) NOT NULL,
    PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE demo_ds_1.`t_address`
(
    `address_id`   bigint(20) NOT NULL,
    `user_id`      bigint(20) NOT NULL,
    `address_name` varchar(100) NOT NULL,
    PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- t_user
CREATE TABLE demo_ds_0.`t_user`
(
    `user_id` bigint(20) NOT NULL,
    `age`     int(10) NOT NULL,
    `gender`  varchar(20) NOT NULL,
    `email`   varchar(100) NULL,
    `avatar`  varchar(100) NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE demo_ds_1.`t_user`
(
    `user_id` bigint(20) NOT NULL,
    `age`     int(10) NOT NULL,
    `gender`  varchar(20) NOT NULL,
    `email`   varchar(100) NULL,
    `avatar`  varchar(100) NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;