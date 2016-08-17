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
import java.net.URLEncoder;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.JsonReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

public class RemoteServerPublisher {

	private HttpURLConnection connection;
	private String urlParameters;
	private JSONObject json;
	ErrorReportInformation errorReportInformation;

	public RemoteServerPublisher(ErrorReportInformation errorReportInformation) {

		this.errorReportInformation = errorReportInformation;
	}

	void initEmail() throws Exception {
		// init : read preferences for JIRA resp API connection params

		// JsonReportGenerator nw = new JsonReportGenerator(key);
		// nw.createReport(errorReportInformation);
		// json = nw.getIssue();
		String emailUrl = Activator.getDefault().getPreferenceStore().getString("EMAIL_URL");
		URL url = new URL(emailUrl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

	}

	void initJira() throws Exception {
		// init : read preferences for JIRA resp API connection params

		String rem = Activator.getDefault().getPreferenceStore().getString("SERVER_URL");
		URL url = new URL(rem);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

	}

	// implement publish method
	public String publishEmail(String recEmail, String body) throws Exception {
		initEmail();
		// post to JIRA api and create issue
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String param1 = recEmail;
		String param2 = body;
		String query = String.format("recmail=%s&body=%s", URLEncoder.encode(param1, charset),
				URLEncoder.encode(param2, charset));
		try {
			// Create connection
			connection.setRequestProperty("Content-Length", Integer.toString(query.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			System.out.println("InsidePublish");
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(query);
			wr.close();

			// Get Response
			InputStream is;

			if (connection.getResponseCode() >= 400) {
				is = connection.getErrorStream();
			} else {
				is = connection.getInputStream();
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
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

	// implement publish method
	public String publishJira(String key) throws Exception {

		JsonReportGenerator nw = new JsonReportGenerator(key);
		nw.createReport(errorReportInformation);
		json = nw.getIssue();

		initJira();

		urlParameters = json.toString();
		// urlParameters="{\"fields\": {\"project\":{ \"key\":
		// \"TOOLS\"},\"summary\": \"GSOC ERROR REPORTER
		// TEST.\",\"description\": \"Creating of an issue through Developer
		// Studio using the REST API\",\"issuetype\": {\"name\": \"Bug\"}}}";
		System.out.println(urlParameters);

		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String param1 = urlParameters;
		String query = String.format("urlParams=%s", URLEncoder.encode(param1, charset));

		
		  try { // Create connection
		  connection.setRequestProperty("Content-Length",
		  Integer.toString(query.getBytes().length));
		  connection.setRequestProperty("Content-Language", "en-US");
		  connection.setRequestProperty("Accept-Charset", charset);
		  connection.setRequestProperty("Content-Type",
		  "application/x-www-form-urlencoded;charset=" + charset);
		  
		  connection.setUseCaches(false); connection.setDoOutput(true);
		  
		  // Send request 
		  DataOutputStream wr = new
		  DataOutputStream(connection.getOutputStream()); 
		  wr.writeBytes(query);
		  wr.close();
		  
		  // Get Response 
		  InputStream is;
		  
		  if (connection.getResponseCode() >= 400) { 
			  is =connection.getErrorStream(); 
			  } 
		  else { 
			  is =connection.getInputStream(); 
			  }
		  
		  BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		  StringBuilder response = new StringBuilder(); 
		  String line;
		  
		  while ((line = rd.readLine()) != null) { 
			  response.append(line);
			  response.append('\r'); 
		  } 
		  
		  rd.close(); 
		  return response.toString(); }
		  
		  catch (Exception e) { 
			  e.printStackTrace(); 
			  System.out.println("error");
			  return null;
		  
		 } 
		  finally { 
			  if (connection != null) 
			  { 
				  connection.disconnect(); 
				  } 
			  }
		 

	}
}
