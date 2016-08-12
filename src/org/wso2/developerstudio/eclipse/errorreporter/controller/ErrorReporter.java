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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONObject;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.formats.ErrorInformation;
import org.wso2.developerstudio.eclipse.errorreporter.publishers.ErrorPublisher;
import org.wso2.developerstudio.eclipse.errorreporter.publishers.FilePublisher;
import org.wso2.developerstudio.eclipse.errorreporter.publishers.RemoteServerPublisher;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialogs.ErrorNotificationDialog;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialogs.UserInputDialog;
import org.wso2.developerstudio.eclipse.errorreporter.util.InfoCollector;

//this class handles the complete process of publishing the error reports 
public class ErrorReporter {

	private IStatus status;
	private int userResponse;// to get user input from dialog
	private InfoCollector errorInfoCollector;
	private ErrorInformation errorInformation;
	private TextReportGenerator textReportGenerator;
	private RemoteServerPublisher jp;
	private String errorMessage;
	private String response;
	private JSONObject json;
	private String id;
	private String key;

	// private static final String
	private static final String TITLE = "Developer Studio Error Report";
	private static final String REPORTER = "Reporting the Developer Studio Error";

	Map<String, ErrorPublisher> registeredPublishers;

	public void addPublisher(String id, ErrorPublisher publisher) {
		registeredPublishers.put(id, publisher);
	}

	public ErrorReporter(IStatus status) {
		this.status = status;

	}

	public void init() {

		// create an InfoCollector
		// get error information and assign it to errorInformation object
		errorInfoCollector = new InfoCollector(status);
		errorInformation = errorInfoCollector.getInformation();

		// create textReportGenerator object
		// store the error report and user space
		textReportGenerator = new TextReportGenerator();
		textReportGenerator.createReport(errorInformation);
		errorMessage = textReportGenerator.getTextString();

		userResponse = openErrorDialog();

	}

	// this method reports the error to the Developer Studio user
	// takes necessary action based on the user response
	public void reportError() {

		// iterate over all publishers
		// and call publish() method

		init();
		System.out.println(userResponse);
		switch (userResponse) {

		case 100:
			try {
				jp = new RemoteServerPublisher(errorInformation);
				sendReportJ();
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

		case 200:
			try {
				jp = new RemoteServerPublisher(errorInformation);
				sendReportE();
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

	

	// open up the error dialog box and get user input
	public int openErrorDialog() {

		// create new shell for the error dialog
		Shell shell = new Shell();

		// if user has filled the preference values
		if (checkUserInput()) {

			// open up error dialog
			ErrorNotificationDialog errorDialog = new ErrorNotificationDialog(shell, errorMessage, status);
			return errorDialog.open();

		}

		else {
			// else open up the User Input Dialog that stores user given values
			UserInputDialog inputDialog = new UserInputDialog(shell);
			int num = inputDialog.open();

			// if user pressed oK
			if (num == 0) {
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


	public void sendReportJ() throws Exception {

		Job reporterJob = new Job(REPORTER) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					 System.out.println("publishJIRA");
					// response=publishJira();
					// JSONReader reader=new JSONReader();
					// id=reader.getJsonId(response);
					// key=reader.getJsonKey(response);
					//
//					 System.out.println(id);
					// System.out.println(response);
					// System.out.println(key);
					// // id="5678";
					// // key="TOOLS-3168";
					// FilePublisher nw = new FilePublisher();
					// nw.publish(textReportGenerator);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		reporterJob.setUser(true);
		reporterJob.schedule();

	}

	private void sendReportE() {

		Job reporterJob = new Job(REPORTER) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				System.out.println("Sending email and jira");
				try {
					//response=publishJira();
					// JSONReader reader=new JSONReader();
					// id=reader.getJsonId(response);
					// key=reader.getJsonKey(response);
					//
					// System.out.println(id);
					// System.out.println(key);
					//System.out.println(response);
					String ret=sendEmail();
					System.out.println(ret);
					// id="5678";
					// key="TOOLS-3168";
					FilePublisher nw = new FilePublisher();
					nw.publish(textReportGenerator);
				}

				catch (IOException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		reporterJob.setUser(true);
		reporterJob.schedule();

	}
	public String publishJira() throws Exception {

		String rp = jp.publishJira();
		System.out.println(rp);
		return rp;

	}

	public String sendEmail() throws IOException, AddressException, MessagingException {

		String recipientEmail = Activator.getDefault().getPreferenceStore().getString("REC EMAIL");

		textReportGenerator.createReport(errorInformation);
		String message = textReportGenerator.getTextString();

		if (recipientEmail == "") {
			Shell shell = new Shell();
			MessageBox msg = new MessageBox(shell);
			msg.setMessage("Please enter your gmail username, password and recipient email adress");
			return "tryAgain";
		}

		else {
			try {
				System.out.println("Trying to publish in Email publisher");
				System.out.println(recipientEmail+"  "+message);
				return jp.publishEmail(recipientEmail, message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}
		}

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
	
	// this method checks whether user has not entered any value in the
	// preference page for Jira username and password
	private boolean checkUserInput() {

		String url = Activator.getDefault().getPreferenceStore().getString("SERVER_URL");

		// checks whether remote server url is not filled
		if (url == "") {

			return false;
		}

		else
			return true;
	}


	public IStatus getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
