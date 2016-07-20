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


package UnitTests;

import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;
import org.wso2.developerstudio.eclipse.errorreporter.util.InfoCollector;

/**
 * @author Nathie
 *
 */
public class InfoCollectorTest {
	
	IStatus status;
	ErrorInformation errorInformation;
	
	InfoCollector ic;
	
	@Before
	public void createStatus()
	{
		
		try {
		      String s = null;
		      System.out.println(s.length());
		    } 
		
		catch (NullPointerException e) 
			{
		      // build the error message and include the current stack trace
		      status = createMultiStatus(e.getLocalizedMessage(), e);
		      
		    }
		ic=new InfoCollector(status);
	}
	
	//create multi status

	private static MultiStatus createMultiStatus(String msg, Throwable t) {

	    List<Status> childStatuses = new ArrayList<>();
	    StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

	     for (StackTraceElement stackTrace: stackTraces) {
	      Status status = new Status(IStatus.ERROR,
	          "org.wso2.developerstudio.eclipse", stackTrace.toString());
	      childStatuses.add(status);
	    }

	    MultiStatus ms = new MultiStatus("org.wso2.developerstudio.eclipse",
	        IStatus.ERROR, childStatuses.toArray(new Status[] {}),
	        t.toString(), t);
	    return ms;
	  }
	
	   @Test
	   public void testSystemInfo() {
		   
		   	 ic.getSystemInfo();
		   	 errorInformation=ic.getErrorInformation();
	         assertEquals("Eclipse Build ID does not match ","4.5.2.M20160212-1500",errorInformation.getEclipseBuildId());
	         assertEquals("Eclipse Product does not match ","org.eclipse.platform.ide",errorInformation.getEclipseProduct());
	         assertEquals("Eclipse Java Runtime Version does not match ","1.7.0_71-b14",errorInformation.getJavaRuntimeVersion());
	         assertEquals("Eclipse OsgiWS does not match ","win32",errorInformation.getOsgiWs());
	         assertEquals("Eclipse OsgiOS does not match ","Windows 8.1",errorInformation.getOsgiOs());
	         assertEquals("Eclipse OsgiOs Version does not match ","6.3",errorInformation.getOsgiOsVersion());
	         assertEquals("Eclipse Osgi Arch does not match ","x86_64",errorInformation.getOsgiArch());
	         

	   }
	   
	   @Test
	   public void testErrorInfo() {
		   
		   	 ic.getErrorInfo();
		   	 errorInformation=ic.getErrorInformation();
//	         assertEquals("Eclipse Plugin ID does not match ","4.5.2.M20160212-1500",errorInformation.getPluginId());
//	         assertEquals("Eclipse severity does not match ","org.eclipse.platform.ide",errorInformation.getSeverity());
//	         assertEquals("Eclipse Code does not match ","1.7.0_71-b14",errorInformation.getCode());
//	         assertEquals("Eclipse Message does not match ","win32",errorInformation.getMessage());
//	         assertEquals("Eclipse Exception does not match ","Windows 8.1",errorInformation.getExceptionS());
//	         assertEquals("Eclipse Plugin Version does not match ","6.3",errorInformation.getPluginVersion());
	  

	   }
}
