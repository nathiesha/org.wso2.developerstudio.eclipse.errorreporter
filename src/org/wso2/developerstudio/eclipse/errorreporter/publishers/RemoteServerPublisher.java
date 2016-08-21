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
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.constants.PreferencePageStrings;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.JsonReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

/**
 * This class contains the methods to publish the error in WSO2 Jira, Email the
 * report through a remote web service.
 */
public class RemoteServerPublisher {

	private HttpURLConnection connectionJira;
	private HttpURLConnection connectionEmail;
	
	private String urlParametersJira;
	private String queryJira;
	private String queryEmail;
	private JSONObject json;
	ErrorReportInformation errorReportInformation;

	/**
	 * The constructor
	 */
	public RemoteServerPublisher(ErrorReportInformation errorReportInformation) {

		this.errorReportInformation = errorReportInformation;
	}

	/**
	 * This method contains initial configurations and settings done prior to
	 * publish the error report in Jira.
	 * 
	 * @param key
	 * @throws JSONException
	 * @throws IOException
	 * 
	 */
	private void initJira(String key) throws JSONException, IOException {

		// create a new JSON report using the given project key
		JsonReportGenerator jsonReport = new JsonReportGenerator(key);
		jsonReport.createReport(errorReportInformation);

		// convert the text report to a JSON object
		json = jsonReport.getIssue();
		urlParametersJira = json.toString();

		// create the query for the POST request
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		queryJira = String.format("urlParams=%s", URLEncoder.encode(urlParametersJira, charset));

		// get the server URL from preference page
		String remoteURL = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.SERVER_URL);
		URL url = new URL(remoteURL);

		// Create connection
		connectionJira = (HttpURLConnection) url.openConnection();
		connectionJira.setRequestMethod("POST");
		connectionJira.setRequestProperty("Content-Length", Integer.toString(queryJira.getBytes().length));
		connectionJira.setRequestProperty("Content-Language", "en-US");
		connectionJira.setRequestProperty("Accept-Charset", charset);
		connectionJira.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		connectionJira.setUseCaches(false);
		connectionJira.setDoOutput(true);

	}

	/**
	 * This method contains the logic to send request to the remote server and
	 * handles the error report publishing task in Jira.
	 * 
	 * @param key
	 * @return response from the server
	 * 
	 */
	public String publishJira(String key) {

		StringBuilder response = new StringBuilder();

		try {
			initJira(key);

			// Send request
			DataOutputStream outputStream = new DataOutputStream(connectionJira.getOutputStream());
			outputStream.writeBytes(queryJira);
			outputStream.close();

			// Get Response
			InputStream inputStream;

			if (connectionJira.getResponseCode() >= 400) {
				inputStream = connectionJira.getErrorStream();
			} else {
				inputStream = connectionJira.getInputStream();
			}

			// get the input stream to a bufferred reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}

			reader.close();

		} catch (JSONException | IOException e1) {

			// Notify the user in case of an error
			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error");
			messageBox.setMessage(
					"Please check the internet connection and Remote server URL in preferences page and try again!");
			e1.printStackTrace();

		} finally {
			// close the connection
			if (connectionJira != null) {
				connectionJira.disconnect();
			}
		}
		// return the response from the web service
		return response.toString();

	}

	/**
	 * This method contains initial configurations and settings done prior to
	 * publish the error report as an Email.
	 * 
	 * @param body
	 * @param recEmail
	 * @throws IOException
	 * 
	 */

	void initEmail(String recEmail, String body) throws IOException {

		//get the remote server URL from the preferences page
		String emailUrl = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.EMAIL_SERVER_URL);
		URL url = new URL(emailUrl);
		

		//create the query to be posted in web service
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		queryEmail = String.format("recmail=%s&body=%s", URLEncoder.encode(recEmail, charset),
				URLEncoder.encode(body, charset));

		// Create connection
		connectionEmail = (HttpURLConnection) url.openConnection();
		connectionEmail.setRequestMethod("POST");
		connectionEmail.setRequestProperty("Content-Length", Integer.toString(queryEmail.getBytes().length));
		connectionEmail.setRequestProperty("Content-Language", "en-US");
		connectionEmail.setRequestProperty("Accept-Charset", charset);
		connectionEmail.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		connectionEmail.setUseCaches(false);
		connectionEmail.setDoOutput(true);

	}

	/**
	 * This method contains the logic to connect with remote server and handles
	 * the error report publishing task as an Email.
	 * 
	 * @param key
	 * @return response from the server
	 * 
	 */
	public String publishEmail(String recEmail, String body) {

		StringBuilder response = new StringBuilder();

		try {
			initEmail(recEmail, body);
			
			// Send request
			DataOutputStream outputStream = new DataOutputStream(connectionEmail.getOutputStream());
			outputStream.writeBytes(queryEmail);
			outputStream.close();

			// Get Response
			InputStream inputStream;

			if (connectionEmail.getResponseCode() >= 400) {
				inputStream = connectionEmail.getErrorStream();
			} else {
				inputStream = connectionEmail.getInputStream();
			}

			//create the response string
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
			if (connectionEmail != null) {
				connectionEmail.disconnect();
			}

		}
		return response.toString();

	}
}
