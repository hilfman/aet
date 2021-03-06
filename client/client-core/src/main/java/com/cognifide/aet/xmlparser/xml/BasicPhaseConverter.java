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
package com.cognifide.aet.xmlparser.xml;

import com.google.common.collect.Maps;

import com.cognifide.aet.xmlparser.api.ParseException;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;

import java.util.Map;

abstract class BasicPhaseConverter<T> implements Converter<T> {

  protected BasicPhaseConverter() {
  }

  static Map<String, String> getParameters(InputNode inputNode) throws ParseException {
    Map<String, String> parameters = Maps.newHashMap();
    try {
      for (String name : inputNode.getAttributes()) {
        String value = inputNode.getAttribute(name).getValue();
        parameters.put(name, value);
      }
    } catch (Exception e) {
      throw new ParseException(e.getMessage(), e);
    }
    return parameters;
  }
}
