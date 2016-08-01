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

package org.wso2.developerstudio.eclipse.errorreporter.startup;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

	private static LogListener listener;

	@Override
	public void earlyStartup() {

		// attach a listener to the eclipse error log
		listener = new LogListener();
		Platform.addLogListener(listener);

		System.out.println("Early start up");

	}

	public static LogListener getListener() {
		return listener;
	}

	public void setListener(LogListener listener) {
		Startup.listener = listener;
	}

}
