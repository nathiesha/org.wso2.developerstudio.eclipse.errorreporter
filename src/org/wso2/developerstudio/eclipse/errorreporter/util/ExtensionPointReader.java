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


package org.wso2.developerstudio.eclipse.errorreporter.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


/**
 * @author Nathie
 *
 */


public class ExtensionPointReader {
	
	private static final String ID="org.wso2.deveoperstudio.eclipse.errorreporter.jirakey";
	
	public static Map<String,String> getKeys() {
		

		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg
				.getConfigurationElementsFor(ID);
		
		Map<String, String> pair = new HashMap<String, String>();
		String pack = "";
		String key = "";
		
		for (int i = 0; i < extensions.length; i++) {
			
			IConfigurationElement element = extensions[i];
			pack=element.getAttribute("package");
						
			if (element.getAttribute("key") != null) {
				key = element.getAttribute("key");
			} 
			
			pair.put(pack, key);
			

		}
		
		return pair;
	}
}
