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

//this is a sample class

//only used for testing
//no relevance to dev studio error reporter plug-in

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.handlers.HandlerUtil;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotifyDialog;
import org.wso2.developerstudio.eclipse.errorreporter.ui.prefs.Preferences;
import org.eclipse.swt.widgets.Shell;

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

		Preferences pr=new Preferences("org.wso2.developerstudio.eclipse.errorreporter");
		pr.setPreferenceValue("hello", "world");
		System.out.println(pr.getPreferenceKey("hello"));
		
		Shell shell = new Shell();
		ErrorNotifyDialog dialog = new ErrorNotifyDialog(shell);
		dialog.open();

		return null;
	}
}
