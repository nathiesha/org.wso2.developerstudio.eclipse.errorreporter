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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.json.simple.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
/**
 * @author Nathie
 *
 */
public class ReportGenerator {
	
	ErrorInformation errorInformation;
	
	//Error Report Contents
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
	


	
	public ReportGenerator(ErrorInformation errorInformation) {		
		super();
		this.errorInformation=errorInformation;
	}


	@SuppressWarnings("unchecked")
	public JSONObject createIssue(Integer projectKey, Integer issuetypeName, String summary, String description) {
        
		JSONObject fields = new JSONObject();
		
        fields.put("project", new JSONObject().put("key", projectKey.toString()));
        fields.put("issuetype", new JSONObject().put("name", issuetypeName.toString()));
        fields.put("summary", summary);
        fields.put("description", description);
 
        JSONObject issue = new JSONObject();
        issue.put("fields", fields);

 
        return issue;
    }
	
	public void storeReport(ErrorInformation errorInformation) throws IOException
	{
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String fileName = timeStamp+".txt";
		
		//temporary storage
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		IPath stateLoc = Platform.getStateLocation(bundle);
		
        File tempFolder = new File(stateLoc.toString());
        File reportFolder = new File(tempFolder, "ErrorReports");
        
        if (!reportFolder.exists()) {
        	
        		reportFolder.mkdir();
        }
        
		File reportTemp = new File(reportFolder, fileName);
		FileWriter fw = new FileWriter(reportTemp);
		writeReport(errorInformation, fw);
		fw.close();
		
		//persistent storage in User Directory
		String userDirLocation=System.getProperty("user.dir");
		
		File persistentFolder = new File(userDirLocation);
        File errorReportsFolder = new File(persistentFolder, "ErrorReports");
        
        if (!errorReportsFolder.exists()) {
        	
        	errorReportsFolder.mkdir();
        }
    
		File reportPersistent = new File(errorReportsFolder, fileName);
	    FileWriter fw2 = new FileWriter(reportPersistent);
	    writeReport(errorInformation, fw2);
	    fw.close();

	}
	
	private void writeReport(ErrorInformation errorInformation,FileWriter fw)
	{
		
	    try {
			fw.write(INTRODUCTION);
		    fw.write(STATUS);
		    fw.write(PLUGIN_ID+errorInformation.getPluginId());
		    fw.write(PLUGIN_VERSION+errorInformation.getPluginVersion());
		    fw.write(CODE+errorInformation.getCode());
		    fw.write(SEVERITY+errorInformation.getSeverity());
		    fw.write(MESSAGE+errorInformation.getMessage());
		    fw.write(EXCEPTION+errorInformation.getExceptionS());
		    fw.write(MULTI_STATUS_INFORMATION+errorInformation.getMultiStatus());
		    fw.write(REPORT);
		    fw.write(ECLIPSE_BUILD_ID+errorInformation.getEclipseBuildId());
		    fw.write(ECLIPSE_PRODUCT+errorInformation.getEclipseProduct());
		    fw.write(JAVA_RUNTIME_VERSION+errorInformation.getJavaRuntimeVersion());
		    fw.write(OSGIWS+errorInformation.getOsgiWs());
		    fw.write(OSGI_OS+errorInformation.getOsgiOs());
		    fw.write(OSGI_OS_VERSION+errorInformation.getOsgiOsVersion());
		    fw.write(OSGI_ARCH+errorInformation.getOsgiArch());
		    fw.write(REPORT_SENDER);
		    fw.write(NAME+errorInformation.getName());
		    fw.write(EMAIL+errorInformation.getEmail());
		    fw.write(COMMENT+errorInformation.getComment());
		    fw.write(SEVERITY_USER+errorInformation.getSeverity2());
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}