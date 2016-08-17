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

/**
 * This class implments the start up bahaviour of the plugin.
 */

public class Startup implements IStartup {

	private static LogListener listener;

	/**
	 * This method runs when the Eclipse IDE starts creates a LogListener object
	 * and attaches it to the platform error log.
	 */

	@Override
	public void earlyStartup() {

		// attach a listener to the eclipse error log
		listener = new LogListener();
		Platform.addLogListener(listener);
		System.out.println("Early startup");
	}

	/**
	 * Getteres and setters
	 */
	public static LogListener getListener() {
		return listener;
	}

	public void setListener(LogListener listener) {
		Startup.listener = listener;
	}

}
