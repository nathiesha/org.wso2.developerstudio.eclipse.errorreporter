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

public class ErrorInfoCollector {

	private String pluginId;
	private String pluginVersion;//
	private int code;
	private int severity;
	private String message;
	private String fingerprint;//
	private Exception e;
	private String exception;
	private String anoId;

	//user information
	private String name;
	private String email;
	private String comment;
	private String severity2;

	//system information
	private String eclipseBuildId;
	private String eclipseProduct;
	private String JavaRuntimeVersion;
	private String osgiWs;
	private String osgiOs;
	private String osgiOsVersion;
	private String osgiArch;

	private Bundles bundleArray[] = new Bundles[10];

	public ErrorInfoCollector(IStatus status, String plugin) {
		pluginId = plugin;
		severity = status.getSeverity();
		message = status.getMessage();
		code=status.getCode();
		
		e=(Exception) status.getException();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		exception=sw.toString();

		collectSystemInfo();
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getAnoId() {
		return anoId;
	}

	public void setAnoId(String anoId) {
		this.anoId = anoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEclipseBuildId() {
		return eclipseBuildId;
	}

	public void setEclipseBuildId(String eclipseBuildId) {
		this.eclipseBuildId = eclipseBuildId;
	}

	public String getEclipseProduct() {
		return eclipseProduct;
	}

	public void setEclipseProduct(String eclipseProduct) {
		this.eclipseProduct = eclipseProduct;
	}

	public String getJavaRuntimeVersion() {
		return JavaRuntimeVersion;
	}

	public void setJavaRuntimeVersion(String javaRuntimeVersion) {
		JavaRuntimeVersion = javaRuntimeVersion;
	}

	public String getOsgiWs() {
		return osgiWs;
	}

	public void setOsgiWs(String osgiWs) {
		this.osgiWs = osgiWs;
	}

	public String getOsgiOs() {
		return osgiOs;
	}

	public void setOsgiOs(String osgiOs) {
		this.osgiOs = osgiOs;
	}

	public String getOsgiOsVersion() {
		return osgiOsVersion;
	}

	public void setOsgiOsVersion(String osgiOsVersion) {
		this.osgiOsVersion = osgiOsVersion;
	}

	public String getOsgiArch() {
		return osgiArch;
	}

	public void setOsgiArch(String osgiArch) {
		this.osgiArch = osgiArch;
	}

	public String getSeverity2() {
		return severity2;
	}

	public void setSeverity2(String severity2) {
		this.severity2 = severity2;
	}

	public Bundles[] getBundleArray() {
		return bundleArray;
	}

	public void setBundleArray(Bundles[] bundleArray) {
		this.bundleArray = bundleArray;
	}

	public Exception getException() {
		return e;
	}

	public void setException(Exception exception) {
		this.e = exception;
	}

	public void collectSystemInfo() {

		eclipseBuildId = System.getProperty("eclipse.buildId");
		eclipseProduct = System.getProperty("eclipse.product");
		JavaRuntimeVersion = System.getProperty("java.runtime.version");
		osgiWs = System.getProperty("osgi.ws");
		osgiOs = System.getProperty("os.name");
		osgiOsVersion = System.getProperty("os.version");
		osgiArch = System.getProperty("osgi.arch");
		
		System.out.println(eclipseBuildId);
		System.out.println(eclipseProduct);
		System.out.println(JavaRuntimeVersion);
		System.out.println(osgiWs);
		System.out.println(osgiOs);
		System.out.println(osgiOsVersion);
		System.out.println(osgiArch);
		System.out.println(code);
		System.out.println(message);
		System.out.println(exception);
		System.out.println(pluginId);
		System.out.println(severity);
		


		// =plugin.Activator.getDefault().getBundle().getVersion().toString();
	}

}
