/**
 * Automated Exploratory Tests
 *
 * Copyright (C) 2013 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognifide.aet.runner.util;

import com.cognifide.aet.communication.api.exceptions.AETException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * Created by tomasz.misiewicz on 2015-01-13.
 */
@Service(MessagesManager.class)
@Component(immediate = true, metatype = true, description = "AET Messages Manager", label = "AET Messages Manager")
public class MessagesManager {

  private static final String JMX_URL_PROPERTY_NAME = "jxm-url";

  private static final String DEFAULT_JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:11199/jmxrmi";

  @Property(name = JMX_URL_PROPERTY_NAME, label = "ActiveMQ JMX endpoint URL", description = "ActiveMQ JMX endpoint URL", value = DEFAULT_JMX_URL)
  private String jmxUrl;

  private static final String REMOVE_OPERATION_NAME = "removeMatchingMessages";

  private static final String JMS_CORRELATION_ID = "JMSCorrelationID";

  private static final String STRING_SIGNATURE = "java.lang.String";

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesManager.class);

  @Activate
  public void activate(Map properties) {
    jmxUrl = PropertiesUtil.toString(properties.get(JMX_URL_PROPERTY_NAME), DEFAULT_JMX_URL);
  }

  /**
   * Removes all messages with given correlationID. AETException is thrown when failed to remove messages.
   *
   * @param correlationID - correlationId of messages that will be removed.
   */
  public void remove(String correlationID) throws AETException {
    Object[] removeSelector = {JMS_CORRELATION_ID + "='" + correlationID + "'"};
    String[] signature = {STRING_SIGNATURE};

    try {
      MBeanServerConnection connection = MessagesHelper.setupConnection(jmxUrl);
      for (ObjectName queue : MessagesHelper.getAetQueuesObjects(connection)) {
        String queueName = queue.getKeyProperty(MessagesHelper.DESTINATION_NAME_PROPERTY);
        int deletedMessagesNumber = (Integer) connection.invoke(queue, REMOVE_OPERATION_NAME,
                removeSelector, signature);
        LOGGER.debug("Deleted: {} jmsMessages from: {} queue", deletedMessagesNumber, queueName);
      }
    } catch (Exception e) {
      throw new AETException(String.format("Error while removing messages with correlationID: %s",
              correlationID), e);
    }
  }

  public static String createFullQueueName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("Queue name can't be null or empty string!");
    }
    return MessagesHelper.AET_QUEUE_DOMAIN + name;
  }

  protected String getJmxUrl() {
    return jmxUrl;
  }
}
