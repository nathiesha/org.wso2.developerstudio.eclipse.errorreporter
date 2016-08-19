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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.constants.PreferencePageStrings;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

/**
 * This util class contains logic to extract all the information relavant to
 * create the error report.
 */

public class InformationCollector {

	static IStatus status;
	static ErrorReportInformation errorReportInformation;

	/**
	 * This method is calling all the required methods to collect information
	 * regarding the error report.
	 * 
	 * @param status
	 * @return An ErrorReportInformation object that contains all the required
	 *         fields needed to create the error repoert.
	 */

	public static ErrorReportInformation getInformation(IStatus status) {

		InformationCollector.status = status;
		errorReportInformation = new ErrorReportInformation();

		getErrorInfo();
		getSystemInfo();
		getUserInfo();
		getMultiStatusInfo();
		getPackageKeyInfo();

		return errorReportInformation;

	}

	/**
	 * This method is extracting the information regarding the exception
	 * occured, and set those values in the respective fields of the
	 * ErrorReportInformation object.
	 */
	public static void getErrorInfo() {

		errorReportInformation.setPluginId(status.getPlugin());

		try {
			Bundle bundle = Platform.getBundle(errorReportInformation.getPluginId());
			Version version = bundle.getVersion();
			errorReportInformation.setPluginVersion(version.toString());
		}

		// in case if given bundle id is not in the platform bundle list
		catch (Exception e) {
			errorReportInformation.setPluginVersion("Not available.");
		}

		errorReportInformation.setSeverity(status.getSeverity());
		errorReportInformation.setMessage(status.getMessage());
		errorReportInformation.setCode(status.getCode());

		errorReportInformation.setException((Exception) status.getException());

		// to convert the exception to string format
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		errorReportInformation.getException().printStackTrace(pw);
		errorReportInformation.setExceptionS(sw.toString());

	}

	/**
	 * This method is extracting the information regarding the environment, and
	 * set those values in the respective fields of the ErrorReportInformation
	 * object.
	 */
	public static void getSystemInfo() {

		errorReportInformation.setEclipseBuildId(System.getProperty("eclipse.buildId"));
		errorReportInformation.setEclipseProduct(System.getProperty("eclipse.product"));
		errorReportInformation.setJavaRuntimeVersion(System.getProperty("java.runtime.version"));
		errorReportInformation.setOsgiWs(System.getProperty("osgi.ws"));
		errorReportInformation.setOsgiOs(System.getProperty("os.name"));
		errorReportInformation.setOsgiOsVersion(System.getProperty("os.version"));
		errorReportInformation.setOsgiArch(System.getProperty("osgi.arch"));

	}

	/**
	 * This method is extracting the user set values important for the error
	 * report, and set those values in the respective fields of the
	 * ErrorReportInformation object.
	 */
	private static void getUserInfo() {

		errorReportInformation.setName(Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.NAME));
		errorReportInformation.setEmail(Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.EMAIL_USER));
		errorReportInformation.setOrganization((Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.ORGANIZATION)));

	}

	/**
	 * This method is extracting the information regarding the multistatus of
	 * the exception, and set those values in the respective fields of the
	 * ErrorReportInformation object.
	 */
	private static void getMultiStatusInfo() {

		StringBuilder message = new StringBuilder();

		if (isMultiStatus(status)) {
			message = extractMultiStatusInfo(status, 0);

		}

		//if multi status information is not availabe
		else {
			message.append("The exception does not contain any multi status information.");
		}

		errorReportInformation.setMultiStatus(message.toString());
	}

	/**
	 * This method gets the list of plugins that are relavant to the given plugin, and
	 * their JIRA keys, if available as extensions, and set those values in the
	 * ErrorReportInformation object.
	 */
	private static void getPackageKeyInfo() {

		Map<String, String> packageKeyAll = ExtensionKeyReader.getKeys();
		Map<String, String> packageKeyRelavant = new HashMap<String, String>();

		
		for (Map.Entry<String, String> entry : packageKeyAll.entrySet()) {
			
			//checks whether the extended plugin names match with the plugin id of the error
			if (errorReportInformation.getPluginId().equals(entry.getKey())) {
				packageKeyRelavant.put(entry.getKey(), entry.getValue());

			}
			//checks whether the extended plugin names included in the stack trace of the error
			else if (errorReportInformation.getExceptionS().contains(entry.getKey())) {
				packageKeyRelavant.put(entry.getKey(), entry.getValue());
			}

			//checks whether the extended plugin names included in the multi status information of the error
			else if (errorReportInformation.getMultiStatus().contains(entry.getKey())) {
				packageKeyRelavant.put(entry.getKey(), entry.getValue());
			}

		}

		errorReportInformation.setPackageKey(packageKeyRelavant);

	}

	/**
	 * This method checks whether multi status information is available for the
	 * given exception.
	 * 
	 * @param status
	 * @return true if the given error contains multi stats information.
	 */
	private static boolean isMultiStatus(IStatus status) {

		if (status.isMultiStatus()) {

			return true;

		} else {
			return false;
		}
	}

	/**
	 * This method extracts multi status information of the given exception.
	 * 
	 * @param status
	 * @param level
	 * @return a stringBuilder object, which contains multi status information
	 */
	private static StringBuilder extractMultiStatusInfo(IStatus status, int level) {
		final StringBuilder message = new StringBuilder();
		if (status == null) {
			message.append("");
			return message;
		}
		message.append("\n");
		for (int nestingLevel = 0; nestingLevel < level; ++nestingLevel) {
			message.append(' ');
		}

		//loop through each nesting level
		message.append(status.getMessage());
		if (status.isMultiStatus()) {
			for (final IStatus child : status.getChildren()) {
				message.append(extractMultiStatusInfo(child, level + 1));
			}
		}
		return message;
	}

	// getters and setters
	public IStatus getStatus() {
		return status;
	}

	public void setStatus(IStatus status) {
		InformationCollector.status = status;
	}

	public static ErrorReportInformation getErrorInformation() {
		return errorReportInformation;
	}

	public void setErrorInformation(ErrorReportInformation errorReportInformation) {
		InformationCollector.errorReportInformation = errorReportInformation;
	}

}