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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;

public class InfoCollector {

	private IStatus status;
	private ErrorInformation errorInformation;

	public InfoCollector(IStatus status) {

		this.status = status;
		errorInformation = new ErrorInformation();
	}

	// get all the information regarding the error
	public ErrorInformation getInformation() {

		getErrorInfo();
		getSystemInfo();
		getUserInfo();
		getMultiStatusInfo();

		return errorInformation;

	}

	// collect the information regarding the exception
	private void getErrorInfo() {

		errorInformation.setPluginId(status.getPlugin());
		//TODO exception might occur here
		//status.getPlugin()
		Bundle bundle = Platform.getBundle("com.google.gson");
		Version version = bundle.getVersion();
		errorInformation.setPluginVersion(version.toString());
		
		errorInformation.setSeverity(status.getSeverity());
		errorInformation.setMessage(status.getMessage());
		errorInformation.setCode(status.getCode());

		errorInformation.setException((Exception) status.getException());

		// to convert the exception to string format
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		errorInformation.getException().printStackTrace(pw);
		errorInformation.setExceptionS(sw.toString());

	}

	// collect information regarding the environment
	private void getSystemInfo() {

		errorInformation.setEclipseBuildId(System.getProperty("eclipse.buildId"));
		errorInformation.setEclipseProduct(System.getProperty("eclipse.product"));
		errorInformation.setJavaRuntimeVersion(System.getProperty("java.runtime.version"));
		errorInformation.setOsgiWs(System.getProperty("osgi.ws"));
		errorInformation.setOsgiOs(System.getProperty("os.name"));
		errorInformation.setOsgiOsVersion(System.getProperty("os.version"));
		errorInformation.setOsgiArch(System.getProperty("osgi.arch"));

		// =plugin.Activator.getDefault().getBundle().getVersion().toString();
	}

	// collect the user set values
	private void getUserInfo() {

		errorInformation.setName(Activator.getDefault().getPreferenceStore().getString("NAME"));
		errorInformation.setEmail(Activator.getDefault().getPreferenceStore().getString("EMAIL"));

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

		errorInformation.setMultiStatus(message.toString());
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

	// getters and setters
	public IStatus getStatus() {
		return status;
	}

	public void setStatus(IStatus status) {
		this.status = status;
	}

	public ErrorInformation getErrorInformation() {
		return errorInformation;
	}

	public void setErrorInformation(ErrorInformation errorInformation) {
		this.errorInformation = errorInformation;
	}

}