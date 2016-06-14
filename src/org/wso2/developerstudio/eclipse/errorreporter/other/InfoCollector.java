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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;

public class InfoCollector {

	IStatus status;
	String plugin;
	private ErrorInformation errorInformation;

	public InfoCollector(IStatus status, String plugin) {

		this.plugin = plugin;
		this.status = status;
		// errorInformation=new ErrorInformation();
	}

	public ErrorInformation getInformation() {

		getErrorInfo();
		getSystemInfo();
		getUserInfo();

		return errorInformation;

	}

	// collect the information regarding the exception
	public void getErrorInfo() {

		errorInformation.setPluginId(plugin);
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
	public void getSystemInfo() {

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
	public void getUserInfo() {

		errorInformation.setName(Activator.getDefault().getPreferenceStore().getString("NAME"));
		errorInformation.setEmail(Activator.getDefault().getPreferenceStore().getString("EMAIL"));

	}

	// collect multi status information if available
	public IStatus[] getMultiStatus(IStatus status) {

		if (status.isMultiStatus()) {
			IStatus multiStatusArray[] = status.getChildren();
			return multiStatusArray;

		} else {
			return null;
		}
	}

}