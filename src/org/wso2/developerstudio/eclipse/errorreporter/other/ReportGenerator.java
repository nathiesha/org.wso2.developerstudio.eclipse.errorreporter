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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.json.simple.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ReportGenerator {

	private ErrorInformation errorInformation;
	private JSONObject json;
	private String filePath;
	private String errorMessage;

	// Error Report Contents
	private static final String DATE = "\nDate: ";
	private static final String ID = "\nIssueId: ";
	private static final String ISSUE_STATUS = "\nStatus: ";
	
	private static final String INTRODUCTION = "The following report will be sent to Jira:\n\n";

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

	@SuppressWarnings("unchecked")
	public JSONObject createIssue() {

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
	public String storeReport(String fileId) throws IOException {

		String fileName = fileId + ".txt";

		// temporary storage
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