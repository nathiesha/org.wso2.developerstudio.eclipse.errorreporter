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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;
import org.wso2.developerstudio.eclipse.errorreporter.ui.dialog.ErrorNotifyDialog;

public class ErrorReporter {
	IStatus status;
	String plugin;

	public ErrorReporter(IStatus status, String plugin) {
		this.status = status;
		this.plugin = plugin;

	}

	public void openErrorDialog() {
		Shell shell = new Shell();
		ErrorNotifyDialog dialog = new ErrorNotifyDialog(shell);
		dialog.open();

	}

	public void collectErrorInfo() {
		//ErrorInfoCollector errInfoCollector = new ErrorInfoCollector(status, plugin);

	}

}
