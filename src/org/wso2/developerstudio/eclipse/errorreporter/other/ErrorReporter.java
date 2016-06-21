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


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotificationDialog;


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
		String filePath=reportGen.storeReport(errorInfo);
		input=openErrorDialog(errorInfo);

		
		switch(input)
		{
			case 0:
			try {
				sendReport(filePath);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
				
			case 1:
				break;
					
		}
		
	}
	
	public void sendReport(String filePath) throws AddressException, MessagingException{
		
		 //   String choice = Activator.getDefault().getPreferenceStore().getString("CHOICE");
		 //   System.out.println(choice);
		if(Activator.getDefault().getPreferenceStore()
				.getBoolean("Jira"))
		{
			
			sendJira();
			
		}
		
		else
			
		{
			sendJira();
			try {
				sendEmail(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void sendJira()
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
	
	public void sendEmail(String filePath) throws IOException, AddressException, MessagingException
	{
		EmailSender emailS=new EmailSender();
		String username=Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String password=Activator.getDefault().getPreferenceStore().getString("PASSWORD");
		String recipientEmail="";
		String ccEmail=""; 
		String title="Developer Studio Error Report";
		String message;

			message = readFile(filePath);
			emailS.Send(username,password,recipientEmail,ccEmail,title,message);

		
	}


	String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        
	        return sb.toString();
	        
	    } finally {
	        br.close();
	    }
	}
	
	//open up the error dialog box and get user input
	public int openErrorDialog(ErrorInformation errorInfo) {
		Shell shell = new Shell();
		String dialogTitle="A problem was detected";
		String message= "An unexpected error occured. Please press send to report the error to the development team";
		return ErrorNotificationDialog.openError(shell, dialogTitle, message, errorInfo, status);
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

	public void setStatus(Status status) {
		this.status = status;
	}

}
