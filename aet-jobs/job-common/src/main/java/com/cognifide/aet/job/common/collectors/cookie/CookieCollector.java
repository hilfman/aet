/*
 * Cognifide AET :: Job Common
 *
 * Copyright (C) 2013 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognifide.aet.job.common.collectors.cookie;

import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.cognifide.aet.job.api.collector.CollectorJob;
import com.cognifide.aet.job.api.collector.CollectorProperties;
import com.cognifide.aet.job.api.exceptions.ProcessingException;
import com.cognifide.aet.vs.Node;
import com.cognifide.aet.vs.VersionStorageException;

/**
 * @author lukasz.wieczorek
 */
public class CookieCollector implements CollectorJob {

	public static final String NAME = "cookie";

	private final Node dataNode;

	private final Node patternNode;

	private final WebDriver webDriver;

	private final String url;

	public CookieCollector(Node dataNode, CollectorProperties collectorProperties, Node patternNode,
			WebDriver webDriver) {
		this.dataNode = dataNode;
		this.patternNode = patternNode;
		this.webDriver = webDriver;
		this.url = collectorProperties.getUrl();
	}

	@Override
	public boolean collect() throws ProcessingException {
		Set<Cookie> cookies = webDriver.manage().getCookies();
		CookieCollectorResult cookieCollectorResult = new CookieCollectorResult(url, cookies);
		try {
			dataNode.saveResult(cookieCollectorResult);
			if (patternNode.getResult(CookieCollectorResult.class) == null) {
				patternNode.saveResult(cookieCollectorResult);
			}
		} catch (VersionStorageException e) {
			throw new ProcessingException(e.getMessage(), e);
		}
		return true;
	}

	@Override
	public void setParameters(Map<String, String> params) {
		// no parameters needed
	}

}