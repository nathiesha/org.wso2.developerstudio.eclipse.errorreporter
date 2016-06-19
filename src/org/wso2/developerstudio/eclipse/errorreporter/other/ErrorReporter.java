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


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;


//this class handles the complete process of sending the error report 
public class ErrorReporter {
	
	private IStatus status;
	private String plugin;
	private int input;//to get user input from dialog

	public ErrorReporter(IStatus status, String plugin) {
		this.status = status;
		this.plugin = plugin;

	}
	
	//this method reports the error to the Developer Studio user
	public void reportError() {
		
		InfoCollector errorInfoCollector = new InfoCollector(status, plugin);
		ErrorInformation errorInfo=errorInfoCollector.getInformation();
		ReportGenerator reportGen=new ReportGenerator(errorInfo);
		reportGen.storeReport(errorInfo);

		input= openErrorDialog();
		
		switch(input)
		{
			case 0:
				sendReport();
				break;
				
			case 1:
				break;
				
				
			case 2:
				break;				
		}
		
	}
	
	public void sendReport(){
		
		 //   String choice = Activator.getDefault().getPreferenceStore().getString("CHOICE");
		 //   System.out.println(choice);
		if(Activator.getDefault().getPreferenceStore()
				.getBoolean("Jira"))
		{
			
		    Job reporterJob = new Job("Report the Developer Studio Error") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					// TODO send report to jira only
					return Status.OK_STATUS;
				}
		     };
		     
		     reporterJob.setUser(true);
		     reporterJob.schedule();
			
		}
		
		else
			
		{
			//TODO email and jira
		}
		
	}


	//open up the error dialog box and get user input
	public int openErrorDialog() {
		//Shell shell = new Shell();
		//ErrorDialogS dialog = new ErrorDialogS(shell);
		return 11;
				//dialog.openErrorDialog();

	}


	//getters and setters
	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public IStatus getStatus() {
		return status;
	}

	public void setStatus(IStatus status) {
		this.status = status;
	}

}
