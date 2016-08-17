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
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;

public class InfoCollector {

	private IStatus status;
	private ErrorReportInformation errorReportInformation;

	public InfoCollector(IStatus status) {

		this.status = status;
		errorReportInformation = new ErrorReportInformation();
	}

	// get all the information regarding the error
	public ErrorReportInformation getInformation() {

		getErrorInfo();
		getSystemInfo();
		getUserInfo();
		getMultiStatusInfo();
		getPackageKeyInfo();

		return errorReportInformation;

	}

	// collect the information regarding the exception
	public void getErrorInfo() {

		errorReportInformation.setPluginId(status.getPlugin());
		// TODO exception might occur here
		// status.getPlugin()
		Bundle bundle = Platform.getBundle("com.google.gson");
		Version version = bundle.getVersion();
		errorReportInformation.setPluginVersion(version.toString());

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

	// collect information regarding the environment
	public void getSystemInfo() {

		errorReportInformation.setEclipseBuildId(System.getProperty("eclipse.buildId"));
		errorReportInformation.setEclipseProduct(System.getProperty("eclipse.product"));
		errorReportInformation.setJavaRuntimeVersion(System.getProperty("java.runtime.version"));
		errorReportInformation.setOsgiWs(System.getProperty("osgi.ws"));
		errorReportInformation.setOsgiOs(System.getProperty("os.name"));
		errorReportInformation.setOsgiOsVersion(System.getProperty("os.version"));
		errorReportInformation.setOsgiArch(System.getProperty("osgi.arch"));

		// =plugin.Activator.getDefault().getBundle().getVersion().toString();
	}

	// collect the user set values
	public void getUserInfo() {

		errorReportInformation.setName(Activator.getDefault().getPreferenceStore().getString("NAME"));
		errorReportInformation.setEmail(Activator.getDefault().getPreferenceStore().getString("EMAIL"));
		errorReportInformation.setOrganization((Activator.getDefault().getPreferenceStore().getString("ORGANIZATION")));

	}

	// collect information regarding the multistatus
	private void getMultiStatusInfo() {

		StringBuilder message = new StringBuilder();

		if (isMultiStatus(status)) {
			message = extractMultiStatusInfo(status, 0);

		}

		else {
			message.append("The exception does not contain any multi status information");
		}

		errorReportInformation.setMultiStatus(message.toString());
	}

	// check if multi status information if available
	private boolean isMultiStatus(IStatus status) {

		if (status.isMultiStatus()) {

			return true;

		} else {
			return false;
		}
	}

	// Extract multi status info
	private StringBuilder extractMultiStatusInfo(IStatus status, int level) {
		final StringBuilder message = new StringBuilder();
		if (status == null) {
			return message;
		}
		message.append("\n");
		for (int nestingLevel = 0; nestingLevel < level; ++nestingLevel) {
			message.append(' ');
		}

		message.append(status.getMessage());
		if (status.isMultiStatus()) {
			for (final IStatus child : status.getChildren()) {
				message.append(extractMultiStatusInfo(child, level + 1));
			}
		}
		return message;
	}

	private void getPackageKeyInfo() {

		Map<String, String> pair = ExtensionPointReader.getKeys();
		Map<String, String> packageKey = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : pair.entrySet()) {
			if (errorReportInformation.getPluginId().equals(entry.getKey())) {
				packageKey.put(entry.getKey(), entry.getValue());

			}

			else if (errorReportInformation.getExceptionS().contains(entry.getKey())) {
				packageKey.put(entry.getKey(), entry.getValue());
			}

			else if (errorReportInformation.getMultiStatus().contains(entry.getKey())) {
				packageKey.put(entry.getKey(), entry.getValue());
			}

		}

		errorReportInformation.setPackageKey(packageKey);

	}

	// getters and setters
	public IStatus getStatus() {
		return status;
	}

	public void setStatus(IStatus status) {
		this.status = status;
	}

	public ErrorReportInformation getErrorInformation() {
		return errorReportInformation;
	}

	public void setErrorInformation(ErrorReportInformation errorReportInformation) {
		this.errorReportInformation = errorReportInformation;
	}

}