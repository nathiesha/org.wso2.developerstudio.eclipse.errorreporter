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
import org.json.simple.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotificationDialog;


//this class handles the complete process of sending the error report 
public class ErrorReporter {
	
	private IStatus status;
	private int userResponse;//to get user input from dialog
	private InfoCollector errorInfoCollector;
	private ErrorInformation errorInformation;
	private ReportGenerator reportGenerator;
	private String filePath;
	private JSONObject json;
	
	private static final String TARGET_URL="https://wso2.org/jira/rest/api/2/issue";

	
	public ErrorReporter(IStatus status) {
		this.status = status;

	}
	
	//this method reports the error to the Developer Studio user
	//takes necessary action based on the user response
	public void reportError(){
		
		//create an InfoCollector
		//get error information and assign it to errorInformation object
		errorInfoCollector = new InfoCollector(status);
		errorInformation=errorInfoCollector.getInformation();
		
		//create reportGenerator object
		//store the error report and user space
		reportGenerator=new ReportGenerator(errorInformation);
		try {
			filePath=reportGenerator.storeReport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		userResponse=openErrorDialog();

		
		switch(userResponse)
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
				
			case 1:
				break;
					
		}
		
	}
	
	//open up the error dialog box and get user input
		public int openErrorDialog() {
			Shell shell = new Shell();
			ErrorNotificationDialog errDialog=new ErrorNotificationDialog(shell, filePath, status);
			return errDialog.open();
		}
	
	public void sendReport(final String filePath) throws AddressException, MessagingException, IOException{
		
		if(Activator.getDefault().getPreferenceStore()
				.getBoolean("Jira"))
		{
			
			Job reporterJob = new Job("Reporting the Developer Studio Error") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
						sendJira();
						return Status.OK_STATUS;
				}
		     };
		     
		     reporterJob.setUser(true);
		     reporterJob.schedule();
			
		}
			
		
		
		else
			
		{	
			Job reporterJob = new Job("Reporting the Developer Studio Error") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {


							try {
								sendJira();
								sendEmail(filePath);
							} catch (IOException | MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					
							return Status.OK_STATUS;
				}
		     };
		     
		     reporterJob.setUser(true);
		     reporterJob.schedule();
			
		}
		
	}
	
	public void sendJira()
	{
				System.out.println("Sending Jira");
				json=reportGenerator.createIssue();
				System.out.println(json.toString());
				RemoteJiraConnector jiraCon= new RemoteJiraConnector();
				String userCredentials = "";
				String response=jiraCon.excutePost(TARGET_URL, json,userCredentials);
				System.out.println(response);
						
		
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
	
	



	public IStatus getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
