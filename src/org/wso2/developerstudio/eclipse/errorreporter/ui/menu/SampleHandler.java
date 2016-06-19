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

package org.wso2.developerstudio.eclipse.errorreporter.ui.menu;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.wso2.developerstudio.eclipse.errorreporter.other.EmailSender;
import org.wso2.developerstudio.eclipse.errorreporter.other.ErrorInformation;
import org.wso2.developerstudio.eclipse.errorreporter.other.ErrorReporter;
import org.wso2.developerstudio.eclipse.errorreporter.other.InfoCollector;
import org.wso2.developerstudio.eclipse.errorreporter.other.RemoteJiraConnector;
import org.wso2.developerstudio.eclipse.errorreporter.other.ReportGenerator;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorDialogChild;


//this is a sample class

//only used for testing
//no relevance to dev studio error reporter plug-in


//import org.wso2.developerstudio.eclipse.errorreporter.other.RemoteJiraConnector;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.wso2.developerstudio.eclipse.errorreporter.Activator;
////import org.eclipse.ui.IWorkbenchWindow;
////import org.eclipse.ui.handlers.HandlerUtil;
//import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotifyDialog;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 *
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// MessageDialog.openInformation(
		// window.getShell(),
		// "ErrorReporterTool",
		// "Hello, Eclipse world");

		
//		RemoteJiraConnector jira=new RemoteJiraConnector();
//		try {
//			jira.get();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		


		
		Shell shell = new Shell();
//		ErrorNotifyDialog dialog = new ErrorNotifyDialog(shell);
//		dialog.open();

//		RemoteJiraConnector j=new RemoteJiraConnector();
//		try {
//			j.Post();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		
//		RemoteJiraConnector jv=new RemoteJiraConnector();
//		System.out.println("createdClient");
//		@SuppressWarnings("static-access")
//		String response=jv.excutePost("","");
//		System.out.println(response);
		
//		ReportGenerator rg=new ReportGenerator();
//		rg.createIssue(null, null, null, null);
		
//		EmailSender es=new EmailSender();
//		try {
//			es.Send("", "", "nathieshamaddage@gmail.com", "", "Error Reporting", " This is a sample email. Trying to send an error report to test the dev studio error reporting plugin");
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String[][] ui=new String[2][2];
//		ui[0][0]="name";
//		ui[0][1]="nathiesha";
//		ui[1][0]="age";
//		ui[1][1]="23";
//		
//
////		InfoCollector ic=new InfoCollector(status, plugin)
////		ReportGenerator rg=new ReportGenerator(ic.getInformation());
////		
//		
//		ReportGenerator rg=new ReportGenerator(ui);
//		try {
//			rg.createReport();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

//		int input;
//		IStatus i = null;
//		ErrorReporter er=new ErrorReporter(i, "");
//		input=er.openErrorDialog();
//		System.out.println(input);
		
		
		
		try {
		      String s = null;
		      System.out.println(s.length());
		    } catch (NullPointerException e) {
		      // build the error message and include the current stack trace
		      MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
		      // show error dialog
		      InfoCollector ic=new InfoCollector(status, "plugin");
		      ErrorInformation ei=ic.getInformation();
		      ReportGenerator r=new ReportGenerator(ei);
		      try {
				r.storeReport(ei);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		     // System.out.println(ei.getMessage()+" "+ei.getCode()+" "+ei.getComment()+" "+ei.getEclipseBuildId()+" "+ei.getExceptionS()+" "+ei.getEmail()+" "+ei.getOsgiArch());
		      ErrorDialogChild.openError(shell, "Error", "This is an error", ic.getInformation(),status);
		    }
		return event;
	}
	
	
	private static MultiStatus createMultiStatus(String msg, Throwable t) {

	    List<Status> childStatuses = new ArrayList<>();
	    StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

	     for (StackTraceElement stackTrace: stackTraces) {
	      Status status = new Status(IStatus.ERROR,
	          "com.example.e4.rcp.todo", stackTrace.toString());
	      childStatuses.add(status);
	    }

	    MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo",
	        IStatus.ERROR, childStatuses.toArray(new Status[] {}),
	        t.toString(), t);
	    return ms;
	  }
}
