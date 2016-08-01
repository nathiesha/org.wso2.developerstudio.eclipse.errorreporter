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
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nathie
 *
 */
public class JiraStatusChecker {
	
	public String getIssueStatus(String jsonStr) throws JSONException
	{
		
			JSONObject jsonObj;
			//jsonStr="{\"expand\":\"renderedFields,names,schema,transitions,operations,editmeta,changelog\",\"key\":\"80403\",\"fields\":{\"summary\":\"GSOC ERROR REPORTER TEST.\",\"timetracking\":{},\"resolutiondate\":null,\"timespent\":null,\"reporter\":{\"self\":\"https://wso2.org/jira/rest/api/2/user?username=nathieshamaddage%40gmail.com\",\"name\":\"nathieshamaddage@gmail.com\",\"emailAddress\":\"nathieshamaddage@gmail.com\",	},\"subtasks\":[],\"status\":{\"self\":\"https://wso2.org/jira/rest/api/2/status/1\",\"description\":\"The issue is open and ready for the assignee to start work on it.\",\"iconUrl\":\"https://wso2.org/jira/images/icons/statuses/open.png\",\"name\":\"Open\",\"key\":\"1\"},}}";
			jsonObj = new JSONObject(jsonStr);
	        String name = "The issue status : "+jsonObj.getJSONObject("fields").getJSONObject("status").getString("name");
	        String desc = "\n"+jsonObj.getJSONObject("fields").getJSONObject("status").getString("description");
	        String status=name+desc;

		return status;
	}
	
	
	public  String executeGet(String targetURL, String userCredentials) throws IOException {

		String output="";
		String response="";

		URL url = new URL(targetURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			while ((output = br.readLine()) != null) {

				response+=output;
			}
			
			conn.disconnect();

			return response;
	}

}
