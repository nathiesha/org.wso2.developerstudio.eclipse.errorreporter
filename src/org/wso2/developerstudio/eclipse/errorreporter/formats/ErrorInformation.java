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


package org.wso2.developerstudio.eclipse.errorreporter.formats;

public class ErrorInformation {

	// error information
		private String pluginId;
		private String pluginVersion;//
		private int code;
		private int severity;
		private String message;
		private Exception e;
		private String exception;
		private String anoId;

		// user information
		private String name;
		private String email;
		private String comment;
		private String severityUser;

		// system information
		private String eclipseBuildId;
		private String eclipseProduct;
		private String JavaRuntimeVersion;
		private String osgiWs;
		private String osgiOs;
		private String osgiOsVersion;
		private String osgiArch;
		
		//multi status information
		private String multiStatus;
		


		private Bundles bundleArray[] = new Bundles[10];
		
		public ErrorInformation() {
			// TODO Auto-generated constructor stub
		}
		
		//getters and setters
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
			return severityUser;
		}

		public void setSeverity2(String severity2) {
			this.severityUser = severity2;
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

		public String getExceptionS() {
			return exception;
		}

		public void setExceptionS(String exception) {
			this.exception = exception;
		}

		public String getMultiStatus() {
			return multiStatus;
		}

		public void setMultiStatus(String multiStatus) {
			this.multiStatus = multiStatus;
		}

	
	

}
