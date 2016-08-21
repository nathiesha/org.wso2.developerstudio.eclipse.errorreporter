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

package org.wso2.developerstudio.eclipse.errorreporter.controller;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.constants.DialogBoxLabels;
import org.wso2.developerstudio.eclipse.errorreporter.constants.PreferencePageStrings;
import org.wso2.developerstudio.eclipse.errorreporter.publishers.FilePublisher;
import org.wso2.developerstudio.eclipse.errorreporter.publishers.RemoteServerPublisher;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.templates.ErrorReportInformation;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialogs.EditPreferencesDialog;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialogs.ErrorNotificationDialog;
import org.wso2.developerstudio.eclipse.errorreporter.util.InformationCollector;
import org.wso2.developerstudio.eclipse.errorreporter.util.JSONReader;

//this class handles the complete process of publishing the error reports 
public class ErrorReporter {

	private IStatus status;
	private ErrorReportInformation errorReportInformation;
	private TextReportGenerator textReportGenerator;
	private RemoteServerPublisher remotePublisher;

	private int userResponse;
	private String errorMessage;
	private String response="";
	private String id;
	private String key;

	/**
	 * The constructor.
	 */
	public ErrorReporter(IStatus status) {
		this.status = status;

	}

	/**
	 * This is the main method of this class, which handles the whole error
	 * reporting process This method reports the error to the Developer Studio
	 * user and takes necessary action based on the user response.
	 */
	public void reportError() {

		init();

		switch (userResponse) {

		//If user press OK and selected the radio button-publish in Jira
		case 100:
			try {
				remotePublisher = new RemoteServerPublisher(errorReportInformation);
				sendReportJira();
			}

			catch (Exception e) {
				// in case of an exception, print stacktrace and open up the
				// message box
				e.printStackTrace();
				Shell shell = new Shell();
				MessageBox msg = new MessageBox(shell);
				msg.setMessage(e.getMessage());
				msg.open();

			}
			break;
		
		//If user press OK and selected the radio button-publish in Jira and Email
		case 200:
			try {
				remotePublisher = new RemoteServerPublisher(errorReportInformation);
				sendReportJiraEmail();
			}

			catch (Exception e) {
				// in case of an exception, print stacktrace and open up the
				// message box
				e.printStackTrace();
				Shell shell = new Shell();
				MessageBox msg = new MessageBox(shell);
				msg.setMessage(e.getMessage());
				msg.open();

			}
			break;

		case 1:
			break;

		}

	}

	/**
	 * This method is responsible for initializing the required variables for
	 * the error reporting process
	 */

	public void init() {

		// get error information and assign it to errorReportInformation object
		errorReportInformation = InformationCollector.getInformation(status);

		// create textReportGenerator object
		// store the error report and user space
		textReportGenerator = new TextReportGenerator();
		textReportGenerator.createReport(errorReportInformation);
		errorMessage = textReportGenerator.getTextString();

		userResponse = openErrorDialog();
	}

	/**
	 * This method is responsible for displaying the error reporter dialog to
	 * the user. In case the mandatory fields in the preferences page are not
	 * filled, this method opens up the EditPreferences dialog
	 * 
	 * @return the return value of the dialog boxes(user responses)
	 */

	public int openErrorDialog() {

		// create new shell for the error dialog
		Shell shell = new Shell();

		// if user has filled the preference values
		if (checkUserInput()) {

			// open up error notification dialog
			ErrorNotificationDialog errorDialog = new ErrorNotificationDialog(shell, errorMessage, status);
			return errorDialog.open();

		}

		else {
			// else open up the User Input Dialog that stores user given values
			EditPreferencesDialog inputDialog = new EditPreferencesDialog(shell);
			int num = inputDialog.open();

			// if user pressed oK and the preferences valus are filled
			if (num == 0 & checkUserInput()) {
				// open up the error dialog box
				ErrorNotificationDialog errorDialog = new ErrorNotificationDialog(shell, errorMessage, status);
				return errorDialog.open();
			}

			// if user presssed cancel
			else {

				return SWT.CANCEL;
			}

		}

	}

	/**
	 * This method contains logic create a single thread to perform the
	 * publishing process of error in Jira , and saving the error report in the
	 * system.
	 * 
	 */
	public void sendReportJira() {

		
		//create new job
		Job reporterJob = new Job(DialogBoxLabels.REPORTER) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {

					//get the response
					response = publishJira();
					
					//extract the project key and id from json response
					JSONReader reader = new JSONReader();
					
					//response is passed to json reader only if its a json string
					if(response.startsWith("{"))
					{
					id = reader.getJsonId(response);
					key = reader.getJsonKey(response);
					
					//use that id as file name and store the file
					FilePublisher filePublisher = new FilePublisher(key, id);
					filePublisher.publish(textReportGenerator);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		//create a user thread and schedule it
		reporterJob.setUser(true);
		reporterJob.schedule();

	}

	/**
	 * This method contains logic create a thread to perform the
	 * publishing process of error in Jira and sending an email and saving the error report in the
	 * system.
	 * 
	 */
	private void sendReportJiraEmail() {

		//create new jobn it
		Job reporterJob = new Job(DialogBoxLabels.REPORTER) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				//publish the error in Jira
//				response = publishJira();
//				
//				//extract the project key and id from json response
//				JSONReader reader = new JSONReader();
//				//response is passed to json reader only if its a json string
//				if(response.startsWith("{"))
//				{
//					id = reader.getJsonId(response);
//					key = reader.getJsonKey(response);
//				
//				//use that id as file name and store the file
//				FilePublisher filePublisher = new FilePublisher(key, id);
//				filePublisher.publish(textReportGenerator);
//				}
				
				//send the email
				sendEmail(); 

				return Status.OK_STATUS;
			}
		};

		//create a user thread and schedule it
		reporterJob.setUser(true);
		reporterJob.schedule();

	}

	/**
	 * This method contains logic to handle the error publishing procedure through the remote server.
	 * If multiple project keys are availale, this method posts the issue in all those projects in wso2 jira
	 * @return the response from the web server
	 * 
	 */
	public String publishJira()  {
		String response = "";
		String key;
		
		//number of plugins related to the error
		if(errorReportInformation.getPackageKey()!=null)
		{
		int keyNo = errorReportInformation.getPackageKey().size();

		//if none of the plugins extended are related to the eror, uses the default project key
		if (keyNo == 0) {
			key = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.PROJECT_KEY);
			response = remotePublisher.publishJira(key);

		}

		}
		//else make issue posts for each  project in Jira
		else {
			for (Map.Entry<String, String> entry : errorReportInformation.getPackageKey().entrySet()) {
				key = entry.getValue();
				response = remotePublisher.publishJira(key);

			}

		}
		return response;

	}

	/**
	 * This method contains logic to send the email using remote web server
	 * @return the response from the remote server
	 * 
	 */
	public String sendEmail(){

		//get the recipient email address from the preferences page
		String recipientEmail = Activator.getDefault().getPreferenceStore().getString("REC EMAIL");

		//create the text report for the message body
		textReportGenerator.createReport(errorReportInformation);
		String message = textReportGenerator.getTextString();

		if (recipientEmail == "") {
			Shell shell = new Shell();
			MessageBox msg = new MessageBox(shell);
			msg.setMessage("Please enter your gmail username, password and recipient email adress");
			return "tryAgain";
		}
		//call remote server email publisher method
		else {
			try {
				return remotePublisher.publishEmail(recipientEmail, message);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}

	}

	/**
	 * This method checks whether user has not entered any value in the
	 * preference page for Jira username and password.
	 * 
	 * @return true if user has entered compulsory fields, false else
	 */
	private boolean checkUserInput() {

		//get the remote sercer urls from the preferences page
		String jiraUrl = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.SERVER_URL);
		String emailUrl = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.EMAIL_SERVER_URL);
		String statusUrl = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.STATUS_URL);
		String projectKey = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.PROJECT_KEY);

		// checks whether remote server urls are filled
		if (jiraUrl == "" || emailUrl == "" || statusUrl == "" || projectKey == "") {

			return false;
		}

		else
			return true;
	}

	/**
	 * This method contains logic to get a map of all the related plugin package
	 * names and their Jira project keys
	 * 
	 * @return key values
	 * 
	 */
	public String getKeys() {

		StringBuffer stringBuffer = new StringBuffer();
		// get all the package project key values
		for (Map.Entry<String, String> map : errorReportInformation.getPackageKey().entrySet()) {
			stringBuffer.append("/n");
			stringBuffer.append(map.getValue());

		}

		return stringBuffer.toString();
	}

	/**
	 * Getters and setters
	 */
	public IStatus getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
