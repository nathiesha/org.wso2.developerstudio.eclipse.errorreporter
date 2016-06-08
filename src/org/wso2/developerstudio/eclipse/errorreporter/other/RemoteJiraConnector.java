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

/**
 * @author Nathie
 *
 */
public class RemoteJiraConnector {

	/**
	 * 
	 */
	public RemoteJiraConnector() {
		// TODO Auto-generated constructor stub
	}
	
	public static String excutePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;  
		  urlParameters="{\"fields\": {\"project\":{ \"key\": \"TOOLS\"},\"summary\": \"GSOC ERROR REPORTER TEST.\",\"description\": \"Creating of an issue through Developer Studio using the REST API\",\"issuetype\": {\"name\": \"Bug\"}}}";	
		  System.out.println("1");
		  try {
			  
		    //Create connection
		    URL url = new URL("https://wso2.org/jira/rest/api/2/issue");
		    connection = (HttpURLConnection)url.openConnection();
			System.out.println("2");
			String userCredentials = "nathieshamaddage@gmail.com:Dinanatz<3";
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			System.out.println("3");
			connection.setRequestProperty ("Authorization", basicAuth);
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/json");
		    System.out.println("4");
		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
			System.out.println("5");

		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
			System.out.println("6");
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

}
