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
package com.cognifide.aet.common;

import com.cognifide.aet.communication.api.ProcessingError;
import com.cognifide.aet.communication.api.exceptions.AETException;
import com.cognifide.aet.communication.api.messages.ProcessingErrorMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProcessingErrorMessageProcessorTest {

  public static final String DESCRIPTION = "Description";

  private ProcessingErrorMessageProcessor tested;

  @Mock
  private ProcessingErrorMessage message;

  private ProcessingError processingError;

  @Before
  public void setUp() {
    processingError = ProcessingError.comparingError(DESCRIPTION);
    when(message.getProcessingError()).thenReturn(processingError);

    tested = new ProcessingErrorMessageProcessor(message);
  }

  @Test
  public void processTest() throws AETException {
    tested.process();

    verify(message, times(2)).getProcessingError();
  }

  @Test(expected = AETException.class)
  public void processTest_nullData() throws AETException {
    when(message.getProcessingError()).thenReturn(null);

    tested.process();
  }
}
