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

import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.constants.JiraIssueFields;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

/**
 * This class contains logic to generate a JSON object, given the error
 * information and the Jira Project key
 */

public class JsonReportGenerator implements ReportGenerator {

	private JSONObject issue;
	private String key;

	/**
	 * 
	 * Constructor that requires the Jira Project key
	 */
	public JsonReportGenerator(String key) {
		super();
		this.key = key;
	}

	/**
	 * This method creates the JSON object that is sent to Jira to create an
	 * issue
	 * 
	 * @param errorReportInformation
	 * @throws JSONException
	 * 
	 */
	@Override
	public void createReport(ErrorReportInformation errorReportInformation) throws JSONException {

		TextReportGenerator textRepGen = new TextReportGenerator();
		textRepGen.createReport(errorReportInformation);

		String summary = JiraIssueFields.TITLE.concat(errorReportInformation.getMessage());
		String description = textRepGen.getTextString();

		JSONObject jsonOut = new JSONObject();

		JSONObject jsonIn1 = new JSONObject();
		jsonIn1.put(JiraIssueFields.PROJECT_KEY, key);

		JSONObject jsonIn2 = new JSONObject();
		jsonIn2.put(JiraIssueFields.ISSUE_TYPE_NAME, JiraIssueFields.ISSUE_TYPE_NAME_VALUE);
		jsonOut.put(JiraIssueFields.PROJECT, jsonIn1);
		jsonOut.put(JiraIssueFields.ISSUE_TYPE, jsonIn2);
		jsonOut.put(JiraIssueFields.SUMMARY, summary);
		jsonOut.put(JiraIssueFields.DESCRIPTION, description);

		issue = new JSONObject();
		issue.put(JiraIssueFields.FIELDS, jsonOut);

	}

	/**
	 * Getters and setters
	 */

	public JSONObject getIssue() {
		return issue;
	}

	public void setIssue(JSONObject issue) {
		this.issue = issue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
