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

package org.wso2.developerstudio.eclipse.errorreporter.other;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

public class RemoteJiraConnector {

	private HttpURLConnection connection;
	private String urlParameters;

	public RemoteJiraConnector() {

	}



	public  String excutePost(String targetURL, JSONObject issue,String userCredentials) {

		  urlParameters="{\"fields\": {\"project\":{ \"key\": \"TOOLS\"},\"summary\": \"GSOC ERROR REPORTER TEST.\",\"description\": \"Creating of an issue through Developer Studio using the REST API\",\"issuetype\": {\"name\": \"Bug\"}}}";	
		  String targetURL1="https://wso2.org/jira/rest/api/2/issue";
		  try {
			  
		    //Create connection
		    URL url = new URL(targetURL1);
		    connection = (HttpURLConnection)url.openConnection();
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			connection.setRequestProperty ("Authorization", basicAuth);
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/json");
		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request


		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is ;

			if (connection.getResponseCode() >= 400) {
			    is = connection.getErrorStream();
			} else {
			    is = connection.getInputStream();
			}
			
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
		    String line;
		    
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
		}

	@SuppressWarnings("unused")
	private String getAuth(String username, String password) {
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

}
