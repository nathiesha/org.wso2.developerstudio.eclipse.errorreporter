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


import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;

public class TextReportGenerator implements ReportGenerator {

	private String textString;
	
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
	private static final String ORGANIZATION = "\nOrganization: ";
	private static final String COMMENT = "\nComment: ";
	private static final String SEVERITY_USER = "\nSeverity: ";
	
	private static final String RELATED_PLUGINS = "\n\n--RELATED PLUGIN IDS--\n";




	public TextReportGenerator() {
		super();
	}


	@Override
	public  void createReport(ErrorInformation errorInformation) {
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
		sb.append(ORGANIZATION + errorInformation.getOrganization());
		sb.append(COMMENT + errorInformation.getComment());
		sb.append(SEVERITY_USER + errorInformation.getSeverity2());
		sb.append(RELATED_PLUGINS);
		sb.append(errorInformation.getPackage());
		
		setTextString(sb.toString());
		
	}


	public String getTextString() {
		
		return textString;
	}


	public void setTextString(String textString) {
		this.textString = textString;
	}


}