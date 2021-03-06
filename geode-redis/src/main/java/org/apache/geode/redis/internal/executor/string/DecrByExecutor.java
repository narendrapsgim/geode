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
 */
package org.apache.geode.redis.internal.executor.string;


import java.util.List;

import org.apache.geode.redis.internal.ByteArrayWrapper;
import org.apache.geode.redis.internal.Coder;
import org.apache.geode.redis.internal.Command;
import org.apache.geode.redis.internal.ExecutionHandlerContext;
import org.apache.geode.redis.internal.RedisConstants.ArityDef;

public class DecrByExecutor extends StringExecutor {

  private final String ERROR_DECREMENT_NOT_USABLE =
      "The decrementation on this key must be numeric";

  private final int DECREMENT_INDEX = 2;

  @Override
  public void executeCommand(Command command, ExecutionHandlerContext context) {
    List<byte[]> commandElems = command.getProcessedCommand();

    if (commandElems.size() < 3) {
      command.setResponse(Coder.getErrorResponse(context.getByteBufAllocator(), ArityDef.DECRBY));
      return;
    }
    ByteArrayWrapper key = command.getKey();

    byte[] decrArray = commandElems.get(DECREMENT_INDEX);
    String decrString = Coder.bytesToString(decrArray);
    long decrement;

    try {
      decrement = Long.parseLong(decrString);
    } catch (NumberFormatException e) {
      command.setResponse(
          Coder.getErrorResponse(context.getByteBufAllocator(), ERROR_DECREMENT_NOT_USABLE));
      return;
    }

    long value = getRedisStringCommands(context).decrby(key, decrement);
    command.setResponse(Coder.getIntegerResponse(context.getByteBufAllocator(), value));
  }
}
