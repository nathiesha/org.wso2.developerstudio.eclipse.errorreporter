/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.errorreporter.publishers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.JsonReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

public class JiraPublisher implements ErrorPublisher {

	private HttpURLConnection connection;
	private String urlParameters;
	private JSONObject json;
	private static final String TARGET_URL = "https://wso2.org/jira/rest/api/2/issue";
	ErrorReportInformation errorReportInformation;

	public JiraPublisher(ErrorReportInformation errorReportInformation) {

		this.errorReportInformation = errorReportInformation;
	}

	public String getAuth(String username, String password) {
		try {
			String s = username + ":" + password;
			byte[] byteArray = s.getBytes();
			String auth;
			auth = Base64.encodeBase64String(byteArray);

			return auth;
		} catch (Exception ignore) {
			return "";
		}
	}

	void init() throws Exception {
		// init : read preferences for JIRA resp API connection params

		JsonReportGenerator nw = new JsonReportGenerator(
				Activator.getDefault().getPreferenceStore().getString("PROJECT_KEY"));
		nw.createReport(errorReportInformation);
		json = nw.getIssue();
		String username = Activator.getDefault().getPreferenceStore().getString("JIRA_USERNAME");
		String password = Activator.getDefault().getPreferenceStore().getString("JIRA_PASSWORD");
		String userCredentials = username + ":" + password;

		URL url = new URL(TARGET_URL);
		connection = (HttpURLConnection) url.openConnection();
		String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
		connection.setRequestProperty("Authorization", basicAuth);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");

		connection.setUseCaches(false);
		connection.setDoOutput(true);
	}

	// implement publish method
	@Override
	public String publish(TextReportGenerator reportGen) throws Exception {
		init();
		// post to JIRA api and create issue

		urlParameters = json.toString();
		try {

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is;

			if (connection.getResponseCode() >= 400) {
				is = connection.getErrorStream();
			} else {
				is = connection.getInputStream();
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
			// not Java 5+
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
