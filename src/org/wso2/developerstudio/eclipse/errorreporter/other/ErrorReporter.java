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
import org.eclipse.swt.widgets.Shell;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotifyDialog;

public class ErrorReporter {
	
	private IStatus status;
	private String plugin;
	private int input;

	public ErrorReporter(IStatus status, String plugin) {
		this.status = status;
		this.plugin = plugin;

	}
	
	public void reportError() {
		InfoCollector errInfoCollector = new InfoCollector(status, plugin);
		collectErrorInfo(errInfoCollector);
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


	public int openErrorDialog() {
		Shell shell = new Shell();
		ErrorNotifyDialog dialog = new ErrorNotifyDialog(shell);
		return dialog.open();

	}

	public void collectErrorInfo( InfoCollector errInfoCollector) {
		errInfoCollector.getErrorInfo();
		errInfoCollector.getSystemInfo();
		errInfoCollector.getUserInfo();
		errInfoCollector.getMultiStatus(status);	

	}

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
