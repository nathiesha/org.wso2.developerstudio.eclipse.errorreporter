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

import org.wso2.developerstudio.eclipse.errorreporter.constants.TextReportLabels;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

/**
 * This class contains logic to generate a text report, given the error
 * information to send to Jira and email
 */
public class TextReportGenerator implements ReportGenerator {

	private String textString;

	/**
	 * This method creates the text report
	 * @param errorReportInformation
	 * @return 
	 */
	@Override
	public void createReport(ErrorReportInformation errorReportInformation) {

		StringBuilder sb = new StringBuilder();

		sb.append(TextReportLabels.INTRODUCTION);
		sb.append(TextReportLabels.STATUS);
		sb.append(TextReportLabels.PLUGIN_ID.concat(errorReportInformation.getPluginId()));
		sb.append(TextReportLabels.PLUGIN_VERSION.concat(errorReportInformation.getPluginVersion()));
		sb.append(TextReportLabels.CODE + errorReportInformation.getCode());
		sb.append(TextReportLabels.SEVERITY + errorReportInformation.getSeverity());
		sb.append(TextReportLabels.MESSAGE.concat(errorReportInformation.getMessage()));
		sb.append(TextReportLabels.EXCEPTION.concat(errorReportInformation.getExceptionS()));
		sb.append(TextReportLabels.MULTI_STATUS_INFORMATION.concat(errorReportInformation.getMultiStatus()));
		sb.append(TextReportLabels.REPORT);
		sb.append(TextReportLabels.ECLIPSE_BUILD_ID.concat(errorReportInformation.getEclipseBuildId()));
		sb.append(TextReportLabels.ECLIPSE_PRODUCT + errorReportInformation.getEclipseProduct());
		sb.append(TextReportLabels.JAVA_RUNTIME_VERSION + errorReportInformation.getJavaRuntimeVersion());
		sb.append(TextReportLabels.OSGIWS + errorReportInformation.getOsgiWs());
		sb.append(TextReportLabels.OSGI_OS + errorReportInformation.getOsgiOs());
		sb.append(TextReportLabels.OSGI_OS_VERSION + errorReportInformation.getOsgiOsVersion());
		sb.append(TextReportLabels.OSGI_ARCH + errorReportInformation.getOsgiArch());
		sb.append(TextReportLabels.REPORT_SENDER);
		sb.append(TextReportLabels.NAME.concat(errorReportInformation.getName()));
		sb.append(TextReportLabels.EMAIL.concat(errorReportInformation.getEmail()));
		sb.append(TextReportLabels.ORGANIZATION.concat(errorReportInformation.getOrganization()));
		sb.append(TextReportLabels.COMMENT + (errorReportInformation.getComment()));
		sb.append(TextReportLabels.RELATED_PLUGINS);
		sb.append(errorReportInformation.getPackage());

		setTextString(sb.toString());

	}

	/**
	 * Getters and setters
	 * 
	 */
	public String getTextString() {

		return textString;
	}

	public void setTextString(String textString) {
		this.textString = textString;
	}

}