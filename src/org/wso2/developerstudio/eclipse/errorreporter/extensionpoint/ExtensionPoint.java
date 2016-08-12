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


package org.wso2.developerstudio.eclipse.errorreporter.extensionpoint;

import java.awt.Window;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Nathie
 *
 */
public class ExtensionPoint {
	public static void run() {
		StringBuffer buffer = new StringBuffer();
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg
				.getConfigurationElementsFor("org.wso2.deveoperstudio.eclipse.errorreporter.jirakey");
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement element = extensions[i];
			buffer.append(element.getAttribute("package"));
			buffer.append(" ");
			String key = "unknown";
			if (element.getAttribute("key") != null) {
				key = element.getAttribute("key");
			} 
			buffer.append(key);
			buffer.append("\n");
		}
		
		Shell sh=new Shell();
		MessageBox mb=new MessageBox(sh);
		mb.setMessage("Installed pizza toppings "+buffer.toString());
		mb.open();
	}
}
