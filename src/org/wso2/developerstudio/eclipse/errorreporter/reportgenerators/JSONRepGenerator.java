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


package org.wso2.developerstudio.eclipse.errorreporter.reportgenerators;


import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;

/**
 * @author Nathie
 *
 */
public class JSONRepGenerator implements ReportGenerator {
	
	// Jira issue fields
	private static final String PROJECT = "project";
	private static final String ISSUE_TYPE = "issuetype";
	private static final String SUMMARY = "summary";
	private static final String DESCRIPTION = "description";
	private static final String iSSUE_TYPE_NAME = "Bug";
	
	private JSONObject issue;

	public JSONRepGenerator() {
		super();
	}

	public void createReport(ErrorInformation errorInformation,String key) throws Exception {
		TextReportGenerator txt=new TextReportGenerator();
		JSONObject json = new JSONObject();
		String summary="Testing error reporting tool-GSoC Project-" +errorInformation.getMessage();
		txt.createReport(errorInformation);
		String description=txt.getTextString();
				
		
		JSONObject js = new JSONObject();
		js.put("key", key);
		
		JSONObject js2 = new JSONObject();
		js2.put("name", iSSUE_TYPE_NAME);

		json.put(PROJECT, js);
		json.put(ISSUE_TYPE,js2);
		json.put(SUMMARY, summary);
		json.put(DESCRIPTION, description);

		issue=new JSONObject();
		issue.put("fields", json);

		
	}

	public JSONObject getIssue() {
		return issue;
	}

	public void setIssue(JSONObject issue) {
		this.issue = issue;
	}

	@Override
	public void createReport(ErrorInformation errorInformation) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
