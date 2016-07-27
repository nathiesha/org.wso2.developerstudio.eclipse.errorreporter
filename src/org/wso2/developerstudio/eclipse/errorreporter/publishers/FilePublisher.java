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


package org.wso2.developerstudio.eclipse.errorreporter.publishers;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;


/**
 * @author Nathie
 *
 */
public class FilePublisher implements ErrorPublisher{
	
	// Error Report Contents
	private static final String DATE = "\nDate: ";
	private static final String KEY = "\nIssue Key: ";
	private static final String ID = "\nIssue ID: ";


	
    public String publish(TextReportGenerator reportGen) throws Exception{
    	String Id="1234";
    	String key="5678";
    	String fileName = Id + ".txt";
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		IPath stateLoc = Platform.getStateLocation(bundle);

		File tempFolder = new File(stateLoc.toString());
		File reportFolder = new File(tempFolder, "ErrorReports");

		if (!reportFolder.exists()) {

			reportFolder.mkdir();
		}

		File reportTemp = new File(reportFolder, fileName);
		FileWriter fw = new FileWriter(reportTemp);
		writeReport(reportGen,fw,Id,key);
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
		writeReport(reportGen,fw2,Id,key);
		fw2.close();

		String filePath = reportPersistent.getPath();
		return filePath;
		//return "ok";
    }
    
	// store the errorReport and return its location


	private void writeReport(TextReportGenerator reportGen, FileWriter fw,String id, String key) throws IOException {

		// = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String timeStamp=(dateFormat.format(date)); //2014/08/06 15:59:48
		
		
		fw.write(KEY+ key);
		fw.write(ID +id);
		fw.write(DATE+ timeStamp);
		fw.write(reportGen.getTextString());
//		fw.write(EXCEPTION + errorInformation.getExceptionS());
//		fw.write(ISSUE_STATUS);
//		fw.write(STATUS);
//		fw.write(PLUGIN_ID + errorInformation.getPluginId());
//		fw.write(PLUGIN_VERSION + errorInformation.getPluginVersion());
//		fw.write(CODE + errorInformation.getCode());
//		fw.write(SEVERITY + errorInformation.getSeverity());
//		fw.write(MESSAGE + errorInformation.getMessage());
//		fw.write(EXCEPTION + errorInformation.getExceptionS());
//		fw.write(MULTI_STATUS_INFORMATION + errorInformation.getMultiStatus());
//		fw.write(REPORT);
//		fw.write(ECLIPSE_BUILD_ID + errorInformation.getEclipseBuildId());
//		fw.write(ECLIPSE_PRODUCT + errorInformation.getEclipseProduct());
//		fw.write(JAVA_RUNTIME_VERSION + errorInformation.getJavaRuntimeVersion());
//		fw.write(OSGIWS + errorInformation.getOsgiWs());
//		fw.write(OSGI_OS + errorInformation.getOsgiOs());
//		fw.write(OSGI_OS_VERSION + errorInformation.getOsgiOsVersion());
//		fw.write(OSGI_ARCH + errorInformation.getOsgiArch());
//		fw.write(REPORT_SENDER);
//		fw.write(NAME + errorInformation.getName());
//		fw.write(EMAIL + errorInformation.getEmail());
//		fw.write(COMMENT + errorInformation.getComment());
//		fw.write(SEVERITY_USER + errorInformation.getSeverity2());

	}



}