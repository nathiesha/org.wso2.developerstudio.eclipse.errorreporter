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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.JSONRepGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;

/**
 * @author Nathie
 *
 */
public class RemoteServerPublisher {


	private HttpURLConnection connection;
	private String urlParameters;
	private JSONObject json;
	private static final String TARGET_URL="http://localhost:8080/publish";
	ErrorInformation errorInformation;
	

	public RemoteServerPublisher(ErrorInformation errorInformation) {

		this.errorInformation = errorInformation;
	}

	public RemoteServerPublisher() {

	}
	   void init () throws Exception{
	        //init : read preferences for JIRA resp API connection params
	    	
//			JSONRepGenerator nw=new JSONRepGenerator();
//			nw.createReport(errorInformation);
//			json=nw.getIssue();
//			String username=Activator.getDefault().getPreferenceStore().getString("JIRA_USERNAME");
//			String password=Activator.getDefault().getPreferenceStore().getString("JIRA_PASSWORD");
//			String userCredentials = username+":"+password;

			
		    URL url = new URL(TARGET_URL);
		    connection = (HttpURLConnection)url.openConnection();
//			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
//			connection.setRequestProperty ("Authorization", basicAuth);
		    connection.setRequestMethod("POST");

	   }
	 
	    // implement publish method 
	    public String publish() throws Exception{
	     init();
	    //post to JIRA api and create issue

	     //urlParameters=json.toString();
		  urlParameters="{\"fields\": {\"project\":{ \"key\": \"TOOLS\"},\"summary\": \"GSOC ERROR REPORTER TEST.\",\"description\": \"Creating of an issue through Developer Studio using the REST API\",\"issuetype\": {\"name\": \"Bug\"}}}";	
		  String charset =java.nio.charset.StandardCharsets.UTF_8.name();
		  String param1 = urlParameters;
		     // ...

		     String query = String.format("urlParams=%s", 
		          URLEncoder.encode(param1, charset));
		  
		  try {
			  
			    connection.setRequestProperty("Content-Length", 
				        Integer.toString(query.getBytes().length));
				    connection.setRequestProperty("Content-Language", "en-US");  
				    connection.setRequestProperty("Accept-Charset", charset);
				    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);


				    connection.setUseCaches(false);
				    connection.setDoOutput(true);
		    //Create connection


		    //Send request

			System.out.println("InsidePublish");
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(query);
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
		  } 
		  catch (Exception e) {
		    e.printStackTrace();
		    return null;
		    
		  } 
		  finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
	    }
}
