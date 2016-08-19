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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
	 * @return the JSON response
	 */

	public static String executeGet(String targetURL) {

		String output = "";
		String response = "";

		URL url;
		HttpURLConnection conn;
		BufferedReader br;
		try {
			url = new URL(targetURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {

				Shell shell = new Shell();
				MessageBox messageBox = new MessageBox(shell);
				messageBox.setText("Error");
				messageBox.setMessage("Failed : HTTP error code : " + conn.getResponseCode()
						+ "\n Please check the internet connection and the remote server URL at Developer Studio Error Reporter Preferences Page and try again!");

			}

			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {

				response += output;
			}
			conn.disconnect();

		} catch (MalformedURLException e) {

			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error");
			messageBox.setMessage(
					"Please check the remote server URL at Developer Studio Error Reporter Preferences Page and try again!");

		} catch (IOException e) {
			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error");
			messageBox.setMessage("Please check the internet connection and try again!");

		}

		return response;
	}

}
