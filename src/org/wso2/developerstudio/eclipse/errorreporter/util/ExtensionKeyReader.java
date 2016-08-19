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
import org.wso2.developerstudio.eclipse.errorreporter.constants.ProjectConstants;

/**
 * This util class contains logic to extract the key value pairs from the
 * plugins that have extended the errorReporter extension point.
 */

public class ExtensionKeyReader {

	/**
	 * This method is extracting the key value pairs from the plugins that have
	 * extended the errorReporter extension point.
	 * 
	 * @return Map of package and key value pairs fetched from the extended
	 *         plugins
	 */

	public static Map<String, String> getKeys() {

		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = extensionRegistry
				.getConfigurationElementsFor(ProjectConstants.EXTENSION_POINT_ID);

		// The map to be returned, with package and key value pairs
		Map<String, String> packageKeyPair = new HashMap<String, String>();
		String packageName = "";
		String key = "";

		// iterate through all the extension point elemetns
		for (int i = 0; i < extensions.length; i++) {

			IConfigurationElement element = extensions[i];

			// Get the package value
			packageName = element.getAttribute(ProjectConstants.EXTENSION_ATTRIBUTE1);

			// get the key value, if not null
			if (element.getAttribute(ProjectConstants.EXTENSION_ATTRIBUTE2) != null) {
				key = element.getAttribute(ProjectConstants.EXTENSION_ATTRIBUTE2);
			}

			// store the package and key value pair in a Map
			packageKeyPair.put(packageName, key);

		}

		return packageKeyPair;
	}
}
