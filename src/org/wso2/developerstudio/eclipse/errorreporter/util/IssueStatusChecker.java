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

package org.wso2.developerstudio.eclipse.errorreporter.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.constants.JiraIssueFields;

/**
 * This util class contains logic to retrive the current status of any
 * previously reported issue in Jira.
 */

public class IssueStatusChecker {

	/**
	 * This method contains logic to extract the issue status and description
	 * from the response JSON string.
	 * 
	 * @param jsonStr
	 * @return the string that contains the issue status.
	 */

	public static String getIssueStatus(String jsonStr) {

		JSONObject jsonObj = null;
		String name = "The issue status cannot be found!";
		String description = "";

		try {
			jsonObj = new JSONObject(jsonStr);
			name = "The issue status : " + jsonObj.getJSONObject(JiraIssueFields.FIELDS)
					.getJSONObject(JiraIssueFields.STATUS).getString(JiraIssueFields.NAME);
			description = "\n" + jsonObj.getJSONObject(JiraIssueFields.FIELDS).getJSONObject(JiraIssueFields.STATUS)
					.getString(JiraIssueFields.DESCRIPTION);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		String status = name + description;

		return status;
	}

	/**
	 * This method contains logic to send a GET request to the remote server to
	 * get the current status of the selected issue.
	 * 
	 * @param targetURL
	 * @param id
	 * @return the JSON response
	 */

	public static String executeGet(String targetURL, String id) {

		StringBuilder response = new StringBuilder();

		URL url;
		HttpURLConnection connection = null;

		try {
			// create the query for the POST request
			String charset = java.nio.charset.StandardCharsets.UTF_8.name();
			String queryStatus = String.format("id=%s", URLEncoder.encode(id, charset));

			url = new URL(targetURL);
			// Create connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", Integer.toString(queryStatus.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(queryStatus);
			outputStream.close();

			// Get Response
			InputStream inputStream;

			if (connection.getResponseCode() >= 400) {
				inputStream = connection.getErrorStream();
			} else {
				inputStream = connection.getInputStream();
			}

			// get the input stream to a bufferred reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}

			reader.close();

		} catch (IOException e1) {

			// Notify the user in case of an error
			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error");
			messageBox.setMessage(
					"Please check the internet connection and Remote server URL in preferences page and try again!");
			e1.printStackTrace();

		} finally {
			// close the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		// return the response from the web service
		return response.toString();

}

}
