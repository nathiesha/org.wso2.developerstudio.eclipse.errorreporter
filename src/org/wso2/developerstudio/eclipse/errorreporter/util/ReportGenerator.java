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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;

public class ReportGenerator {

	private ErrorInformation errorInformation;
	private JSONObject json;
	private String filePath;


	// Error Report Contents
	private static final String DATE = "\nDate: ";
	private static final String ID = "\nIssueId: ";
	private static final String ISSUE_STATUS = "\nStatus: ";
	
	private static final String INTRODUCTION = "\nThe following report will be sent to Jira:\n\n";

	private static final String STATUS = "\n--STATUS--\n";
	private static final String PLUGIN_ID = "\nPlugin ID: ";
	private static final String PLUGIN_VERSION = "\nPlugin Version: ";
	private static final String CODE = "\nCode: ";
	private static final String SEVERITY = "\nSeverity: ";
	private static final String MESSAGE = "\nMessage: ";
	private static final String EXCEPTION = "\nException: ";
	private static final String MULTI_STATUS_INFORMATION = "\n\nMulti status: ";

	private static final String REPORT = "\n\n--REPORT--\n";
	private static final String ECLIPSE_BUILD_ID = "\nEclipse Build ID: ";
	private static final String ECLIPSE_PRODUCT = "\nEclipse Product: ";
	private static final String JAVA_RUNTIME_VERSION = "\nJava Runtime Version: ";
	private static final String OSGIWS = "\nOsgiWS: ";
	private static final String OSGI_OS = "\nOsgiOS: ";
	private static final String OSGI_OS_VERSION = "\nOsgiOS Version: ";
	private static final String OSGI_ARCH = "\nOsgiArch: ";

	private static final String REPORT_SENDER = "\n\n--REPORT SENDER DETAILS--\n";
	private static final String NAME = "\nName: ";
	private static final String EMAIL = "\nEmail: ";
	private static final String COMMENT = "\nComment: ";
	private static final String SEVERITY_USER = "\nSeverity: ";

	// Jira issue fields
	private static final String PROJECT = "project";
	private static final String ISSUE_TYPE = "issuetype";
	private static final String SUMMARY = "summary";
	private static final String DESCRIPTION = "description";
	
	private static final String PROJECT_KEY = "TOOLS";
	private static final String iSSUE_TYPE_NAME = "Bug";



	public ReportGenerator(ErrorInformation errorInformation) {
		super();
		this.errorInformation = errorInformation;
	}

	public JSONObject createIssue() throws JSONException {

		json = new JSONObject();
		String summary="Testing error reporting tool-GSoC Project-" +errorInformation.getMessage();
		String description=writeString();
		
		JSONObject js = new JSONObject();
		js.put("key", PROJECT_KEY);
		
		JSONObject js2 = new JSONObject();
		js2.put("name", iSSUE_TYPE_NAME);

		json.put(PROJECT, js);
		json.put(ISSUE_TYPE,js2);
		json.put(SUMMARY, summary);
		json.put(DESCRIPTION, description);

		JSONObject issue = new JSONObject();
		issue.put("fields", json);

		return issue;
	}
	
	
	public String writeString(){
		
		StringBuilder sb = new StringBuilder();


		sb.append(INTRODUCTION);
		sb.append(STATUS);
		sb.append(PLUGIN_ID + errorInformation.getPluginId());
		sb.append(PLUGIN_VERSION + errorInformation.getPluginVersion());
		sb.append(CODE + errorInformation.getCode());
		sb.append(SEVERITY + errorInformation.getSeverity());
		sb.append(MESSAGE + errorInformation.getMessage());
		sb.append(EXCEPTION + errorInformation.getExceptionS());
		sb.append(MULTI_STATUS_INFORMATION + errorInformation.getMultiStatus());
		sb.append(REPORT);
		sb.append(ECLIPSE_BUILD_ID + errorInformation.getEclipseBuildId());
		sb.append(ECLIPSE_PRODUCT + errorInformation.getEclipseProduct());
		sb.append(JAVA_RUNTIME_VERSION + errorInformation.getJavaRuntimeVersion());
		sb.append(OSGIWS + errorInformation.getOsgiWs());
		sb.append(OSGI_OS + errorInformation.getOsgiOs());
		sb.append(OSGI_OS_VERSION + errorInformation.getOsgiOsVersion());
		sb.append(OSGI_ARCH + errorInformation.getOsgiArch());
		sb.append(REPORT_SENDER);
		sb.append(NAME + errorInformation.getName());
		sb.append(EMAIL + errorInformation.getEmail());
		sb.append(COMMENT + errorInformation.getComment());
		sb.append(SEVERITY_USER + errorInformation.getSeverity2());
		
		return sb.toString();

	}

	// store the errorReport and return its location
	public String storeReport(String Id) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String timeStamp=(dateFormat.format(date)); //2014/08/06 15:59:48
		
		String fileName = timeStamp + ".txt";
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		IPath stateLoc = Platform.getStateLocation(bundle);

		File tempFolder = new File(stateLoc.toString());
		File reportFolder = new File(tempFolder, "ErrorReports");

		if (!reportFolder.exists()) {

			reportFolder.mkdir();
		}

		File reportTemp = new File(reportFolder, fileName);
		FileWriter fw = new FileWriter(reportTemp);
		writeReport(fw);
		fw.close();

		// persistent storage in User Directory
		String userDirLocation = System.getProperty("user.dir");

		File persistentFolder = new File(userDirLocation);
		File errorReportsFolder = new File(persistentFolder, "ErrorReports");

		if (!errorReportsFolder.exists()) {

			errorReportsFolder.mkdir();
		}

		File reportPersistent = new File(errorReportsFolder, fileName);
		FileWriter fw2 = new FileWriter(reportPersistent);
		writeReport(fw2);
		fw2.close();

		filePath = reportPersistent.getPath();
		return filePath;

	}


	private void writeReport(FileWriter fw) throws IOException {

		// = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String timeStamp=(dateFormat.format(date)); //2014/08/06 15:59:48
		
		
		fw.write(ID);
		fw.write(DATE+ timeStamp);
		fw.write(EXCEPTION + errorInformation.getExceptionS());
		fw.write(ISSUE_STATUS);
		fw.write(STATUS);
		fw.write(PLUGIN_ID + errorInformation.getPluginId());
		fw.write(PLUGIN_VERSION + errorInformation.getPluginVersion());
		fw.write(CODE + errorInformation.getCode());
		fw.write(SEVERITY + errorInformation.getSeverity());
		fw.write(MESSAGE + errorInformation.getMessage());
		fw.write(EXCEPTION + errorInformation.getExceptionS());
		fw.write(MULTI_STATUS_INFORMATION + errorInformation.getMultiStatus());
		fw.write(REPORT);
		fw.write(ECLIPSE_BUILD_ID + errorInformation.getEclipseBuildId());
		fw.write(ECLIPSE_PRODUCT + errorInformation.getEclipseProduct());
		fw.write(JAVA_RUNTIME_VERSION + errorInformation.getJavaRuntimeVersion());
		fw.write(OSGIWS + errorInformation.getOsgiWs());
		fw.write(OSGI_OS + errorInformation.getOsgiOs());
		fw.write(OSGI_OS_VERSION + errorInformation.getOsgiOsVersion());
		fw.write(OSGI_ARCH + errorInformation.getOsgiArch());
		fw.write(REPORT_SENDER);
		fw.write(NAME + errorInformation.getName());
		fw.write(EMAIL + errorInformation.getEmail());
		fw.write(COMMENT + errorInformation.getComment());
		fw.write(SEVERITY_USER + errorInformation.getSeverity2());

	}
	
	public String getIssueId(String jsonStr) throws JSONException
	{
		
			JSONObject jsonObj;

			jsonObj = new JSONObject(jsonStr);
	        String id = jsonObj.getString("id");
	        System.out.println(id);
	        String key = jsonObj.getString("key");
	        System.out.println(key);
//	        first = jsonObj.getJSONArray("arrArray").getJSONObject(0).getString("a");
//	        System.out.println(first);
	        
		return jsonStr;
	}
	
		//{"expand":"renderedFields,names,schema,transitions,operations,editmeta,changelog","id":"80403","self":"https://wso2.org/jira/rest/api/2/issue/80403","key":"TOOLS-3418","fields":{"progress":{"progress":0,"total":0},"summary":"GSOC ERROR REPORTER TEST.","timetracking":{},"customfield_10084":null,"customfield_10113":null,"issuetype":{"self":"https://wso2.org/jira/rest/api/2/issuetype/1","id":"1","description":"A problem which impairs or prevents the functions of the product.","iconUrl":"https://wso2.org/jira/images/icons/issuetypes/bug.png","name":"Bug","subtask":false},"customfield_10080":null,"votes":{"self":"https://wso2.org/jira/rest/api/2/issue/TOOLS-3418/votes","votes":0,"hasVoted":false},"resolution":null,"fixVersions":[],"resolutiondate":null,"timespent":null,"reporter":{"self":"https://wso2.org/jira/rest/api/2/user?username=nathieshamaddage%40gmail.com","name":"nathieshamaddage@gmail.com","emailAddress":"nathieshamaddage@gmail.com","avatarUrls":{"16x16":"https://wso2.org/jira/secure/useravatar?size=xsmall&avatarId=10142","24x24":"https://wso2.org/jira/secure/useravatar?size=small&avatarId=10142","32x32":"https://wso2.org/jira/secure/useravatar?size=medium&avatarId=10142","48x48":"https://wso2.org/jira/secure/useravatar?avatarId=10142"},"displayName":"Nathiesha Maddage","active":true},"customfield_10122":"68285","aggregatetimeoriginalestimate":null,"updated":"2016-06-08T09:00:28.000+0530","created":"2016-06-08T09:00:28.000+0530","priority":{"self":"https://wso2.org/jira/rest/api/2/priority/3","iconUrl":"https://wso2.org/jira/images/icons/priorities/major.png","name":"Normal","id":"3"},"description":"Creating of an issue through Developer Studio using the REST API","customfield_10120":null,"customfield_10121":null,"duedate":null,"customfield_10020":null,"customfield_10040":{"self":"https://wso2.org/jira/rest/api/2/customFieldOption/10012","value":"Moderate","id":"10012"},"issuelinks":[],"watches":{"self":"https://wso2.org/jira/rest/api/2/issue/TOOLS-3418/watchers","watchCount":1,"isWatching":false},"worklog":{"startAt":0,"maxResults":0,"total":0,"worklogs":[]},"subtasks":[],"status":{"self":"https://wso2.org/jira/rest/api/2/status/1","description":"The issue is open and ready for the assignee to start work on it.","iconUrl":"https://wso2.org/jira/images/icons/statuses/open.png","name":"Open","id":"1"},"customfield_10090":{"self":"https://wso2.org/jira/rest/api/2/customFieldOption/10061","value":"Yes","id":"10061"},"labels":[],"workratio":-1,"assignee":null,"attachment":[],"customfield_10220":null,"aggregatetimeestimate":null,"project":{"self":"https://wso2.org/jira/rest/api/2/project/TOOLS","id":"10030","key":"TOOLS","name":"WSO2 Developer Studio","avatarUrls":{"16x16":"https://wso2.org/jira/secure/projectavatar?size=xsmall&pid=10030&avatarId=10011","24x24":"https://wso2.org/jira/secure/projectavatar?size=small&pid=10030&avatarId=10011","32x32":"https://wso2.org/jira/secure/projectavatar?size=medium&pid=10030&avatarId=10011","48x48":"https://wso2.org/jira/secure/projectavatar?pid=10030&avatarId=10011"}},"versions":[],"customfield_10075":{"self":"https://wso2.org/jira/rest/api/2/customFieldOption/10039","value":"Major","id":"10039"},"environment":null,"timeestimate":null,"aggregateprogress":{"progress":0,"total":0},"lastViewed":null,"components":[],"comment":{"startAt":0,"maxResults":0,"total":0,"comments":[]},"timeoriginalestimate":null,"aggregatetimespent":null}}


	public String getIssueStatus(String jsonStr) throws JSONException
	{
		
			JSONObject jsonObj;

			jsonObj = new JSONObject(jsonStr);
	        String id = jsonObj.getString("id");
	        System.out.println(id);
//	        String key = jsonObj.getString("key");
//	        System.out.println(key);
	        String key = jsonObj.getJSONArray("fields").getJSONObject(0).getString("key");
	        System.out.println(key);
	        
		return jsonStr;
	}

	//getters and setters
	public ErrorInformation getErrorInformation() {
		return errorInformation;
	}

	public void setErrorInformation(ErrorInformation errorInformation) {
		this.errorInformation = errorInformation;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}