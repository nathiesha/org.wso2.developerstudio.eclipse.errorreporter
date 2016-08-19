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

package org.wso2.developerstudio.eclipse.errorreporter.ui.archivemenu;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * This class contains the execute method, called upon selecting the Report
 * Archive menu Item.
 */

public class ReportArchiveHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ReportArchiveHandler() {
	}

	/**
	 * This method opens up the Report Archive window.
	 * 
	 * @param event
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		Shell parentShell = new Shell();

		try {

			ReportArchive archive = new ReportArchive(parentShell);
			archive.open();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return event;
	}

}
