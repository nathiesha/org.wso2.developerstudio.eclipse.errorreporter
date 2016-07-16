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

package org.wso2.developerstudio.eclipse.errorreporter.startup;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.wso2.developerstudio.eclipse.errorreporter.controller.ErrorReporter;


public class LogListener implements ILogListener {

	private IStatus loggedStatus;
	ErrorReporter errorReporter;

	@Override
	public void logging(IStatus status, String plugin) {
		this.loggedStatus = status;

	
		if (checkError(status, plugin)) {

			System.out.println("error found");
			// create error reporter object and report the error to user and jira
			errorReporter = new ErrorReporter(status);
			errorReporter.reportError();

		}

	}
	
	// method to check whether the error belongs to dev studio
	private Boolean checkError(IStatus status, String plugin)
	{
		
		//To convert the exception to a string
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		status.getException().printStackTrace(pw);
		String exception=(sw.toString());

		//check whether the plugin belongs to wso2.developerstudio
		if(status.getSeverity() == IStatus.ERROR && plugin.contains("org.wso2.developerstudio"))
		{
			return true;
			
		}
		
		//check whether the exception message contains wso2.developerstudio string
		else if(status.getSeverity() == IStatus.ERROR && exception.contains("org.wso2.developerstudio"))
		{
			
			return true;
		}

		else
			return false;		
		
	}
	

	public IStatus getLoggedStatus() {
		return loggedStatus;
	}

	public void setLoggedStatus(IStatus loggedStatus) {
		this.loggedStatus = loggedStatus;
	}

}
