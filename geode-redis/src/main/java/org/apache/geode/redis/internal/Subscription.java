/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.apache.geode.redis.internal;

import java.util.List;


/**
 * Interface that represents the relationship between a channel or pattern and client.
 */
public interface Subscription {
  /**
   * Equality of a subscription is represented by a combination of client and one of channel or
   * pattern
   */
  boolean isEqualTo(Object channelOrPattern, Client client);

  /**
   * Will publish a message to the designated channel.
   */
  void publishMessage(String channel, byte[] message,
      PublishResultCollector publishResultCollector);

  /**
   * Verifies that the subscription is established with the designated client.
   */
  boolean matchesClient(Client client);

  /**
   * Verifies that the subscription channel or pattern matches the designated channel.
   */
  boolean matches(String channel);

  /**
   * The response dependent on the type of the subscription
   */
  List<Object> createResponse(String channel, byte[] message);
}
